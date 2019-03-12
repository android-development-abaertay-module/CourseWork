package com.example.coursework.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.coursework.R;
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.viewmodel.CheckGoalsVM.CheckWeeklyGoalViewModel;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class CheckWeeklyGoal extends Fragment {

    private CheckWeeklyGoalViewModel checkWeeklyViewModel;
    private GoalWeekly goalWeekly;
    private User user;
    private int numberSportAchived;
    private int numberBoulderAchived;
    private Grades avgSportAchived;
    private Grades avgBoulderAchived;
    private ProgressBar noSportAchivedPb;
    private ProgressBar noBoulderAchivedPb;
    private ProgressBar avgSportAchivedDisplay;
    private ProgressBar avgBoulderAchivedDisplay;

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
                checkWeeklyViewModel.getUserLD(user.getId());
            }
        }

        //region [Declare properties]
        noSportAchivedPb = getView().findViewById(R.id.checkGoalWeeklyNoSportPB);
        noBoulderAchivedPb = getView().findViewById(R.id.checkGoalWeeklyNoBoulderPB);
        avgSportAchivedDisplay = getView().findViewById(R.id.checkGoalWeeklyAvgSportTV);
        avgBoulderAchivedDisplay = getView().findViewById(R.id.checkGoalWeeklyAvgBoulderTV);

        //endregion


        //region [Register Observers]
        checkWeeklyViewModel.getUserLD(user.getId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userVal) {
                user = userVal;
            }
        });
        checkWeeklyViewModel.getGoalWeeklyLD(user.getId()).observe(this, new Observer<GoalWeekly>() {
            @Override
            public void onChanged(@Nullable GoalWeekly goalWeeklyVal) {
                goalWeekly = goalWeeklyVal;
            }
        });
        checkWeeklyViewModel.getNumberBoulderProgressLD(user.getId()).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer numBoulderVal) {
                numberBoulderAchived = numBoulderVal;
            }
        });
        checkWeeklyViewModel.getNumberSportProgressLD(user.getId()).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer numSportVal) {
                numberSportAchived = numSportVal;
            }
        });
        checkWeeklyViewModel.getAverageSportGradeLD(user.getId()).observe(this, new Observer<Grades>() {
            @Override
            public void onChanged(@Nullable Grades avgSportGradeVal) {
                avgSportAchived = avgSportGradeVal;
            }
        });
        checkWeeklyViewModel.getAverageBoulderGradeLD(user.getId()).observe(this, new Observer<Grades>() {
            @Override
            public void onChanged(@Nullable Grades avgBoulderGradeVal) {
                avgBoulderAchived = avgBoulderGradeVal;
            }
        });

        //endregion
    }

}
