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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.helper.GoalCheckDTO;
import com.example.coursework.viewmodel.CheckGoalsVM.CheckWeeklyGoalViewModel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class CheckWeeklyGoal extends Fragment {

    private CheckWeeklyGoalViewModel checkWeeklyViewModel;
    private GoalWeekly goalWeekly;
    private User user;
    private ProgressBar noSportAchievedPb;
    private ProgressBar noBoulderAchievedPb;
    private TextView avgSportAchievedDisplay;
    private TextView avgBoulderAchievedDisplay;
    private TextView weeklyGoalSummaryTxt;
    private TextView isWeeklyGoalAchievedTat;


    public static CheckWeeklyGoal newInstance() {
        return new CheckWeeklyGoal();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.check_weekly_goal_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkWeeklyViewModel = ViewModelProviders.of(this).get(CheckWeeklyGoalViewModel.class);

        Intent intent = getActivity().getIntent();
        if (intent != null){
            if (intent.hasExtra(USER_ID)){
                user = new User(intent.getStringExtra(USERNAME));
                user.setId(intent.getLongExtra(USER_ID,0));
                checkWeeklyViewModel.setUserLD(user);
            }
        }

        //region [Declare properties]
        noSportAchievedPb = getView().findViewById(R.id.checkGoalWeeklyNoSportPB);
        noBoulderAchievedPb = getView().findViewById(R.id.checkGoalWeeklyNoBoulderPB);
        avgSportAchievedDisplay = getView().findViewById(R.id.checkGoalWeeklyAvgSportTV);
        avgBoulderAchievedDisplay = getView().findViewById(R.id.checkGoalWeeklyAvgBoulderTV);
        weeklyGoalSummaryTxt = getView().findViewById(R.id.weeklyGoalSummaryTxt);
        isWeeklyGoalAchievedTat = getView().findViewById(R.id.isWeeklyGoalActiveTxt);
        //endregion


        //region [Register Observers]
        checkWeeklyViewModel.getUserLD().observe(this, userVal -> {
            user = userVal;
        });
        checkWeeklyViewModel.getGoalWeeklyLD().observe(this, goalWeeklyVal -> {
            //TODO: put code int to populate was achieved TextView that's already in the views
            if (goalWeeklyVal != null){
                goalWeekly = goalWeeklyVal;
                if (goalWeekly.getGoalAchieved())
                    isWeeklyGoalAchievedTat.setText(R.string.goal_achived);
                else
                    isWeeklyGoalAchievedTat.setText("");
                if (goalWeekly.getDateExpires().isBefore(OffsetDateTime.now())){
                    //goal has expired...
                    weeklyGoalSummaryTxt.setText(R.string.goal_expired_summary);
                }else{
                    //display days remaining
                    int daysRemaining = (int)Duration.between(LocalDateTime.now(),goalWeekly.getDateExpires()).toDays();
                    if (daysRemaining == 0)
                        weeklyGoalSummaryTxt.setText(R.string.goal_expires_today);
                    else
                        weeklyGoalSummaryTxt.setText(getString(R.string.days_remaining_for_goal,daysRemaining+""));
                }
            }else{
                //No Goal Set
                weeklyGoalSummaryTxt.setText("No Weekly goal found. \n Set a Goals First");
            }
        });
        checkWeeklyViewModel.getNumberBoulderProgressLD().observe(this, numBoulderAchievedVal -> {
            if (numBoulderAchievedVal != null && goalWeekly != null)
                noBoulderAchievedPb.setProgress(goalWeekly.checkNumberOfRoutesForTypeGoalPercentage(numBoulderAchievedVal,goalWeekly.getNumberOfBoulder()));
        });
        checkWeeklyViewModel.getNumberSportProgressLD().observe(this, numSportAchievedVal -> {
            if (numSportAchievedVal != null && goalWeekly != null)
                noSportAchievedPb.setProgress(goalWeekly.checkNumberOfRoutesForTypeGoalPercentage(numSportAchievedVal,goalWeekly.getNumberOfSport()));
        });
        checkWeeklyViewModel.getAverageBoulderGradeLD().observe(this, avgBoulderGradeVal -> {
            if (goalWeekly != null) {
                updateView(avgBoulderGradeVal, goalWeekly.getAverageBoulderGrade(), "No Boulder Routes Logged", avgBoulderAchievedDisplay);
            }
        });
        checkWeeklyViewModel.getAverageSportGradeLD().observe(this, avgSportGradeVal -> {
            if (goalWeekly != null) {
                updateView(avgSportGradeVal, goalWeekly.getAverageSportGrade(), "No Sport Routes Logged", avgSportAchievedDisplay);
            }
        });
        //endregion
    }

    private void updateView(Grades avgGradeFor_TypeX, Grades targetGradeFor_TypeX, String noRoutesFoundMessage, TextView textViewToUpdate) {
        GoalCheckDTO result = goalWeekly.checkAverageGradeForRouteTypeXGoal(avgGradeFor_TypeX, targetGradeFor_TypeX, noRoutesFoundMessage);
        textViewToUpdate.setText(result.getOutput());
        if (result.getIsAchieved())
            textViewToUpdate.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.green));
        else
            textViewToUpdate.setTextColor(ContextCompat.getColor(getView().getContext(), R.color.red));
    }
}
