package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coursework.model.Logbook;

import java.util.List;

@Dao
public interface LogbookDAO {
    @Insert
    long  insert(Logbook logbook);
    @Update
    void update(Logbook logbook);
    @Delete
    void delete(Logbook logbook);

    @Query("SELECT * FROM Logbook WHERE userId ==:userId Limit 1")
    LiveData<Logbook> getLogbookForUser(long userId);

}
