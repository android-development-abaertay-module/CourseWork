package com.example.coursework.view.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.model.User;
import com.example.coursework.model.helper.GoalCheckDTO;
import com.example.coursework.viewmodel.CheckGoalsVM.CheckSeasonalGoalViewModel;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class CheckSeasonalGoal extends Fragment {


    private User user;

    private TextView highestSportOSDisplay;
    private TextView highestBoulderOSDisplay;
    private TextView highestSportWorkedDisplay;
    private TextView highestBoulderWorkedDisplay;
    private TextView seasonalGoalSummaryTxt;
    private TextView isSeasonalGoalAchievedTat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.check_seasonal_goal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CheckSeasonalGoalViewModel checkSeasonalGoalVM = ViewModelProviders.of(this).get(CheckSeasonalGoalViewModel.class);

        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        if (intent != null){
            if (intent.hasExtra(USER_ID)){
                user = new User(intent.getStringExtra(USERNAME));
                user.setId(intent.getLongExtra(USER_ID,0));
                checkSeasonalGoalVM.setUserLD(user);
            }
        }

        //region [Declare properties]
        highestSportOSDisplay = Objects.requireNonNull(getView()).findViewById(R.id.checkGoalSeasonalSportOSTV);
        highestBoulderOSDisplay = getView().findViewById(R.id.checkGoalSeasonalBoulderOSTV);
        highestSportWorkedDisplay = getView().findViewById(R.id.checkGoalSeasonalSportWorkedTV);
        highestBoulderWorkedDisplay = getView().findViewById(R.id.checkGoalSeasonalBoulderWorkedTV);
        seasonalGoalSummaryTxt = getView().findViewById(R.id.seasonalGoalSummaryTxt);
        isSeasonalGoalAchievedTat = getView().findViewById(R.id.isSeasonalGoalActiveTxt);
        //endregion


        //region [Register Observers]
        checkSeasonalGoalVM.getUserLD().observe(this, userVal -> user = userVal);

        checkSeasonalGoalVM.getGoalSeasonalLD().observe(this, goalSeasonalVal -> {
            if (goalSeasonalVal != null){
                user.setSeasonalGoal(goalSeasonalVal);
                if (user.getSeasonalGoal().getGoalAchieved())
                    isSeasonalGoalAchievedTat.setText(R.string.goal_achived);
                else
                    isSeasonalGoalAchievedTat.setText("");
                if (user.getSeasonalGoal().getDateExpires().isBefore(OffsetDateTime.now())){
                    //goal has expired...
                    seasonalGoalSummaryTxt.setText(R.string.goal_expired_summary);
                }else{
                    //display days remaining
                    int daysRemaining = (int) Duration.between(OffsetDateTime.now(),user.getSeasonalGoal().getDateExpires()).toDays();
                    if (daysRemaining == 0)
                        seasonalGoalSummaryTxt.setText(R.string.goal_expires_today);
                    else
                        seasonalGoalSummaryTxt.setText(getString(R.string.days_remaining_for_goal,daysRemaining+""));
                }
            }else{
                //No Goal Set
                seasonalGoalSummaryTxt.setText("No Seasonal Goal found. \n Set a Goals First");
            }
        });
        checkSeasonalGoalVM.getHighestSportOnsightLD().observe(this, highestSportOSVal -> {
            if (user.getSeasonalGoal() != null)
                updateView(user.getSeasonalGoal().checkGoalAvgGradeTypeX(highestSportOSVal,user.getSeasonalGoal().getHighestSportOnsight(),"No Sport Routes Logged."), highestSportOSDisplay);
        });
        checkSeasonalGoalVM.getHighestBoulderOnsightLD().observe(this, highestBoulderOSVal -> {
            if (user.getSeasonalGoal() != null)
                updateView(user.getSeasonalGoal().checkGoalAvgGradeTypeX(highestBoulderOSVal,user.getSeasonalGoal().getHighestBoulderOnsight(),"No Boulder Routes Logged"), highestBoulderOSDisplay);
        });
        checkSeasonalGoalVM.getHighestSportWorkedLD().observe(this, highestSportWorkedVal -> {
            if (user.getSeasonalGoal() != null)
                updateView(user.getSeasonalGoal().checkGoalAvgGradeTypeX(highestSportWorkedVal,user.getSeasonalGoal().getHighestSportWorked(),"No Sport Routes Logged"), highestSportWorkedDisplay);
        });
        checkSeasonalGoalVM.getHighestBoulderWorkedLD().observe(this, highestBoulderWorkedVal -> {
            if (user.getSeasonalGoal() != null)
                updateView(user.getSeasonalGoal().checkGoalAvgGradeTypeX(highestBoulderWorkedVal, user.getSeasonalGoal().getHighestBoulderWorked(),"No Boulder Routes Logged"), highestBoulderWorkedDisplay);
        });
        //endregion
    }

    private void updateView(GoalCheckDTO result, TextView displayTextView) {
            displayTextView.setText(result.getOutput());
            if (result.getIsAchieved())
                displayTextView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getView()).getContext(), R.color.green));
            else
                displayTextView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getView()).getContext(), R.color.red));
    }
}
