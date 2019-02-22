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
