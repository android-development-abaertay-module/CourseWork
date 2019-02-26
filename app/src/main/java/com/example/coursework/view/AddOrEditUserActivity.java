package com.example.coursework.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.model.User;
import com.example.coursework.viewmodel.AddOrEditUserViewModel;

import java.util.List;

import static android.app.PendingIntent.getActivity;

public class AddOrEditUserActivity extends AppCompatActivity {

    AddOrEditUserViewModel addOrEditUserViewModel;
    EditText usernameTxt;
    List<User> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addoredituser);

        usernameTxt = findViewById(R.id.userNameTxt);
        addOrEditUserViewModel = ViewModelProviders.of(this).get(AddOrEditUserViewModel.class);
        addOrEditUserViewModel.updateUserList().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if(users != null) {
                    allUsers = users;
                }
            }
        });

    }

    public void addOrEditUserBtn_Click(View view) {
        User user = new User(usernameTxt.getText().toString());
        if (user.getUserName() ==""){
            Toast.makeText(this,"Username Required",Toast.LENGTH_LONG).show();
        }
        addOrEditUserViewModel.updateUserList();

        for(User usr: allUsers) {
            if(usr.getUserName().contains(user.getUserName())){
                Toast.makeText(this,"Username Already Used",Toast.LENGTH_LONG).show();
                return;
            }
        }
    }
}
