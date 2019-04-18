package com.example.coursework.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.model.User;
import com.example.coursework.viewmodel.AddOrEditUserViewModel;

import java.util.List;

public class AddOrEditUserActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    public static final String USER_ID = "USER_ID";

    AddOrEditUserViewModel addOrEditUserViewModel;
    EditText usernameTxt;
    List<User> allUsers;
    long userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addoredituser);

        usernameTxt = findViewById(R.id.userNameTxt);
        addOrEditUserViewModel = ViewModelProviders.of(this).get(AddOrEditUserViewModel.class);
        addOrEditUserViewModel.updateUserList().observe(this, users -> {
            if(users != null) {
                allUsers = users;
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(USER_ID)){
            setTitle("Edit User");
            usernameTxt.setText(intent.getStringExtra(USERNAME));
            userID = intent.getLongExtra(USER_ID,0);
        }else{
            setTitle("Add User");
        }
    }

    public void addOrEditUserBtn_Click(View view) {
        User user = new User(usernameTxt.getText().toString());
        //Validate
        if (user.getUserName().equals("")){
            Toast.makeText(this,"Username Required",Toast.LENGTH_LONG).show();
            return;
        }

        for(User usr: allUsers) {
            if(usr.getUserName().equals(user.getUserName())){
                Toast.makeText(this,"Username Already Used",Toast.LENGTH_LONG).show();
                return;
            }
        }
        addOrEditUser();
    }

    private void addOrEditUser() {
        Intent intent=new Intent();
        intent.putExtra(USERNAME,usernameTxt.getText().toString());
        intent.putExtra(USER_ID,userID);
        setResult(RESULT_OK,intent);
        finish();
    }
}
