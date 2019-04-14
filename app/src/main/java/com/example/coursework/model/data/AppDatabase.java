package com.example.coursework.model.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.coursework.model.GoalAnnual;
import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.data.converters.GradeConverter;
import com.example.coursework.model.data.converters.OffsetDateTimeConverter;
import com.example.coursework.model.data.converters.RouteTypeConverter;
import com.example.coursework.model.data.converters.StyleConverter;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.RouteType;
import com.example.coursework.model.enums.StyleDone;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Database(entities = {
        GoalAnnual.class,
        GoalSeasonal.class,
        GoalWeekly.class,
        Route.class,
        Session.class,
        User.class}, version = 1)
@TypeConverters({GradeConverter.class,
        StyleConverter.class,
        OffsetDateTimeConverter.class,
        RouteTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public  abstract GoalAnnualDAO goalAnnualDAO();
    public abstract GoalSeasonalDAO goalSeasonalDAO();
    public abstract GoalWeeklyDAO goalWeeklyDAO();
    public abstract RouteDAO routeDAO();
    public abstract SessionDAO sessionDAO();
    public abstract UserDAO userDAO();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            //fallback to destructive migration. delete then recreate
            //Singleton Design pattern
            Log.d("gwyd","initializing database");
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

            new InitialDataAsyncTask(instance).execute();
        }
    };
    private static class InitialDataAsyncTask extends AsyncTask<Void,Void,Void> {
        private GoalAnnualDAO goalAnnualDAO;
        private GoalSeasonalDAO goalSeasonalDAO;
        private GoalWeeklyDAO goalWeeklyDAO;
        private RouteDAO routeDAO;
        private SessionDAO sessionDAO;
        private UserDAO userDAO;

        public InitialDataAsyncTask(AppDatabase appDatabase) {
            goalAnnualDAO = appDatabase.goalAnnualDAO();
            goalSeasonalDAO = appDatabase.goalSeasonalDAO();
            goalWeeklyDAO = appDatabase.goalWeeklyDAO();
            routeDAO = appDatabase.routeDAO();
            sessionDAO = appDatabase.sessionDAO();
            userDAO = appDatabase.userDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User user = new User("gwyd");
            user.setId(userDAO.insert(user));

            GoalWeekly goalWeekly = new GoalWeekly(user.getId(),10,20,Grades.SIX_A,Grades.SIX_B, OffsetDateTime.now());
            goalWeekly.setId(goalWeeklyDAO.insert(goalWeekly));

            GoalSeasonal goalSeasonal = new GoalSeasonal(user.getId(),Grades.SEVEN_A,Grades.SEVEN_A,Grades.SEVEN_B,Grades.SEVEN_B,OffsetDateTime.now().minusWeeks(5));
            goalSeasonal.setId(goalSeasonalDAO.insert(goalSeasonal));

            GoalAnnual goalAnnual = new GoalAnnual(user.getId(),Grades.SEVEN_A,Grades.SEVEN_A,Grades.SEVEN_B,Grades.SEVEN_B,OffsetDateTime.now().minusMonths(2));
            goalAnnual.setId(goalAnnualDAO.insert(goalAnnual));

            Session oldSession = new Session(OffsetDateTime.now().minusDays(1).minusHours(3),user.getId());
            oldSession.setEndTime(OffsetDateTime.now());
            oldSession.setId(sessionDAO.insert(oldSession));

            Route oldRouteOne = new Route(oldSession.getId(),user.getId(),Grades.FIVE_A, RouteType.BOULDER, StyleDone.Onsight,OffsetDateTime.now().minusDays(1).minusHours(2).minusMinutes(30));
            Route oldRouteTwo = new Route(oldSession.getId(),user.getId(),Grades.FIVE_B, RouteType.BOULDER, StyleDone.Onsight,OffsetDateTime.now().minusDays(1).minusHours(2).minusMinutes(20));
            Route oldRouteThree = new Route(oldSession.getId(),user.getId(),Grades.FIVE_C, RouteType.BOULDER, StyleDone.Onsight,OffsetDateTime.now().minusDays(1).minusHours(2).minusMinutes(10));
            oldRouteOne.setId(routeDAO.insert(oldRouteOne));
            oldRouteTwo.setId(routeDAO.insert(oldRouteTwo));
            oldRouteThree.setId(routeDAO.insert(oldRouteThree));

            Session currentSession = new Session(OffsetDateTime.now(),user.getId());
            currentSession.setId(sessionDAO.insert(currentSession));

            Route cRouteOne = new Route(currentSession.getId(),user.getId(),Grades.SIX_A,RouteType.SPORT,StyleDone.Onsight,OffsetDateTime.now().minusMinutes(20));
            Route cRouteTwo = new Route(currentSession.getId(),user.getId(),Grades.SIX_B,RouteType.SPORT,StyleDone.Onsight,OffsetDateTime.now().minusMinutes(10));
            Route cRouteThree = new Route(currentSession.getId(),user.getId(),Grades.SIX_C,RouteType.SPORT,StyleDone.Onsight,OffsetDateTime.now().minusMinutes(5));
            cRouteOne.setId(routeDAO.insert(cRouteOne));
            cRouteTwo.setId(routeDAO.insert(cRouteTwo));
            cRouteThree.setId(routeDAO.insert(cRouteThree));
            Log.d("gwyd","initial data setup completed");
            return null;
        }
    }
}
