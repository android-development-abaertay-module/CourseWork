package com.example.coursework.view;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.example.coursework.model.enums.PermissionCheck;
import com.example.coursework.model.enums.RouteType;
import com.example.coursework.model.enums.StyleDone;
import com.example.coursework.view.adapters.RouteAdapter;
import com.example.coursework.view.adapters.SessionAdapter;
import com.example.coursework.viewmodel.TrainingActivityViewModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class TrainingActivity extends AppCompatActivity {

    private static final int ACCESS_FINE_LOCATION_REQUEST = 1;
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
        trainingActivityViewModel.getLocationPermissionGranted().observe(this, new Observer<PermissionCheck>() {
            @Override
            public void onChanged(@Nullable PermissionCheck permissionCheckVal) {
                //if permissions already denied by user on this visit to TrainingActivity don't show dialog again.
                if (permissionCheckVal == PermissionCheck.NOT_REQUESTED || permissionCheckVal == null) {
                    checkPermissions();
                }
            }
        });
        trainingActivityViewModel.getUserLD(user.getId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userVal) {
                user = userVal;
            }
        });
        trainingActivityViewModel.getCurrentSession(user.getId()).observe(this, new Observer<Session>() {
            @Override
            public void onChanged(@Nullable Session session) {
                user.setCurSesh(session);
                if (user.getCurSesh() == null){
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

                    if (user.getCurSesh().getRoutes().size() == 0) {
                        //no recent routes to display:
                        mTextMessage.setVisibility(View.VISIBLE);
                        mTextMessage.setText(R.string.no_routes_to_display);
                    } else {
                        mTextMessage.setVisibility(View.GONE);
                    }
                    RouteAdapter adapter = new RouteAdapter(getApplicationContext(), user.getCurSesh().getRoutes());
                    displayRecentRoutesLV.setAdapter(adapter);
                }
            }
        });

        trainingActivityViewModel.getRecentSessionsLD(user.getId()).observe(this, new Observer<List<Session>>() {
            @Override
            public void onChanged(@Nullable List<Session> sessions) {
                if (sessions != null) {
                    user.setSessionsList((ArrayList)sessions);
                    SessionAdapter adapter = new SessionAdapter(getApplicationContext(), user.getSessionsList());
                    displayRecentSessionsLV.setAdapter(adapter);
                    mTextMessage.setVisibility(View.GONE);
                }else{
                    Log.d("gwyd","no sessions found");
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.no_recent_sesions_to_display);
                }
            }
        });
        trainingActivityViewModel.getRecentRoutesForUserLD(user.getId()).observe(this, new Observer<List<Route>>() {
            @Override
            public void onChanged(@Nullable List<Route> routes) {
                //if user has no current session then they have no routes to display in session
                if (user.getCurSesh() != null) {
                    user.getCurSesh().getRoutes().clear();

                    if (routes != null) {

                        for (Route r : routes) {
                            if (r.getSessionId() == user.getCurSesh().getId())
                                user.getCurSesh().getRoutes().add(r);
                        }
                        if (user.getCurSesh().getRoutes().size() == 0) {
                            //no recent routes to display:
                            mTextMessage.setVisibility(View.VISIBLE);
                            mTextMessage.setText(R.string.no_routes_to_display);
                        } else {
                            mTextMessage.setVisibility(View.GONE);
                        }
                        RouteAdapter adapter = new RouteAdapter(getApplicationContext(), user.getCurSesh().getRoutes());
                        displayRecentRoutesLV.setAdapter(adapter);
                    } else {
                        Log.d("gwyd", "no routes returned");
                        mTextMessage.setVisibility(View.VISIBLE);
                        mTextMessage.setText(R.string.no_routes_to_display);
                    }
                }
            }
        });
        //endregion
    }


    private void startSession_Click() {
        user.setCurSesh( new Session(LocalDateTime.now(),user.getId()));
        trainingActivityViewModel.CreateNewSession(user.getCurSesh());
    }

    public void addRouteBtn_Click(View view) {
        int selectedRouteTypeID =  routeTypeRG.getCheckedRadioButtonId();
        RadioButton RTRadioBtn = findViewById(selectedRouteTypeID);
        RouteType routeType = RouteType.getFromInteger(Integer.parseInt(RTRadioBtn.getTag().toString()));
        int selectedStyleTypeID = routeStyleRG.getCheckedRadioButtonId();
        StyleDone styleDone = StyleDone.getFromInteger(Integer.parseInt(findViewById(selectedStyleTypeID).getTag().toString()));
        Grades grade =(Grades) gradeAchievedSpinner.getSelectedItem();

        Route newRoute = new Route(user.getCurSesh().getId(),user.getId(),grade,routeType,styleDone,LocalDateTime.now());
        trainingActivityViewModel.addRoute(newRoute);
        addRouteForm.setVisibility(View.GONE);
        displayRecentRoutesLV.setVisibility(View.VISIBLE);
    }
    private void endSession_Click() {
        if (addRouteForm.getVisibility() == View.VISIBLE)
            addRouteForm.setVisibility(View.GONE);
        if (user.getCurSesh() != null){
            user.getCurSesh().setEndTime(LocalDateTime.now());
            trainingActivityViewModel.updateCurrentSession(user.getCurSesh());
        }else {
            Toast.makeText(this,"No Active Session",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_REQUEST);

        } else {
            Log.d("gwyd","access fine location permission already granted");
            //permissions already granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case ACCESS_FINE_LOCATION_REQUEST:
                Log.d("gwyd","ACCESS_FINE_LOCATION_REQUEST received");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permissions granted
                    Log.d("gs", "granted");
                    trainingActivityViewModel.setLocationPermissionGranted(PermissionCheck.GRANTED);
                } else {
                    //permission denied
                    Log.d("gs", "denied");
                    Toast.makeText(this,"Session Location will not be stored unless Location Permissions are granted",Toast.LENGTH_LONG).show();
                    trainingActivityViewModel.setLocationPermissionGranted(PermissionCheck.DENIED);
                }
                break;
        }
    }
}
