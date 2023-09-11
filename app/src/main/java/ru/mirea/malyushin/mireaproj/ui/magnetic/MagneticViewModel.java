package ru.mirea.malyushin.mireaproj.ui.magnetic;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MagneticViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public MagneticViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is magnetic fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
