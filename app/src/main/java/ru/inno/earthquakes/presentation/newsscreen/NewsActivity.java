package ru.inno.earthquakes.presentation.newsscreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.squareup.picasso.Picasso;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.mapstruct.factory.Mappers;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.data.network.NewsApiService;
import ru.inno.earthquakes.models.entities.News;
import ru.inno.earthquakes.models.mappers.NewsMapper;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;
import ru.inno.earthquakes.presentation.common.views.EmptyRecyclerView;
import timber.log.Timber;

public class NewsActivity extends MvpAppCompatActivity {

  private static final long TIMEOUT = 20;
  @Inject
  SchedulersProvider schedulersProvider;

  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  private SwipeRefreshLayout swipeRefreshLayout;
  private EmptyRecyclerView recyclerView;
  private NewsAdapter newsAdapter;
  private Snackbar snackbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EartquakeApp.getComponentsManager().getNewsComponent().inject(this);
    setContentView(R.layout.activity_news);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    swipeRefreshLayout = findViewById(R.id.news_swipe_refresh);
    swipeRefreshLayout.setOnRefreshListener(this::updateNews);
    initRecyclerView();
    updateNews();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EartquakeApp.getComponentsManager().clearNewsComponent();
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public static Intent getStartIntent(Context callingContext) {
    return new Intent(callingContext, NewsActivity.class);
  }

  public void showNetworkError(boolean show) {
    if (show) {
      snackbar = Snackbar.make(swipeRefreshLayout, R.string.error_connection,
          Snackbar.LENGTH_INDEFINITE)
          .setAction(R.string.action_ok, (d) -> snackbar.dismiss());
      snackbar.show();
    } else if (snackbar != null) {
      snackbar.dismiss();
    }
  }

  public void showLoading(boolean show) {
    swipeRefreshLayout.setRefreshing(show);
  }

  public void navigateToNews(News news) {
    Uri webpage = Uri.parse(news.getUrl());
    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
    if (intent.resolveActivity(this.getPackageManager()) != null) {
      startActivity(intent);
    } else {
      showError("Can't find a web browser. Do you have one installed?");
    }
  }

  private void showError(String message) {
    snackbar = Snackbar.make(swipeRefreshLayout, R.string.error_connection,
        Snackbar.LENGTH_LONG)
        .setAction(R.string.action_ok, (d) -> snackbar.dismiss());
    snackbar.show();
  }

  public void showNews(List<News> news) {
    newsAdapter.setItems(news);
  }

  private void updateNews() {
    String string = getString(R.string.newsapi_key);
    if (TextUtils.isEmpty(string)) {
      showError("App is not fully configured. Please, check the news api key");
      return;
    }

    showLoading(true);

    NewsApiService newsApiService = initRetrofit();
    compositeDisposable.add(newsApiService.getEarthquakes(string)
        .map(newsResponse -> newsResponse.getArticles())
        .toObservable()
        .flatMapIterable(articles -> articles)
        .map(article -> Mappers.getMapper(NewsMapper.class).newsDataToEntity(article))
        .toList()
        .observeOn(schedulersProvider.ui())
        .doAfterTerminate(() -> showLoading(false))
        .subscribe(this::showNews, throwable -> {
          Timber.e(throwable, "NewsActivity::updateNews: Couldn't download news");
          showError("Couldn't get the list. Please, try again later");
        }));
  }

  private NewsApiService initRetrofit() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Date.class,
            (JsonDeserializer<Date>) (json, typeOfT, context)
                -> new Date(json.getAsJsonPrimitive().getAsLong()))
        .create();

    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
        message -> Timber.v(message));
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

    builder.addInterceptor(loggingInterceptor)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS);
    OkHttpClient client = builder.build();

    Retrofit retrofit = new Builder()
        .client(client)
        .baseUrl("https://newsapi.org")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build();

    return retrofit.create(NewsApiService.class);
  }

  private void initRecyclerView() {
    recyclerView = findViewById(R.id.news_recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setEmptyViewLayout(R.layout.empty_news);
    newsAdapter = new NewsAdapter(
        news -> navigateToNews(news));
    recyclerView.setAdapter(newsAdapter);
  }

  /**
   * @author Artur Badretdinov (Gaket) 01.08.17
   */
  public static class NewsAdapter extends
      RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> items = new ArrayList<>();
    private OnNewsClickListener listener;

    public NewsAdapter(OnNewsClickListener listener) {
      this.listener = listener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.row_news, parent, false);
      return new NewsViewHolder(v, parent.getContext(), listener);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
      holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
      return items.size();
    }

    public void setItems(List<News> earthquakeWithDists) {
      items = earthquakeWithDists;
      notifyDataSetChanged();
    }

    interface OnNewsClickListener {

      void onNewsClicked(News earthquakeWithDist);
    }

    /**
     * Created by Artur (gaket) on 2019-11-04.
     */
    public static class NewsViewHolder extends RecyclerView.ViewHolder {

      private OnNewsClickListener listener;

      private ImageView picture;
      private TextView title;
      private TextView description;

      NewsViewHolder(View itemView, Context context, OnNewsClickListener listener) {
        super(itemView);
        picture = itemView.findViewById(R.id.news_item_picture);
        title = itemView.findViewById(R.id.news_item_title);
        description = itemView.findViewById(R.id.news_item_description);
        this.listener = listener;
      }

      void bind(News news) {
        itemView.setOnClickListener(v -> listener.onNewsClicked(news));
        Picasso.get()
            .load(news.getPicture())
            .into(picture);
        title.setText(news.getTitle());
        description.setText(news.getDescription());
      }
    }
  }
}
