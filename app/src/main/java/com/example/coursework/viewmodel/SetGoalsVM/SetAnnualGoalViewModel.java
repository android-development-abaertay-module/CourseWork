package com.example.coursework.viewmodel.SetGoalsVM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.coursework.model.GoalAnnual;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;

public class SetAnnualGoalViewModel extends AndroidViewModel {
    private DaoRepository daoRepository;
    private LiveData<GoalAnnual> annualGoalLD;
    private LiveData<User> userLD;

    public SetAnnualGoalViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        annualGoalLD = new MutableLiveData<>();
        userLD = new MutableLiveData<>();
    }
    public LiveData<User> getUserDL(long userId){
        userLD = daoRepository.getUserById(userId);
        return userLD;
    }
    public LiveData<GoalAnnual> getAnnualGoalLD(long userId) {
        annualGoalLD = daoRepository.getMostRecentGoalAnnual(userId);
        return annualGoalLD;
    }

    public void createGoalAnnual(GoalAnnual annualGoal) {
        daoRepository.insertAnnualGoal(annualGoal);
    }

    public void updateGoalAnnual(GoalAnnual annualGoal) {
        daoRepository.updateAnnualGoal(annualGoal);
    }

    public void closeAnnualGoalSetWasMet(GoalAnnual annualGoal) {
        daoRepository.updateGoalSetWasAnnualGoalMet(annualGoal);
    }
}
