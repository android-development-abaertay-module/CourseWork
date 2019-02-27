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
import com.example.coursework.viewmodel.SetGoalsVM.SetSeasonalGoalViewModel;

public class SetSeasonalGoal extends Fragment {

    private SetSeasonalGoalViewModel mViewModel;

    Spinner boulderOSSpinner;
    Spinner sportOsSpinner;
    Spinner boulderWorkedSpinner;
    Spinner sportWorkedSpinner;


    public static SetSeasonalGoal newInstance() {
        return new SetSeasonalGoal();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_seasonal_goal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SetSeasonalGoalViewModel.class);

        boulderOSSpinner = getView().findViewById(R.id.boulderOsGoalSpinner);
        boulderOSSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        sportOsSpinner = getView().findViewById(R.id.sportOsGoalSpinner);
        sportOsSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        boulderWorkedSpinner = getView().findViewById(R.id.boulderWorkedGoalSpinner);
        boulderWorkedSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        sportWorkedSpinner = getView().findViewById(R.id.sportWorkedGoalSpinner);
        sportWorkedSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

    }

}
