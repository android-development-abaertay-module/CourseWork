package com.example.coursework.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;
import com.example.coursework.model.enums.PermissionCheck;

import java.util.List;

public class TrainingActivityViewModel extends AndroidViewModel {
    private DaoRepository daoRepository;
    private MutableLiveData<User> userLD;
    private LiveData<Session> currentSessionLD;
    private LiveData<List<Route>> recentRoutesLD;
    private LiveData<List<Session>> recentSessionsLD;
    private LiveData<GoalWeekly> goalWeeklyLD;
    private LiveData<GoalSeasonal> goalSeasonalLD;

    private MutableLiveData<PermissionCheck> locationPermissionGranted;
    private MutableLiveData<Double> currentLatitudeLD;
    private MutableLiveData<Double> currentLongitudeLD;

    public LiveData<GoalWeekly> getGoalWeeklyLD(long userId) {
        if (goalWeeklyLD == null)
            goalWeeklyLD = daoRepository.getMostRecentGoalWeekly(userId);
        return goalWeeklyLD;
    }

    public LiveData<GoalSeasonal> getGoalSeasonalLD(long userId) {
        if (goalSeasonalLD == null)
            goalSeasonalLD = daoRepository.getMostRecentGoalSeasonal(userId);
        return goalSeasonalLD;
    }

    public MutableLiveData<Double> getCurrentLatitudeLD() {
        if (currentLatitudeLD == null)
            currentLatitudeLD = new MutableLiveData<>();
        return currentLatitudeLD;
    }

    public void setCurrentLatitudeLD(double currentLatitude) {
        this.currentLatitudeLD.setValue(currentLatitude);
    }

    public MutableLiveData<Double> getCurrentLongitudeLD() {
        if (currentLongitudeLD == null)
            currentLongitudeLD = new MutableLiveData<>();
        return currentLongitudeLD;
    }

    public void setCurrentLongitudeLD(double currentLongitude) {
        this.currentLongitudeLD.setValue(currentLongitude);
    }

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

    public MutableLiveData<User> getUserLD() {
        return userLD;
    }
    public  void setUserLD(User user){
        userLD.setValue(user);
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
        userLD = new MutableLiveData<>();
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

    public void deleteRoute(Route routeToDelete) {
        daoRepository.deleteRoute(routeToDelete);
    }
    public void deleteSession(Session sessionToDelete) {
        daoRepository.deleteSession(sessionToDelete);
    }

    public void UpdateGoalSetWasWeeklyGoalMet(GoalWeekly weeklyGoal) {
        daoRepository.UpdateGoalSetWasWeeklyGoalMet(weeklyGoal);
    }
    public void UpdateGoalSetWasSeasonalGoalMet(GoalSeasonal seasonalGoal) {
        daoRepository.UpdateGoalSetWasSeasonalGoalMet(seasonalGoal);
    }
}
