package com.example.coursework.view.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.R;
import com.example.coursework.viewmodel.SetGoalsVM.SetSeasonalGoalViewModel;

public class SetSeasonalGoal extends Fragment {

    private SetSeasonalGoalViewModel mViewModel;

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
        // TODO: Use the ViewModel
    }

}
