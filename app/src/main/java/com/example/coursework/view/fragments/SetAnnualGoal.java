package com.example.coursework.view.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.time.OffsetDateTime;
import java.util.Objects;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class SetAnnualGoal extends Fragment implements View.OnClickListener{

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_annual_goal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SetAnnualGoalViewModel.class);

        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        if (intent != null){
            if (intent.hasExtra(USER_ID)){
                user = new User(intent.getStringExtra(USERNAME));
                user.setId(intent.getLongExtra(USER_ID,0));
                mViewModel.getUserDL(user.getId());
                mViewModel.getAnnualGoalLD(user.getId());
            }
        }

        boulderOSSpinner = Objects.requireNonNull(getView()).findViewById(R.id.annualBoulderOsGoalSpinner);
        boulderOSSpinner.setAdapter(new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        sportOsSpinner = getView().findViewById(R.id.annualSportOsGoalSpinner);
        sportOsSpinner.setAdapter(new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        boulderWorkedSpinner = getView().findViewById(R.id.annualBoulderWorkedGoalSpinner);
        boulderWorkedSpinner.setAdapter(new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        sportWorkedSpinner = getView().findViewById(R.id.annualSportWorkedGoalSpinner);
        sportWorkedSpinner.setAdapter(new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, Grades.values()));
        createdOnTxt = getView().findViewById(R.id.annualCreatedOnTxt);
        expiresOnTxt = getView().findViewById(R.id.annualExpiresOnTxt);
        resetAnnualGoalBtn = getView().findViewById(R.id.resetAnnualGoalBtn);
        resetAnnualGoalBtn.setOnClickListener(this);

        mViewModel.getUserDL(user.getId()).observe(this, userVal -> user = userVal);

        mViewModel.getAnnualGoalLD(user.getId()).observe(this, goalAnnualVal -> {
            annualGoal = goalAnnualVal;
            if (goalAnnualVal != null) {
                updateAnnualGoalView(goalAnnualVal);
            }else {
                //no weekly goal found
                resetAnnualGoalBtn.setText(R.string.create_annual_goal);
                resetAnnualGoalBtn.setBackgroundColor(Color.RED);
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
        if (goalAnnual.getDateExpires().isBefore(OffsetDateTime.now())) {
            //Weekly Goal Expired.
            resetAnnualGoalBtn.setBackgroundColor(Color.RED);
            resetAnnualGoalBtn.setText(R.string.reset_annual_goal);

        }else{
            resetAnnualGoalBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
            resetAnnualGoalBtn.setText(R.string.update_annual_goal);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetAnnualGoalBtn:
                Log.d("gwyd","btn clicked");
                if (annualGoal != null){
                    //user Has a goal
                    //check if goal expired
                    if (annualGoal.getDateExpires().isBefore(OffsetDateTime.now())){
                        //goal Expired - Close it and create a new one
                        mViewModel.closeAnnualGoalSetWasMet(annualGoal);
                        getNewAnnualGoalFromForm();
                        mViewModel.createGoalAnnual(annualGoal);
                        mViewModel.getAnnualGoalLD(user.getId());
                    }else{
                        //update current goal
                        updateAnnualGoalFromForm();
                        mViewModel.updateGoalAnnual(annualGoal);
                    }

                }else{
                    //user doesn't have a goal - create one
                    getNewAnnualGoalFromForm();
                    mViewModel.createGoalAnnual(annualGoal);
                }
                //refresh the view
                mViewModel.getAnnualGoalLD(user.getId());
                break;
        }
    }
    private void updateAnnualGoalFromForm(){
        Grades bOs = (Grades) boulderOSSpinner.getSelectedItem();
        Grades sOs = (Grades) sportOsSpinner.getSelectedItem();
        Grades bWorked = (Grades) boulderWorkedSpinner.getSelectedItem();
        Grades sWorked = (Grades) sportWorkedSpinner.getSelectedItem();
        annualGoal.setHighestBoulderOnsight(bOs);
        annualGoal.setHighestSportOnsight(sOs);
        annualGoal.setHighestBoulderWorked(bWorked);
        annualGoal.setHighestSportWorked(sWorked);
    }
    private void getNewAnnualGoalFromForm() {
        Grades bOs = (Grades) boulderOSSpinner.getSelectedItem();
        Grades sOs = (Grades) sportOsSpinner.getSelectedItem();
        Grades bWorked = (Grades) boulderWorkedSpinner.getSelectedItem();
        Grades sWorked = (Grades) sportWorkedSpinner.getSelectedItem();
        annualGoal = new GoalAnnual(user.getId(),bOs,sOs,bWorked,sWorked, OffsetDateTime.now());
    }
}
