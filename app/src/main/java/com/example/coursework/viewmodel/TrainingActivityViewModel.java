package com.example.coursework.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalAnnual;
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
    private LiveData<GoalAnnual> goalAnnualLD;

    private MutableLiveData<PermissionCheck> locationPermissionGranted;
    private MutableLiveData<Double> currentLatitudeLD;
    private MutableLiveData<Double> currentLongitudeLD;
    private MutableLiveData<Boolean> addRouteFormVisible;

    public MutableLiveData<Boolean> getAddRouteFormVisible() {
        return addRouteFormVisible;
    }
    public void setAddRouteFormVisible(boolean isAddRouteFormVisible) {
        this.addRouteFormVisible.setValue(isAddRouteFormVisible);
    }

    public LiveData<GoalWeekly> getGoalWeeklyLD() {
        return goalWeeklyLD;
    }
    public LiveData<GoalSeasonal> getGoalSeasonalLD() {
        return goalSeasonalLD;
    }
    public LiveData<GoalAnnual> getGoalAnnualLD() {
        return goalAnnualLD;
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

    public LiveData<Session> getCurrentSession() {
        return currentSessionLD;
    }
    public LiveData<List<Route>> getRecentRoutesForUserLD() {
        return recentRoutesLD;
    }
    public LiveData<List<Session>> getRecentSessionsLD() {
        return recentSessionsLD;
    }

    public TrainingActivityViewModel(@NonNull Application application) {
        super(application);
        userLD = new MutableLiveData<>();
        addRouteFormVisible = new MutableLiveData<>();
        daoRepository = new DaoRepository(application);
        goalWeeklyLD = Transformations.switchMap(userLD, user -> daoRepository.getMostRecentGoalWeekly(getUserIdForQuery(user)));
        goalSeasonalLD = Transformations.switchMap(userLD,user -> daoRepository.getMostRecentGoalSeasonal(getUserIdForQuery(user)));
        goalAnnualLD = Transformations.switchMap(userLD, user -> daoRepository.getMostRecentGoalAnnual(getUserIdForQuery(user)));
        currentSessionLD = Transformations.switchMap(userLD, user -> daoRepository.getCurrentSession(getUserIdForQuery(user)));
        recentRoutesLD = Transformations.switchMap(userLD, user -> daoRepository.getRecentRoutesForUser(6, getUserIdForQuery(user)));
        recentSessionsLD = Transformations.switchMap(userLD, user -> daoRepository.getRecentSessionsForUser(6, getUserIdForQuery(user)));
    }

    private long getUserIdForQuery(User user) {
        long id = -1;
        if (user != null)
            id = user.getId();
        return id;
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

    public void updateGoalSetWasWeeklyGoalMet(GoalWeekly weeklyGoal) {
        daoRepository.UpdateGoalSetWasWeeklyGoalMet(weeklyGoal);
    }
    public void updateGoalSetWasSeasonalGoalMet(GoalSeasonal seasonalGoal) {
        daoRepository.UpdateGoalSetWasSeasonalGoalMet(seasonalGoal);
    }
    public void updateGoalSetWasAnnualGoalMet(GoalAnnual goalAnnual){
        daoRepository.updateGoalSetWasAnnualGoalMet(goalAnnual);
    }

    public void updateGoalWeekly(GoalWeekly goalWeekly) {
        daoRepository.updateWeeklyGoal(goalWeekly);
    }
    public void updateGoalSeasonal(GoalSeasonal goalSeasonal){
        daoRepository.updateSeasonalGoal(goalSeasonal);
    }
    public void updateGoalAnnual(GoalAnnual goalAnnual){
        daoRepository.updateAnnualGoal(goalAnnual);
    }
}
