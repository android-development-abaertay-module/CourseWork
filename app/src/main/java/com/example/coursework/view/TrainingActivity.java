package com.example.coursework.view;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class TrainingActivity extends AppCompatActivity implements LocationListener {

    private static final int ACCESS_FINE_LOCATION_REQUEST = 1;
    //region [Properties]
    private TrainingActivityViewModel trainingActivityViewModel;
    BottomNavigationView navigation;
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

    //endregion

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

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
                        displayRecentRoutesLV.setVisibility(View.GONE);
                    } else {
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
        if (intent.hasExtra(USER_ID) && intent.hasExtra(USERNAME)) {
            user = new User(intent.getStringExtra(USERNAME));
            user.setId(intent.getLongExtra(USER_ID, 0));
            trainingActivityViewModel.setUserLD(user);
        }

        //region [declare Properties]
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        startMenuItem = navigation.getMenu().findItem(R.id.navigation_start_session);
        endMenuItem = navigation.getMenu().findItem(R.id.navigation_end_session);
        addRouteMenuItem = navigation.getMenu().findItem(R.id.navigation_add_route);

        messageTxt = findViewById(R.id.message);

        displayRecentRoutesLV = findViewById(R.id.displayRecentRoutesLV);
        displayRecentSessionsLV = findViewById(R.id.displayRecentSessionsLV);
        addRouteForm = findViewById(R.id.addRouteLayout);

        routeTypeRG = findViewById(R.id.routeTypeBtnGrp);
        routeStyleRG = findViewById(R.id.styleDoneBtnGrp);
        gradeAchievedSpinner = findViewById(R.id.gradeAchievedSpinner);
        gradeAchievedSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Grades.values()));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //endregion

        //region [Register Observers]
        trainingActivityViewModel.getCurrentLatitudeLD().observe(this, latitudeVal -> {
            latitude = latitudeVal;
        });
        trainingActivityViewModel.getCurrentLongitudeLD().observe(this, longitudeVal -> {
            longitude = longitudeVal;
        });

        trainingActivityViewModel.getLocationPermissionGranted().observe(this, permissionCheckVal -> {
            permissionChecked = permissionCheckVal;
            //if permissions already denied by user on this visit to TrainingActivity don't show dialog again.
            if (permissionCheckVal == PermissionCheck.NOT_REQUESTED || permissionCheckVal == null) {
                checkPermissions();
            }
        });
        trainingActivityViewModel.getUserLD().observe(this, userVal -> {
            user = userVal;
        });
        trainingActivityViewModel.getCurrentSession(user.getId()).observe(this, session -> {
            user.setCurSesh(session);
            if (user.getCurSesh() == null) {
                //Display Start Session stuff
                startMenuItem.setChecked(false);
                startMenuItem.setVisible(true);
                endMenuItem.setVisible(false);
                addRouteMenuItem.setEnabled(false);
                displayRecentRoutesLV.setVisibility(View.GONE);
                displayRecentSessionsLV.setVisibility(View.VISIBLE);
            } else {
                //We have a Current Session. display add route stuff
                endMenuItem.setChecked(true);
                startMenuItem.setVisible(false);
                addRouteMenuItem.setEnabled(true);
                endMenuItem.setVisible(true);
                displayRecentSessionsLV.setVisibility(View.GONE);
                displayRecentRoutesLV.setVisibility(View.VISIBLE);

                if (user.getCurSesh().getRoutes().size() == 0) {
                    //no recent routes to display:
                    messageTxt.setText(getString(R.string.no_routes_to_display, PrintNull.Print(user.getCurSesh().getLocation())));
                } else {
                    messageTxt.setText(getString(R.string.recent_routes,PrintNull.Print(user.getCurSesh().getLocation())));
                }
                RouteAdapter adapter = new RouteAdapter(getApplicationContext(), user.getCurSesh().getRoutes());
                displayRecentRoutesLV.setAdapter(adapter);
            }
        });

        trainingActivityViewModel.getRecentSessionsLD(user.getId()).observe(this, sessions -> {
            if (sessions != null) {
                user.setSessionsList((ArrayList<Session>) sessions);
                SessionAdapter adapter = new SessionAdapter(getApplicationContext(), user.getSessionsList());
                displayRecentSessionsLV.setAdapter(adapter);
            }
        });
        trainingActivityViewModel.getRecentRoutesForUserLD(user.getId()).observe(this, routes -> {
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
                        messageTxt.setText(getString(R.string.no_routes_to_display, PrintNull.Print(user.getCurSesh().getLocation())));

                    } else {
                        messageTxt.setText(getString(R.string.recent_routes,PrintNull.Print(user.getCurSesh().getLocation())));
                    }
                    RouteAdapter adapter = new RouteAdapter(getApplicationContext(), user.getCurSesh().getRoutes());
                    displayRecentRoutesLV.setAdapter(adapter);
                } else {
                    Log.d("gwyd", "no routes returned");
                    messageTxt.setText(getString(R.string.no_routes_to_display, PrintNull.Print(user.getCurSesh().getLocation())));
                }
            }
        });
        //endregion
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("gwyd", "onPause: Removing location update from the location manager");
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionChecked == null) {
            Log.d("gwyd", "onResume: location permissions not requested yet. ");
            checkPermissions();
        } else if (permissionChecked == PermissionCheck.GRANTED) {
            Log.d("gwyd", "onResume: location permissions already granted. Re assigning listener");
            turnOnLocationTracking();
        } else {
            //permission already denied.
            Log.d("gwyd", "onResume: location permissions already denied by user in this activity");
        }
    }

    private void startSession_Click() {
        user.setCurSesh(new Session(OffsetDateTime.now(), user.getId()));
        if (permissionChecked != null)
            if (permissionChecked == PermissionCheck.GRANTED) {
                //if location services are disabled on the phone latitude and longitude are defaulted to 0.0
                user.getCurSesh().setLat(latitude);
                user.getCurSesh().setLon(longitude);
                if (user.getCurSesh().getLon() != 0){
                   user.getCurSesh().setLocation(getAddressFromLocation(user.getCurSesh()));
                }
            }
        trainingActivityViewModel.CreateNewSession(user.getCurSesh());
    }

    public void addRouteBtn_Click(View view) {
        int selectedRouteTypeID = routeTypeRG.getCheckedRadioButtonId();
        RadioButton RTRadioBtn = findViewById(selectedRouteTypeID);
        RouteType routeType = RouteType.getFromInteger(Integer.parseInt(RTRadioBtn.getTag().toString()));
        int selectedStyleTypeID = routeStyleRG.getCheckedRadioButtonId();
        StyleDone styleDone = StyleDone.getFromInteger(Integer.parseInt(findViewById(selectedStyleTypeID).getTag().toString()));
        Grades grade = (Grades) gradeAchievedSpinner.getSelectedItem();

        Route newRoute = new Route(user.getCurSesh().getId(), user.getId(), grade, routeType, styleDone, OffsetDateTime.now());
        trainingActivityViewModel.addRoute(newRoute);
        addRouteForm.setVisibility(View.GONE);
        displayRecentRoutesLV.setVisibility(View.VISIBLE);

        //TODO:check goals progress with the new route
        checkGoalProgress();
    }

    private void checkGoalProgress() {
        
    }

    private void endSession_Click() {
        if (addRouteForm.getVisibility() == View.VISIBLE)
            addRouteForm.setVisibility(View.GONE);
        if (user.getCurSesh() != null) {
            user.getCurSesh().setEndTime(OffsetDateTime.now());
            trainingActivityViewModel.updateCurrentSession(user.getCurSesh());
        } else {
            Toast.makeText(this, "No Active Session", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("gwyd", "access fine location permission not granted. Requesting permission");

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
        //passive provider to save battery as this is not a live update
        //(1 * 60 * 1000) time = every minute
        //0 = don't care about refreshing on distance
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

    private String getAddressFromLocation(Session session) {
        String address = "";
        try {
            Geocoder geocoder;
            geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> localAddress = geocoder.getFromLocation(session.getLat(), session.getLon(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = localAddress.get(0).getAddressLine(0);
//            city = localAddress.get(0).getLocality();
//            state = localAddress.get(0).getAdminArea();
//            country = localAddress.get(0).getCountryName();
//            postalCode = localAddress.get(0).getPostalCode();
//            knownName = localAddress.get(0).getFeatureName();
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

        //store the current latitude and longitude in the View Model
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
}
