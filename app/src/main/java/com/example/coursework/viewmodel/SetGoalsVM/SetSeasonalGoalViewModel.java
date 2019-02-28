package com.example.coursework.viewmodel.SetGoalsVM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;

import java.util.List;

public class SetSeasonalGoalViewModel extends AndroidViewModel {
    private DaoRepository daoRepository;
    private LiveData<GoalSeasonal> seasoanlGoalLiveData;
    private LiveData<User> user;

    public SetSeasonalGoalViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        seasoanlGoalLiveData = new MutableLiveData<>();
        user = new MutableLiveData<>();
    }
    public LiveData<User>getUserById(long userId){
        return daoRepository.getUserById(userId);
    }
    public LiveData<GoalSeasonal> getMostRecentSeasonalGoal(long userId) {
        return daoRepository.getMostRecentGoalSeasonal(userId);
    }
}
