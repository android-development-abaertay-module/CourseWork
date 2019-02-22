package com.example.coursework.model;

import java.time.LocalDateTime;
import java.util.Date;

public abstract class Goal {

    protected int _IDGoal;

    protected int _IDUserFK;

    protected LocalDateTime _dateCreated;

    protected LocalDateTime _dateExpires;

    protected Boolean _goalAcheaved;

    protected int _goalDruation;

    public int get_IDGoal() {
        return _IDGoal;
    }
    public void set_IDGoal(int _IDGoal) {
        this._IDGoal = _IDGoal;
    }

    public int get_IDUserFK() {
        return _IDUserFK;
    }
    public void set_IDUserFK(int _IDUserFK) {
        this._IDUserFK = _IDUserFK;
    }

    public LocalDateTime get_dateCreated() {
        return _dateCreated;
    }
    public void set_dateCreated(LocalDateTime _dateCreated) {
        this._dateCreated = _dateCreated;
    }

    public LocalDateTime get_dateExpires() {
        return _dateExpires;
    }
    public void set_dateExpires(LocalDateTime _dateExpires) {
        this._dateExpires = _dateExpires;
    }

    public Boolean get_goalAcheaved() {
        return _goalAcheaved;
    }
    public void set_goalAcheaved(Boolean _goalAcheaved) {
        this._goalAcheaved = _goalAcheaved;
    }

    public int get_goalDruation() {
        return _goalDruation;
    }
    public void set_goalDruation(int _goalDruation) {
        this._goalDruation = _goalDruation;
    }

    //-----------Constructor
    public Goal(int iDUserFK) {
        _IDUserFK = iDUserFK;
        _goalAcheaved = false;
    }
    public Goal() {
    }

}
