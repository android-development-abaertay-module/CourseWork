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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.RouteType;
import com.example.coursework.model.enums.StyleDone;
import com.example.coursework.view.adapters.RouteAdapter;
import com.example.coursework.view.adapters.SessionAdapter;
import com.example.coursework.viewmodel.TrainingActivityViewModel;

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
    private ListView displayRecentRoutesLV;
    private ListView displayRecentSessionsLV;
    private View addRouteForm;
    private User user;
    private Session currentSession;
    private List<Session> recentSessions;
    private List<Route> recentRoutes;

    private RadioGroup routeTypeRG;
    private RadioGroup routeStyleRG;
    private Spinner gradeAchievedSpinner;
    //endregion

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_start_session:
                    mTextMessage.setText(R.string.title_start_session);
                    startSession_Click();
                    return true;
                case R.id.navigation_add_route:
                    mTextMessage.setText(R.string.add_route);
                    if(addRouteForm.getVisibility() != View.VISIBLE) {
                        addRouteForm.setVisibility(View.VISIBLE);
                        displayRecentRoutesLV.setVisibility(View.GONE);
                    }
                    else {
                        addRouteForm.setVisibility(View.GONE);
                        displayRecentRoutesLV.setVisibility(View.VISIBLE);
                    }
                    return true;
                case R.id.navigation_end_session:
                    endSession_Click();
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
            trainingActivityViewModel.getCurrentSession(user.getId());
        }

        //region [declare Properties]
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        startMenuItem = navigation.getMenu().findItem(R.id.navigation_start_session);
        endMenuItem = navigation.getMenu().findItem(R.id.navigation_end_session);
        addRouteMenuItem = navigation.getMenu().findItem(R.id.navigation_add_route);

        mTextMessage =  findViewById(R.id.message);

        displayRecentRoutesLV = findViewById(R.id.displayRecentRoutesLV);
        displayRecentSessionsLV = findViewById(R.id.displayRecentSessionsLV);
        addRouteForm = findViewById(R.id.addRouteLayout);

        routeTypeRG = findViewById(R.id.routeTypeBtnGrp);
        routeStyleRG = findViewById(R.id.styleDoneBtnGrp);
        gradeAchievedSpinner = findViewById(R.id.gradeAchievedSpinner);
        gradeAchievedSpinner.setAdapter(new ArrayAdapter<Grades>(this, android.R.layout.simple_list_item_1, Grades.values()));
        //endregion

        //region [Register Observers]
        trainingActivityViewModel.getUserLD(user.getId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userVal) {
                user = userVal;
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
                    displayRecentRoutesLV.setVisibility(View.GONE);
                    displayRecentSessionsLV.setVisibility(View.VISIBLE);
                }else{
                    //We have a Current Session. display add route stuff
                    endMenuItem.setChecked(true);
                    startMenuItem.setVisible(false);
                    addRouteMenuItem.setEnabled(true);
                    endMenuItem.setVisible(true);
                    displayRecentSessionsLV.setVisibility(View.GONE);
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
                    mTextMessage.setVisibility(View.GONE);
                }else{
                    Log.d("gwyd","no sessions found");
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText("No Recent Sessions to Display");
                }
            }
        });
        trainingActivityViewModel.getRecentRoutesForUserLD(user.getId()).observe(this, new Observer<List<Route>>() {
            @Override
            public void onChanged(@Nullable List<Route> routes) {
                if (routes != null){
                    if (routes.size() ==0){
                        //no recent routes to display:
                        mTextMessage.setVisibility(View.VISIBLE);
                        mTextMessage.setText("No Recent Routes to Display. Add Some");
                    }else{
                        mTextMessage.setVisibility(View.GONE);
                    }
                    recentRoutes = routes;
                    RouteAdapter adapter = new RouteAdapter(getApplicationContext(),recentRoutes);
                    displayRecentRoutesLV.setAdapter(adapter);
                }else{
                    Log.d("gwyd","no routes returned");
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText("No Recent Routes to Display");
                }
            }
        });
        //endregion
    }


    private void startSession_Click() {
        currentSession = new Session(LocalDateTime.now(),user.getId());
        trainingActivityViewModel.CreateNewSession(currentSession);
    }

    public void addRouteBtn_Click(View view) {
        int selectedRouteTypeID =  routeTypeRG.getCheckedRadioButtonId();
        RadioButton RTRadioBtn = findViewById(selectedRouteTypeID);
        RouteType routeType = RouteType.getFromInteger(Integer.parseInt(RTRadioBtn.getTag().toString()));
        int selectedStyleTypeID = routeStyleRG.getCheckedRadioButtonId();
        StyleDone styleDone = StyleDone.getFromInteger(Integer.parseInt(findViewById(selectedStyleTypeID).getTag().toString()));
        Grades grade =(Grades) gradeAchievedSpinner.getSelectedItem();

        Route newRoute = new Route(currentSession.getId(),user.getId(),grade,routeType,styleDone,LocalDateTime.now());
        trainingActivityViewModel.addRoute(newRoute);
        addRouteForm.setVisibility(View.GONE);
    }
    private void endSession_Click() {
        if (addRouteForm.getVisibility() == View.VISIBLE)
            addRouteForm.setVisibility(View.GONE);
        if (currentSession != null){
            currentSession.setEndTime(LocalDateTime.now());
            trainingActivityViewModel.updateCurrentSession(currentSession);
        }else {
            Toast.makeText(this,"No Active Session",Toast.LENGTH_SHORT).show();
        }

    }
}
