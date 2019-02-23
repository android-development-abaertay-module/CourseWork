package com.example.coursework.model.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.coursework.model.GoalAnnual;
import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.Logbook;
import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.data.converters.GradeConverter;
import com.example.coursework.model.data.converters.LocalDateTimeConverter;
import com.example.coursework.model.data.converters.RouteTypeConverter;
import com.example.coursework.model.data.converters.StyleConverter;
import com.example.coursework.model.enums.Grades;

import java.time.LocalDateTime;

@Database(entities = {
        GoalAnnual.class,
        GoalSeasonal.class,
        GoalWeekly.class,
        Logbook.class,
        Route.class,
        Session.class,
        User.class}, version = 1)
@TypeConverters({GradeConverter.class,
        StyleConverter.class,
        LocalDateTimeConverter.class,
        RouteTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public  abstract GoalAnnualDAO goalAnnualDAO();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            //fallback to destructive migration. delete then recreate
            //Singleton Design pattern
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    //Callback inner class to initialize database with test data
    private  static RoomDatabase.Callback callback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            Log.d("gwyd","Room database opened successfully");

        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("gwyd","Room database created successfully");
            LocalDateTime today = LocalDateTime.now();

            GoalAnnual test = new GoalAnnual(1, Grades.FIVE_A,Grades.FIVE_A,Grades.FIVE_B,Grades.FIVE_B,today);
            instance.goalAnnualDAO().insert(test);
        }
    };
}
