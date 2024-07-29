package uk.ac.aston.cs3mdd.f1_tracker.ui.diary;

public class NotesDatabaseModel {
    private String id;
    private String raceName;
    private String raceDescription;

    public NotesDatabaseModel() {
    }

    public NotesDatabaseModel(String raceName, String raceDescription) {
        this.raceName = raceName;
        this.raceDescription = raceDescription;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getRaceDescription() {
        return raceDescription;
    }

    public void setRaceDescription(String raceDescription) {
        this.raceDescription = raceDescription;
    }

}