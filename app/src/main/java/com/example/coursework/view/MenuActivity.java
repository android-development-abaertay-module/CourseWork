package com.example.coursework.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.coursework.R;
import com.example.coursework.model.User;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //region [properties]
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //endregion

        //user pulled from intent
        Intent intent = getIntent();
        if (intent.hasExtra(USER_ID) && intent.hasExtra(USERNAME)){
            user = new User(intent.getStringExtra(USERNAME));
            user.setId(Long.parseLong(intent.getStringExtra(USER_ID)));
        }
    }

    @Override
    public void onBackPressed() {
        //if back button pressed when navigation draw visible hide it.
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //else perform normal back action
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //get id from selected item
        if (id == R.id.nav_set_goals) {
            goToActivity(SetGoalsActivity.class);
        } else if (id == R.id.nav_check_goals) {
            goToActivity(CheckGoalsActivity.class);
        } else if (id == R.id.nav_start_training) {
            goToActivity(TrainingActivity.class);
        } else if (id == R.id.nav_my_map) {
            goToActivity(MainMapActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToActivity(Class goToClass) {
        //go to the selected activity with the users details
        Intent intent = new Intent(MenuActivity.this, goToClass);
        intent.putExtra(USER_ID,user.getId());
        intent.putExtra(USERNAME,user.getUserName());
        startActivity(intent);
    }
}
