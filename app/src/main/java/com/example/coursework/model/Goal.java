package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static android.arch.persistence.room.ForeignKey.CASCADE;

public abstract class Goal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected long id;
    @ColumnInfo(name = "userId")
    protected long userId;
    @ColumnInfo(name = "dateCreated")
    protected OffsetDateTime dateCreated;
    @ColumnInfo(name = "dateExpires")
    protected OffsetDateTime dateExpires;
    @ColumnInfo(name = "goalAchieved")
    protected Boolean goalAchieved;
    @ColumnInfo(name = "goalDuration")
    protected int goalDuration;

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

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getDateExpires() {
        return dateExpires;
    }
    public void setDateExpires(OffsetDateTime dateExpires) {
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
    public Goal(long iDUserFK) {
        userId = iDUserFK;
        goalAchieved = false;
    }


}
