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
import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.helper.GoalCheckDTO;
import com.example.coursework.viewmodel.CheckGoalsVM.CheckSeasonalGoalViewModel;

import java.time.Duration;
import java.time.OffsetDateTime;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class CheckSeasonalGoal extends Fragment {

    private CheckSeasonalGoalViewModel checkSeasonalGoalVM;
    private GoalSeasonal goalSeasonal;
    private User user;

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
            if (goalSeasonal != null)
                updateView(goalSeasonal.checkGoalAvgGradeTypeX(highestSportOSVal,goalSeasonal.getHighestSportOnsight(),"No Sport Routes Logged."), highestSportOSDisplay);
        });
        checkSeasonalGoalVM.getHighestBoulderOnsightLD().observe(this, highestBoulderOSVal -> {
            if (goalSeasonal != null)
                updateView(goalSeasonal.checkGoalAvgGradeTypeX(highestBoulderOSVal,goalSeasonal.getHighestBoulderOnsight(),"No Boulder Routes Logged"), highestBoulderOSDisplay);
        });
        checkSeasonalGoalVM.getHighestSportWorkedLD().observe(this, highestSportWorkedVal -> {
            if (goalSeasonal != null)
                updateView(goalSeasonal.checkGoalAvgGradeTypeX(highestSportWorkedVal,goalSeasonal.getHighestSportWorked(),"No Sport Routes Logged"), highestSportWorkedDisplay);
        });
        checkSeasonalGoalVM.getHighestBoulderWorkedLD().observe(this, highestBoulderWorkedVal -> {
            if (goalSeasonal != null)
                updateView(goalSeasonal.checkGoalAvgGradeTypeX(highestBoulderWorkedVal, goalSeasonal.getHighestBoulderWorked(),"No Boulder Routes Logged"), highestBoulderWorkedDisplay);
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
