package ru.mirea.malyushin.mireaproj.ui.filework;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FileWorkViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public FileWorkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is fashion fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}