package com.example.coursework.model;

import com.example.coursework.model.enums.Grades;

import java.time.LocalDateTime;

public class GoalWeekly extends Goal {
//---------------------------------------------------------------------Attributes----------------------------------------------------

    private int _hoursOfTraining;
    private int _numberOfSport;
    private int _numberOfBoulder;
    private Grades _averageSportGrade;
    private int _sportGoalValue;
    private Grades _averageBoulderGrade;
    private int _boulderGoalValue;

    public int get_hoursOfTraining() {
        return _hoursOfTraining;
    }
    public void set_hoursOfTraining(int _hoursOfTraining) {
        this._hoursOfTraining = _hoursOfTraining;
    }

    public int get_numberOfSport() {
        return _numberOfSport;
    }
    public void set_numberOfSport(int _numberOfSport) {
        this._numberOfSport = _numberOfSport;
    }

    public int get_numberOfBoulder() {
        return _numberOfBoulder;
    }
    public void set_numberOfBoulder(int _numberOfBoulder) {
        this._numberOfBoulder = _numberOfBoulder;
    }

    public Grades get_averageSportGrade() {
        return _averageSportGrade;
    }
    public void set_averageSportGrade(Grades _averageSportGrade) {
        this._averageSportGrade = _averageSportGrade;
    }

    public int get_sportGoalValue() {
        return _sportGoalValue;
    }
    public void set_sportGoalValue(int _sportGoalValue) {
        this._sportGoalValue = _sportGoalValue;
    }

    public Grades get_averageBoulderGrade() {
        return _averageBoulderGrade;
    }
    public void set_averageBoulderGrade(Grades _averageBoulderGrade) {
        this._averageBoulderGrade = _averageBoulderGrade;
    }

    public int get_boulderGoalValue() {
        return _boulderGoalValue;
    }
    public void set_boulderGoalValue(int _boulderGoalValue) {
        this._boulderGoalValue = _boulderGoalValue;
    }

    //--------------------------------------------------------------------------Constructor----------------------------------------------------
    public GoalWeekly(int iDUserFK, int hoursOfTraining, int numberOfSport, int numberOfBoulder, Grades averageSportGrade, Grades averageBoulderGrade, LocalDateTime dateCreated)
        {
            super(iDUserFK);
            _goalDruation = 7;//7 Days
            _dateExpires = dateCreated.plusDays(_goalDruation);
            _goalAcheaved = false;
            _dateCreated = dateCreated;

            _hoursOfTraining = hoursOfTraining;
            _numberOfSport = numberOfSport;
            _numberOfBoulder = numberOfBoulder;
            _averageSportGrade = averageSportGrade;
            _sportGoalValue = _averageSportGrade.getValue();
            _averageBoulderGrade = averageBoulderGrade;
            _boulderGoalValue = _averageBoulderGrade.ordinal();
        }

    public GoalWeekly()
        {
            super();
        }

//-----------------------------------------------------------------------------Methods-----------------------------------------------------

}