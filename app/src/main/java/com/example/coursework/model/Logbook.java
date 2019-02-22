package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Logbook",foreignKeys = @ForeignKey(entity = User.class,parentColumns = "id",childColumns = "userId", onDelete = CASCADE))
public class Logbook
{
    //----------------------------------------------Attributes
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "userId")
    private int userId;
    @Ignore
    private ArrayList<Session> sessionLog;
    @Ignore
    private Session currentSession;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<Session> getSessionLog() {
        return sessionLog;
    }
    public void setSessionLog(ArrayList<Session> sessionLog) {
        this.sessionLog = sessionLog;
    }

    public Session getCurrentSession() {
        return currentSession;
    }
    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    //----Constructor
    public Logbook(int iDUserFK)
    {
        userId = iDUserFK;
        sessionLog = new ArrayList<Session>();
        currentSession = null;
    }

}