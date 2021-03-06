package com.example.coursework.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;
import java.util.List;

public class LandingActivityViewModel extends AndroidViewModel {
    private DaoRepository daoRepository;
    private LiveData<List<User>> users;

    public DaoRepository getDaoRepository() {
        return daoRepository;
    }

    public LiveData<List<User>> getUsers() {
        if (users == null){
           users = updateUsersList();
        }
        return users;
    }
    public LandingActivityViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        users = updateUsersList();
    }
    public LiveData<List<User>> updateUsersList(){
        return daoRepository.getAllUsers();
    }
}
