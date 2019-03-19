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
        checkSeasonalGoalVM.getUserLD(user.getId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userVal) {
                user = userVal;
            }
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
                        seasonalGoalSummaryTxt.setText(daysRemaining + " Days Remaining");

                }
            }else{
                //No Goal Set
                seasonalGoalSummaryTxt.setText("No Weekly goal found. \n Set a Goals First");
            }
        });
        checkSeasonalGoalVM.getHighestSportOnsightLD().observe(this, highestSportOSVal -> {
            if (highestSportOSVal != null && goalSeasonal != null){
                highestSportOS = highestSportOSVal;

                String output = highestSportOS.toString() + " : " + goalSeasonal.get_highestSportOnsight().toString();
                highestSportOSDisplay.setText(output);
                if (highestSportOS.getValue() > goalSeasonal.get_highestSportOnsight().getValue())
                    highestSportOSDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
            }
        });
        checkSeasonalGoalVM.getHighestBoulderOnsightLD().observe(this, highestBoulderOSVal -> {
            if (highestBoulderOSVal != null && goalSeasonal != null){
                highestBoulderOS = highestBoulderOSVal;

                String output = highestBoulderOS.toString() + " : " + goalSeasonal.get_highestBoulderOnsight().toString();
                highestBoulderOSDisplay.setText(output);
                if (highestBoulderOS.getValue() > goalSeasonal.get_highestBoulderOnsight().getValue())
                    highestBoulderOSDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
            }
        });
        checkSeasonalGoalVM.getHighestSportWorkedLD().observe(this, highestSportWorkedVal -> {
            if (highestSportWorkedVal != null && goalSeasonal != null) {
                highestSportWorked = highestSportWorkedVal;

                String output = highestSportWorked.toString() + " : " + goalSeasonal.get_highestSportWorked().toString();
                highestSportWorkedDisplay.setText(output);
                if (highestSportWorked.getValue() > goalSeasonal.get_highestSportWorked().getValue())
                    highestSportWorkedDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
            }
        });
        checkSeasonalGoalVM.getHighestBoulderWorkedLD().observe(this, highestBoulderWorkedVal -> {
            if (highestBoulderWorkedVal != null && goalSeasonal != null) {
                highestBoulderWorked = highestBoulderWorkedVal;

                String output = highestBoulderWorked.toString() + " : " + goalSeasonal.get_highestBoulderWorked().toString();
                highestBoulderWorkedDisplay.setText(output);
                if (highestBoulderWorked.getValue() > goalSeasonal.get_highestBoulderWorked().getValue())
                    highestBoulderWorkedDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
            }
        });
        //endregion
    }

}
