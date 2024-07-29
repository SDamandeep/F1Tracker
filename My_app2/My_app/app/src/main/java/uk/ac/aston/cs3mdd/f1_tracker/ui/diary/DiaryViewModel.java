package uk.ac.aston.cs3mdd.f1_tracker.ui.diary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiaryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DiaryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Diary");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
