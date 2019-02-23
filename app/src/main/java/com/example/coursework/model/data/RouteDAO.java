package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coursework.model.Route;

public interface RouteDAO {
    @Insert
    void  insert(Route route);
    @Update
    void update(Route route);
    @Delete
    void delete(Route route);

    @Query("SELECT * FROM Route WHERE sessionId ==:sessionId")
    LiveData<Route> getAllRoutesInSession(int sessionId);
}
