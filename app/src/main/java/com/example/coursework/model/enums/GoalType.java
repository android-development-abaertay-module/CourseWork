package com.example.coursework.model.enums;

public enum GoalType {
    WEEKLY (0),
    SEASONAL(1),
    ANNUAL(2);


    private int value;
    GoalType(int value) {
        this.value = value;
    }

    public static GoalType getFromInteger(int value) {
        GoalType result;
        switch (value) {
            case 0:
                result = GoalType.WEEKLY;
                break;
            case 1:
                result = GoalType.SEASONAL;
                break;
            case 2:
                result = GoalType.ANNUAL;
                break;
            default:
                result = null;
                break;
        }
        if (result != null)
            result.value = value;
        return result;
    }
    public int getValue() {
        return value;
    }
}
