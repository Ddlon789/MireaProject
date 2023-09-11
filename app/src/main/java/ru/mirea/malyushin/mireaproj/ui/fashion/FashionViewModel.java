package ru.mirea.malyushin.mireaproj.ui.fashion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FashionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FashionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is fashion fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}