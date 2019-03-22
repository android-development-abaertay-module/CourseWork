package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coursework.model.Session;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface SessionDAO {
    @Insert
    long insert(Session session);
    @Update
    int update(Session session);
    @Delete
    void delete(Session session);

    @Query("SELECT * FROM Session s WHERE userId ==:userId AND endTime IS NULL ORDER BY startTime DESC LIMIT 1")
    LiveData<Session> getCurrentSessionForUser(long userId);

    @Query("SELECT * FROM Session WHERE userId ==:userId   ORDER BY startTime DESC LIMIT :numberOfSessions")
    LiveData<List<Session>> getRecentSessionsForUser(int numberOfSessions, long userId);

    @Query("SELECT * FROM Session WHERE userId ==:userId AND location is not null " +
            "GROUP BY location " +
            "ORDER BY startTime DESC LIMIT :numberOfSessions")
    LiveData<List<Session>> getRecentSessionsWithLocationForUser(int numberOfSessions, long userId);

}
