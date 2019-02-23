package com.example.coursework.model.enums;

public enum StyleDone {
    Onsight(1), Worked(2), Dogged(3);


    private int value;
    StyleDone(int value) {
        this.value = value;
    }
    public static StyleDone getFromInteger(int value){
        StyleDone result;
        switch(value) {
            case 1:
                result = StyleDone.Onsight;
                break;
            case 2:
                result = StyleDone.Worked;
                break;
            case 3:
                result = StyleDone.Dogged;
                break;
            default:
                result = StyleDone.Dogged;
                break;
        }
        result.value = value;
        return result;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this){
            case Dogged:
                return "Dogged";
            case Worked:
                return "Worked";
            case Onsight:
                return "Onsight";
        }
        return super.toString();
    }
}
