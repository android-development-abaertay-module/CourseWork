package com.example.coursework.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.support.annotation.NonNull;

import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.data.DaoRepository;

import java.util.List;

public class TrainingActivityViewModel extends AndroidViewModel {
    DaoRepository daoRepository;
    LiveData<User> userLD;
    public LiveData<Session> currentSessionLD;
    LiveData<List<Route>> recentRoutesLD;
    LiveData<List<Session>> recentSessionsLD;

    public LiveData<User> getUserLD(long userId) {
        return daoRepository.getUserById(userId);
    }
    public LiveData<Session> getCurrentSession(long userId) {
        return daoRepository.getCurrentSession(userId);
    }
    public LiveData<List<Route>> getRecentRoutesLD() {
        return recentRoutesLD;
    }

    public LiveData<List<Session>> getRecentSessionsLD() {
        return recentSessionsLD;
    }

    public TrainingActivityViewModel(@NonNull Application application) {
        super(application);
        daoRepository = new DaoRepository(application);
        userLD = new MutableLiveData<>();
        currentSessionLD = new MutableLiveData<>();
        recentRoutesLD = new MutableLiveData<>();
        recentSessionsLD = new MutableLiveData<>();
    }
    public void updateRecentRoutes(int numberOfRoutes, long sessionId){
        recentRoutesLD =  daoRepository.getRecentRoutesForSession( numberOfRoutes,  sessionId);
        //return getRecentRoutesLD();
    }
    public LiveData<List<Session>> updateRecentSessions(int numberOfSessions, long userId){
        recentSessionsLD = daoRepository.getRecentSessionsForUser(numberOfSessions,userId);
        return getRecentSessionsLD();
    }

    public void updateCurrentSession(Session session){
        daoRepository.updateSession(session);
        currentSessionLD = getCurrentSession(userLD.getValue().getId());
    }
    public void CreateNewSession(Session session){
        daoRepository.insertSession(session);
        currentSessionLD = getCurrentSession(userLD.getValue().getId());
    }
}
