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

public class TeamsFragmentAdapter extends RecyclerView.Adapter<TeamsFragmentAdapter.TeamsHolder>{
    private List<TeamsRankings.Result> mySingleTeam;
    private final LayoutInflater myInflater;

    public TeamsFragmentAdapter(Context context, List<TeamsRankings.Result> singleTeam){
        myInflater = LayoutInflater.from(context);
        this.mySingleTeam = singleTeam;
    }

    @NonNull
    @Override
    public TeamsFragmentAdapter.TeamsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("TeamFragmentAdapter", "onCreateViewHolder method of driver adapter is working fine");
        View mItemView = myInflater.inflate(R.layout.team_item, parent, false);
        return new TeamsFragmentAdapter.TeamsHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsFragmentAdapter.TeamsHolder holder, int position) {
        Log.d("TeamFragmentAdapter", "team position called: " + position);
        TeamsRankings.Result result = mySingleTeam.get(position);

        holder.teamName.setText("( " + result.getPosition() +" ) "+ result.getTeam().getName());
        holder.points.setText("Points: " + result.getPoints());


        Picasso.get()
                .load(result.getTeam().getLogo())
                .into(holder.teamLogo);
    }

    @Override
    public int getItemCount() {
        return mySingleTeam.size();
    }

    public void updateData(List<TeamsRankings.Result> newData) {
        mySingleTeam.clear();
        mySingleTeam.addAll(newData);
        notifyDataSetChanged();
        Log.d("TeamsFragmentAdapter", "size of the list teams adapter: " + mySingleTeam.size());
    }

    public class TeamsHolder extends RecyclerView.ViewHolder {
        public final TextView teamName;
        public final TextView points;
        public final ImageView teamLogo;
        final TeamsFragmentAdapter mAdapter;

        public TeamsHolder(@NonNull View itemView, TeamsFragmentAdapter adapter) {
            super(itemView);
            teamName = itemView.findViewById(R.id.mainTitle);
            points = itemView.findViewById(R.id.points);
            teamLogo = itemView.findViewById(R.id.teamLogo);

            this.mAdapter = adapter;
        }
    }

}
