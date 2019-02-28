package com.example.coursework.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.viewmodel.SetGoalsVM.SetWeeklyGoalViewModel;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class SetWeeklyGoal extends Fragment {

    private SetWeeklyGoalViewModel mViewModel;
    Spinner hoursTrainingSpinner;
    Spinner numSportSpinner;
    Spinner numBoulderSpinner;
    Spinner avgSportSpinner;
    Spinner avgBoulderSpinner;
    TextView createdOnTxt;
    TextView expiresOnTxt;
    Button resetWeeklyGoalBtn;

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
                mViewModel.getUserById(user.getId());
                mViewModel.getMostRecentWeeklyGoal(user.getId());
            }
        }


        hoursTrainingSpinner = getView().findViewById(R.id.hoursTrainingSpinner);
        numSportSpinner = getView().findViewById(R.id.numberOfSportSpinner);
        numBoulderSpinner = getView().findViewById(R.id.numberOfBoulderSpinner);
        avgSportSpinner = getView().findViewById(R.id.averageSportGradeSpinner);
        avgSportSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));

        avgBoulderSpinner = getView().findViewById(R.id.averageBoulderGradeSpinner);
        avgBoulderSpinner.setAdapter(new ArrayAdapter<Grades>(getContext(), android.R.layout.simple_list_item_1, Grades.values()));
        createdOnTxt = getView().findViewById(R.id.createdOnTxt);
        expiresOnTxt = getView().findViewById(R.id.expiresOnTxt);
        resetWeeklyGoalBtn = getView().findViewById(R.id.resetWeeklyGoalBtn);

        mViewModel.getUserById(user.getId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userVal) {
                user = userVal;
            }
        });


        mViewModel.getMostRecentWeeklyGoal(user.getId()).observe(this, new Observer<GoalWeekly>() {
            @Override
            public void onChanged(@Nullable GoalWeekly goalWeekly) {
                if (goalWeekly != null) {
                    updateWeeklyGoalView(goalWeekly);
                }else {
                    //no weekly goal found
                    resetWeeklyGoalBtn.setText("CREATE WEEKLY GOAL");
                    resetWeeklyGoalBtn.setBackgroundColor(Color.RED);
                }
            }
        });
    }

    private void updateWeeklyGoalView(GoalWeekly goalWeekly) {
        hoursTrainingSpinner.setSelection(((ArrayAdapter<String>)hoursTrainingSpinner.getAdapter()).getPosition(goalWeekly.getHoursOfTraining() +""));
        numSportSpinner.setSelection(((ArrayAdapter<String>)numSportSpinner.getAdapter()).getPosition(goalWeekly.getNumberOfSport() +""));
        numBoulderSpinner.setSelection(((ArrayAdapter<String>)numBoulderSpinner.getAdapter()).getPosition(goalWeekly.getNumberOfBoulder() +""));
        avgSportSpinner.setSelection(((ArrayAdapter<Grades>)avgSportSpinner.getAdapter()).getPosition(goalWeekly.getAverageSportGrade()));
        avgBoulderSpinner.setSelection(((ArrayAdapter<Grades>)avgBoulderSpinner.getAdapter()).getPosition(goalWeekly.getAverageBoulderGrade()));
        createdOnTxt.setText(goalWeekly.getDateCreated().toLocalDate().toString());
        expiresOnTxt.setText(goalWeekly.getDateExpires().toLocalDate().toString());
        resetWeeklyGoalBtn.setText("RESET WEEKLY GOAL");
        if (goalWeekly.getDateExpires().isBefore(LocalDateTime.now())) {
            //Weekly Goal Expired.
            resetWeeklyGoalBtn.setBackgroundColor(Color.RED);
        }else{
            resetWeeklyGoalBtn.setBackgroundColor(Color.GRAY);
        }
    }
}
