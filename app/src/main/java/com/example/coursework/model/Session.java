package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Session",foreignKeys = @ForeignKey(entity = User.class,parentColumns = "id",childColumns = "userId", onDelete = CASCADE))
public class Session
{
    //---------------------------------------------------Attributes-----------------------------
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "userId", index = true)
    private long userId;

    @ColumnInfo(name = "startTime")
    private LocalDateTime startTime;
    @ColumnInfo(name = "endTime")
    private LocalDateTime endTime;
    @Ignore
    private ArrayList<Route> routeLog;

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

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Route> getRouteLog() {
        return routeLog;
    }
    public void setRouteLog(ArrayList<Route> routeLog) {
        this.routeLog = routeLog;
    }

    //---Constructor
    public Session(){
        routeLog = new ArrayList<>();
    }
    @Ignore
    public Session(LocalDateTime startTime, long userId)
    {
        this.userId = userId;
        this.startTime = startTime;
        routeLog = new ArrayList<>();
    }
}