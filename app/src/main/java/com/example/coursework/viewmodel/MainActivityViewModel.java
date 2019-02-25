package com.example.coursework.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private DaoRepository daoRepository;
    private LiveData<List<User>> users;


    public LiveData<List<User>> getUsers() {
        if (users == null){
            updateUsersList();
        }
        return users;
    }

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        users = updateUsersList();
    }
    public LiveData<List<User>> updateUsersList(){
       return daoRepository.getAllUsers();
    }
}
