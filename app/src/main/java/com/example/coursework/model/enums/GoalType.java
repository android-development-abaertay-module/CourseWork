package com.example.coursework.model.enums;

public enum GoalType {
    WEEKLY (0),
    SEASONAL(1),
    ANNUAL(2);


    private int value;
    GoalType(int value) {
        this.value = value;
    }

    //get numerical value of enum
    public int getValue() {
        return value;
    }
}
