package com.example.coursework.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;

import java.util.List;

public class MainMapActivityViewModel extends AndroidViewModel {

    DaoRepository daoRepository;
    MutableLiveData<User> userLD;
    LiveData<List<Session>> recentSessionsLD;

    public void setUserLD(User user) {
        userLD.setValue(user);
    }

    public LiveData<List<Session>> getRecentSessionsLD() {
        return recentSessionsLD;
    }

    public MainMapActivityViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        userLD = new MutableLiveData<>();


        recentSessionsLD = Transformations.switchMap(userLD, (User user) -> {
            return daoRepository.getRecentSessionsWithLocationForUser(10, user.getId());
        });
    }
}
