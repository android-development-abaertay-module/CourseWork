package com.example.coursework.viewmodel.CheckGoalsVM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.RouteType;

import java.time.LocalDateTime;

public class CheckWeeklyGoalViewModel extends AndroidViewModel {
    DaoRepository daoRepository;
    MutableLiveData<LocalDateTime> periodStartLD;
    MutableLiveData<LocalDateTime> periodEndLD;
    LiveData<GoalWeekly> goalWeeklyLD;
    LiveData<User> userLD;
    LiveData<Integer> numberSportProgressLD;
    LiveData<Integer> numberBoulderProgressLD;
    LiveData<Grades> averageSportGradeLD;
    LiveData<Grades> averageBoulderGradeLD;

    public LiveData<GoalWeekly> getGoalWeeklyLD(long userId) {
        if (goalWeeklyLD == null)
            goalWeeklyLD = daoRepository.getMostRecentGoalWeekly(userId);
        return goalWeeklyLD;
    }

    public LiveData<User> getUserLD(long userId) {
        if (userLD == null)
            userLD = daoRepository.getUserById(userId);
        return userLD;
    }

    public LiveData<Integer> getNumberSportProgressLD(long userId) {
        if (numberSportProgressLD == null)
            numberBoulderProgressLD = daoRepository.getNumberRoutesInPeriod(userId, periodStartLD.getValue(), periodEndLD.getValue(), RouteType.SPORT);
        return numberSportProgressLD;
    }

    public LiveData<Integer> getNumberBoulderProgressLD(long userId) {
        if (numberBoulderProgressLD == null)
            numberBoulderProgressLD = daoRepository.getNumberRoutesInPeriod(userId, periodStartLD.getValue(), periodEndLD.getValue(),RouteType.BOULDER);
        return numberBoulderProgressLD;
    }

    public LiveData<Grades> getAverageSportGradeLD(long userId) {
        if (averageSportGradeLD == null)
            averageBoulderGradeLD = daoRepository.getAvgGradeRouteInPeriod(userId, periodStartLD.getValue(), periodEndLD.getValue(),RouteType.SPORT);
        return averageSportGradeLD;
    }


    public LiveData<Grades> getAverageBoulderGradeLD(long userId) {
        if (averageBoulderGradeLD == null)
            averageBoulderGradeLD = daoRepository.getAvgGradeRouteInPeriod(userId, periodStartLD.getValue(), periodEndLD.getValue(),RouteType.BOULDER);
        return averageBoulderGradeLD;
    }


    public CheckWeeklyGoalViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
    }

}
