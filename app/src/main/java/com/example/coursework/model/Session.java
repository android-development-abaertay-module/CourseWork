package com.example.coursework.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Session
{
    //---------------------------------------------------Attributes-----------------------------


    private int _IDSession;
    private int _IDLogbookFK;

    private LocalDateTime _startTime;
    private LocalDateTime _endTime;
    private ArrayList<Route> _routeLog;
    
    public int get_IDSession() {
        return _IDSession;
    }
    public void set_IDSession(int _IDSession) {
        this._IDSession = _IDSession;
    }

    public int get_IDLogbookFK() {
        return _IDLogbookFK;
    }
    public void set_IDLogbookFK(int _IDLogbookFK) {
        this._IDLogbookFK = _IDLogbookFK;
    }

    public LocalDateTime get_startTime() {
        return _startTime;
    }
    public void set_startTime(LocalDateTime _startTime) {
        this._startTime = _startTime;
    }

    public LocalDateTime get_endTime() {
        return _endTime;
    }
    public void set_endTime(LocalDateTime _endTime) {
        this._endTime = _endTime;
    }

    public ArrayList<Route> get_routeLog() {
        return _routeLog;
    }
    public void set_routeLog(ArrayList<Route> _routeLog) {
        this._routeLog = _routeLog;
    }

    //---Constructor
    public Session(LocalDateTime startTime, int idLogbookFK)
    {
        _IDLogbookFK = idLogbookFK;
        _startTime = startTime;
        _routeLog = new ArrayList<Route>();
    }
}