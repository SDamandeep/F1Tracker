package uk.ac.aston.cs3mdd.f1_tracker.ui.races;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RacesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RacesViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("Races");
    }

    public LiveData<String> getText() {
        return mText;
    }
}