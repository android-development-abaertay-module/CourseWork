package com.example.coursework.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;
import com.example.coursework.model.helper.MapMediator;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class MainMapActivityViewModel extends AndroidViewModel {

    DaoRepository daoRepository;
    MutableLiveData<User> userLD;
    MutableLiveData<GoogleMap> mapLD;
    LiveData<List<Session>> recentSessionsLD;
    MediatorLiveData<MapMediator> mediator;

    public void setUserLD(User user) {
        userLD.setValue(user);
    }
    public MutableLiveData<User> getUserLD(){
        return userLD;
    }

    public MutableLiveData<GoogleMap> getMapLD() {
        return mapLD;
    }

    public void setMapLD(MutableLiveData<GoogleMap> mapLD) {
        this.mapLD = mapLD;
    }

    public LiveData<List<Session>> getRecentSessionsLD() {
        return recentSessionsLD;
    }

    public MediatorLiveData<MapMediator> getMediator(){
        return mediator;
    }
    public MainMapActivityViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        userLD = new MutableLiveData<>();
        mapLD = new MutableLiveData<>();

        recentSessionsLD = Transformations.switchMap(userLD, (User user) -> {
            return daoRepository.getRecentSessionsWithLocationForUser(10, user.getId());
        });

        mediator = new MediatorLiveData<>();
        mediator.setValue(new MapMediator(null,null));
        mediator.addSource(recentSessionsLD, sessions -> {
            MapMediator med = mediator.getValue();
            med.setRecentSessions(sessions);
            mediator.setValue(med);
        });
        mediator.addSource(mapLD, map ->{
            MapMediator med = mediator.getValue();
            med.setMap(map);
            mediator.setValue(med);
        });
    }
}
