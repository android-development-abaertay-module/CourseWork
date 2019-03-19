package com.example.coursework.model.data.converters;

import android.arch.persistence.room.TypeConverter;

import com.example.coursework.model.enums.Grades;

public class GradeConverter {
    @TypeConverter
    public static int gradeToInt(Grades grade){
        return grade == null ? 0 : grade.getValue();
    }

    @TypeConverter
    public static Grades intToGrade(int gradeValue){
        return gradeValue == 0 ? null :  Grades.getFromInteger(gradeValue);
    }
}
