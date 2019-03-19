package com.example.coursework.viewmodel.CheckGoalsVM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalAnnual;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.RouteType;
import com.example.coursework.model.enums.StyleDone;

public class CheckAnnualGoalViewModel extends AndroidViewModel {
    DaoRepository daoRepository;

    MutableLiveData<User> userLD;
    LiveData<GoalAnnual> goalAnnualLD;

    LiveData<Grades> highestBoulderOnsightLD;
    LiveData<Grades> highestSportOnsightLD;
    LiveData<Grades> highestBoulderWorkedLD;
    LiveData<Grades> highestSportWorkedLD;


    public LiveData<GoalAnnual> getGoalAnnualLD() {
        return goalAnnualLD;
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

    public CheckAnnualGoalViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        userLD = new MutableLiveData<>();

        setupLiveData();
    }

    private void setupLiveData() {
        goalAnnualLD = Transformations.switchMap(userLD, user ->
                daoRepository.getMostRecentGoalAnnual(user.getId()));

        highestBoulderOnsightLD = Transformations.switchMap(goalAnnualLD, goal ->
        {
            if (goal != null)
                return daoRepository.getHighestRouteInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.BOULDER, StyleDone.Onsight);
            else
                return daoRepository.noGoalSetReturnNull();
        });
        highestSportOnsightLD = Transformations.switchMap(goalAnnualLD, goal ->
        {
            if (goal != null)
                return daoRepository.getHighestRouteInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.SPORT, StyleDone.Onsight);
            else
                return daoRepository.noGoalSetReturnNull();
        });
        highestBoulderWorkedLD = Transformations.switchMap(goalAnnualLD, goal ->
        {
            if (goal != null)
                return daoRepository.getHighestRouteInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.BOULDER, StyleDone.Worked);
            else
                return daoRepository.noGoalSetReturnNull();
        });
        highestSportWorkedLD = Transformations.switchMap(goalAnnualLD, goal ->
        {
            if (goal != null)
                return daoRepository.getHighestRouteInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.SPORT, StyleDone.Worked);
            else
                return daoRepository.noGoalSetReturnNull();
        });
    }
}
