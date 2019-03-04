package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coursework.model.Route;
import com.example.coursework.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface RouteDAO {
    @Insert
    long  insert(Route route);
    @Update
    void update(Route route);
    @Delete
    void delete(Route route);

    @Query("SELECT * FROM Route WHERE userId ==:userId and timeDone BETWEEN datetime(:periodStart) AND datetime(:periodEnd)")
    List<Route> getAllRoutesForUserInPeriod(long userId, LocalDateTime periodStart, LocalDateTime periodEnd);

    @Query("SELECT * FROM Route WHERE userId ==:userId")
    List<Route> getAllRoutesForUser(long userId);

    @Query("SELECT * FROM Route WHERE userId ==:userId ORDER BY timeDone ASC")
    List<Route> getAllRoutesForUserOrderASC(long userId);

    @Query("SELECT * FROM Route WHERE userId ==:userId ORDER BY timeDone ASC LIMIT :numberOfRoutes")
    LiveData<List<Route>> getRecentRoutesForUser(int numberOfRoutes, long userId);
}
