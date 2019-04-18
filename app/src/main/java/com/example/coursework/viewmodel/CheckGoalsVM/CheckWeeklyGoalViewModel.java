package com.example.coursework.viewmodel.CheckGoalsVM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.RouteType;

public class CheckWeeklyGoalViewModel extends AndroidViewModel {
    private DaoRepository daoRepository;

    private MutableLiveData<User> userLD;

    private LiveData<GoalWeekly> goalWeeklyLD;
    private LiveData<Integer> numberSportProgressLD;
    private LiveData<Integer> numberBoulderProgressLD;
    private LiveData<Grades> averageSportGradeLD;
    private LiveData<Grades> averageBoulderGradeLD;


    public LiveData<GoalWeekly> getGoalWeeklyLD() {
        return goalWeeklyLD;
    }

    public MutableLiveData<User> getUserLD() {
        return userLD;
    }
    public void setUserLD(User user) {
        userLD.setValue(user);
    }

    public LiveData<Integer> getNumberSportProgressLD() {
        return numberSportProgressLD;
    }

    public LiveData<Integer> getNumberBoulderProgressLD() {
        return numberBoulderProgressLD;
    }

    public LiveData<Grades> getAverageSportGradeLD() {
        return averageSportGradeLD;
    }


    public LiveData<Grades> getAverageBoulderGradeLD() {
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
                return daoRepository.getNumberRoutesInPeriod(goal.getUserId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.SPORT);
            else
                return daoRepository.noGoalSetReturnZeroCount();
        });
        numberBoulderProgressLD = Transformations.switchMap(goalWeeklyLD,goal ->
        {
            if (goal != null)
                return daoRepository.getNumberRoutesInPeriod(goal.getUserId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.BOULDER);
            else
                return daoRepository.noGoalSetReturnZeroCount();
        });
        averageSportGradeLD = Transformations.switchMap(goalWeeklyLD,goal ->
        {
            if (goal != null)
                return daoRepository.getAvgGradeRouteInPeriod(goal.getUserId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.SPORT);
            else
                return daoRepository.noGoalSetReturnNull();
        });
        averageBoulderGradeLD = Transformations.switchMap(goalWeeklyLD,goal ->
        {
            if (goal != null)
                return daoRepository.getAvgGradeRouteInPeriod(goal.getUserId(), goal.getDateCreated(), goal.getDateExpires(), RouteType.BOULDER);
            else
                return daoRepository.noGoalSetReturnNull();
        });
    }
}
