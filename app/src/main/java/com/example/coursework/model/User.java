package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.coursework.model.data.DaoRepository;
import com.example.coursework.model.data.UserDAO;

@Entity(tableName = "User")
public class User
{
    //----------------------------------------------------Attributes
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "userName")
    private String userName;
    @Ignore
    private Logbook logBook;
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

    public Logbook getLogBook() {
        return logBook;
    }
    public void setLogBook(Logbook logBook) {
        this.logBook = logBook;
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

    }
    @Ignore
    public User(String  userName)
    {
        this.userName = userName;
        logBook = new Logbook(id);
        weeklyGoal = new GoalWeekly();
        seasonalGoal = new GoalSeasonal();
        annualGoal = new GoalAnnual();
    }
    //-----Methods

}
