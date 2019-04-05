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
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.viewmodel.SetGoalsVM.SetWeeklyGoalViewModel;

import java.time.OffsetDateTime;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;
import static com.example.coursework.view.TrainingActivity.GOAL_TYPE;

public class SetWeeklyGoal extends Fragment implements View.OnClickListener {

    private SetWeeklyGoalViewModel mViewModel;
    Spinner numSportSpinner;
    Spinner numBoulderSpinner;
    Spinner avgSportSpinner;
    Spinner avgBoulderSpinner;
    TextView createdOnTxt;
    TextView expiresOnTxt;
    Button resetWeeklyGoalBtn;
    GoalWeekly weeklyGoal;

    public static SetWeeklyGoal newInstance() {
        return new SetWeeklyGoal();
    }

    User user;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_weekly_goal_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mViewModel = ViewModelProviders.of(this).get(SetWeeklyGoalViewModel.class);

        Intent intent = getActivity().getIntent();
        if (intent != null){
            if (intent.hasExtra(USER_ID)){
                user = new User(intent.getStringExtra(USERNAME));
                user.setId(intent.getLongExtra(USER_ID,0));
                mViewModel.setUserLD(user);
                mViewModel.getWeeklyGoalLD(user.getId());
            }
        }

        numSportSpinner = getView().findViewById(R.id.weeklyNumberOfSportSpinner);
        numBoulderSpinner = getView().findViewById(R.id.weeklyNumberOfBoulderSpinner);
        avgSportSpinner = getView().findViewById(R.id.weeklyAverageSportGradeSpinner);
        avgSportSpinner.setAdapter(new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        avgBoulderSpinner = getView().findViewById(R.id.weeklyAverageBoulderGradeSpinner);
        avgBoulderSpinner.setAdapter(new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_list_item_1, Grades.values()));
        createdOnTxt = getView().findViewById(R.id.weeklyCreatedOnTxt);
        expiresOnTxt = getView().findViewById(R.id.weeklyExpiresOnTxt);
        resetWeeklyGoalBtn = getView().findViewById(R.id.resetWeeklyGoalBtn);
        resetWeeklyGoalBtn.setOnClickListener(this);

        mViewModel.getUserLD().observe(this, userVal -> {
            user = userVal;
        });


        mViewModel.getWeeklyGoalLD(user.getId()).observe(this, goalWeekly -> {
            weeklyGoal = goalWeekly;
            if (goalWeekly != null) {

                updateWeeklyGoalView(goalWeekly);
            }else {
                //no weekly goal found
                resetWeeklyGoalBtn.setText(R.string.create_weekly_goal);
                resetWeeklyGoalBtn.setBackgroundColor(Color.RED);
            }
        });
    }

    private void updateWeeklyGoalView(GoalWeekly goalWeekly) {
        numSportSpinner.setSelection(((ArrayAdapter<String>)numSportSpinner.getAdapter()).getPosition(goalWeekly.getNumberOfSport() +""));
        numBoulderSpinner.setSelection(((ArrayAdapter<String>)numBoulderSpinner.getAdapter()).getPosition(goalWeekly.getNumberOfBoulder() +""));
        avgSportSpinner.setSelection(((ArrayAdapter<Grades>)avgSportSpinner.getAdapter()).getPosition(goalWeekly.getAverageSportGrade()));
        avgBoulderSpinner.setSelection(((ArrayAdapter<Grades>)avgBoulderSpinner.getAdapter()).getPosition(goalWeekly.getAverageBoulderGrade()));
        createdOnTxt.setText(goalWeekly.getDateCreated().toLocalDate().toString());
        expiresOnTxt.setText(goalWeekly.getDateExpires().toLocalDate().toString());
        if (goalWeekly.getDateExpires().isBefore(OffsetDateTime.now())) {
            //Weekly Goal Expired.
            resetWeeklyGoalBtn.setBackgroundColor(Color.RED);
            resetWeeklyGoalBtn.setText(R.string.reset_weekly_goal);
        }else{
            resetWeeklyGoalBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
            resetWeeklyGoalBtn.setText(R.string.update_weekly_goal);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetWeeklyGoalBtn:
                Log.d("gwyd","btn clicked");
                if (weeklyGoal != null){
                    //user Has a goal
                    //check if goal expired
                    if (weeklyGoal.getDateExpires().isBefore(OffsetDateTime.now())){
                        //goal Expired - Close it and create a new one
                        mViewModel.closeGoalSetWasMet(weeklyGoal);
                        getNewWeeklyGoalFromForm();
                        mViewModel.createGoalWeekly(weeklyGoal);
                        mViewModel.getWeeklyGoalLD(user.getId());
                    }else{
                        //update current goal
                        updateWeeklyGoalFromForm();
                        mViewModel.updateGoalWeekly(weeklyGoal);
                    }

                }else{
                    //user doesn't have a goal - create one
                    getNewWeeklyGoalFromForm();
                    mViewModel.createGoalWeekly(weeklyGoal);
                }
                mViewModel.getWeeklyGoalLD(user.getId());
                break;
        }
    }

    private void updateWeeklyGoalFromForm(){
        int numSport = Integer.parseInt(numSportSpinner.getSelectedItem().toString());
        int numBoulder = Integer.parseInt(numBoulderSpinner.getSelectedItem().toString());
        Grades avgSport = (Grades) avgSportSpinner.getSelectedItem();
        Grades avgBoulder = (Grades) avgBoulderSpinner.getSelectedItem();
        weeklyGoal.setNumberOfSport(numSport);
        weeklyGoal.setNumberOfBoulder(numBoulder);
        weeklyGoal.setAverageSportGrade(avgSport);
        weeklyGoal.setAverageBoulderGrade(avgBoulder);
    }
    private void getNewWeeklyGoalFromForm() {
        int numSport = Integer.parseInt(numSportSpinner.getSelectedItem().toString());
        int numBoulder = Integer.parseInt(numBoulderSpinner.getSelectedItem().toString());
        Grades avgSport = (Grades) avgSportSpinner.getSelectedItem();
        Grades avgBoulder = (Grades) avgBoulderSpinner.getSelectedItem();
        weeklyGoal = new GoalWeekly(user.getId(),numSport,numBoulder,avgSport,avgBoulder, OffsetDateTime.now());
    }
}
