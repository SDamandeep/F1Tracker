package uk.ac.aston.cs3mdd.f1_tracker.ui.standings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StandingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public StandingsViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}