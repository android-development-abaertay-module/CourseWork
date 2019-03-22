package com.example.coursework.model.helper;

public class PrintNull {

    public static String Print(Object value){
        if (value == null)
            return "";
        else
            return  value.toString();
    }
}
