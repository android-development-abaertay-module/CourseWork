package com.example.coursework.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.coursework.model.User;

public class CheckGoalsViewModel extends AndroidViewModel {
    private MutableLiveData<User> userLD;

    public void setUserLD(User user){
        userLD.setValue(user);
    }
    public MutableLiveData<User> getUserLD(){
        return  userLD;
    }

    public CheckGoalsViewModel(@NonNull Application application) {
        super(application);
        userLD = new MutableLiveData<>();
    }
}
