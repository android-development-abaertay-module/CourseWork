package com.example.coursework.view.fragments;

import android.arch.lifecycle.Observer;
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
import com.example.coursework.model.GoalAnnual;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.viewmodel.CheckGoalsVM.CheckAnnualGoalViewModel;

import java.time.Duration;
import java.time.OffsetDateTime;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class CheckAnnualGoal extends Fragment {

    private CheckAnnualGoalViewModel checkGoalAnnualVM;
    private GoalAnnual goalAnnual;
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

    public static CheckAnnualGoal newInstance() {
        return new CheckAnnualGoal();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.check_annual_goal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkGoalAnnualVM = ViewModelProviders.of(this).get(CheckAnnualGoalViewModel.class);

        Intent intent = getActivity().getIntent();
        if (intent != null){
            if (intent.hasExtra(USER_ID)){
                user = new User(intent.getStringExtra(USERNAME));
                user.setId(intent.getLongExtra(USER_ID,0));
                checkGoalAnnualVM.setUserLD(user);
            }
        }

        //region [Declare properties]
        highestSportOSDisplay = getView().findViewById(R.id.checkGoalAnnualSportOSTV);
        highestBoulderOSDisplay = getView().findViewById(R.id.checkGoalAnnualBoulderOSTV);
        highestSportWorkedDisplay = getView().findViewById(R.id.checkGoalAnnualSportWorkedTV);
        highestBoulderWorkedDisplay = getView().findViewById(R.id.checkGoalAnnualBoulderWorkedTV);
        seasonalGoalSummaryTxt = getView().findViewById(R.id.annualGoalSummaryTxt);
        //endregion


        //region [Register Observers]
        checkGoalAnnualVM.getUserLD().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userVal) {
                user = userVal;
            }
        });
        checkGoalAnnualVM.getGoalAnnualLD().observe(this, goalSeasonalVal -> {
            if (goalSeasonalVal != null){
                goalAnnual = goalSeasonalVal;
                if (goalAnnual.getDateExpires().isBefore(OffsetDateTime.now())){
                    //goal has expired...
                    seasonalGoalSummaryTxt.setText(R.string.goal_expired_summary);
                }else{
                    //display days remaining
                    int daysRemaining = (int) Duration.between(OffsetDateTime.now(), goalAnnual.getDateExpires()).toDays();
                    if (daysRemaining == 0)
                        seasonalGoalSummaryTxt.setText(R.string.goal_expires_today);
                    else
                        seasonalGoalSummaryTxt.setText(daysRemaining + " Days Remaining");

                }
            }else{
                //No Goal Set
                seasonalGoalSummaryTxt.setText("No Seasonal Goal found. \n Set a Goals First");
            }
        });
        checkGoalAnnualVM.getHighestSportOnsightLD().observe(this, highestSportOSVal -> {
            highestSportOS = highestSportOSVal;
            if (goalAnnual != null){
                if (highestSportOSVal != null){
                    String output = highestSportOS.toString() + " : " + goalAnnual.getHighestSportOnsight().toString();
                    highestSportOSDisplay.setText(output);
                    if (highestSportOS.getValue() > goalAnnual.getHighestSportOnsight().getValue())
                        highestSportOSDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
                }else {
                    //no routes in period
                    highestSportOSDisplay.setText(R.string.no_routes_completed);
                    highestSportOSDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.red));
                }
            }
        });
        checkGoalAnnualVM.getHighestBoulderOnsightLD().observe(this, highestBoulderOSVal -> {
            highestBoulderOS = highestBoulderOSVal;
            if (goalAnnual != null){
                if (highestBoulderOSVal != null){
                    String output = highestBoulderOS.toString() + " : " + goalAnnual.getHighestBoulderOnsight().toString();
                    highestBoulderOSDisplay.setText(output);
                    if (highestBoulderOS.getValue() > goalAnnual.getHighestBoulderOnsight().getValue())
                        highestBoulderOSDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
                }else{
                    //no routes done in period
                    highestBoulderOSDisplay.setText(R.string.no_routes_completed);
                    highestBoulderOSDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.red));
                }
            }
        });
        checkGoalAnnualVM.getHighestSportWorkedLD().observe(this, highestSportWorkedVal -> {
            highestSportWorked = highestSportWorkedVal;
            if (goalAnnual != null) {
                if (highestSportWorkedVal != null){
                    String output = highestSportWorked.toString() + " : " + goalAnnual.getHighestSportWorked().toString();
                    highestSportWorkedDisplay.setText(output);
                    if (highestSportWorked.getValue() > goalAnnual.getHighestSportWorked().getValue())
                        highestSportWorkedDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
                }else{
                    //no routes logged in period yet
                    highestSportWorkedDisplay.setText(R.string.no_routes_completed);
                    highestSportWorkedDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.red));
                }
            }
        });
        checkGoalAnnualVM.getHighestBoulderWorkedLD().observe(this, highestBoulderWorkedVal -> {
            highestBoulderWorked = highestBoulderWorkedVal;
            if ( goalAnnual != null) {
                if (highestBoulderWorked != null){
                    String output = highestBoulderWorked.toString() + " : " + goalAnnual.getHighestBoulderWorked().toString();
                    highestBoulderWorkedDisplay.setText(output);
                    if (highestBoulderWorked.getValue() > goalAnnual.getHighestBoulderWorked().getValue())
                        highestBoulderWorkedDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.green));
                }else{
                    //no routes logged in period yet
                    highestBoulderWorkedDisplay.setText(R.string.no_routes_completed);
                    highestBoulderWorkedDisplay.setTextColor(ContextCompat.getColor(getView().getContext(),R.color.red));
                }
            }
        });
        //endregion
    }

}
