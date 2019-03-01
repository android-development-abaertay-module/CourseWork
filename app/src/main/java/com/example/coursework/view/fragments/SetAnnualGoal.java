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
import com.example.coursework.model.GoalAnnual;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.viewmodel.SetGoalsVM.SetAnnualGoalViewModel;

import java.time.LocalDateTime;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class SetAnnualGoal extends Fragment {

    private SetAnnualGoalViewModel mViewModel;
    User user;
    GoalAnnual annualGoal;

    Spinner boulderOSSpinner;
    Spinner sportOsSpinner;
    Spinner boulderWorkedSpinner;
    Spinner sportWorkedSpinner;
    TextView createdOnTxt;
    TextView expiresOnTxt;
    Button resetAnnualGoalBtn;

    public static SetAnnualGoal newInstance() {
        return new SetAnnualGoal();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_annual_goal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SetAnnualGoalViewModel.class);

        Intent intent = getActivity().getIntent();
        if (intent != null){
            if (intent.hasExtra(USER_ID)){
                user = new User(intent.getStringExtra(USERNAME));
                user.setId(intent.getLongExtra(USER_ID,0));
                mViewModel.getUserDL(user.getId());
                mViewModel.getAnnualGoalLD(user.getId());
            }
        }

        boulderOSSpinner = getView().findViewById(R.id.annualBoulderOsGoalSpinner);
        boulderOSSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        sportOsSpinner = getView().findViewById(R.id.annualSportOsGoalSpinner);
        sportOsSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        boulderWorkedSpinner = getView().findViewById(R.id.annualBoulderWorkedGoalSpinner);
        boulderWorkedSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        sportWorkedSpinner = getView().findViewById(R.id.annualSportWorkedGoalSpinner);
        sportWorkedSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));
        createdOnTxt = getView().findViewById(R.id.annualCreatedOnTxt);
        expiresOnTxt = getView().findViewById(R.id.annualExpiresOnTxt);
        resetAnnualGoalBtn = getView().findViewById(R.id.resetAnnualGoalBtn);

        mViewModel.getUserDL(user.getId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userVal) {
                user = userVal;
            }
        });
        mViewModel.getAnnualGoalLD(user.getId()).observe(this, new Observer<GoalAnnual>() {
            @Override
            public void onChanged(@Nullable GoalAnnual goalAnnualVal) {
                annualGoal = goalAnnualVal;
                if (goalAnnualVal != null) {
                    updateAnnualGoalView(goalAnnualVal);
                }else {
                    //no weekly goal found
                    resetAnnualGoalBtn.setText("CREATE WEEKLY GOAL");
                    resetAnnualGoalBtn.setBackgroundColor(Color.RED);
                }
            }
        });
    }

    private void updateAnnualGoalView(GoalAnnual goalAnnual) {
        boulderOSSpinner.setSelection(((ArrayAdapter<Grades>)boulderOSSpinner.getAdapter()).getPosition(goalAnnual.getHighestBoulderOnsight()));
        sportOsSpinner.setSelection(((ArrayAdapter<Grades>)sportOsSpinner.getAdapter()).getPosition(goalAnnual.getHighestSportOnsight()));
        boulderWorkedSpinner.setSelection(((ArrayAdapter<Grades>)boulderWorkedSpinner.getAdapter()).getPosition(goalAnnual.getHighestBoulderWorked()));
        sportWorkedSpinner.setSelection(((ArrayAdapter<Grades>)sportWorkedSpinner.getAdapter()).getPosition(goalAnnual.getHighestSportWorked()));
        createdOnTxt.setText(goalAnnual.getDateCreated().toLocalDate().toString());
        expiresOnTxt.setText(goalAnnual.getDateExpires().toLocalDate().toString());
        resetAnnualGoalBtn.setText("RESET SEASONAL GOAL");
        if (goalAnnual.getDateExpires().isBefore(LocalDateTime.now())) {
            //Weekly Goal Expired.
            resetAnnualGoalBtn.setBackgroundColor(Color.RED);
        }else{
            resetAnnualGoalBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
        }
    }

}
