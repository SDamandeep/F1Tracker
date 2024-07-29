package uk.ac.aston.cs3mdd.f1_tracker.model;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.aston.cs3mdd.f1_tracker.R;
import uk.ac.aston.cs3mdd.f1_tracker.service.apiFormula;

import uk.ac.aston.cs3mdd.f1_tracker.databinding.FragmentDriversStandingsBinding;
import uk.ac.aston.cs3mdd.f1_tracker.ui.races.RacesFragmentAdapter;

public class DriverFragment extends Fragment {
    private TextView textStandings;
    private FragmentDriversStandingsBinding binding;
    private DriverFragmentAdapter driverAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDriversStandingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        driverAdapter = new DriverFragmentAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(driverAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

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
        Call<DriversRankings> call = api.returnDrivers();
        call.enqueue(new Callback<DriversRankings>() {
            @Override
            public void onResponse(Call<DriversRankings> call, Response<DriversRankings> response) {
                if (response.isSuccessful()) {
                    DriversRankings drivers = response.body();
                    if (drivers != null && drivers.getResults() != null && !drivers.getResults().isEmpty()) {
                        Log.d("DriverFragment", "list of drivers: " + drivers.getResults().size());
                        driverAdapter.updateData(drivers.getResults());
                    } else {
                        Log.d("DriverFragment", "no response from api");
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("DriverFragment", "error: " + response.code() + ", " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<DriversRankings> call, Throwable t) {
                t.printStackTrace();
                textStandings.setText("network error: " + t.getMessage());
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

