package ru.mirea.malyushin.mireaproj.ui.audio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AudioViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public AudioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}