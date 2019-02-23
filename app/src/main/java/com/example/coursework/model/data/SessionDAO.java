package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coursework.model.Session;

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
}
