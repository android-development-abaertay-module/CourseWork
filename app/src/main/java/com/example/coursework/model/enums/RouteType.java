package com.example.coursework.model.enums;

public enum RouteType {
    SPORT(1),
    BOULDER(2);

    private int value;
    RouteType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public static RouteType getFromInteger(int value){
        RouteType result;
        switch(value) {
            case 1:
                result = RouteType.SPORT;
                break;
            case 2:
                result = RouteType.BOULDER;
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
            case SPORT:
                return  "Sport";
            case BOULDER:
                return "Boulder";
        }
        return super.toString();
    }
}
