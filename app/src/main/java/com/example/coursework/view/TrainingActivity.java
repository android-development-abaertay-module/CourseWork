package com.example.coursework.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.SEService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.view.adapters.SessionAdapter;
import com.example.coursework.viewmodel.TrainingActivityViewModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class TrainingActivity extends AppCompatActivity {

    private TrainingActivityViewModel trainingActivityViewModel;
    private TextView mTextMessage;
    private Spinner gradeAchievedSpinner;
    private ListView displayRecentLV;
    private User user;
    private List<Session> recentSessions;
    private List<Route> recentRoutes;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_start_session);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_add_route);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_end_session);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        trainingActivityViewModel = ViewModelProviders.of(this).get(TrainingActivityViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra(USER_ID) && intent.hasExtra(USERNAME)){
            user = new User(intent.getStringExtra(USERNAME));
            user.setId(intent.getLongExtra(USER_ID,0));
            trainingActivityViewModel.getUserLD(user.getId());
            trainingActivityViewModel.updateRecentSessions(6,user.getId());
        }

        mTextMessage = (TextView) findViewById(R.id.message);
        gradeAchievedSpinner = findViewById(R.id.gradeAchievedSpinner);
        gradeAchievedSpinner.setAdapter(new ArrayAdapter<Grades>(this, android.R.layout.simple_list_item_1, Grades.values()));
        displayRecentLV = findViewById(R.id.displayRecentLV);

        trainingActivityViewModel.getUserLD(user.getId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userVal) {
                user = userVal;
            }
        });
        trainingActivityViewModel.getRecentSessionsLD().observe(this, new Observer<List<Session>>() {
            @Override
            public void onChanged(@Nullable List<Session> sessions) {
                recentSessions = sessions;
                SessionAdapter adapter = new SessionAdapter(getApplicationContext(),recentSessions);
                displayRecentLV.setAdapter(adapter);
            }
        });
        trainingActivityViewModel.getRecentRoutesLD().observe(this, new Observer<List<Route>>() {
            @Override
            public void onChanged(@Nullable List<Route> routes) {
                recentRoutes = routes;
            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
}
