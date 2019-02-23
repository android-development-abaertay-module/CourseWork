package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Session",foreignKeys = @ForeignKey(entity = Logbook.class,parentColumns = "id",childColumns = "logbookId", onDelete = CASCADE))
public class Session
{
    //---------------------------------------------------Attributes-----------------------------
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "logbookId", index = true)
    private int logbookId;

    @ColumnInfo(name = "startTime")
    private LocalDateTime startTime;
    @ColumnInfo(name = "endTime")
    private LocalDateTime endTime;
    @Ignore
    private ArrayList<Route> routeLog;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getLogbookId() {
        return logbookId;
    }
    public void setLogbookId(int logbookId) {
        this.logbookId = logbookId;
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

    }
    @Ignore
    public Session(LocalDateTime startTime, int idLogbookFK)
    {
        logbookId = idLogbookFK;
        this.startTime = startTime;
        routeLog = new ArrayList<>();
    }
}