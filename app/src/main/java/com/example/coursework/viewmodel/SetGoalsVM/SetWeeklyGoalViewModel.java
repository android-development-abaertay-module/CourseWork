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
    private LiveData<GoalWeekly> weeklyGoalLD;
    private MutableLiveData<User> userLD;


    public SetWeeklyGoalViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        userLD = new MutableLiveData<>();
        weeklyGoalLD = new MutableLiveData<>();
        userLD = new MutableLiveData<>();
    }

    public LiveData<User> getUserLD(){
        return  userLD;
    }
    public void setUserLD(User user){
        userLD.setValue(user);
    }
    public LiveData<GoalWeekly> getWeeklyGoalLD(long userId) {
        weeklyGoalLD = daoRepository.getMostRecentGoalWeekly(userId);
        return weeklyGoalLD;
    }

    public void closeGoalSetWasMet(GoalWeekly weeklyGoal) {
        daoRepository.UpdateGoalSetWasWeeklyGoalMet(weeklyGoal);
    }
    public void updateGoalWeekly(GoalWeekly goalWeekly){
        daoRepository.updateWeeklyGoal(goalWeekly);
    }
    public void createGoalWeekly(GoalWeekly goalWeekly){
        daoRepository.insertWeeklyGoal(goalWeekly);
    }
}
