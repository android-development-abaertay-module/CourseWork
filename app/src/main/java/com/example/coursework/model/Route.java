package com.example.coursework.model;

import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.RouteType;
import com.example.coursework.model.enums.StyleDone;

import java.time.LocalDateTime;


public  class Route
{
    //-----------------------------------------Attributes

    private int _IDRoute;
    private int _IDSessionFK;

    private StyleDone _styleDone;
    private LocalDateTime _dateAndTime;
    private RouteType _routeType;
    private Grades _grade;
    private int _gradeValue;


    //------Constructor
    public Route(int sessionIDFK, Grades grade,RouteType routeType, StyleDone styleDone, LocalDateTime dateAndtime)
    {
        _grade = grade;
        _gradeValue = grade.getValue();//get enum index position and add one
        _IDSessionFK = sessionIDFK;
        _styleDone = styleDone;
        _routeType = routeType;
        _dateAndTime = dateAndtime;

    }

    //------Methods
}
