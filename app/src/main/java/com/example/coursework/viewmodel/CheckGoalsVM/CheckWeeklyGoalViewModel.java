package com.example.coursework.viewmodel.CheckGoalsVM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.RouteType;

public class CheckWeeklyGoalViewModel extends AndroidViewModel {
    DaoRepository daoRepository;

    MutableLiveData<User> userLD;

    LiveData<GoalWeekly> goalWeeklyLD;
    LiveData<Integer> numberSportProgressLD;
    LiveData<Integer> numberBoulderProgressLD;
    LiveData<Grades> averageSportGradeLD;
    LiveData<Grades> averageBoulderGradeLD;



    public LiveData<GoalWeekly> getGoalWeeklyLD(long userId) {
        return goalWeeklyLD;
    }

    public MutableLiveData<User> getUserLD(long userId) {
        return userLD;
    }
    public void setUserLD(User user) {
        userLD.setValue(user);
    }

    public LiveData<Integer> getNumberSportProgressLD(long userId) {
        return numberSportProgressLD;
    }

    public LiveData<Integer> getNumberBoulderProgressLD(long userId) {
        return numberBoulderProgressLD;
    }

    public LiveData<Grades> getAverageSportGradeLD(long userId) {
        return averageSportGradeLD;
    }


    public LiveData<Grades> getAverageBoulderGradeLD(long userId) {
        return averageBoulderGradeLD;
    }


    public CheckWeeklyGoalViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        userLD = new MutableLiveData<>();

       setupLiveData();
    }

    private void setupLiveData() {
        goalWeeklyLD = Transformations.switchMap(userLD,user ->
                daoRepository.getMostRecentGoalWeekly(user.getId()));

        numberSportProgressLD = Transformations.switchMap(goalWeeklyLD,goal ->
        {
            if (goal != null)
                return daoRepository.getNumberRoutesInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.SPORT);
            else
                return daoRepository.noGoalSetReturnZeroCount();
        });
        numberBoulderProgressLD = Transformations.switchMap(goalWeeklyLD,goal ->
        {
            if (goal != null)
                return daoRepository.getNumberRoutesInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.BOULDER);
            else
                return daoRepository.noGoalSetReturnZeroCount();
        });
        averageSportGradeLD = Transformations.switchMap(goalWeeklyLD,goal ->
        {
            if (goal != null)
                return daoRepository.getAvgGradeRouteInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.SPORT);
            else
                return daoRepository.noGoalSetReturnNull();
        });
        averageBoulderGradeLD = Transformations.switchMap(goalWeeklyLD,goal ->
        {
            if (goal != null)
                return daoRepository.getAvgGradeRouteInPeriod(userLD.getValue().getId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.BOULDER);
            else
                return daoRepository.noGoalSetReturnNull();
        });
    }
}
