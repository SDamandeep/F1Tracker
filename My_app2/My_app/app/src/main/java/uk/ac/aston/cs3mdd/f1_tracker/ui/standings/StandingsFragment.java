package uk.ac.aston.cs3mdd.f1_tracker.ui.standings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import uk.ac.aston.cs3mdd.f1_tracker.R;
import uk.ac.aston.cs3mdd.f1_tracker.databinding.FragmentStandingsBinding;
import uk.ac.aston.cs3mdd.f1_tracker.model.DriverFragment;
import uk.ac.aston.cs3mdd.f1_tracker.model.TeamsFragment;

public class StandingsFragment extends Fragment {
    private TextView textStandings;
    private DriverFragment viewModel;
    private FragmentStandingsBinding binding;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_standings, container, false);

        Button driversButton = rootView.findViewById(R.id.drivers_button);
        Button teamButton = rootView.findViewById(R.id.team_button);

        driversButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverFragment fragment = new DriverFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(requireActivity(), "Driver Standings", Toast.LENGTH_SHORT).show();
            }
        });

        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamsFragment fragment = new TeamsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(requireActivity(), "Constructor Standings", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}

