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
import com.example.coursework.model.helper.GoalCheckDTO;
import com.example.coursework.viewmodel.CheckGoalsVM.CheckAnnualGoalViewModel;

import java.time.Duration;
import java.time.OffsetDateTime;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class CheckAnnualGoal extends Fragment {

    private CheckAnnualGoalViewModel checkGoalAnnualVM;
    private GoalAnnual goalAnnual;
    private User user;

    private TextView highestSportOSDisplay;
    private TextView highestBoulderOSDisplay;
    private TextView highestSportWorkedDisplay;
    private TextView highestBoulderWorkedDisplay;
    private TextView annualGoalSummaryTxt;

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
        annualGoalSummaryTxt = getView().findViewById(R.id.annualGoalSummaryTxt);
        //endregion


        //region [Register Observers]
        checkGoalAnnualVM.getUserLD().observe(this, userVal -> {
            user = userVal;
        });
        checkGoalAnnualVM.getGoalAnnualLD().observe(this, goalSeasonalVal -> {
            if (goalSeasonalVal != null){
                goalAnnual = goalSeasonalVal;
                if (goalAnnual.getDateExpires().isBefore(OffsetDateTime.now())){
                    //goal has expired...
                    annualGoalSummaryTxt.setText(R.string.goal_expired_summary);
                }else{
                    //display days remaining
                    int daysRemaining = (int) Duration.between(OffsetDateTime.now(), goalAnnual.getDateExpires()).toDays();
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
            if (goalAnnual != null)
                    updateView(goalAnnual.checkAverageGradeForRouteTypeXGoal(highestSportOSVal,
                            goalAnnual.getHighestSportOnsight(),
                            "No Sport Routes Logged."),
                            highestSportOSDisplay);
        });
        checkGoalAnnualVM.getHighestBoulderOnsightLD().observe(this, highestBoulderOSVal -> {
            if (goalAnnual != null){
                updateView(goalAnnual.checkAverageGradeForRouteTypeXGoal(highestBoulderOSVal,
                        goalAnnual.getHighestBoulderOnsight(),
                        "No Boulder Routes Logged."),
                        highestBoulderOSDisplay);
            }
        });
        checkGoalAnnualVM.getHighestSportWorkedLD().observe(this, highestSportWorkedVal -> {
            if (goalAnnual != null) {
                updateView(goalAnnual.checkAverageGradeForRouteTypeXGoal(highestSportWorkedVal,
                        goalAnnual.getHighestSportWorked(),
                        "No Sport Routes Logged."),
                        highestSportWorkedDisplay);
            }
        });
        checkGoalAnnualVM.getHighestBoulderWorkedLD().observe(this, highestBoulderWorkedVal -> {
            if ( goalAnnual != null) {
                updateView(goalAnnual.checkAverageGradeForRouteTypeXGoal(highestBoulderWorkedVal,
                        goalAnnual.getHighestBoulderWorked(),
                        "No Boulder Routes Logged."),
                        highestBoulderWorkedDisplay);
            }
        });
        //endregion
    }
    private void updateView(GoalCheckDTO result, TextView displayTextView) {
        displayTextView.setText(result.getOutput());
        if (result.getIsAchieved())
            displayTextView.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.green));
        else
            displayTextView.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.red));
    }

}
