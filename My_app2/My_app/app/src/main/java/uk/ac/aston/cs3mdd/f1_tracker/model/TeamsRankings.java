package uk.ac.aston.cs3mdd.f1_tracker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TeamsRankings {
    @SerializedName("response")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public class Result {
        @SerializedName("position")
        private int position;

        @SerializedName("team")
        private Team team;

        @SerializedName("points")
        private int points;

        @SerializedName("season")
        private int season;

        public int getPosition() {
            return position;
        }

        public Team getTeam() {
            return team;
        }

        public int getPoints() {
            return points;
        }

        public int getSeason() {
            return season;
        }
    }
    public class Team {
        private int id;
        private String name;
        private String logo;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getLogo() {
            return logo;
        }
    }
}
