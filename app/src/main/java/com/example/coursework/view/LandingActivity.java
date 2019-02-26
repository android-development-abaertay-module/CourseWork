package com.example.coursework.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.coursework.R;
import com.example.coursework.model.Logbook;
import com.example.coursework.model.User;
import com.example.coursework.view.adapters.UsersAdapter;
import com.example.coursework.viewmodel.AddOrEditUserViewModel;
import com.example.coursework.viewmodel.MainActivityViewModel;

import java.util.List;

public class LandingActivity extends AppCompatActivity {

    private static final int ADD_USER_REQUEST = 1;
    private static final int EDIT_USER_REQUEST = 2;
    MainActivityViewModel mainActivityViewModel;
    ListView usersLV;
    UsersAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        usersLV = findViewById(R.id.usersLV);

        mainActivityViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if(users != null) {
                    userAdapter =new UsersAdapter(getApplicationContext(), users);
                    usersLV.setAdapter(userAdapter);
                }
                userAdapter.notifyDataSetChanged();

            }
        });
    }

    public void addNewUserBtn_Click(View view) {
        Intent intent = new Intent(LandingActivity.this,AddOrEditUserActivity.class);
        startActivityForResult(intent, ADD_USER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ADD_USER_REQUEST:
                User user = new User();
                user.setUserName(data.getStringExtra(AddOrEditUserActivity.USERNAME));
                //create user, get id then create logbook for user
                Logbook logbook = new Logbook();
                break;
            case EDIT_USER_REQUEST:
                break;
            default:
                break;
        }
    }
}
