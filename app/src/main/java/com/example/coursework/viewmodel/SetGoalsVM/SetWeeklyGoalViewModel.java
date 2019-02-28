package com.example.coursework.viewmodel.SetGoalsVM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;

public class SetWeeklyGoalViewModel extends AndroidViewModel {
    private DaoRepository daoRepository;
    private LiveData<GoalWeekly> weeklyGoalLiveData;
    private LiveData<User> user;

    public LiveData<GoalWeekly> getWeeklyGoalLiveData() {
        return weeklyGoalLiveData;
    }

    public SetWeeklyGoalViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        weeklyGoalLiveData = new MutableLiveData<>();
        user = new MutableLiveData<>();
    }

    public LiveData<User>getUserById(long userId){
        return daoRepository.getUserById(userId);
    }
    public LiveData<GoalWeekly> getMostRecentWeeklyGoal(long userId) {
        return daoRepository.getMostRecentGoalWeekly(userId);
    }

    public void closeGoalSetWasMet(GoalWeekly weeklyGoal) {
        daoRepository.closeGoalSetWasWeeklyGoalMet(weeklyGoal);
    }
    public void updateGoalWeekly(GoalWeekly goalWeekly){
        daoRepository.updateWeeklyGoal(goalWeekly);
    }
    public void createGoalWeekly(GoalWeekly goalWeekly){
        daoRepository.insertWeeklyGoal(goalWeekly);
    }
}
