package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Logbook",foreignKeys = @ForeignKey(entity = User.class,parentColumns = "id",childColumns = "userId", onDelete = CASCADE))
public class Logbook
{
    //----------------------------------------------Attributes
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "userId", index = true)
    private long userId;
    @Ignore
    private ArrayList<Session> sessionLog;
    @Ignore
    private Session currentSession;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
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
    public Logbook(){

    }
    @Ignore
    public Logbook(long iDUserFK)
    {
        userId = iDUserFK;
        sessionLog = new ArrayList<Session>();
        currentSession = null;
    }

}