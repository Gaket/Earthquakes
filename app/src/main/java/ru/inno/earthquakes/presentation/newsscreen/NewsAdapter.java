package ru.inno.earthquakes.presentation.newsscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import ru.inno.earthquakes.R;
import ru.inno.earthquakes.models.entities.EarthquakeWithDist;
import ru.inno.earthquakes.models.entities.News;

/**
 * @author Artur Badretdinov (Gaket) 01.08.17
 */
public class NewsAdapter extends
    RecyclerView.Adapter<NewsViewHolder> {

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
}
