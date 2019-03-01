package com.example.coursework.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.viewmodel.SetGoalsVM.SetSeasonalGoalViewModel;

import java.time.LocalDateTime;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class SetSeasonalGoal extends Fragment {

    private SetSeasonalGoalViewModel mViewModel;
    User user;
    GoalSeasonal seasonalGoal;

    Spinner boulderOSSpinner;
    Spinner sportOsSpinner;
    Spinner boulderWorkedSpinner;
    Spinner sportWorkedSpinner;
    TextView createdOnTxt;
    TextView expiresOnTxt;
    Button resetSeasonalGoalBtn;

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

        Intent intent = getActivity().getIntent();
        if (intent != null){
            if (intent.hasExtra(USER_ID)){
                user = new User(intent.getStringExtra(USERNAME));
                user.setId(intent.getLongExtra(USER_ID,0));
                mViewModel.getUserLD(user.getId());
                mViewModel.getSeasonalGoalLD(user.getId());
            }
        }

        boulderOSSpinner = getView().findViewById(R.id.seasonalBoulderOsGoalSpinner);
        boulderOSSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        sportOsSpinner = getView().findViewById(R.id.seasonalSportOsGoalSpinner);
        sportOsSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        boulderWorkedSpinner = getView().findViewById(R.id.seasonalBoulderWorkedGoalSpinner);
        boulderWorkedSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        sportWorkedSpinner = getView().findViewById(R.id.seasonalSportWorkedGoalSpinner);
        sportWorkedSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));
        createdOnTxt = getView().findViewById(R.id.seasonalCreatedOnTxt);
        expiresOnTxt = getView().findViewById(R.id.seasonalExpiresOnTxt);
        resetSeasonalGoalBtn = getView().findViewById(R.id.resetSeasonalGoalBtn);

        mViewModel.getUserLD(user.getId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userVal) {
                user = userVal;
            }
        });
        mViewModel.getSeasonalGoalLD(user.getId()).observe(this, new Observer<GoalSeasonal>() {
            @Override
            public void onChanged(@Nullable GoalSeasonal goalSeasonal) {
                seasonalGoal = goalSeasonal;
                if (goalSeasonal != null) {

                    updateSeasonalGoalView(goalSeasonal);
                }else {
                    //no weekly goal found
                    resetSeasonalGoalBtn.setText("CREATE WEEKLY GOAL");
                    resetSeasonalGoalBtn.setBackgroundColor(Color.RED);
                }
            }
        });
    }
    private void updateSeasonalGoalView(GoalSeasonal goalSeasonal) {
        boulderOSSpinner.setSelection(((ArrayAdapter<Grades>)boulderOSSpinner.getAdapter()).getPosition(goalSeasonal.get_highestBoulderOnsight()));
        sportOsSpinner.setSelection(((ArrayAdapter<Grades>)sportOsSpinner.getAdapter()).getPosition(goalSeasonal.get_highestSportOnsight()));
        boulderWorkedSpinner.setSelection(((ArrayAdapter<Grades>)boulderWorkedSpinner.getAdapter()).getPosition(goalSeasonal.get_highestBoulderWorked()));
        sportWorkedSpinner.setSelection(((ArrayAdapter<Grades>)sportWorkedSpinner.getAdapter()).getPosition(goalSeasonal.get_highestSportWorked()));
        createdOnTxt.setText(goalSeasonal.getDateCreated().toLocalDate().toString());
        expiresOnTxt.setText(goalSeasonal.getDateExpires().toLocalDate().toString());
        resetSeasonalGoalBtn.setText("RESET SEASONAL GOAL");
        if (goalSeasonal.getDateExpires().isBefore(LocalDateTime.now())) {
            //Weekly Goal Expired.
            resetSeasonalGoalBtn.setBackgroundColor(Color.RED);
        }else{
            resetSeasonalGoalBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
        }
    }
}
