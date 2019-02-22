package com.example.coursework.model;

import java.time.LocalDateTime;

public class User
{
    //----------------------------------------------------Attributes

    private int _IDUser;
    private String _userName;
    private Logbook _logBook;
    private GoalWeekly _weeklyGoal;
    private GoalSeasonal _seasonalGoal;
    private GoalAnnual _annualGoal;

    public int get_IDUser() {
        return _IDUser;
    }
    public void set_IDUser(int _IDUser) {
        this._IDUser = _IDUser;
    }

    public String get_userName() {
        return _userName;
    }
    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public Logbook get_logBook() {
        return _logBook;
    }
    public void set_logBook(Logbook _logBook) {
        this._logBook = _logBook;
    }

    public GoalWeekly get_weeklyGoal() {
        return _weeklyGoal;
    }
    public void set_weeklyGoal(GoalWeekly _weeklyGoal) {
        this._weeklyGoal = _weeklyGoal;
    }

    public GoalSeasonal get_seasonalGoal() {
        return _seasonalGoal;
    }
    public void set_seasonalGoal(GoalSeasonal _seasonalGoal) {
        this._seasonalGoal = _seasonalGoal;
    }

    public GoalAnnual get_annualGoal() {
        return _annualGoal;
    }
    public void set_annualGoal(GoalAnnual _annualGoal) {
        this._annualGoal = _annualGoal;
    }

    //-----Constructor
    public User(String  userName)
    {
        _userName = userName;
        _logBook = new Logbook(_IDUser);
        _weeklyGoal = new GoalWeekly();
        _seasonalGoal = new GoalSeasonal();
        _annualGoal = new GoalAnnual();
    }

    public User()
    {
    }


    //-----Methods
}
