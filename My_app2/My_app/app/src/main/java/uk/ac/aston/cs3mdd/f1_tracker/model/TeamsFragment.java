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
import uk.ac.aston.cs3mdd.f1_tracker.databinding.FragmentTeamsStendingsBinding;
import uk.ac.aston.cs3mdd.f1_tracker.service.apiFormula;

public class TeamsFragment extends Fragment {
    private TextView textStandings;
    private FragmentTeamsStendingsBinding binding;
    private TeamsFragmentAdapter teamsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTeamsStendingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        teamsAdapter = new TeamsFragmentAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(teamsAdapter);
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
        Call<TeamsRankings> call = api.returnTeams();
        call.enqueue(new Callback<TeamsRankings>() {
            @Override
            public void onResponse(Call<TeamsRankings> call, Response<TeamsRankings> response) {
                if (response.isSuccessful()) {
                    TeamsRankings teams = response.body();
                    if (teams != null && teams.getResults() != null && !teams.getResults().isEmpty()) {
                        Log.d("TeamsFragment", "list of drivers: " + teams.getResults().size());
                        teamsAdapter.updateData(teams.getResults());
                    } else {
                        Log.d("TeamsFragment", "no response from api");
                        textStandings.setText("No data received from the server");
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("TeamsFragment", "error: " + response.code() + ", " + errorBody);
                        textStandings.setText("error: " + response.code() + ", " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<TeamsRankings> call, Throwable t) {
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
