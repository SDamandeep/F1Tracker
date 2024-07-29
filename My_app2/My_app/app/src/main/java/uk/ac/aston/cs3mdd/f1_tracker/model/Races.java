package uk.ac.aston.cs3mdd.f1_tracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import java.io.Serializable;

public class Races {
    @SerializedName("get")
    private String get;

    @SerializedName("parameters")
    private Parameters parameters;

    @SerializedName("errors")
    private List<Object> errors;

    @SerializedName("results")
    private int results;

    @SerializedName("response")
    private List<ResponseItem> response;

    public String getGet() {
        return get;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public int getResults() {
        return results;
    }

    public List<ResponseItem> getResponse() {
        return response;
    }

    public class Parameters {
        @SerializedName("type")
        private String type;

        @SerializedName("season")
        private String season;

        public String getType() {
            return type;
        }

        public String getSeason() {
            return season;
        }
    }

    public class ResponseItem {
        @SerializedName("id")
        private int id;

        @SerializedName("distance")
        private String distance;

        public String getDistance() {
            return distance;
        }

        @SerializedName("competition")
        private Competition competition;

        @SerializedName("circuit")
        private Circuit circuit;

        @SerializedName("laps")
        private Laps laps;

        @SerializedName("fastest_lap")
        private FastestLap fastestLap;

        @SerializedName("timezone")
        private String timezone;

        @SerializedName("date")
        private String date;

        @SerializedName("weather")
        private Object weather;

        @SerializedName("status")
        private String status;

        public int getId() {
            return id;
        }
        @SerializedName("season")
        private int season;

        @SerializedName("type")
        private String type;

        public String getType() {
            return type;
        }
        public int getSeason() {
            return season;
        }

        public Competition getCompetition() {
            return competition;
        }


        public Circuit getCircuit() {
            return circuit;
        }

        public Laps getLaps() {
            return laps;
        }

        public FastestLap getFastestLap() {
            return fastestLap;
        }

        public String getTimezone() {
            return timezone;
        }

        public String getDate() {
            return date;
        }

        public Object getWeather() {
            return weather;
        }

        public String getStatus() {
            return status;
        }
    }

    public class Competition {

        @SerializedName("location")
        private Location location;
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
        public Location getLocation() {
            return location;
        }
    }

    public class Location {
        @SerializedName("country")
        private String country;

        @SerializedName("city")
        private String city;

        public String getCountry() {
            return country;
        }

        public String getCity() {
            return city;
        }
    }


    public class Circuit {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private String image;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }
    }

    public class Laps {
        @SerializedName("current")
        private Object current;

        @SerializedName("total")
        private int total;

        public Object getCurrent() {
            return current;
        }

        public int getTotal() {
            return total;
        }
    }

    public class FastestLap {
        @SerializedName("driver")
        private Driver driver;

        @SerializedName("time")
        private String time;

        @SerializedName("distance")
        private String distance;

        public Driver getDriver() {
            return driver;
        }

        public String getTime() {
            return time;
        }

        public String getDistance() {
            return distance;
        }
    }

    public class Driver {
        @SerializedName("id")
        private int id;

        public int getId() {
            return id;
        }
    }
}
