package com.example.coursework.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.coursework.R;
import com.example.coursework.model.User;
import com.example.coursework.view.adapters.UsersAdapter;
import com.example.coursework.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {

    MainActivityViewModel mainActivityViewModel;
    ListView usersLV;
    UsersAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
