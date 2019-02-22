package com.example.coursework.model.enums;

public enum StyleDone {
    Onsight, Worked, Dogged;


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
