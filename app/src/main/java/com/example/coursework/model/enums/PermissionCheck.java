package com.example.coursework.model.enums;

public enum PermissionCheck {
    NOT_REQUESTED(1),
    GRANTED(2),
    DENIED(3);

    private int value;
    PermissionCheck(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public static PermissionCheck getFromInteger(int value){
        PermissionCheck result;
        switch(value) {
            case 1:
                result = PermissionCheck.NOT_REQUESTED;
                break;
            case 2:
                result = PermissionCheck.GRANTED;
                break;
            case 3:
                result = PermissionCheck.DENIED;
                break;
            default:
                throw new IndexOutOfBoundsException("only route values of 1 or 2 accepted");
        }
        result.value = value;
        return result;
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
