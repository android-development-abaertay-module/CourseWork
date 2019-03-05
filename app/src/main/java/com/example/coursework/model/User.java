package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "User")
public class User
{
    //----------------------------------------------------Attributes
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "userName")
    private String userName;
    @Ignore
    private ArrayList<Session> sessionsList;
    @Ignore
    private Session curSesh;
    @Ignore
    private GoalWeekly weeklyGoal;
    @Ignore
    private GoalSeasonal seasonalGoal;
    @Ignore
    private GoalAnnual annualGoal;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Session getCurSesh() {
        return curSesh;
    }
    public void setCurSesh(Session curSesh) {
        this.curSesh = curSesh;
    }

    public ArrayList<Session> getSessionsList() {
        return sessionsList;
    }
    public void setSessionsList(ArrayList<Session> sessionsList) {
        this.sessionsList = sessionsList;
    }

    public GoalWeekly getWeeklyGoal() {
        return weeklyGoal;
    }
    public void setWeeklyGoal(GoalWeekly weeklyGoal) {
        this.weeklyGoal = weeklyGoal;
    }

    public GoalSeasonal getSeasonalGoal() {
        return seasonalGoal;
    }
    public void setSeasonalGoal(GoalSeasonal seasonalGoal) {
        this.seasonalGoal = seasonalGoal;
    }

    public GoalAnnual getAnnualGoal() {
        return annualGoal;
    }
    public void setAnnualGoal(GoalAnnual annualGoal) {
        this.annualGoal = annualGoal;
    }

    //-----Constructor
    public User() {
        sessionsList = new ArrayList<>();
    }
    @Ignore
    public User(String  userName)
    {
        this.userName = userName;
        sessionsList = new ArrayList<>();
        weeklyGoal = new GoalWeekly();
        seasonalGoal = new GoalSeasonal();
        annualGoal = new GoalAnnual();
    }
    //-----Methods

}
