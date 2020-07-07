package com.example.parstagram.ui.camera;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parse.ParseFile;

public class CameraViewModel extends ViewModel {

    private MutableLiveData<String> description;
    private MutableLiveData<ParseFile> image;

    public CameraViewModel() {
        description = new MutableLiveData<>();
        image = new MutableLiveData<>();
    }

    public LiveData<String> getDescription() {
        return description;
    }
    public LiveData<ParseFile> getImage() {
        return image;
    }

}