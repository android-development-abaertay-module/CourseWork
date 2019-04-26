package com.example.coursework.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.model.GoalAnnual;
import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.GoalType;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.LogType;
import com.example.coursework.model.enums.PermissionCheck;
import com.example.coursework.model.enums.RouteType;
import com.example.coursework.model.enums.StyleDone;
import com.example.coursework.model.helper.PrintNull;
import com.example.coursework.view.adapters.RouteAdapter;
import com.example.coursework.view.adapters.SessionAdapter;
import com.example.coursework.viewmodel.TrainingActivityViewModel;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class TrainingActivity extends AppCompatActivity implements LocationListener, View.OnTouchListener {

    //region [Properties]
    private static final int ACCESS_FINE_LOCATION_REQUEST = 1;
    private static final String CHANNEL_ID = "trainingActivityForRouteLog";
    public static final String GOAL_TYPE = "goalType";
    private int weeklyGoalNotificationID = 1;
    private int seasonalGoaNotificationID = 2;
    private int annualGoalNotificationID = 3;


    private TrainingActivityViewModel trainingActivityViewModel;
    BottomNavigationView navigation;
    private GestureDetector routeGestureDetector;
    private GestureDetector sessionGestureDetector;

    MenuItem startMenuItem;
    MenuItem endMenuItem;
    MenuItem addRouteMenuItem;
    private TextView messageTxt;
    private ListView displayRecentRoutesLV;
    private ListView displayRecentSessionsLV;
    private View addRouteForm;
    private User user;
    private PermissionCheck permissionChecked;

    private RadioGroup routeTypeRG;
    private RadioGroup routeStyleRG;
    private Spinner gradeAchievedSpinner;

    LocationManager locationManager;
    private double latitude;
    private double longitude;
    private GoalWeekly weeklyGoal;
    private GoalSeasonal seasonalGoal;
    private GoalAnnual annualGoal;

    //endregion

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        //setting up training activity navigation buttons
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_start_session:
                    messageTxt.setText(R.string.title_start_session);
                    startSession_Click();
                    return true;
                case R.id.navigation_add_route:
                    if (addRouteForm.getVisibility() != View.VISIBLE) {
                        addRouteForm.setVisibility(View.VISIBLE);
                        trainingActivityViewModel.setAddRouteFormVisible(true);
                        displayRecentRoutesLV.setVisibility(View.GONE);
                    } else {
                        addRouteForm.setVisibility(View.GONE);
                        displayRecentRoutesLV.setVisibility(View.VISIBLE);
                        trainingActivityViewModel.setAddRouteFormVisible(false);
                    }
                    return true;
                case R.id.navigation_end_session:
                    endSession_Click();
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        //create channel so training activity can send notifications
        createNotificationChannel();

        trainingActivityViewModel = ViewModelProviders.of(this).get(TrainingActivityViewModel.class);
        //get user details from intent
        Intent intent = getIntent();
        if (intent.hasExtra(USER_ID) && intent.hasExtra(USERNAME)) {
            user = new User(intent.getStringExtra(USERNAME));
            user.setId(intent.getLongExtra(USER_ID, 0));
            //store user in ViewModel
            trainingActivityViewModel.setUserLD(user);
        }

        //region [declare Properties]

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        startMenuItem = navigation.getMenu().findItem(R.id.navigation_start_session);
        endMenuItem = navigation.getMenu().findItem(R.id.navigation_end_session);
        addRouteMenuItem = navigation.getMenu().findItem(R.id.navigation_add_route);
        //setup Routes ListView Gesture detector (so can delete on fling)
        routeGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.OnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }
            @Override
            public void onShowPress(MotionEvent e) {

            }
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }
            @Override
            public void onLongPress(MotionEvent e) {

            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //get original and end locations of fling
                float originalX = e1.getX();
                float originalY = e1.getY();
                float newX = e2.getX();
                float newY = e2.getY();
                float distanceY;
                float distanceX;
                //determine if fling horizontal or vertical
                if (originalX > newX)
                    distanceX = originalX - newX;
                else
                    distanceX = newX - originalX;

                if (originalY > newY)
                    distanceY = originalY - newY;
                else
                    distanceY = newY - originalY;
                //if horizontal - delete selected item
                if (distanceX > distanceY){
                    Log.d("gwyd", "onFling hit for Route : X distance : " + distanceX + ", y distance: " + distanceY);
                    deleteRoute(e1);
                }else{
                    Log.d("gwyd","Vertical fling. just scroll:  X distance : " + distanceX + ", y distance: " + distanceY);
                }


                return true;
            }
        });
        //setup Session ListView Gesture detector (so can delete on fling)
        sessionGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }
            @Override
            public void onShowPress(MotionEvent e) {

            }
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }
            @Override
            public void onLongPress(MotionEvent e) {

            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //get initial and end locations of fling
                float originalX = e1.getX();
                float originalY = e1.getY();
                float newX = e2.getX();
                float newY = e2.getY();
                float distanceY;
                float distanceX;
                //determin if fling horizontal or vertical
                if (originalX > newX)
                    distanceX = originalX - newX;
                else
                    distanceX = newX - originalX;

                if (originalY > newY)
                    distanceY = originalY - newY;
                else
                    distanceY = newY - originalY;
                //if horizontal delete session
                if (distanceX > distanceY) {
                    Log.d("gwyd", "onFling hit for Session : X distance : " + distanceX + ", y distance: " + distanceY);
                    int startingPosition = displayRecentSessionsLV.pointToPosition((int) e1.getX(), (int) e1.getY());
                    if (startingPosition != -1){
                        Session sessionToDelete = (Session) displayRecentSessionsLV.getAdapter().getItem(startingPosition);
                        deleteSession(sessionToDelete);
                    }
                }else{
                    Log.d("gwyd","Vertical fling. just scroll:  X distance : " + distanceX + ", y distance: " + distanceY);
                }
                    return true;
            }
        });
        messageTxt = findViewById(R.id.message);

        displayRecentRoutesLV = findViewById(R.id.displayRecentRoutesLV);
        displayRecentRoutesLV.setOnTouchListener(this);
        displayRecentSessionsLV = findViewById(R.id.displayRecentSessionsLV);
        displayRecentSessionsLV.setOnTouchListener(this);
        addRouteForm = findViewById(R.id.addRouteLayout);

        routeTypeRG = findViewById(R.id.routeTypeBtnGrp);
        routeStyleRG = findViewById(R.id.styleDoneBtnGrp);
        gradeAchievedSpinner = findViewById(R.id.gradeAchievedSpinner);
        gradeAchievedSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Grades.values()));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //endregion

        //region [Register Observers]
        //get current latitude from ViewModel
        trainingActivityViewModel.getCurrentLatitudeLD().observe(this, latitudeVal -> latitude = latitudeVal);

        //get current longitude from ViewModel
        trainingActivityViewModel.getCurrentLongitudeLD().observe(this, longitudeVal -> longitude = longitudeVal);

        //get weather addRoute form visible from ViewModel
        trainingActivityViewModel.getAddRouteFormVisible().observe(this, isVisible ->{
            if (isVisible !=null)
                //update view to match view model
                if (isVisible){
                    addRouteForm.setVisibility(View.VISIBLE);
                }else{
                    addRouteForm.setVisibility(View.GONE);
                }
        });

        //get flag to track if location permission granted from ViewModel
        trainingActivityViewModel.getLocationPermissionGranted().observe(this, permissionCheckVal -> {
            permissionChecked = permissionCheckVal;
            //if permissions already denied by user on this visit to TrainingActivity don't show dialog again.
            if (permissionCheckVal == PermissionCheck.NOT_REQUESTED || permissionCheckVal == null) {
                checkPermissions();
            }
        });

        //get user from ViewModel
        trainingActivityViewModel.getUserLD().observe(this, userVal -> user = userVal);

        //get current session from ViewModel
        trainingActivityViewModel.getCurrentSession().observe(this, session -> {
            user.setCurSesh(session);
            //check if user has current session
            if (user.getCurSesh() == null) {
                //if not: Display Start Session stuff and hide route stuff
                startMenuItem.setChecked(false);
                startMenuItem.setVisible(true);
                endMenuItem.setVisible(false);
                addRouteMenuItem.setEnabled(false);
                displayRecentRoutesLV.setVisibility(View.GONE);
                displayRecentSessionsLV.setVisibility(View.VISIBLE);
            } else {
                //We have a Current Session. display add route stuff and hide start session stuff
                endMenuItem.setChecked(true);
                startMenuItem.setVisible(false);
                addRouteMenuItem.setEnabled(true);
                endMenuItem.setVisible(true);
                displayRecentSessionsLV.setVisibility(View.GONE);
                displayRecentRoutesLV.setVisibility(View.VISIBLE);
                //check if current session has routes
                if (user.getCurSesh().getRoutes().size() == 0) {
                    //no recent routes to display:
                    messageTxt.setText(getString(R.string.no_routes_to_display, PrintNull.Print(user.getCurSesh().getLocation())));
                } else {
                    //display recent routes
                    messageTxt.setText(getString(R.string.recent_routes,PrintNull.Print(user.getCurSesh().getLocation())));
                }
                //add new route collection to the routes List View via the RouteAdapter
                RouteAdapter adapter = new RouteAdapter(getApplicationContext(), user.getCurSesh().getRoutes());
                displayRecentRoutesLV.setAdapter(adapter);
            }
        });

        //get recent sessions from ViewModel
        trainingActivityViewModel.getRecentSessionsLD().observe(this, sessions -> {
            //check user has recent sessions
            if (sessions != null) {
                //put recent sessions in the sessions list view via the SessionAdapter
                user.setSessionsList((ArrayList<Session>) sessions);
                SessionAdapter adapter = new SessionAdapter(getApplicationContext(), user.getSessionsList());
                displayRecentSessionsLV.setAdapter(adapter);
            }
        });

        //get recent routes for uses from the viewModel
        trainingActivityViewModel.getRecentRoutesForUserLD().observe(this, routes -> {
            //if user has no current session then they have no routes to display in session
            if (user.getCurSesh() != null) {
                user.getCurSesh().getRoutes().clear();
                if (routes != null) {
                    //add routes to the users session
                    for (Route r : routes) {
                        if (r.getSessionId() == user.getCurSesh().getId())
                            user.getCurSesh().getRoutes().add(r);
                    }
                    if (user.getCurSesh().getRoutes().size() == 0) {
                        //no recent routes to display:
                        messageTxt.setText(getString(R.string.no_routes_to_display, PrintNull.Print(user.getCurSesh().getLocation())));
                    } else {
                        messageTxt.setText(getString(R.string.recent_routes,PrintNull.Print(user.getCurSesh().getLocation())));
                    }
                    //put routes in list view via RouteAdapter
                    RouteAdapter adapter = new RouteAdapter(getApplicationContext(), user.getCurSesh().getRoutes());
                    displayRecentRoutesLV.setAdapter(adapter);
                } else {
                    Log.d("gwyd", "no routes returned");
                    messageTxt.setText(getString(R.string.no_routes_to_display, PrintNull.Print(user.getCurSesh().getLocation())));
                }
            }
        });
        //get Weekly Goal from ViewModel
        trainingActivityViewModel.getGoalWeeklyLD().observe(this ,goalWeeklyVal -> {
            weeklyGoal = goalWeeklyVal;
            //check if weekly goal is set
            if (weeklyGoal != null){
                //check if weekly goal achieved
                if (weeklyGoal.getGoalAchieved()) {
                    //check if notification already sent
                    if (!weeklyGoal.isAchievedSent()){
                        //send notification for goal achieved
                        sendNotificationToCheckGoals("Weekly Goal Accomplished", "Weekly Goal Accomplished \n Review and Re-set Goal.", GoalType.WEEKLY, seasonalGoaNotificationID, 0);
                        //set weekly goal notification sent flag
                        weeklyGoal.setAchievedSent(true);
                        trainingActivityViewModel.updateGoalWeekly(weeklyGoal);
                }else
                        Log.d("gwyd","Weekly goal achieved notification already sent.");
                }else
                    Log.d("gwyd","Weekly goal Not achieved");
            }
        });

        //get seasonal goal from ViewModel
        trainingActivityViewModel.getGoalSeasonalLD().observe(this, goalSeasonalVal ->{
            seasonalGoal = goalSeasonalVal;
            //check if seasonal goal set
            if(seasonalGoal !=null){
                //check if Achieved
                if (seasonalGoal.getGoalAchieved()){
                    //check if notification already sent
                    if (!seasonalGoal.isAchievedSent()) {
                        //send notification
                        sendNotificationToCheckGoals("Seasonal Goal Accomplished", "Seasonal Goal Accomplished \n Review and Re-set Goal.", GoalType.SEASONAL, weeklyGoalNotificationID, 1);
                        //update notification sent flag for seasonal goal
                        seasonalGoal.setAchievedSent(true);
                        trainingActivityViewModel.updateGoalSeasonal(seasonalGoal);
                    }else
                        Log.d("gwyd","Seasonal goal achieved notification already sent.");
                }else
                    Log.d("gwyd","Seasonal goal Not achieved");
            }
        });

        //get annual goal from ViewModel
        trainingActivityViewModel.getGoalAnnualLD().observe(this, goalAnnualVal ->{
            annualGoal = goalAnnualVal;
            //check if annual goal set
            if (annualGoal != null){
                //check if Achieved
                if(annualGoal.getGoalAchieved()) {
                    //check if notification sent
                    if (!annualGoal.isAchievedSent()) {
                        //send notification for goal achieved
                        sendNotificationToCheckGoals("Annual Goal Accomplished", "Annual Goal Accomplished \n Review and Re-set Goal.", GoalType.ANNUAL, annualGoalNotificationID, 2);
                        //update notification sent flag in annal goal
                        annualGoal.setAchievedSent(true);
                        trainingActivityViewModel.updateGoalAnnual(annualGoal);
                    }else
                        Log.d("gwyd","Annual goal achieved notification already sent.");

                }else
                    Log.d("gwyd","Annual goal Not achieved");
            }
        });
        //endregion
    }



    private void sendNotificationToCheckGoals(String title, String contentText, GoalType goalType, int notificationID, int requestCode) {
        // Create an explicit intent to navigate to check goals activity
        Intent i = new Intent(TrainingActivity.this, CheckGoalsActivity.class);
        i.putExtra(USER_ID,user.getId());
        i.putExtra(USERNAME,user.getUserName());
        //goal type used to tell activity what fragment to initially display
        i.putExtra(GOAL_TYPE,goalType.getValue());
        //flags to open it as a new activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //pending intent only to fire once notification clicked
        PendingIntent pendingIntent = PendingIntent.getActivity(TrainingActivity.this, requestCode,i,0);
        //build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(TrainingActivity.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(contentText)
                .setGroup("TrainingGoals")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);
        //notification manager used to send notifications
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(TrainingActivity.this);
        //send the notification
        notificationManager.notify(notificationID, builder.build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop location manager receiving updates. saves battery
        Log.d("gwyd", "onPause: Removing location update from the location manager");
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //confirm permissions have been granted
        if (permissionChecked == null) {
            Log.d("gwyd", "onResume: location permissions not requested yet. ");
            checkPermissions();
        } else if (permissionChecked == PermissionCheck.GRANTED) {
            //re-enable location updates
            Log.d("gwyd", "onResume: location permissions already granted. Re assigning listener");
            turnOnLocationTracking();
        } else {
            //permission already denied.
            Log.d("gwyd", "onResume: location permissions already denied by user in this activity");
        }
    }

    private void startSession_Click() {
        //create a new session
        user.setCurSesh(new Session(OffsetDateTime.now(), user.getId()));
        //only set session location if permission granted
        if (permissionChecked != null)
            if (permissionChecked == PermissionCheck.GRANTED) {
                //if location services are disabled on the phone latitude and longitude are defaulted to 0.0
                user.getCurSesh().setLat(latitude);
                user.getCurSesh().setLon(longitude);
                if (user.getCurSesh().getLon() != 0){
                    //set the string value of the location
                   user.getCurSesh().setLocation(getAddressFromLocation(user.getCurSesh()));
                }
            }
            //save the new session
        trainingActivityViewModel.CreateNewSession(user.getCurSesh());
    }

    public void addRouteBtn_Click(View view) {
        //pull route info from form
        int selectedRouteTypeID = routeTypeRG.getCheckedRadioButtonId();
        RadioButton RTRadioBtn = findViewById(selectedRouteTypeID);
        RouteType routeType = RouteType.getFromInteger(Integer.parseInt(RTRadioBtn.getTag().toString()));
        int selectedStyleTypeID = routeStyleRG.getCheckedRadioButtonId();
        StyleDone styleDone = StyleDone.getFromInteger(Integer.parseInt(findViewById(selectedStyleTypeID).getTag().toString()));
        Grades grade = (Grades) gradeAchievedSpinner.getSelectedItem();
        //create new route & save
        Route newRoute = new Route(user.getCurSesh().getId(), user.getId(), grade, routeType, styleDone, OffsetDateTime.now());
        trainingActivityViewModel.addRoute(newRoute);
        //hide add route form and update view model flag to show/hide form
        addRouteForm.setVisibility(View.GONE);
        trainingActivityViewModel.setAddRouteFormVisible(false);
        displayRecentRoutesLV.setVisibility(View.VISIBLE);
        //check  goal progress to see if any complete
        checkGoalProgress();
    }

    private void checkGoalProgress() {
        //go through each goal and see if has been completed
        if (weeklyGoal != null)
            trainingActivityViewModel.updateGoalSetWasWeeklyGoalMet(weeklyGoal);
        if (seasonalGoal != null)
            trainingActivityViewModel.updateGoalSetWasSeasonalGoalMet(seasonalGoal);
        if (annualGoal != null)
            trainingActivityViewModel.updateGoalSetWasAnnualGoalMet(annualGoal);
    }

    private void endSession_Click() {
        //hide the add route form if its visible
        if (addRouteForm.getVisibility() == View.VISIBLE){
            addRouteForm.setVisibility(View.GONE);
            trainingActivityViewModel.setAddRouteFormVisible(false);
        }
        //close the users current session if they have one (they should always if  end session visible)
        if (user.getCurSesh() != null) {
            user.getCurSesh().setEndTime(OffsetDateTime.now());
            trainingActivityViewModel.updateCurrentSession(user.getCurSesh());
        } else {
            toastAndLog("No Active Session",LogType.ERROR);
        }
    }

    private void checkPermissions() {
        //check if location permissions granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("gwyd", "access fine location permission not granted. Requesting permission");
            //if not request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_REQUEST);

        } else {
            Log.d("gwyd", "access fine location permission already granted");
            //permissions already granted turn on location listening...
            turnOnLocationTracking();
        }
    }

    private void turnOnLocationTracking() {
        //(1 * 60 * 1000) time = every minute,
        //0 = don't care about refreshing on distance as people don't move about far while climbing
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("gwyd", "granted");
            trainingActivityViewModel.setLocationPermissionGranted(PermissionCheck.GRANTED);
            //listening to both providers for speed when getting initial location
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (60 * 1000),0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, (60 * 1000),0, this);
        }
        else{
            Log.d("gwyd", "location manager requesting updates skipped because of lack of permissions. shouldn't get hit");
            throw new RuntimeException("location manager requesting updates skipped because of lack of permissions. shouldn't get hit");
        }
    }

    private void deleteSession(Session sessionToDelete) {
        //dispaly confirmation dialog for delete sessions
        new AlertDialog.Builder(this)
                .setTitle("Delete Session?")
                .setMessage("Are you sure you want to delete this session? \nAll routes associated will also be deleted.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    //if confirmation accepted...
                    trainingActivityViewModel.deleteSession(sessionToDelete);
                    toastAndLog("Session Deleted",LogType.DEBUG);
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
    private void deleteRoute(MotionEvent e1) {
        //get the position of the ListViewItem
        int  startingPosition = displayRecentRoutesLV.pointToPosition((int) e1.getX(), (int) e1.getY());
        if(startingPosition  != -1){
            //show confirmation dialog for delete Routes
            new AlertDialog.Builder(this)
                    .setTitle("Delete Route?")
                    .setMessage("Are you sure you want to delete this Route?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        //horizontal fling - delete
                        Log.d("gwyd","onFling hit for Route");
                        //get route by its position
                        Route routeToDelete = (Route) displayRecentRoutesLV.getAdapter().getItem(startingPosition);
                        //delete route
                        trainingActivityViewModel.deleteRoute(routeToDelete);
                        toastAndLog("Route Deleted", LogType.DEBUG);
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }



    }
    private String getAddressFromLocation(Session session) {
        String address = "";
        try {
            Geocoder geocoder;
            geocoder = new Geocoder(this, Locale.getDefault());

            //Geocoder used to get address from location
            List<Address> localAddress = geocoder.getFromLocation(session.getLat(), session.getLon(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = localAddress.get(0).getAddressLine(0);
            Log.d("gwyd","lat and lon used for below address was: " + session.getLat() + "   " + session.getLon());
            Log.d("gwyd","address found for location was : " + address);
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("gwyd","Failed to Find Address");
        return address;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //check if location permission granted
            case ACCESS_FINE_LOCATION_REQUEST:
                Log.d("gwyd", "ACCESS_FINE_LOCATION_REQUEST received");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permissions granted
                    turnOnLocationTracking();

                } else {
                    //permission denied
                    Log.d("gwyd", "denied");
                    Toast.makeText(this,"Session Location will not be stored unless Location Permissions are granted",Toast.LENGTH_LONG).show();
                    trainingActivityViewModel.setLocationPermissionGranted(PermissionCheck.DENIED);
                }
                break;
        }
    }

    //region [Location Listener Methods]
    @Override
    public void onLocationChanged(Location location) {
        Log.d("gwyd","onLocationChanged hit: lat" + location.getLatitude() + " -   lon" + location.getLongitude());
        //save old lat long to see if location already pulled
        double oldLat = latitude;
        double oldLong = longitude;
        if (oldLat == 0 && oldLong ==0){
            //session was started before location update received (it takes a second or two)
            if (user.getCurSesh() != null){
                //update the current session if it doesn't already have a location associated (eg session started and then location enabled)
                if (user.getCurSesh().getLon() == 0 || user.getCurSesh().getLat() == 0) {
                    user.getCurSesh().setLon(location.getLongitude());
                    user.getCurSesh().setLat(location.getLatitude());
                    user.getCurSesh().setLocation(getAddressFromLocation(user.getCurSesh()));

                    trainingActivityViewModel.updateCurrentSession(user.getCurSesh());
                }
            }
        }

        //store the current latitude and longitude in the ViewModel
        trainingActivityViewModel.setCurrentLatitudeLD(location.getLatitude());
        trainingActivityViewModel.setCurrentLongitudeLD(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("gwyd","onStatusChanged hit");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("gwyd","onProviderEnabled hit");

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("gwyd","onProviderDisabled hit");

    }
    //endregion

    //region[OnTouchListener]
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("gwyd","onTouch hit");
        //Gesture detectors setup for delete sessions and routes on fling
        switch (v.getId()){
            case R.id.displayRecentRoutesLV:
                routeGestureDetector.onTouchEvent(event);
                break;
            case R.id.displayRecentSessionsLV:
                sessionGestureDetector.onTouchEvent(event);
                break;
        }
        return super.onTouchEvent(event);
    }
    //endregion

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ (this one) because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system;
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
    }

    private void toastAndLog(String message, LogType logType){
        switch (logType){
            case INFO:
                Log.i("gwyd",message);
                break;
            case DEBUG:
                Log.d("gwyd",message);
                break;
            case ERROR:
                Log.e("gwyd",message);
                break;
            case VERBOSE:
                Log.v("gwyd",message);
                break;
            case WARNING:
                Log.w("gwyd",message);
                break;
        }
        Toast.makeText(TrainingActivity.this,message,Toast.LENGTH_LONG).show();
    }
}
