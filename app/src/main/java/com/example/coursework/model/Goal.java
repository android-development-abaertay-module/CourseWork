package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;

import static android.arch.persistence.room.ForeignKey.CASCADE;

public abstract class Goal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected int id;
    @ColumnInfo(name = "userId")
    protected int userId;
    @ColumnInfo(name = "dateCreated")
    protected LocalDateTime dateCreated;
    @ColumnInfo(name = "dateExpires")
    protected LocalDateTime dateExpires;
    @ColumnInfo(name = "goalAchieved")
    protected Boolean goalAchieved;
    @ColumnInfo(name = "goalDuration")
    protected int goalDuration;

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

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateExpires() {
        return dateExpires;
    }
    public void setDateExpires(LocalDateTime dateExpires) {
        this.dateExpires = dateExpires;
    }

    public Boolean getGoalAchieved() {
        return goalAchieved;
    }
    public void setGoalAchieved(Boolean goalAchieved) {
        this.goalAchieved = goalAchieved;
    }

    public int getGoalDuration() {
        return goalDuration;
    }
    public void setGoalDuration(int goalDuration) {
        this.goalDuration = goalDuration;
    }

    //-----------Constructor
    public Goal() {
    }
    @Ignore
    public Goal(int iDUserFK) {
        userId = iDUserFK;
        goalAchieved = false;
    }


}
