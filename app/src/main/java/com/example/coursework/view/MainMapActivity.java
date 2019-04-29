package com.example.coursework.view;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.coursework.R;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.LogType;
import com.example.coursework.model.helper.PlaceInfoHolder;
import com.example.coursework.view.adapters.CustomMapInfoWindowAdapter;
import com.example.coursework.viewmodel.MainMapActivityViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;


public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //region [properties]
    private static final int ACCESS_FINE_LOCATION_REQUEST = 1;
    private final  float defaultZoom = 15f;
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private ImageView mGps;
    private PlaceInfoHolder customPlaceInfo;
    private LatLng selectedLatLong;
    private  Polygon polygon;


    MainMapActivityViewModel mapViewModel;
    private User user;
    private List<Session> recentSessions;
    private Boolean isInitialCameraMoveComplete;
    //endregion

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
        //get user from ViewModel
        mapViewModel.getUserLD().observe(this, userVal -> user = userVal);
        //get selected latLong from ViewModel
        mapViewModel.getSelectedLatLngLD().observe(this, latLngVal ->{
            selectedLatLong = latLngVal;
        });

        //get isInitialCameraMoveComplete from View model (initial camera move performs extra actions)
        mapViewModel.getIsInitCameraMoveComplete().observe(this, isComplete -> isInitialCameraMoveComplete = isComplete);
        //get mediator live from ViewModel - the map mediator holds all the live data related to the map.
        mapViewModel.getMediator().observe(this, mediator -> {
            if (mediator != null) {
                //if map hasn't been retrieved apply it
                if(mMap == null)
                    mMap = mediator.getMap();

                //if map hasn't been retrieved apply it
                if (placesClient == null)
                    placesClient = mediator.getPlacesClient();

                //if customPlaceInfo hasn't been retrieved apply it
                if (customPlaceInfo == null)
                    customPlaceInfo = mediator.getCustomPlace();

                //if Recent Sessions hasn't been retrieved apply it
                if (mediator.getRecentSessions() == null || mediator.getRecentSessions().size() == 0)
                    recentSessions = null;
                else
                    recentSessions = mediator.getRecentSessions();

                //redraw sessions and/or custom marker
                if (recentSessions != null && mMap != null ) {
                    DrawItemsOnMap(isInitialCameraMoveComplete);
                }else if(customPlaceInfo != null && mMap != null)
                    updateCustomPlace(customPlaceInfo,defaultZoom,false);
            }
        });
        //endregion

        //Request permissions
        checkPermissions();
    }

    private void checkPermissions() {
        //check if permissions granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("gwyd", "access fine location permission not granted. Requesting permission");
            //not granted - request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_REQUEST);

        } else {
            Log.d("gwyd", "access fine location permission already granted");
            //permissions already granted initialize map
            initMap();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check permission request results
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_REQUEST:
                Log.d("gwyd", "ACCESS_FINE_LOCATION_REQUEST received");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permissions granted initialize map
                    initMap();

                } else {
                    //permission denied - return to main menu
                    toastAndLog("Turn on Location Permissions to access Map functionality.", LogType.INFO);
                    Intent intent = new Intent(MainMapActivity.this, MenuActivity.class);
                    intent.putExtra(USER_ID, user.getId() + "");
                    intent.putExtra(USERNAME, user.getUserName());
                    startActivity(intent);
                }
                break;
        }
    }

    //init map is called. this then triggers onMapReady
    private void initMap() {
        //setup essential map stuff..
        Log.d("gwyd", "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(MainMapActivity.this);

        //* Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),getResources().getString(R.string.google_maps_key));
            // Create a new Places client instance.
            placesClient = Places.createClient(this);
            mapViewModel.setPlacesClientLD(placesClient);
        }

        // Initialize the AutocompleteSupportFragment. (used in the places search)
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        //set callback for when place selected
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place placeSelected) {
                hideSoftKeyboard();

                String placeId =  placeSelected.getId();
                // Specify the fields to return.
                List<Place.Field> placeFields = Arrays.asList(
                        Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS,Place.Field.PHONE_NUMBER,
                        Place.Field.OPENING_HOURS,Place.Field.WEBSITE_URI,Place.Field.TYPES,Place.Field.VIEWPORT);

                // Construct a places request object, passing the place ID and fields array.
                FetchPlaceRequest request;
                if (placeId != null){
                    request = FetchPlaceRequest.builder(placeId, placeFields).build();
                    //fetch place..
                    placesClient.fetchPlace(request).addOnSuccessListener((response) -> newCustomPlaceFound(response)).addOnFailureListener((exception)
                            -> toastAndLog("Place not Found..",LogType.ERROR));
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                toastAndLog("Error: Unable to Find Location" + status,LogType.ERROR);
            }
        });
    }

    private void newCustomPlaceFound(FetchPlaceResponse response) {
        //remove old custom place marker from map before loading the new one
        if (customPlaceInfo != null)
            if (customPlaceInfo.getMarker() != null)
                customPlaceInfo.getMarker().remove();

        //set the details for the new place
        customPlaceInfo = new PlaceInfoHolder();
        customPlaceInfo.setId(response.getPlace().getId());
        if (response.getPlace().getRating() != null)
            customPlaceInfo.setRating(response.getPlace().getRating());
        customPlaceInfo.setName(response.getPlace().getName());
        customPlaceInfo.setLatLng(response.getPlace().getLatLng());
        customPlaceInfo.setAddress(response.getPlace().getAddress());
        customPlaceInfo.setPhoneNumber(response.getPlace().getPhoneNumber());
        customPlaceInfo.setOpeningHours(response.getPlace().getOpeningHours());
        customPlaceInfo.setWebsiteUri(response.getPlace().getWebsiteUri());
        customPlaceInfo.setTypes(response.getPlace().getTypes());
        customPlaceInfo.setViewPort(response.getPlace().getViewport());
        Log.d("gwyd", customPlaceInfo.toString());

        //move camera to new place
        if ( customPlaceInfo.getLatLng() != null)
            updateCustomPlace(customPlaceInfo,defaultZoom,true);
        if (customPlaceInfo.getLatLng() != null)
            moveCamera(customPlaceInfo.getLatLng(),defaultZoom);
        else
            moveCamera(customPlaceInfo.getViewPort().getCenter(),defaultZoom);
    }


    private void getDeviceLocation() {
        Log.d("gwyd", "getting current location");
        //fused location provider client used to get device location
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //get location from result of async task
                    Location currentLocation = (Location) task.getResult();
                    if (currentLocation != null)
                        //move camera to the new location and zoom
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), defaultZoom);
                    else{
                        //couldn't find location
                        toastAndLog("Couldn't find Device Location",LogType.ERROR);
                    }
                } else {
                    //couldn't find location
                   toastAndLog("Couldn't find Device Location",LogType.ERROR);
                }
            });
        } catch (SecurityException ex) {
            toastAndLog("get device location: security exception: " + ex.getMessage(),LogType.ERROR);
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d("gwyd", "moving camera to : " + latLng.latitude + " " + latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        hideSoftKeyboard();
    }
    private void updateCustomPlace(PlaceInfoHolder place, float zoom, Boolean updateViewModel){
        //get latitude and longitude from place. if not available (should be) get center of Viewport
        LatLng coordinates;
        if (place.getLatLng() != null)
            coordinates = place.getLatLng();
        else
            coordinates = place.getViewPort().getCenter();
        //Re-place old custom marker
        if (customPlaceInfo != null){
            //add the new custom marker (old marker removed in on place select)
            customPlaceInfo.setMarker(mMap.addMarker(new MarkerOptions()
                    .position(coordinates)
                    .title(place.getName())
                    .snippet(customPlaceInfo.displaySnippetDetails())));
            if (updateViewModel)
                mapViewModel.setCustomPlaceLD(customPlaceInfo);
        }
        hideSoftKeyboard();
    }

    private void DrawItemsOnMap(boolean initialFocusOnSessionBounds) {
        Log.d("gwyd", "drawItemsOnMap entered");
        //clear the map
        mMap.clear();
        //update the marker for the custom place (if there is one)
        if (customPlaceInfo != null)
            updateCustomPlace(customPlaceInfo,defaultZoom, false);

        //draw recent session marker locations on map if there are any
        if (recentSessions != null ) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            //foreach session
            for (Session s : recentSessions) {
                //populate marker details for session
                LatLng latLon = new LatLng(s.getLat(), s.getLon());
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLon);
                marker.title(s.getLocation());
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                marker.snippet(s.detailsSummaryForMap());
                //add to map
                mMap.addMarker(marker);
                //add marker to bonds (for auto zoom and focus)
                builder.include(marker.getPosition());
            }
            //build the bonds
            LatLngBounds bounds = builder.build();
            LatLng topRight = bounds.northeast;
            LatLng topLeft = new LatLng(bounds.southwest.latitude,bounds.northeast.longitude);
            LatLng bottomRight = new LatLng(bounds.northeast.latitude, bounds.southwest.longitude);
            LatLng bottomLeft = bounds.southwest;
            //setup rectangle (to see area covered by your sessions)
            PolygonOptions rectangle = new PolygonOptions()
                    .add(topRight)
                    .add(bottomRight)
                    .add(bottomLeft)
                    .add(topLeft);
            polygon = mMap.addPolygon(rectangle);
            //get data from view model to determine if poly should be shown or not
            polygon.setVisible(mapViewModel.isPollyVisible().getValue());

            //flag to execute on initial focus stuff
            //don't want camera location to reset every time configuration changes
            if(!initialFocusOnSessionBounds){
                Log.d("gwyd","initial camera focus done");
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                mapViewModel.setIsInitCameraMoveComplete(true);
            }
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
        //check required permissions granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //hides the zoom to my location button (i've implemented it myself)
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            //enable map compass
            mMap.getUiSettings().setCompassEnabled(true);
            //turn on camera zoom controls for map
            mMap.getUiSettings().setZoomControlsEnabled(true);
            //setup the custom info window
            mMap.setInfoWindowAdapter(new CustomMapInfoWindowAdapter(MainMapActivity.this));
            //enable on marker click
            mMap.setOnMarkerClickListener(this);
            //get map type from ViewModel and apply to map
            if (mapViewModel.getMapTypeLD().getValue()!= null)
                mMap.setMapType(mapViewModel.getMapTypeLD().getValue());


            //setup listener for go to my location
            mGps.setOnClickListener(v -> {
                Log.d("gwyd","custom gps icon clicked");
                getDeviceLocation();
            });
            hideSoftKeyboard();
            //update the map instance in the view model
            mapViewModel.setMapLD(mMap);
        }
    }
    public void loadNavigationView(double lat,double lng){
        //start implicit intent for maps activity - to navigate to selected marker
        Uri navigation = Uri.parse("google.navigation:q="+lat+","+lng+"");
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
        startActivity(navigationIntent);
    }
    private void  hideSoftKeyboard(){
        //hides the phones software keyboard
        this.getWindow().setSoftInputMode((WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN));
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
        Toast.makeText(MainMapActivity.this,message,Toast.LENGTH_LONG).show();
    }


    public void mapsDrawPolygon_Click(View view) {
        //toggle polygon visible state on map and update View Model
        boolean vis;
        if (polygon != null){
            //toggle polygon visibility
            vis = !polygon.isVisible();
            polygon.setVisible(vis);
            mapViewModel.setIsPollyVisibleLD(vis);
        }else
            toastAndLog("No Sessions To draw polygon around.", LogType.DEBUG);

    }
    public void mapsImplicitIntent_Click(View view) {
        if (selectedLatLong != null)
            loadNavigationView(selectedLatLong.latitude,selectedLatLong.longitude);
        else
            toastAndLog("No Location Selected to Navigate to",LogType.INFO);
    }
    public void mapsChangeType_Click(View view) {
        //toggle through available map types on icon click
        switch (mMap.getMapType()){
            case GoogleMap.MAP_TYPE_NORMAL:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case GoogleMap.MAP_TYPE_SATELLITE:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case GoogleMap.MAP_TYPE_TERRAIN:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case GoogleMap.MAP_TYPE_HYBRID:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }
        //update the view model to store the new map type
        mapViewModel.setMapTypeLD(mMap.getMapType());
    }

    //region [OnMarkerClickListener methods]
    @Override
    public boolean onMarkerClick(Marker marker) {
        //update the selected Location to be the selected marker
        selectedLatLong = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        mapViewModel.setSelectedLatLngLD(selectedLatLong);
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Log.d("gwyd","onPointerCaptureChanged hit: " + hasCapture);
    }
    //endregion
}
