package com.example.coursework.viewmodel.SetGoalsVM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;

public class SetWeeklyGoalViewModel extends AndroidViewModel {
    private DaoRepository daoRepository;
    private LiveData<GoalWeekly> weeklyGoalLiveData;
    private LiveData<User> user;

    public DaoRepository getDaoRepository() {
        return daoRepository;
    }

    public LiveData<GoalWeekly> getWeeklyGoalLiveData() {
        return weeklyGoalLiveData;
    }

    public SetWeeklyGoalViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
    }

    private LiveData<GoalWeekly> getMostRecentWeeklyGoal(long userId) {
        return daoRepository.getMostRecentGoalWeekly(userId);
    }
}
