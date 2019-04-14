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
import com.example.coursework.model.helper.PlaceInfoHoulder;
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

    private static final int ACCESS_FINE_LOCATION_REQUEST = 1;
    private final  float defaultZoom = 15f;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PlacesClient placesClient;
    private SupportMapFragment mapFragment;
    private AutocompleteSupportFragment autocompleteFragment;
    private ImageView mGps;
    private PlaceInfoHoulder customPlaceInfo;
    private LatLng selectedLatLong;
    private  Polygon polygon;


    MainMapActivityViewModel mapViewModel;
    private User user;
    private List<Session> recentSessions;
    private Boolean isInitialCameraMoveComplete;


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
        mapViewModel.getIsInitCameraMoveComplete().observe(this, isComplete ->{
            isInitialCameraMoveComplete = isComplete;
        });
        mapViewModel.getMediator().observe(this, mediator -> {
            if (mediator != null) {
                if(mMap == null)
                    mMap = mediator.getMap();

                if (placesClient == null)
                    placesClient = mediator.getPlacesClient();

                if (customPlaceInfo == null)
                    customPlaceInfo = mediator.getCustomPlace();

                //read data from the Mediator
                if (mediator.getRecentSessions() == null || mediator.getRecentSessions().size() == 0)
                    recentSessions = null;
                else
                    recentSessions = mediator.getRecentSessions();

                //redraw sessions and or custom marker
                if (recentSessions != null && mMap != null ) {
                    DrawItemsOnMap(isInitialCameraMoveComplete);
                }else if(customPlaceInfo != null && mMap != null)
                    updateCustomPlace(customPlaceInfo,defaultZoom,false);
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
        Log.d("gwyd", "initMap: initializing map");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainMapActivity.this);

        //* Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),getResources().getString(R.string.google_maps_key));
            // Create a new Places client instance.
            placesClient = Places.createClient(this);
            mapViewModel.setPlacesClientLD(placesClient);
        }

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place placeSelected) {
                hideSoftKeyboard();

                String placeId =  placeSelected.getId();
                // Specify the fields to return (in this example all fields are returned).
                List<Place.Field> placeFields = Arrays.asList(
                        Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS,Place.Field.PHONE_NUMBER,
                        Place.Field.OPENING_HOURS,Place.Field.WEBSITE_URI,Place.Field.TYPES,Place.Field.VIEWPORT);

                // Construct a request object, passing the place ID and fields array.
                FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    newCustomPlaceFound(response);

                }).addOnFailureListener((exception) -> {
                    toastAndLog("Place not Found..",LogType.ERROR);
                });
            }

            @Override
            public void onError(Status status) {
                toastAndLog("Error: Unable to Find Location" + status,LogType.ERROR);
            }
        });
    }

    private void newCustomPlaceFound(FetchPlaceResponse response) {
        //remove old custom place marker from map before loading the new one
        if (customPlaceInfo != null)
            if (customPlaceInfo.getMarker() != null)
                customPlaceInfo.getMarker().remove();

        customPlaceInfo = new PlaceInfoHoulder();
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

        if ( customPlaceInfo.getLatLng() != null)
            updateCustomPlace(customPlaceInfo,defaultZoom,true);
        if (customPlaceInfo.getLatLng() != null)
            moveCamera(customPlaceInfo.getLatLng(),defaultZoom);
        else
            moveCamera(customPlaceInfo.getViewPort().getCenter(),defaultZoom);
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
                    if (currentLocation != null)
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

    //If title is null no new marker created
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d("gwyd", "moving camera to : " + latLng.latitude + " " + latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        hideSoftKeyboard();
    }
    private void updateCustomPlace(PlaceInfoHoulder place, float zoom, Boolean updateViewModel){
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
        Log.d("gwyd", "drawing items on map");

        mMap.clear();
        if (customPlaceInfo != null)
            updateCustomPlace(customPlaceInfo,defaultZoom, false);

        if (recentSessions != null ) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (Session s : recentSessions) {
                LatLng latLon = new LatLng(s.getLat(), s.getLon());
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLon);
                marker.title(s.getLocation());
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                marker.snippet(s.detailsSummaryForMap());

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
            PolygonOptions rectangle = new PolygonOptions()
                    .add(topRight)
                    .add(bottomRight)
                    .add(bottomLeft)
                    .add(topLeft);
            polygon = mMap.addPolygon(rectangle);
                polygon.setVisible(mapViewModel.isPollyVisible().getValue());

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);//hides the zoom to my location button (i've implemented it myself)
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            //mMap.getUiSettings().setMapToolbarEnabled(true); //adds buttons for explicit intents to open in google maps.//TODO; try to do myself?
            //setup the custom info window
            mMap.setInfoWindowAdapter(new CustomMapInfoWindowAdapter(MainMapActivity.this));
            //enable on marker click
            mMap.setOnMarkerClickListener(this);
            if (mapViewModel.getMapTypeLD().getValue()!= null)
                mMap.setMapType(mapViewModel.getMapTypeLD().getValue());


            //setup listener for go to my location
            mGps.setOnClickListener(v -> {
                Log.d("gwyd","custom gps icon clicked");
                getDeviceLocation();
            });
            hideSoftKeyboard();
            // Do Other On load map stuff
            //mMap.setPadding(0,10,0,0);
            mapViewModel.setMapLD(mMap);
        }
    }
    public void loadNavigationView(double lat,double lng){
        Uri navigation = Uri.parse("google.navigation:q="+lat+","+lng+"");
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
        startActivity(navigationIntent);
    }
    private void  hideSoftKeyboard(){
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
        boolean vis;
        if (polygon != null){
            if (polygon.isVisible())
                vis =false;
            else
                vis = true;
            polygon.setVisible(vis);
            mapViewModel.setIsPollyVisibleLD(vis);
        }else
            toastAndLog("No Sessions To draw polygon around.", LogType.DEBUG);

    }
    public void mapsExplicitIntent_Click(View view) {
        if (selectedLatLong != null)
            loadNavigationView(selectedLatLong.latitude,selectedLatLong.longitude);
        else
            toastAndLog("No Location Selected to Navigate to",LogType.INFO);
    }
    public void mapsChangeType_Click(View view) {
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
        mapViewModel.setMapTypeLD(mMap.getMapType());
    }

    //region [OnMarkerClickListener methods]
    @Override
    public boolean onMarkerClick(Marker marker) {
        //update the selected Location
        selectedLatLong = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Log.d("gwyd","onPointerCaptureChanged hit: " + hasCapture);
    }
    //endregion
}
