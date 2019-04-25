package com.example.coursework.model.helper;

public class PrintNull {
    //simple helper class to print null objects as empty strings instead of 'null'
    public static String Print(Object value){
        if (value == null)
            return "";
        else
            return  value.toString();
    }
}
