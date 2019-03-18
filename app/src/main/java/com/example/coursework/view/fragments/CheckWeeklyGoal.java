package com.example.coursework.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.viewmodel.CheckGoalsVM.CheckWeeklyGoalViewModel;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class CheckWeeklyGoal extends Fragment {

    private CheckWeeklyGoalViewModel checkWeeklyViewModel;
    private GoalWeekly goalWeekly;
    private User user;
    private int numberSportPercentage;
    private int numberBoulderPercentage;
    private Grades avgSportAchieved;
    private Grades avgBoulderAchieved;
    private ProgressBar noSportAchievedPb;
    private ProgressBar noBoulderAchievedPb;
    private TextView avgSportAchievedDisplay;
    private TextView avgBoulderAchievedDisplay;
    private TextView timeRemaingTxt;


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
        timeRemaingTxt = getView().findViewById(R.id.timeRemainingWeeklyGoalActiveTxt);
        //endregion


        //region [Register Observers]
        checkWeeklyViewModel.getUserLD(user.getId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userVal) {
                user = userVal;
            }
        });
        checkWeeklyViewModel.getGoalWeeklyLD(user.getId()).observe(this, goalWeeklyVal -> {
            if (goalWeeklyVal != null){
                goalWeekly = goalWeeklyVal;
                if (goalWeekly.getDateExpires().isBefore(LocalDateTime.now())){
                    //goal has expired...
                    timeRemaingTxt.setText("Goal Has Expired. Please Set a new Weekly goal");
                }else{
                    //display days remaining
                    int daysRemaining = (int)Duration.between(LocalDateTime.now(),goalWeekly.getDateExpires()).toDays();
                    if (daysRemaining == 0)
                        timeRemaingTxt.setText(R.string.goal_expires_today);
                    else
                        timeRemaingTxt.setText(daysRemaining + " Days Remaining");

                }
            }
        });
        checkWeeklyViewModel.getNumberBoulderProgressLD(user.getId()).observe(this, numBoulderAchievedVal -> {
            if (numBoulderAchievedVal != null && goalWeekly != null){
                if (numBoulderAchievedVal > goalWeekly.getNumberOfBoulder()) {
                    numBoulderAchievedVal = goalWeekly.getNumberOfBoulder();
                }

                float fraction = (float) numBoulderAchievedVal / goalWeekly.getNumberOfBoulder();
                numberBoulderPercentage = (int) Math.floor(fraction * 100);
                noBoulderAchievedPb.setProgress(numberBoulderPercentage);
            }
        });
        checkWeeklyViewModel.getNumberSportProgressLD(user.getId()).observe(this, numSportAchievedVal -> {
            if (numSportAchievedVal != null && goalWeekly != null){
                if (numSportAchievedVal > goalWeekly.getNumberOfSport())
                    numSportAchievedVal = goalWeekly.getNumberOfSport();

                float fraction = (float) numSportAchievedVal / goalWeekly.getNumberOfSport();
                numberSportPercentage = (int) Math.floor(fraction * 100);
                noSportAchievedPb.setProgress(numberSportPercentage);
            }
        });
        checkWeeklyViewModel.getAverageBoulderGradeLD(user.getId()).observe(this, avgBoulderGradeVal -> {
            if (avgBoulderGradeVal != null && goalWeekly != null) {
                avgBoulderAchieved = avgBoulderGradeVal;
                String output = avgBoulderAchieved.toString() + " : " + goalWeekly.getAverageBoulderGrade().toString();
                avgBoulderAchievedDisplay.setText(output);
            }else{
                Log.d("gwyd", "getAverageBoulderGradeLD returned null");
            }
        });
        checkWeeklyViewModel.getAverageSportGradeLD(user.getId()).observe(this, avgSportGradeVal -> {
            if (avgSportGradeVal != null && goalWeekly != null) {
                avgSportAchieved = avgSportGradeVal;

                String output = avgSportAchieved.toString() + " : " + goalWeekly.getAverageBoulderGrade().toString();
                avgBoulderAchievedDisplay.setText(output);
            }
        });
        //endregion
    }
}
