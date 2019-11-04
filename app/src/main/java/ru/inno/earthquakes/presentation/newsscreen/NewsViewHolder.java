package ru.inno.earthquakes.presentation.newsscreen;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.models.entities.News;
import ru.inno.earthquakes.presentation.newsscreen.NewsAdapter.OnNewsClickListener;

/**
 * Created by Artur (gaket) on 2019-11-04.
 */
public class NewsViewHolder extends RecyclerView.ViewHolder {

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
