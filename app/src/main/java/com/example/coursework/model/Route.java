package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.RouteType;
import com.example.coursework.model.enums.StyleDone;

import java.time.OffsetDateTime;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Route",foreignKeys = {@ForeignKey(entity = Session.class,parentColumns = "id",childColumns = "sessionId", onDelete = CASCADE),
        @ForeignKey(entity = User.class,parentColumns = "id",childColumns = "userId")})
public  class Route
{
    //-----------------------------------------Attributes
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "sessionId", index = true)
    private long sessionId;
    @ColumnInfo(name = "userId")
    private long userId;
    @ColumnInfo(name = "styleDone")
    private StyleDone styleDone;
    @ColumnInfo(name = "timeDone")
    private OffsetDateTime timeDone;
    @ColumnInfo(name = "routeType")
    private RouteType routeType;
    @ColumnInfo(name = "grade")
    private Grades grade;
    @ColumnInfo(name = "gradeValue")
    private int gradeValue;

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

    public long getSessionId() {
        return sessionId;
    }
    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public StyleDone getStyleDone() {
        return styleDone;
    }
    public void setStyleDone(StyleDone styleDone) {
        this.styleDone = styleDone;
    }

    public OffsetDateTime getTimeDone() {
        return timeDone;
    }
    public void setTimeDone(OffsetDateTime timeDone) {
        this.timeDone = timeDone;
    }

    public RouteType getRouteType() {
        return routeType;
    }
    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public Grades getGrade() {
        return grade;
    }
    public void setGrade(Grades grade) {
        this.grade = grade;
    }

    public int getGradeValue() {
        return gradeValue;
    }
    public void setGradeValue(int gradeValue) {
        this.gradeValue = gradeValue;
    }

    //------Constructor

    public Route(){

    }

    @Ignore
    public Route(long sessionIDFK,long userId, Grades grade,RouteType routeType, StyleDone styleDone, OffsetDateTime dateAndtime)
    {
        this.userId = userId;
        this.grade = grade;
        gradeValue = grade.getValue();//get enum index position and add one
        sessionId = sessionIDFK;
        this.styleDone = styleDone;
        this.routeType = routeType;
        timeDone = dateAndtime;

    }

    //------Methods
}
