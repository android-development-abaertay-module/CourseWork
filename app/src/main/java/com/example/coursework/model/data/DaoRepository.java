package com.example.coursework.model.data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.coursework.model.GoalAnnual;
import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.Logbook;
import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;

public class DaoRepository {
    private GoalAnnualDAO goalAnnualDAO;
    private GoalSeasonalDAO goalSeasonalDAO;
    private GoalWeeklyDAO goalWeeklyDAO;
    private LogbookDAO logbookDAO;
    private RouteDAO routeDAO;
    private SessionDAO sessionDAO;
    private UserDAO userDAO;

    public DaoRepository(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        goalAnnualDAO = appDatabase.goalAnnualDAO();
        goalSeasonalDAO = appDatabase.goalSeasonalDAO();
        goalWeeklyDAO = appDatabase.goalWeeklyDAO();
        logbookDAO = appDatabase.logbookDAO();
        routeDAO = appDatabase.routeDAO();
        sessionDAO = appDatabase.sessionDAO();
        userDAO = appDatabase.userDAO();
    }

    //region [Inserts]
    public  void insertAnnualGoal(GoalAnnual goalAnnual){
        new InsertGoalAnnualAsyncTask(goalAnnualDAO).execute(goalAnnual);
    }
    private static  class InsertGoalAnnualAsyncTask extends AsyncTask<GoalAnnual,Void,Void> {
        GoalAnnualDAO anGoalDao;
        public InsertGoalAnnualAsyncTask(GoalAnnualDAO goalAnnualDAO) {
            this.anGoalDao = goalAnnualDAO;
        }

        @Override
        protected Void doInBackground(GoalAnnual... annuals) {
            anGoalDao.insert(annuals[0]);
            return  null;
        }
    }

    public  void insertSeasonalGoal(GoalSeasonal goalSeasonal){
        new InsertGoalSeasonalAsyncTask(goalSeasonalDAO).execute(goalSeasonal);
    }
    private static  class InsertGoalSeasonalAsyncTask extends AsyncTask<GoalSeasonal,Void,Void> {
        GoalSeasonalDAO seasonGoalDao;
        public InsertGoalSeasonalAsyncTask(GoalSeasonalDAO goalSeasonalDAO) {
            this.seasonGoalDao = goalSeasonalDAO;
        }

        @Override
        protected Void doInBackground(GoalSeasonal... annuals) {
            seasonGoalDao.insert(annuals[0]);
            return  null;
        }
    }

    public  void insertWeeklyGoal(GoalWeekly goalWeekly){
        new InsertGoalWeeklyAsyncTask(goalWeeklyDAO).execute(goalWeekly);
    }
    private static  class InsertGoalWeeklyAsyncTask extends AsyncTask<GoalWeekly,Void,Void> {
        GoalWeeklyDAO goalWeeklyDAO;
        public InsertGoalWeeklyAsyncTask(GoalWeeklyDAO goalWeeklyDAO) {
            this.goalWeeklyDAO = goalWeeklyDAO;
        }

        @Override
        protected Void doInBackground(GoalWeekly... weeklyGoal) {
            goalWeeklyDAO.insert(weeklyGoal[0]);
            return  null;
        }
    }

    public  void insertLogbook(Logbook logbook){
        new InsertLogbookAsyncTask(logbookDAO).execute(logbook);
    }
    private static  class InsertLogbookAsyncTask extends AsyncTask<Logbook,Void,Void> {
        LogbookDAO logbookDAO;
        public InsertLogbookAsyncTask(LogbookDAO logbookDAO) {
            this.logbookDAO = logbookDAO;
        }

        @Override
        protected Void doInBackground(Logbook... logbooks) {
            logbookDAO.insert(logbooks[0]);
            return  null;
        }
    }

    public  void insertRoute(Route route){
        new InsertRouteAsyncTask(routeDAO).execute(route);
    }
    private static  class InsertRouteAsyncTask extends AsyncTask<Route,Void,Void> {
        RouteDAO routeDAO;
        public InsertRouteAsyncTask(RouteDAO routeDAO) {
            this.routeDAO = routeDAO;
        }

        @Override
        protected Void doInBackground(Route... routes) {
            routeDAO.insert(routes[0]);
            return  null;
        }
    }

    public  void insertSession(Session session){
        new InsertSessionAsyncTask(sessionDAO).execute(session);
    }
    private static  class InsertSessionAsyncTask extends AsyncTask<Session,Void,Void> {
        SessionDAO sessionDAO;
        public InsertSessionAsyncTask(SessionDAO sessionDAO) {
            this.sessionDAO = sessionDAO;
        }

        @Override
        protected Void doInBackground(Session... sessions) {
            sessionDAO.insert(sessions[0]);
            return  null;
        }
    }

    public  void insertUser(User user){
        new InsertUserAsyncTask(userDAO).execute(user);
    }
    private static class InsertUserAsyncTask extends AsyncTask<User,Void,Void> {
        UserDAO userDAO;
        public InsertUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.insert(users[0]);
            return  null;
        }
    }

    //endregion

    //region [Updates]

    //endregion
    //region [Deletes]

    //endregion
}
