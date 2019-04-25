package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
    private OffsetDateTime startTime;
    @ColumnInfo(name = "endTime")
    private OffsetDateTime endTime;
    @ColumnInfo(name = "latitude")
    private double lat;
    @ColumnInfo(name = "longitude")
    private double lon;
    @ColumnInfo(name = "location")
    private String location;
    @Ignore
    private ArrayList<Route> routes;

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

    public OffsetDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(OffsetDateTime endTime) {
        this.endTime = endTime;
    }

    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }
    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    //---Constructor
    public Session(){
        routes = new ArrayList<>();
    }
    @Ignore
    public Session(OffsetDateTime startTime, long userId)
    {
        this.userId = userId;
        this.startTime = startTime;
        routes = new ArrayList<>();
    }
    public Session(OffsetDateTime startTime, long userId,double latitude, double longitude)
    {
        this.userId = userId;
        this.startTime = startTime;
        this.lat = latitude;
        this.lon = longitude;
        routes = new ArrayList<>();
    }
    //---Methods---
    public String detailsSummaryForMap(){
        String st ="";
        String et = "";
        if (startTime != null)
            st = startTime.format(DateTimeFormatter.ofPattern("MM/dd  HH:mm:ss"));
        if (endTime != null)
            et = endTime.format(DateTimeFormatter.ofPattern("MM/dd  HH:mm:ss"));
        //summary string to return
        return "Start Time: " + st + "\n" +
                "End Time: " + et + "\n" +
                "Latitude: " + lat + "\n" +
                "Longitude: " + lon;
    }
}