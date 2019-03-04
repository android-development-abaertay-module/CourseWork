package com.example.coursework.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.model.Logbook;
import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.view.adapters.RouteAdapter;
import com.example.coursework.view.adapters.SessionAdapter;
import com.example.coursework.viewmodel.TrainingActivityViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class TrainingActivity extends AppCompatActivity {

    //region [Properties]
    private TrainingActivityViewModel trainingActivityViewModel;
    BottomNavigationView navigation;
    MenuItem startMenuItem;
    MenuItem endMenuItem;
    MenuItem addRouteMenuItem;
    private TextView mTextMessage;
    private Spinner gradeAchievedSpinner;
    private ListView displayRecentRoutesLV;
    private ListView displayRecentSessionsLV;
    private User user;
    private Logbook logbook;
    private Session currentSession;
    private List<Session> recentSessions;
    private List<Route> recentRoutes;
    //endregion

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_start_session:
                    mTextMessage.setText(R.string.title_start_session);
                    startSession();
                    return true;
                case R.id.navigation_add_route:
                    mTextMessage.setText(R.string.title_add_route);
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
            trainingActivityViewModel.getLogbookLD(user.getId());
            trainingActivityViewModel.getCurrentSession(user.getId());
        }
        //region [declare Properties]
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        startMenuItem = navigation.getMenu().findItem(R.id.navigation_start_session);
        endMenuItem = navigation.getMenu().findItem(R.id.navigation_end_session);
        addRouteMenuItem = navigation.getMenu().findItem(R.id.navigation_add_route);

        mTextMessage =  findViewById(R.id.message);
        gradeAchievedSpinner = findViewById(R.id.gradeAchievedSpinner);
        gradeAchievedSpinner.setAdapter(new ArrayAdapter<Grades>(this, android.R.layout.simple_list_item_1, Grades.values()));
        displayRecentRoutesLV = findViewById(R.id.displayRecentRoutesLV);
        displayRecentSessionsLV = findViewById(R.id.displayRecentSessionsLV);
        //endregion

        //region [Register Observers]
        trainingActivityViewModel.getUserLD(user.getId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userVal) {
                user = userVal;
            }
        });
        trainingActivityViewModel.getLogbookLD(user.getId()).observe(this, new Observer<Logbook>() {
            @Override
            public void onChanged(@Nullable Logbook logbookVal) {
                logbook = logbookVal;
            }
        });
        trainingActivityViewModel.getCurrentSession(user.getId()).observe(this, new Observer<Session>() {
            @Override
            public void onChanged(@Nullable Session session) {
                currentSession = session;
                if (currentSession == null){
                    //Display Start Session stuff
                    startMenuItem.setChecked(false);
                    startMenuItem.setVisible(true);
                    endMenuItem.setVisible(false);
                    addRouteMenuItem.setEnabled(false);
                    displayRecentRoutesLV.setVisibility(View.INVISIBLE);
                    displayRecentSessionsLV.setVisibility(View.VISIBLE);
                }else{
                    //We have a Current Session. display add route stuff
                    endMenuItem.setChecked(true);
                    startMenuItem.setVisible(false);
                    addRouteMenuItem.setEnabled(true);
                    endMenuItem.setVisible(true);
                    displayRecentSessionsLV.setVisibility(View.INVISIBLE);
                    displayRecentRoutesLV.setVisibility(View.VISIBLE);
                }
            }
        });

        trainingActivityViewModel.getRecentSessionsLD(user.getId()).observe(this, new Observer<List<Session>>() {
            @Override
            public void onChanged(@Nullable List<Session> sessions) {
                if (sessions != null) {
                    recentSessions = sessions;
                    SessionAdapter adapter = new SessionAdapter(getApplicationContext(), recentSessions);
                    displayRecentSessionsLV.setAdapter(adapter);
                }else{
                    Log.d("gwyd","no sessions found");
                    //TODO:sort display
                }
            }
        });
        trainingActivityViewModel.getRecentRoutesForUserLD(user.getId()).observe(this, new Observer<List<Route>>() {
            @Override
            public void onChanged(@Nullable List<Route> routes) {
                if (routes != null){
                    if (routes.size() ==0){
                        //no recent routes to display:
                        Log.d("gwyd","no routes to display");
                        Toast.makeText(getApplicationContext(),"Start Training to See Routes",Toast.LENGTH_SHORT).show();
                    }
                    recentRoutes = routes;
                    RouteAdapter adapter = new RouteAdapter(getApplicationContext(),recentRoutes);
                    displayRecentRoutesLV.setAdapter(adapter);
                }else{
                    Log.d("gwyd","no routes returned");
                    //TODO:display something here
                }
            }
        });
        //endregion
    }


    private void startSession() {
        currentSession = new Session(LocalDateTime.now(),logbook.getId());
        trainingActivityViewModel.CreateNewSession(currentSession);
    }
}
