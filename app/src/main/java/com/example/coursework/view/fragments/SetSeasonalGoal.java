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
import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.viewmodel.SetGoalsVM.SetSeasonalGoalViewModel;

import java.time.OffsetDateTime;
import java.util.Objects;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class SetSeasonalGoal extends Fragment implements View.OnClickListener{

    private SetSeasonalGoalViewModel mViewModel;
    User user;

    Spinner boulderOSSpinner;
    Spinner sportOsSpinner;
    Spinner boulderWorkedSpinner;
    Spinner sportWorkedSpinner;
    TextView createdOnTxt;
    TextView expiresOnTxt;
    Button resetSeasonalGoalBtn;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_seasonal_goal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SetSeasonalGoalViewModel.class);

        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        if (intent != null){
            if (intent.hasExtra(USER_ID)){
                user = new User(intent.getStringExtra(USERNAME));
                user.setId(intent.getLongExtra(USER_ID,0));
                mViewModel.setUserLD(user);
                mViewModel.getSeasonalGoalLD(user.getId());
            }
        }

        boulderOSSpinner = Objects.requireNonNull(getView()).findViewById(R.id.seasonalBoulderOsGoalSpinner);
        boulderOSSpinner.setAdapter(new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        sportOsSpinner = getView().findViewById(R.id.seasonalSportOsGoalSpinner);
        sportOsSpinner.setAdapter(new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        boulderWorkedSpinner = getView().findViewById(R.id.seasonalBoulderWorkedGoalSpinner);
        boulderWorkedSpinner.setAdapter(new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        sportWorkedSpinner = getView().findViewById(R.id.seasonalSportWorkedGoalSpinner);
        sportWorkedSpinner.setAdapter(new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, Grades.values()));
        createdOnTxt = getView().findViewById(R.id.seasonalCreatedOnTxt);
        expiresOnTxt = getView().findViewById(R.id.seasonalExpiresOnTxt);
        resetSeasonalGoalBtn = getView().findViewById(R.id.resetSeasonalGoalBtn);
        resetSeasonalGoalBtn.setOnClickListener(this);

        mViewModel.getUserLD().observe(this, userVal -> user = userVal);

        mViewModel.getSeasonalGoalLD(user.getId()).observe(this, goalSeasonalVal -> {
            user.setSeasonalGoal(goalSeasonalVal);
            if (user.getSeasonalGoal() != null) {

                updateSeasonalGoalView(user.getSeasonalGoal());
            }else {
                //no weekly goal found
                resetSeasonalGoalBtn.setText(R.string.create_seasonal_goal);
                resetSeasonalGoalBtn.setBackgroundColor(Color.RED);
            }
        });
    }
    private void updateSeasonalGoalView(GoalSeasonal goalSeasonal) {
        boulderOSSpinner.setSelection(((ArrayAdapter<Grades>)boulderOSSpinner.getAdapter()).getPosition(goalSeasonal.getHighestBoulderOnsight()));
        sportOsSpinner.setSelection(((ArrayAdapter<Grades>)sportOsSpinner.getAdapter()).getPosition(goalSeasonal.getHighestSportOnsight()));
        boulderWorkedSpinner.setSelection(((ArrayAdapter<Grades>)boulderWorkedSpinner.getAdapter()).getPosition(goalSeasonal.getHighestBoulderWorked()));
        sportWorkedSpinner.setSelection(((ArrayAdapter<Grades>)sportWorkedSpinner.getAdapter()).getPosition(goalSeasonal.getHighestSportWorked()));
        createdOnTxt.setText(goalSeasonal.getDateCreated().toLocalDate().toString());
        expiresOnTxt.setText(goalSeasonal.getDateExpires().toLocalDate().toString());
        if (goalSeasonal.getDateExpires().isBefore(OffsetDateTime.now())) {
            //Weekly Goal Expired.
            resetSeasonalGoalBtn.setBackgroundColor(Color.RED);
            resetSeasonalGoalBtn.setText(R.string.reset_seasonal_goal);
        }else{
            resetSeasonalGoalBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
            resetSeasonalGoalBtn.setText(R.string.update_seasonal_goal);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetSeasonalGoalBtn:
                Log.d("gwyd","btn clicked");
                if (user.getSeasonalGoal() != null){
                    //user Has a goal
                    //check if goal expired
                    if (user.getSeasonalGoal().getDateExpires().isBefore(OffsetDateTime.now())){
                        //goal Expired - Close it and create a new one
                        mViewModel.closeSeasonalGoalSetWasMet(user.getSeasonalGoal());
                        getNewSeasonalGoalFromForm();
                        mViewModel.createGoalSeasonal(user.getSeasonalGoal());
                        mViewModel.getSeasonalGoalLD(user.getId());
                    }else{
                        //update current goal
                        updateSeasonalGoalFromForm();
                        mViewModel.updateGoalSeasonal(user.getSeasonalGoal());
                    }

                }else{
                    //user doesn't have a goal - create one
                    getNewSeasonalGoalFromForm();
                    mViewModel.createGoalSeasonal(user.getSeasonalGoal());
                }
                //refresh the view
                mViewModel.getSeasonalGoalLD(user.getId());
                break;
        }
    }

    private void updateSeasonalGoalFromForm(){
        Grades bOs = (Grades) boulderOSSpinner.getSelectedItem();
        Grades sOs = (Grades) sportOsSpinner.getSelectedItem();
        Grades bWorked = (Grades) boulderWorkedSpinner.getSelectedItem();
        Grades sWorked = (Grades) sportWorkedSpinner.getSelectedItem();
        user.getSeasonalGoal().setHighestBoulderOnsight(bOs);
        user.getSeasonalGoal().setHighestSportOnsight(sOs);
        user.getSeasonalGoal().setHighestBoulderWorked(bWorked);
        user.getSeasonalGoal().setHighestSportWorked(sWorked);
    }
    private void getNewSeasonalGoalFromForm() {
        Grades bOs = (Grades) boulderOSSpinner.getSelectedItem();
        Grades sOs = (Grades) sportOsSpinner.getSelectedItem();
        Grades bWorked = (Grades) boulderWorkedSpinner.getSelectedItem();
        Grades sWorked = (Grades) sportWorkedSpinner.getSelectedItem();
        user.setSeasonalGoal(new GoalSeasonal(user.getId(),bOs,sOs,bWorked,sWorked,OffsetDateTime.now()));
    }
}
