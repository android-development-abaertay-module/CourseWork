package com.example.coursework.model.enums;

public enum PermissionCheck {
    NOT_REQUESTED(1),
    GRANTED(2),
    DENIED(3);

    private int value;
    PermissionCheck(int value) {
        this.value = value;
    }
    //get numerical value of enum
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this){
            case NOT_REQUESTED:
                return  "Not Requested Yet";
            case GRANTED:
                return "Granted";
            case DENIED:
                return "Denied";
        }
        return super.toString();
    }
}
