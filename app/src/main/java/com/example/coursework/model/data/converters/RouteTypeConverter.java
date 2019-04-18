package com.example.coursework.model.data.converters;

import android.arch.persistence.room.TypeConverter;
import com.example.coursework.model.enums.RouteType;


public class RouteTypeConverter {
    @TypeConverter
    public static int routeTypeToInt(RouteType routeType){
        return routeType == null ? 0 : routeType.getValue();
    }

    @TypeConverter
    public static RouteType intToRoutType(int styleDoneValue){
        if (styleDoneValue == 1 || styleDoneValue ==2){
            return RouteType.getFromInteger(styleDoneValue);
        }else{
            throw new IndexOutOfBoundsException("only route values of 1 or 2 accepted");

        }
    }
}
