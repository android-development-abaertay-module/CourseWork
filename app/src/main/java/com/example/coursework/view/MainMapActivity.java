package com.example.coursework.view;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.PermissionCheck;
import com.example.coursework.viewmodel.MainMapActivityViewModel;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int ACCESS_FINE_LOCATION_REQUEST = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment mapFragment;

    MainMapActivityViewModel mapViewModel;
    private User user;
    private List<Session> recentSessions;

    //Widgets
    private EditText mSearchText;
    private ImageView mGps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        //region [Properties]
        mapViewModel = ViewModelProviders.of(this).get(MainMapActivityViewModel.class);
        mSearchText = findViewById(R.id.input_search);
        mGps = findViewById(R.id.ic_gps);

        Intent intent = getIntent();
        if (intent.hasExtra(USER_ID) && intent.hasExtra(USERNAME)) {
            user = new User(intent.getStringExtra(USERNAME));
            user.setId(intent.getLongExtra(USER_ID, 0));
            mapViewModel.setUserLD(user);
        }
        //endregion

        //region [Register observers]
        mapViewModel.getUserLD().observe(this, userVal -> {
            user = userVal;
        });
        mapViewModel.getMediator().observe(this, mediator -> {
            if (mediator != null) {
                //read data from the Mediator
                recentSessions = mediator.getRecentSessions();

                //redraw sessions
                if (recentSessions != null) {
                    DrawSessionsOnMap();
                }
            }
        });
        //endregion

        //Request permissions and initialise map
        checkPermissions();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("gwyd", "access fine location permission not granted. Requesting permission");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_REQUEST);

        } else {
            Log.d("gwyd", "access fine location permission already granted");
            //permissions already granted initialize map if required
//            if (mMap ==null)
            initMap();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_REQUEST:
                Log.d("gwyd", "ACCESS_FINE_LOCATION_REQUEST received");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permissions granted initialize map
//                    if (mMap ==null)
                    initMap();

                } else {
                    //permission denied
                    Log.d("gwyd", "denied");
                    Toast.makeText(this, "Turn on Location Permissions to access Map functionality.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainMapActivity.this, MenuActivity.class);
                    intent.putExtra(USER_ID, user.getId() + "");
                    intent.putExtra(USERNAME, user.getUserName());
                    startActivity(intent);
                }
                break;
        }
    }

    private void initMap() {
        Log.d("gwyd", "initMap: initializing map");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MainMapActivity.this);
    }
    private void init(){
        Log.d("gwyd","init hit");
        mSearchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.getAction() == KeyEvent.ACTION_DOWN
                    || event.getAction() == KeyEvent.KEYCODE_ENTER){
                //execute code to search for text
                geoLocate();
            }
            return false;
        });
        mGps.setOnClickListener(v -> {
            Log.d("gwyd","custom gps icon clicked");
            getDeviceLocation();
        });
        hideSoftKeyboard();
    }
    private void geoLocate(){
        Log.d("gwyd","geoLocate entered");
        String search = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder((MainMapActivity.this));
        List<Address> list = new ArrayList<>();
        try{
            //get the  first address
            list = geocoder.getFromLocationName(search,1);
        }catch (IOException ex){
            Log.e("gwyd","IOException "+ ex.getMessage());
        }
        if (list.size() > 0){
            //address found
            Address address = list.get(0);
            Log.d("gwyd",address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),15f,address.getAddressLine(0));

        }
        else
            Log.d("gwyd","no address found for location: " + search);

    }
    private void getDeviceLocation() {
        Log.d("gwyd", "getting current location");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            //TODO: decide between using location manager or google play services location (what i used in traing activity vs the youtube guy)
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //found location
                    Location currentLocation = (Location) task.getResult();
                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15f,null);
                } else {
                    //couldn't find location
                }
            });
        } catch (SecurityException ex) {
            Log.e("gwyd", "get device location: security exception: " + ex.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom,String title) {
        Log.d("gwyd", "moving camera to : " + latLng.latitude + " " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (title != null){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    private void DrawSessionsOnMap() {
        Log.d("gwyd", "drawing items on map");
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        mMap.clear();
        if (recentSessions != null)
            for (Session s : recentSessions) {
                LatLng latLon = new LatLng(s.getLat(), s.getLon());
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLon);
                marker.title(s.getStartTime().toLocalDate().toString());

                mMap.addMarker(marker);
                //add marker to bonds (for auto zoom and focus)
                builder.include(marker.getPosition());
            }

        //build the bonds
        LatLngBounds bounds = builder.build();

        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("gwyd", "onMapReady Hit");
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);//hides the button if choose to use my custom button instead
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            //mMap.getUiSettings().setMapToolbarEnabled(true); //adds buttons for explicit intents to open in google maps. try to do myself

            //enable search functionality:
            init();
        }
        // Do Other On load map stuff
        //mMap.setPadding(0,10,0,0);

    }
    private void  hideSoftKeyboard(){
        this.getWindow().setSoftInputMode((WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN));
    }
}
