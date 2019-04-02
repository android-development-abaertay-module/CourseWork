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
    private MutableLiveData<User> userLD;

    public SetSeasonalGoalViewModel(@NonNull Application application) {
        super(application);
        userLD = new MutableLiveData<>();
        daoRepository = new DaoRepository(application);
        seasonalGoalLD = new MutableLiveData<>();
        userLD = new MutableLiveData<>();
    }
    public LiveData<User> getUserLD(){
        return userLD;
    }
    public void setUserLD(User user){
        userLD.setValue(user);
    }
    public LiveData<GoalSeasonal> getSeasonalGoalLD(long userId) {
        seasonalGoalLD = daoRepository.getMostRecentGoalSeasonal(userId);
        return seasonalGoalLD;
    }

    public void createGoalSeasonal(GoalSeasonal seasonalGoal) {
        daoRepository.insertSeasonalGoal(seasonalGoal);
    }

    public void updateGoalSeasonal(GoalSeasonal seasonalGoal) {
        daoRepository.updateSeasonalGoal(seasonalGoal);
    }

    public void closeSeasonalGoalSetWasMet(GoalSeasonal seasonalGoal) {
        daoRepository.closeGoalSetWasSeasonalGoalMet(seasonalGoal);
    }
}
