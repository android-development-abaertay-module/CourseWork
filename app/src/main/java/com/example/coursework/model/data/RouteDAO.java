package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coursework.model.Route;

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

    @Query("SELECT * FROM Route WHERE userId ==:userId and timeDone BETWEEN :periodStart AND :periodEnd")
    List<Route> getAllRoutesForUserInPeriod(long userId, LocalDateTime periodStart, LocalDateTime periodEnd);

}
