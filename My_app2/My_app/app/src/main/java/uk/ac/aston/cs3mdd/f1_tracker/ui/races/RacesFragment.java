package uk.ac.aston.cs3mdd.f1_tracker.ui.races;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.aston.cs3mdd.f1_tracker.R;
import uk.ac.aston.cs3mdd.f1_tracker.databinding.FragmentRacesBinding;
import uk.ac.aston.cs3mdd.f1_tracker.model.Races;
import uk.ac.aston.cs3mdd.f1_tracker.service.apiFormula;

public class RacesFragment extends Fragment {
    private TextView textRaces;
    private RacesViewModel viewModel;
    private FragmentRacesBinding binding;

    private RacesFragmentAdapter racesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRacesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        racesAdapter = new RacesFragmentAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(racesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel = new ViewModelProvider(requireActivity()).get(RacesViewModel.class);
        RacesViewModel racesViewModel = new ViewModelProvider(this).get(RacesViewModel.class);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-formula-1.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();
                                Request request = original.newBuilder()
                                        .header("X-RapidAPI-Host", "api-formula-1.p.rapidapi.com")
                                        .header("X-RapidAPI-Key", "APIKEYHERE")
                                        .method(original.method(), original.body())
                                        .build();
                                return chain.proceed(request);
                            }
                        })
                        .build())
                .build();
        apiFormula api = retrofit.create(apiFormula.class);
        Call<Races> call = api.returnRaces();
        call.enqueue(new Callback<Races>() {
            @Override
            public void onResponse(Call<Races> call, Response<Races> response) {
                if (response.isSuccessful()) {
                    Races races = response.body();
                    if (races != null && races.getResults() > 0) {
                        racesAdapter.updateData(races.getResponse());
                    } else {
                        textRaces.append("No data from api");
                        Log.d("api","No data from api");
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        textRaces.append("Error: " + response.code() + ", " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Races> call, Throwable t) {
                t.printStackTrace();
                textRaces.setText("Network error: " + t.getMessage());
            }

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}