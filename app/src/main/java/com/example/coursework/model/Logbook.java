package com.example.coursework.model;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Logbook
{
    //----------------------------------------------Attributes


    private int _IDLogbook;
    private int _IDUserFK;
    private ArrayList<Session> _sessionLog;
    private Session _currentSession;

    public int get_IDLogbook() {
        return _IDLogbook;
    }
    public void set_IDLogbook(int _IDLogbook) {
        this._IDLogbook = _IDLogbook;
    }

    public int get_IDUserFK() {
        return _IDUserFK;
    }
    public void set_IDUserFK(int _IDUserFK) {
        this._IDUserFK = _IDUserFK;
    }

    public ArrayList<Session> get_sessionLog() {
        return _sessionLog;
    }
    public void set_sessionLog(ArrayList<Session> _sessionLog) {
        this._sessionLog = _sessionLog;
    }

    public Session get_currentSession() {
        return _currentSession;
    }
    public void set_currentSession(Session _currentSession) {
        this._currentSession = _currentSession;
    }

    //----Constructor
    public Logbook(int iDUserFK)
    {
        _IDUserFK = iDUserFK;
        _sessionLog = new ArrayList<Session>();
        _currentSession = null;
    }

}