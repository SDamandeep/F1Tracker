package uk.ac.aston.cs3mdd.f1_tracker.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import uk.ac.aston.cs3mdd.f1_tracker.model.DriversRankings;
import uk.ac.aston.cs3mdd.f1_tracker.model.Races;
import uk.ac.aston.cs3mdd.f1_tracker.model.TeamsRankings;

public interface apiFormula {
    @GET("rankings/drivers?season=2023")
    Call<DriversRankings> returnDrivers();

    @GET("races?type=race&season=2023")
    Call<Races> returnRaces();

    @GET("rankings/teams?season=2023")
    Call<TeamsRankings> returnTeams();

}
