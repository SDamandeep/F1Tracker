package uk.ac.aston.cs3mdd.f1_tracker.model;

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

import java.util.List;

import uk.ac.aston.cs3mdd.f1_tracker.R;

public class DriverFragmentAdapter extends RecyclerView.Adapter<DriverFragmentAdapter.DriverHolder>{

    private List<DriversRankings.Result> mySingleDriver;
    private final LayoutInflater myInflater;

    public DriverFragmentAdapter(Context context, List<DriversRankings.Result> singleDriver){
        myInflater = LayoutInflater.from(context);
        this.mySingleDriver = singleDriver;
    }

    @NonNull
    @Override
    public DriverHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("DriverFragmentAdapter", "onCreateViewHolder method of driver adapter is working fine");
        View mItemView = myInflater.inflate(R.layout.driver_item, parent, false);
        return new DriverFragmentAdapter.DriverHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverFragmentAdapter.DriverHolder holder, int position) {
        Log.d("DriverFragmentAdapter", "driver position called: " + position);
        DriversRankings.Result result = mySingleDriver.get(position);

        holder.driverName.setText("( " + result.getPosition() +" ) "+result.getDriver().getName());
        holder.teamName.setText("Team: " + result.getTeam().getName());
        holder.winsPoints.setText("Wins: " + result.getWins() + " / "+"Points: " + result.getPoints());

        Picasso.get()
                .load(result.getDriver().getImage())
                .into(holder.driverImage);
    }

    @Override
    public int getItemCount() {
        return mySingleDriver.size();
    }

    public void updateData(List<DriversRankings.Result> newData) {
        mySingleDriver.clear();
        mySingleDriver.addAll(newData);
        notifyDataSetChanged();
        Log.d("DriverFragmentAdapter", "size of the list driver adapter " + mySingleDriver.size());
    }

    public class DriverHolder extends RecyclerView.ViewHolder {
        public final TextView driverName;
        public final TextView teamName;
        public final TextView winsPoints;
        public final ImageView driverImage;
        final DriverFragmentAdapter mAdapter;

        public DriverHolder(@NonNull View itemView, DriverFragmentAdapter adapter) {
            super(itemView);
            driverName = itemView.findViewById(R.id.driverName);
            teamName = itemView.findViewById(R.id.teamName);
            winsPoints = itemView.findViewById(R.id.winsPoints);
            driverImage = itemView.findViewById(R.id.driverImage);

            this.mAdapter = adapter;
        }
    }
}
