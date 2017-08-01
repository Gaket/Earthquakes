package ru.inno.earthquakes.presentation.earthquakeslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.inno.earthquakes.R;
import ru.inno.earthquakes.entities.EarthquakeWithDist;

/**
 * @author Artur Badretdinov (Gaket)
 *         01.08.17
 */
public class EarthquakesListAdapter extends RecyclerView.Adapter<EarthquakesListAdapter.EarthquakeViewHolder> {

    private List<EarthquakeWithDist> items = new ArrayList<>();

    @Override
    public EarthquakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_earthqake, parent, false);
        return new EarthquakeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EarthquakeViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<EarthquakeWithDist> earthquakeWithDists) {
        items = earthquakeWithDists;
        notifyDataSetChanged();
    }

    public static class EarthquakeViewHolder extends RecyclerView.ViewHolder {

        private DateFormat dateFormat = new SimpleDateFormat("hh:mm MMM, dd", Locale.GERMANY);

        private TextView magnitude;
        private TextView place;
        private TextView time;
        private TextView dist;

        public EarthquakeViewHolder(View itemView) {
            super(itemView);
            magnitude = (TextView) itemView.findViewById(R.id.earthquake_tv_mag);
            place = (TextView) itemView.findViewById(R.id.earthquake_tv_place);
            time = (TextView) itemView.findViewById(R.id.earthquake_tv_time);
            dist = (TextView) itemView.findViewById(R.id.earthquake_tv_dist);
        }

        public void bind(EarthquakeWithDist model) {
            magnitude.setText(String.format(Locale.GERMANY, "%.1f", model.getMagnitude()));
            dist.setText(String.format(Locale.GERMANY, "%.2f km from you", model.getDistance()));
            place.setText(model.getLocation().getName());
            time.setText(dateFormat.format(model.getTime()));
        }
    }

}
