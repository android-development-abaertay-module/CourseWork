package com.example.coursework.viewmodel.CheckGoalsVM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.RouteType;
import com.example.coursework.model.enums.StyleDone;

public class CheckSeasonalGoalViewModel extends AndroidViewModel {
    DaoRepository daoRepository;

    MutableLiveData<User> userLD;
    LiveData<GoalSeasonal> goalSeasonalLD;

    LiveData<Grades> highestBoulderOnsightLD;
    LiveData<Grades> highestSportOnsightLD;
    LiveData<Grades> highestBoulderWorkedLD;
    LiveData<Grades> highestSportWorkedLD;


    public LiveData<GoalSeasonal> getGoalSeasonalLD() {
        return goalSeasonalLD;
    }

    public MutableLiveData<User> getUserLD() {
        return userLD;
    }
    public void setUserLD(User user) {
        userLD.setValue(user);
    }

    public LiveData<Grades> getHighestBoulderOnsightLD() {
        return highestBoulderOnsightLD;
    }
    public LiveData<Grades> getHighestSportOnsightLD() {
        return highestSportOnsightLD;
    }
    public LiveData<Grades> getHighestBoulderWorkedLD() {
        return highestBoulderWorkedLD;
    }
    public LiveData<Grades> getHighestSportWorkedLD() {
        return highestSportWorkedLD;
    }

    public CheckSeasonalGoalViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        userLD = new MutableLiveData<>();

        setupLiveData();
    }

    private void setupLiveData() {
        goalSeasonalLD = Transformations.switchMap(userLD, user ->
                daoRepository.getMostRecentGoalSeasonal(user.getId()));

        highestBoulderOnsightLD = Transformations.switchMap(goalSeasonalLD, goal ->
        {
            if (goal != null)
                return daoRepository.getHighestRouteInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.BOULDER, StyleDone.Onsight);
            else
                return daoRepository.noGoalSetReturnNull();
        });
        highestSportOnsightLD = Transformations.switchMap(goalSeasonalLD, goal ->
        {
            if (goal != null)
                return daoRepository.getHighestRouteInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.SPORT, StyleDone.Onsight);
            else
                return daoRepository.noGoalSetReturnNull();
        });
        highestBoulderWorkedLD = Transformations.switchMap(goalSeasonalLD, goal ->
        {
            if (goal != null)
                return daoRepository.getHighestRouteInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.BOULDER, StyleDone.Worked);
            else
                return daoRepository.noGoalSetReturnNull();
        });
        highestSportWorkedLD = Transformations.switchMap(goalSeasonalLD, goal ->
        {
            if (goal != null)
                return daoRepository.getHighestRouteInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.SPORT, StyleDone.Worked);
            else
                return daoRepository.noGoalSetReturnNull();
        });
    }
}
