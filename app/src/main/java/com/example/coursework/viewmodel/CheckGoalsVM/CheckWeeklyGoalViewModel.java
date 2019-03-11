package com.example.coursework.viewmodel.CheckGoalsVM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;
import com.example.coursework.model.enums.Grades;

public class CheckWeeklyGoalViewModel extends AndroidViewModel {
    DaoRepository daoRepository;
    LiveData<GoalWeekly> goalWeeklyLD;
    LiveData<User> userLD;
    MediatorLiveData<Integer> numberSportProgressLD;
    MediatorLiveData<Integer> numberBoulderProgressLD;
    MediatorLiveData<Grades> averageSportGradeLD;
    MediatorLiveData<Grades> averageBoulderGradeLD;

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

    public MediatorLiveData<Integer> getNumberSportProgressLD() {
        return numberSportProgressLD;
    }

    public void setNumberSportProgressLD(MediatorLiveData<Integer> numberSportProgressLD) {
        this.numberSportProgressLD = numberSportProgressLD;
    }

    public MediatorLiveData<Integer> getNumberBoulderProgressLD() {
        return numberBoulderProgressLD;
    }

    public void setNumberBoulderProgressLD(MediatorLiveData<Integer> numberBoulderProgressLD) {
        this.numberBoulderProgressLD = numberBoulderProgressLD;
    }

    public MediatorLiveData<Grades> getAverageSportGradeLD() {
        return averageSportGradeLD;
    }

    public void setAverageSportGradeLD(MediatorLiveData<Grades> averageSportGradeLD) {
        this.averageSportGradeLD = averageSportGradeLD;
    }

    public MediatorLiveData<Grades> getAverageBoulderGradeLD() {
        return averageBoulderGradeLD;
    }

    public void setAverageBoulderGradeLD(MediatorLiveData<Grades> averageBoulderGradeLD) {
        this.averageBoulderGradeLD = averageBoulderGradeLD;
    }

    public CheckWeeklyGoalViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
    }

}
