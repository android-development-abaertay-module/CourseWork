package com.example.coursework.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;
import com.example.coursework.model.enums.PermissionCheck;

import java.util.List;

public class TrainingActivityViewModel extends AndroidViewModel {
    DaoRepository daoRepository;
    LiveData<User> userLD;
    LiveData<Session> currentSessionLD;
    LiveData<List<Route>> recentRoutesLD;
    LiveData<List<Session>> recentSessionsLD;

    MutableLiveData<PermissionCheck> locationPermissionGranted;
    public MutableLiveData<PermissionCheck> getLocationPermissionGranted() {
        if (locationPermissionGranted == null){
            locationPermissionGranted = new MutableLiveData<>();
            locationPermissionGranted.setValue(PermissionCheck.NOT_REQUESTED);
        }
        return locationPermissionGranted;
    }

    public void setLocationPermissionGranted(PermissionCheck locationPermissionGranted) {
        this.locationPermissionGranted.setValue(locationPermissionGranted);
    }

    public LiveData<User> getUserLD(long userId) {
        if (userLD ==null)
            userLD = daoRepository.getUserById(userId);

        return userLD;
    }
    public LiveData<Session> getCurrentSession(long userId) {
        if (currentSessionLD == null)
            currentSessionLD = daoRepository.getCurrentSession(userId);

        return currentSessionLD;
    }
    public LiveData<List<Route>> getRecentRoutesForUserLD(long userId) {
        if (recentRoutesLD == null)
            recentRoutesLD =  daoRepository.getRecentRoutesForUser( 6,  userId);

        return recentRoutesLD;
    }

    public LiveData<List<Session>> getRecentSessionsLD(long userId) {
        //if null check the database as it may be first load
        if (recentSessionsLD == null)
            recentSessionsLD = daoRepository.getRecentSessionsForUser(6,userId);

        return recentSessionsLD;
    }

    public TrainingActivityViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
    }

    public void updateCurrentSession(Session session){
        daoRepository.updateSession(session);
        //updateRecentSessions(6,userLD.getValue().getId());
        //currentSessionLD = null;
    }
    public void CreateNewSession(Session session){
        //save new session to database
        daoRepository.insertSession(session);
        //Live Data will refresh the Session data automatically, updating the UI
    }
    public void addRoute(Route routeToAdd) {
        daoRepository.insertRoute(routeToAdd);
    }
}
