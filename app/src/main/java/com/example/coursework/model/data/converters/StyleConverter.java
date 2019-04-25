package com.example.coursework.model.data.converters;

import android.arch.persistence.room.TypeConverter;
import com.example.coursework.model.enums.StyleDone;
//Grade converter to convert data types to and from SQLite
public class StyleConverter {
    @TypeConverter
    public static int styleToInt(StyleDone styleDone){
        return styleDone == null ? 0 : styleDone.getValue();
    }

    @TypeConverter
    public static StyleDone intToStyle(int styleDoneValue){
        return styleDoneValue == 0 ? StyleDone.Dogged :  StyleDone.getFromInteger(styleDoneValue);
    }
}
