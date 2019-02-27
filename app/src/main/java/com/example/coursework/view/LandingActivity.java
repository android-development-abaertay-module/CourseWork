package com.example.coursework.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.model.Logbook;
import com.example.coursework.model.User;
import com.example.coursework.view.adapters.UsersAdapter;
import com.example.coursework.viewmodel.LandingActivityViewModel;

import java.util.List;

import static com.example.coursework.view.AddOrEditUserActivity.USERNAME;
import static com.example.coursework.view.AddOrEditUserActivity.USER_ID;

public class LandingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int ADD_USER_REQUEST = 1;
    private static final int EDIT_USER_REQUEST = 2;
    LandingActivityViewModel landingActivityViewModel;
    ListView usersLV;
    UsersAdapter userAdapter;
    List<User> allUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        landingActivityViewModel = ViewModelProviders.of(this).get(LandingActivityViewModel.class);
        usersLV = findViewById(R.id.usersLV);
        usersLV.setOnItemClickListener(this);
        registerForContextMenu(usersLV);
        landingActivityViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if(users != null) {
                    userAdapter =new UsersAdapter(getApplicationContext(), users);
                    usersLV.setAdapter(userAdapter);
                    allUsers = users;
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
                if (resultCode ==  RESULT_OK){
                    createNewUser(data);
                }
                break;
            case EDIT_USER_REQUEST:
                if (resultCode == RESULT_OK){
                    editUser(data);
                }
                break;
            default:
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.userListItemLL:
                LinearLayout ll = (LinearLayout) view;
                TextView tv = ll.findViewById(R.id.userLvItemTV);
                Toast.makeText(getApplicationContext(),tv.getText().toString() + "  " + tv.getTag(),Toast.LENGTH_SHORT).show();
                break;
        }

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.usersLV) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(allUsers.get(info.position).getUserName());
            String[] menuItems = getResources().getStringArray(R.array.userContextMenu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.userContextMenu);
        String menuItemName = menuItems[menuItemIndex];
        User selectedUser = allUsers.get(info.position);

        switch (menuItemName){
            case "Edit":
                Intent intent = new Intent(LandingActivity.this,AddOrEditUserActivity.class);
                intent.putExtra(USERNAME,selectedUser.getUserName());
                intent.putExtra(USER_ID,selectedUser.getId());
                startActivityForResult(intent, EDIT_USER_REQUEST);
                break;
            case "Delete":
                landingActivityViewModel.getDaoRepository().deleteUser(selectedUser);
                //Refresh all users list
                landingActivityViewModel.getDaoRepository().getAllUsers();
                break;
        }
        Toast.makeText(this,selectedUser.getUserName(),Toast.LENGTH_SHORT).show();
        return true;
    }
    private void createNewUser(@Nullable Intent data) {
        User user = new User();
        user.setUserName(data.getStringExtra(USERNAME));
        //create user, get id then create logbook for user
        user.setId(landingActivityViewModel.getDaoRepository().insertUser(user));
        //create user logbook
        Logbook logbook = new Logbook(user.getId());
        try{
            logbook.setId(landingActivityViewModel.getDaoRepository().insertLogbook(logbook));
            //refresh loaded list
            landingActivityViewModel.updateUsersList();
        }catch (Exception ex){
            Toast.makeText(this,"Error Creating User. Please contact support",Toast.LENGTH_SHORT).show();
        }
    }
    private void editUser(@Nullable Intent data) {
        User user = new User();
        user.setUserName(data.getStringExtra(USERNAME));
        //create user, get id then create logbook for user
        long id = data.getLongExtra(USER_ID,0);
        if (id == 0){
            Toast.makeText(this,"error Updating User. Please contact support",Toast.LENGTH_SHORT).show();
            return;
        }
        user.setId(id);
        try{
            landingActivityViewModel.getDaoRepository().updateUser(user);
            //refresh loaded list
            landingActivityViewModel.updateUsersList();
        }catch (Exception ex){
            Toast.makeText(this,"error Updating User. Please contact support",Toast.LENGTH_SHORT).show();
        }
    }
}
