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
import com.example.coursework.viewmodel.CheckGoalsVM.CheckAnnualGoalViewModel;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class CheckAnnualGoal extends Fragment {

    private User user;

    private TextView highestSportOSDisplay;
    private TextView highestBoulderOSDisplay;
    private TextView highestSportWorkedDisplay;
    private TextView highestBoulderWorkedDisplay;
    private TextView annualGoalSummaryTxt;
    private TextView isAnnualGoalAchievedTat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.check_annual_goal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CheckAnnualGoalViewModel checkGoalAnnualVM = ViewModelProviders.of(this).get(CheckAnnualGoalViewModel.class);

        //throw exception if get activity returns null, wont happen
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        if (intent != null){
            if (intent.hasExtra(USER_ID)){
                user = new User(intent.getStringExtra(USERNAME));
                user.setId(intent.getLongExtra(USER_ID,0));
                checkGoalAnnualVM.setUserLD(user);
            }
        }

        //region [Declare properties]
        highestSportOSDisplay = Objects.requireNonNull(getView()).findViewById(R.id.checkGoalAnnualSportOSTV);
        highestBoulderOSDisplay = getView().findViewById(R.id.checkGoalAnnualBoulderOSTV);
        highestSportWorkedDisplay = getView().findViewById(R.id.checkGoalAnnualSportWorkedTV);
        highestBoulderWorkedDisplay = getView().findViewById(R.id.checkGoalAnnualBoulderWorkedTV);
        annualGoalSummaryTxt = getView().findViewById(R.id.annualGoalSummaryTxt);
        isAnnualGoalAchievedTat = getView().findViewById(R.id.isAnnualGoalActiveTxt);
        //endregion


        //region [Register Observers]
        checkGoalAnnualVM.getUserLD().observe(this, userVal -> user = userVal);

        checkGoalAnnualVM.getGoalAnnualLD().observe(this, goalAnnualVal -> {
            if (goalAnnualVal != null){
                user.setAnnualGoal(goalAnnualVal);
                if (user.getAnnualGoal().getGoalAchieved())
                    isAnnualGoalAchievedTat.setText(R.string.goal_achived);
                else
                    isAnnualGoalAchievedTat.setText("");
                if (user.getAnnualGoal().getDateExpires().isBefore(OffsetDateTime.now())){
                    //goal has expired...
                    annualGoalSummaryTxt.setText(R.string.goal_expired_summary);
                }else{
                    //display days remaining
                    int daysRemaining = (int) Duration.between(OffsetDateTime.now(), user.getAnnualGoal().getDateExpires()).toDays();
                    if (daysRemaining == 0)
                        annualGoalSummaryTxt.setText(R.string.goal_expires_today);
                    else
                        annualGoalSummaryTxt.setText(getString(R.string.days_remaining_for_goal,daysRemaining+""));

                }
            }else{
                //No Goal Set
                annualGoalSummaryTxt.setText("No Annual Goal found. \n Set a Goals First");
            }
        });
        checkGoalAnnualVM.getHighestSportOnsightLD().observe(this, highestSportOSVal -> {
            if (user.getAnnualGoal() != null)
                    updateView(user.getAnnualGoal().checkAverageGradeForRouteTypeXGoal(highestSportOSVal,
                            user.getAnnualGoal().getHighestSportOnsight(),
                            "No Sport Routes Logged."),
                            highestSportOSDisplay);
        });
        checkGoalAnnualVM.getHighestBoulderOnsightLD().observe(this, highestBoulderOSVal -> {
            if (user.getAnnualGoal() != null){
                updateView(user.getAnnualGoal().checkAverageGradeForRouteTypeXGoal(highestBoulderOSVal,
                        user.getAnnualGoal().getHighestBoulderOnsight(),
                        "No Boulder Routes Logged."),
                        highestBoulderOSDisplay);
            }
        });
        checkGoalAnnualVM.getHighestSportWorkedLD().observe(this, highestSportWorkedVal -> {
            if (user.getAnnualGoal() != null) {
                updateView(user.getAnnualGoal().checkAverageGradeForRouteTypeXGoal(highestSportWorkedVal,
                        user.getAnnualGoal().getHighestSportWorked(),
                        "No Sport Routes Logged."),
                        highestSportWorkedDisplay);
            }
        });
        checkGoalAnnualVM.getHighestBoulderWorkedLD().observe(this, highestBoulderWorkedVal -> {
            if ( user.getAnnualGoal() != null) {
                updateView(user.getAnnualGoal().checkAverageGradeForRouteTypeXGoal(highestBoulderWorkedVal,
                        user.getAnnualGoal().getHighestBoulderWorked(),
                        "No Boulder Routes Logged."),
                        highestBoulderWorkedDisplay);
            }
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
