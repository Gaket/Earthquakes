package ru.inno.earthquakes.presentation.newsscreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import javax.inject.Inject;
import ru.inno.earthquakes.EartquakeApp;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.models.entities.News;
import ru.inno.earthquakes.presentation.common.SchedulersProvider;
import ru.inno.earthquakes.presentation.common.SmartDividerItemDecoration;
import ru.inno.earthquakes.presentation.common.views.EmptyRecyclerView;

public class NewsActivity extends MvpAppCompatActivity
    implements NewsView {

  @Inject
  SchedulersProvider schedulersProvider;

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

    swipeRefreshLayout = findViewById(R.id.earthquakes_swipe_refresh);
    swipeRefreshLayout.setOnRefreshListener(this::updateNews);
    initRecyclerView();
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

  }

  private void initRecyclerView() {
    recyclerView = findViewById(R.id.earthquakes_recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setEmptyViewLayout(R.layout.empty_earthquakes);
    SmartDividerItemDecoration itemDecoration = new SmartDividerItemDecoration.Builder(this)
        .setMarginProvider((position, parent) -> 56)
        .build();
    recyclerView.addItemDecoration(itemDecoration);
    newsAdapter = new NewsAdapter(
        earthquakeWithDist -> navigateToNews(earthquakeWithDist));
    recyclerView.setAdapter(newsAdapter);
  }
}
