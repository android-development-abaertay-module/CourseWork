package com.example.coursework.view.fragments;

import android.arch.lifecycle.ViewModelProviders;
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
import com.example.coursework.model.enums.Grades;
import com.example.coursework.viewmodel.CheckGoalsVM.CheckWeeklyGoalViewModel;

public class CheckWeeklyGoal extends Fragment {

    private CheckWeeklyGoalViewModel mViewModel;
    private GoalWeekly goalWeekly;
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
        mViewModel = ViewModelProviders.of(this).get(CheckWeeklyGoalViewModel.class);


        //region [Declare properties]
        noSportAchivedPb = getView().findViewById(R.id.checkGoalWeeklyNoSportPB);
        noBoulderAchivedPb = getView().findViewById(R.id.checkGoalWeeklyNoBoulderPB);
        avgSportAchivedDisplay = getView().findViewById(R.id.checkGoalWeeklyAvgSportTV);
        avgBoulderAchivedDisplay = getView().findViewById(R.id.checkGoalWeeklyAvgBoulderTV);

        //endregion
    }

}
