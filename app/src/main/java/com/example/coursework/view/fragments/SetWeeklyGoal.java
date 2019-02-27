package com.example.coursework.view.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.coursework.R;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.viewmodel.SetGoalsVM.SetWeeklyGoalViewModel;

public class SetWeeklyGoal extends Fragment {

    private SetWeeklyGoalViewModel mViewModel;
    Spinner hoursTrainingSpinner;
    Spinner numSportSpinner;
    Spinner numBoulderSpinner;
    Spinner avgSportSpinner;
    Spinner avgBoulderSpinner;


    public static SetWeeklyGoal newInstance() {
        return new SetWeeklyGoal();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_weekly_goal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SetWeeklyGoalViewModel.class);

        hoursTrainingSpinner = getView().findViewById(R.id.hoursTrainingSpinner);

        numSportSpinner = getView().findViewById(R.id.numberOfSportSpinner);
        numBoulderSpinner = getView().findViewById(R.id.numberOfBoulderSpinner);
        avgSportSpinner = getView().findViewById(R.id.averageSportGradeSpinner);
        avgSportSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        avgBoulderSpinner = getView().findViewById(R.id.averageBoulderGradeSpinner);
        avgBoulderSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

    }
}
