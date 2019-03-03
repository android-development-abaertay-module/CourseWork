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
    long  insert(Session session);
    @Update
    void update(Session session);
    @Delete
    void delete(Session session);

    @Query("SELECT * FROM Session WHERE logbookId ==:logbookId AND endTime = NULL LIMIT 1")
    LiveData<Session> getCurrentSessionForLogbook(int logbookId);

    @Query("SELECT * FROM Session WHERE logbookId ==:logbookId")
    LiveData<Session> getAllSessionsInLogbook(int logbookId);

    @Query("SELECT * FROM Session WHERE logbookId ==:logbookId AND startTime between :statTime AND :endTime")
    LiveData<Session> getAllSessionsInLogbookForPeriod(long logbookId, LocalDateTime statTime, LocalDateTime endTime);

    @Query("SELECT * FROM Session " +
            "INNER JOIN Logbook l on l.userId ==:userId " +
            "INNER JOIN User u on u.id ==l.userId " +
            "WHERE u.id ==:userId   ORDER BY startTime ASC LIMIT :numberOfSessions")
    LiveData<List<Session>> getRecentSessionsForUser(int numberOfSessions, long userId);
}
