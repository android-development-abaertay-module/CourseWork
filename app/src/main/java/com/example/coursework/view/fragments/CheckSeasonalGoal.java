package com.example.coursework.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.helper.GoalCheckDTO;
import com.example.coursework.model.helper.PrintNull;
import com.example.coursework.viewmodel.CheckGoalsVM.CheckSeasonalGoalViewModel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class CheckSeasonalGoal extends Fragment {

    private CheckSeasonalGoalViewModel checkSeasonalGoalVM;
    private GoalSeasonal goalSeasonal;
    private User user;
    private Grades highestSportOS;
    private Grades highestBoulderOS;
    private Grades highestSportWorked;
    private Grades highestBoulderWorked;

    private TextView highestSportOSDisplay;
    private TextView highestBoulderOSDisplay;
    private TextView highestSportWorkedDisplay;
    private TextView highestBoulderWorkedDisplay;
    private TextView seasonalGoalSummaryTxt;


    public static CheckSeasonalGoal newInstance() {
        return new CheckSeasonalGoal();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.check_seasonal_goal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkSeasonalGoalVM = ViewModelProviders.of(this).get(CheckSeasonalGoalViewModel.class);

        Intent intent = getActivity().getIntent();
        if (intent != null){
            if (intent.hasExtra(USER_ID)){
                user = new User(intent.getStringExtra(USERNAME));
                user.setId(intent.getLongExtra(USER_ID,0));
                checkSeasonalGoalVM.setUserLD(user);
            }
        }

        //region [Declare properties]
        highestSportOSDisplay = getView().findViewById(R.id.checkGoalSeasonalSportOSTV);
        highestBoulderOSDisplay = getView().findViewById(R.id.checkGoalSeasonalBoulderOSTV);
        highestSportWorkedDisplay = getView().findViewById(R.id.checkGoalSeasonalSportWorkedTV);
        highestBoulderWorkedDisplay = getView().findViewById(R.id.checkGoalSeasonalBoulderWorkedTV);
        seasonalGoalSummaryTxt = getView().findViewById(R.id.seasonalGoalSummaryTxt);
        //endregion


        //region [Register Observers]
        checkSeasonalGoalVM.getUserLD(user.getId()).observe(this, userVal -> {
            user = userVal;
        });
        checkSeasonalGoalVM.getGoalSeasonalLD(user.getId()).observe(this, goalSeasonalVal -> {
            if (goalSeasonalVal != null){
                goalSeasonal = goalSeasonalVal;
                if (goalSeasonal.getDateExpires().isBefore(OffsetDateTime.now())){
                    //goal has expired...
                    seasonalGoalSummaryTxt.setText(R.string.goal_expired_summary);
                }else{
                    //display days remaining
                    int daysRemaining = (int) Duration.between(OffsetDateTime.now(),goalSeasonal.getDateExpires()).toDays();
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
            highestSportOS = highestSportOSVal;
            if (goalSeasonal != null){
                GoalCheckDTO result = goalSeasonal.checkHighestSportOnsightGoal(highestSportOS);
                highestSportOSDisplay.setText(result.getOutput());
                if (result.getIsAchieved())
                    highestSportOSDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
                else
                    highestSportOSDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.red));
            }
        });
        checkSeasonalGoalVM.getHighestBoulderOnsightLD().observe(this, highestBoulderOSVal -> {
            highestBoulderOS = highestBoulderOSVal;
            if (goalSeasonal != null){
                GoalCheckDTO result = goalSeasonal.checkHighestBoulderOnsightGoal(highestBoulderOS);
                highestBoulderOSDisplay.setText(result.getOutput());
                if (result.getIsAchieved())
                    highestBoulderOSDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
                else
                    highestBoulderOSDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.red));
            }
        });
        checkSeasonalGoalVM.getHighestSportWorkedLD().observe(this, highestSportWorkedVal -> {
            highestSportWorked = highestSportWorkedVal;
            if (goalSeasonal != null) {
                GoalCheckDTO result = goalSeasonal.checkHighestSportWorkedGoal(highestSportWorked);
                highestSportWorkedDisplay.setText(result.getOutput());
                if (result.getIsAchieved())
                    highestSportWorkedDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
                else
                    highestSportWorkedDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.red));
            }
        });
        checkSeasonalGoalVM.getHighestBoulderWorkedLD().observe(this, highestBoulderWorkedVal -> {
            highestBoulderWorked = highestBoulderWorkedVal;
            if ( goalSeasonal != null) {
               GoalCheckDTO result = goalSeasonal.checkHighestBoulderWorkedGoal(highestBoulderWorked);
               highestBoulderWorkedDisplay.setText(result.getOutput());
               if (result.getIsAchieved())
                   highestBoulderWorkedDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
               else
                   highestBoulderWorkedDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.red));
            }
        });
        //endregion
    }

}
