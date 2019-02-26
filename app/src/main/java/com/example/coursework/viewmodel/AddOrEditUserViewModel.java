package com.example.coursework.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;

import java.util.List;

public class AddOrEditUserViewModel extends AndroidViewModel {
    private DaoRepository daoRepository;
    private LiveData<List<User>> usersListLD;

    public LiveData<List<User>> getUsersListLD() {
        if (usersListLD == null){
            usersListLD =  updateUserList();
        }
        return usersListLD;
    }
    public LiveData<List<User>> updateUserList(){
        return  daoRepository.getAllUsers();
    }

    public DaoRepository getDaoRepository() {
        return daoRepository;
    }

    public AddOrEditUserViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        usersListLD = daoRepository.getAllUsers();
    }
}
