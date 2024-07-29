package uk.ac.aston.cs3mdd.f1_tracker.ui.races;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.ac.aston.cs3mdd.f1_tracker.R;
import uk.ac.aston.cs3mdd.f1_tracker.model.Races;

public class RacesFragmentAdapter extends RecyclerView.Adapter<RacesFragmentAdapter.RacesHolder>{

    private List<Races.ResponseItem> mySingleRace;
    private final LayoutInflater myInflater;

    public RacesFragmentAdapter(Context context, List<Races.ResponseItem> singleRace) {
        myInflater = LayoutInflater.from(context);
        this.mySingleRace = singleRace;
    }

    @NonNull
    @Override
    public RacesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = myInflater.inflate(R.layout.race_item, parent, false);
        return new RacesHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RacesHolder holder, int position) {
        Races.ResponseItem result = mySingleRace.get(position);

        holder.raceName.setText(result.getCompetition().getName());
        holder.location.setText(result.getCompetition().getLocation().getCountry() + ", " + result.getCompetition().getLocation().getCity());
        holder.circuitType.setText(null);
        try {
            String originalDateTime = result.getDate();
            Log.d("time", "original date time: " + originalDateTime);
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
            Date date = originalFormat.parse(originalDateTime);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            String formattedDate = dateFormat.format(date);
            String formattedTime = timeFormat.format(date);

            holder.dateTime.setText("Date: " + formattedDate + "\n" +"Time: " + formattedTime);
        } catch (ParseException e) {
            Log.e("time", "error while formatting the date", e);
        }

        holder.raceStatus.setText(result.getStatus());
        holder.circuitName.setText(result.getCircuit().getName());

        Picasso.get()
                .load(result.getCircuit().getImage())
                .into(holder.raceTrack);
    }


    @Override
    public int getItemCount() {
        return mySingleRace.size();
    }

    public void updateData(List<Races.ResponseItem> newData) {
        mySingleRace.clear();
        mySingleRace.addAll(newData);
        notifyDataSetChanged();
    }

    public class RacesHolder extends RecyclerView.ViewHolder {
        public final TextView raceName;
        public final ImageView raceTrack;
        public final TextView location;
        public final TextView circuitType;
        public final TextView dateTime;
        public final TextView raceStatus;
        public final TextView circuitName;
        final RacesFragmentAdapter mAdapter;

        public RacesHolder(@NonNull View itemView, RacesFragmentAdapter adapter) {
            super(itemView);
            raceName = itemView.findViewById(R.id.raceName);
            raceTrack = itemView.findViewById(R.id.raceTrack);
            location = itemView.findViewById(R.id.location);
            circuitType = itemView.findViewById(R.id.circuitType);
            dateTime = itemView.findViewById(R.id.dateTime);
            raceStatus = itemView.findViewById(R.id.raceStatus);
            circuitName = itemView.findViewById(R.id.circuitName);
            this.mAdapter = adapter;
        }
    }

}
