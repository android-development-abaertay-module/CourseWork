package com.example.coursework.view;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.PermissionCheck;
import com.example.coursework.viewmodel.MainMapActivityViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int ACCESS_FINE_LOCATION_REQUEST = 1;
    private GoogleMap mMap;
    MainMapActivityViewModel mapViewModel;
    private User user;
    private List<Session> recentSessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        //region [Properties]
        mapViewModel = ViewModelProviders.of(this).get(MainMapActivityViewModel.class);

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
            if (mediator != null){
                if (mediator.getRecentSessions() != null)
                recentSessions = mediator.getRecentSessions();
                //redraw map if necessary
                if (mMap != null)
                    DrawSessionsOnMap();
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
            //permissions already granted turn on location listening...
            if (mMap ==null)
                initMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case ACCESS_FINE_LOCATION_REQUEST:
                Log.d("gwyd", "ACCESS_FINE_LOCATION_REQUEST received");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permissions granted initialize map
                    if (mMap ==null)
                        initMap();

                } else {
                    //permission denied
                    Log.d("gwyd", "denied");
                    Toast.makeText(this,"Turn on Location Permissions to access Map functionality.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainMapActivity.this,MenuActivity.class );
                    intent.putExtra(USER_ID,user.getId()+"");
                    intent.putExtra(USERNAME,user.getUserName());
                    startActivity(intent);
                }
                break;
        }
    }

    private void initMap(){
        Log.d("gwyd", "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MainMapActivity.this);
        DrawSessionsOnMap();
    }

    private void DrawSessionsOnMap() {
        mMap.clear();
        if (recentSessions != null)
            for (Session s: recentSessions) {
                LatLng latLon = new LatLng(s.getLat(), s.getLon());
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLon);
                marker.title(s.getStartTime().toLocalDate().toString());

                mMap.addMarker(marker);
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
