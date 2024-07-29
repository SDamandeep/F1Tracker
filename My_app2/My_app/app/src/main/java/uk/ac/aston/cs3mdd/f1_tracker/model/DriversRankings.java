package uk.ac.aston.cs3mdd.f1_tracker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class DriversRankings {
    @SerializedName("response")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public class Result {
        private int position;

        @SerializedName("driver")
        private Driver driver;

        @SerializedName("team")
        private Team team;

        private int points;
        private int wins;

        public int getPosition() {
            return position;
        }

        public Driver getDriver() {
            return driver;
        }

        public Team getTeam() {
            return team;
        }

        public int getPoints() {
            return points;
        }

        public int getWins() {
            return wins;
        }
    }

    public class Driver {
        private int id;
        private String name;
        private String abbr;
        private String number;
        private String image;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAbbr() {
            return abbr;
        }

        public String getNumber() {
            return number;
        }

        public String getImage() {
            return image;
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

