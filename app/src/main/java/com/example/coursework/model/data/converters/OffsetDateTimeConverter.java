package com.example.coursework.model.data.converters;

import android.arch.persistence.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public class OffsetDateTimeConverter {
    @TypeConverter
    public static OffsetDateTime toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return OffsetDateTime.parse(dateString);
        }
    }

    @TypeConverter
    public static String toDateString(OffsetDateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }
}