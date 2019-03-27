package com.example.coursework.view;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.viewmodel.MainMapActivityViewModel;
import com.google.android.gms.common.api.Status;
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
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int ACCESS_FINE_LOCATION_REQUEST = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment mapFragment;
    private AutocompleteSupportFragment autocompleteFragment;
    private Marker customMarker;
    private ImageView mGps;

    MainMapActivityViewModel mapViewModel;
    private User user;
    private List<Session> recentSessions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        //region [Properties]
        mapViewModel = ViewModelProviders.of(this).get(MainMapActivityViewModel.class);
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

                if (mediator.getRecentSessions() == null || mediator.getRecentSessions().size() == 0)
                    recentSessions = null;
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

        //* Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),getResources().getString(R.string.google_maps_key));
        }
        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("gwyd", "Place: " + place.getName() + ", " + place.getId());
                if ( place.getLatLng() != null)
                    moveCamera(place.getLatLng(),15f,place.getName());
                else
                    geoLocate(place);

            }

            @Override
            public void onError(Status status) {
                Log.i("gwyd", "An error occurred: " + status);
                Toast.makeText(MainMapActivity.this,"Unable to Find Location",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void init(){
        Log.d("gwyd","init hit");

        mGps.setOnClickListener(v -> {
            Log.d("gwyd","custom gps icon clicked");
            getDeviceLocation();
        });
        hideSoftKeyboard();
    }
    ///gooLocate locate and zoom to location from address
    private void geoLocate(Place place){
        Log.d("gwyd","geoLocate entered");
        String search = place.getName();
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
            //TODO: decide between using location manager or google play services location (what i used in training activity vs the youtube guy)
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //found location
                    Location currentLocation = (Location) task.getResult();
                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15f,null);
                } else {
                    //couldn't find location
                    Log.e("gwyd", "couldn't find location");
                    Toast.makeText(MainMapActivity.this,"Couldn't find Device Location",Toast.LENGTH_LONG).show();
                }
            });
        } catch (SecurityException ex) {
            Log.e("gwyd", "get device location: security exception: " + ex.getMessage());
            Toast.makeText(MainMapActivity.this,"Couldn't find Device Location: " + ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d("gwyd", "moving camera to : " + latLng.latitude + " " + latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (title != null){
            //remove old custom marker
            if (customMarker != null)
                customMarker.remove();
            //add the new custom marker
            customMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(title));
        }
        hideSoftKeyboard();
    }

    private void DrawSessionsOnMap() {
        Log.d("gwyd", "drawing items on map");

        mMap.clear();
        if (recentSessions != null ) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

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
            mMap.getUiSettings().setMyLocationButtonEnabled(false);//hides the zoom to my location button (i've implemented it myself)
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            //mMap.getUiSettings().setMapToolbarEnabled(true); //adds buttons for explicit intents to open in google maps.//TODO; try to do myself?

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
