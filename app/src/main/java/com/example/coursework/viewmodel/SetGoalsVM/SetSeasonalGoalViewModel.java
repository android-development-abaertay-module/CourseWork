package com.example.coursework.viewmodel.SetGoalsVM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;

public class SetSeasonalGoalViewModel extends AndroidViewModel {
    private DaoRepository daoRepository;
    private LiveData<GoalSeasonal> seasonalGoalLD;
    private LiveData<User> userLD;

    public SetSeasonalGoalViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        seasonalGoalLD = new MutableLiveData<>();
        userLD = new MutableLiveData<>();
    }
    public LiveData<User> getUserLD(long userId){
        userLD = daoRepository.getUserById(userId);
        return userLD;
    }
    public LiveData<GoalSeasonal> getSeasonalGoalLD(long userId) {
        seasonalGoalLD = daoRepository.getMostRecentGoalSeasonal(userId);
        return seasonalGoalLD;
    }
}
