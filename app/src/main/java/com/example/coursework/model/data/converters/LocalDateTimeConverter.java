package com.example.coursework.model.data.converters;

import android.arch.persistence.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;

public class LocalDateTimeConverter {
    @TypeConverter
    public static LocalDateTime toDate(Long timestamp) {
        LocalDateTime ldt = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return ldt;
    }

    @TypeConverter
    public static Long toTimestamp(LocalDateTime date) {
        return date.getLong(ChronoField.CLOCK_HOUR_OF_DAY);
    }
}
