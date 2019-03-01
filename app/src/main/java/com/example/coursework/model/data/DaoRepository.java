package com.example.coursework.model.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.coursework.model.GoalAnnual;
import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.Logbook;
import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.RouteType;

import java.util.ArrayList;
import java.util.List;

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

    public  long insertLogbook(Logbook logbook){
        InsertLogbookAsyncTask task = new InsertLogbookAsyncTask(logbookDAO);
        task.execute(logbook);
        return task.newLogbookId;
    }
    private static  class InsertLogbookAsyncTask extends AsyncTask<Logbook,Void,Void> {
        LogbookDAO logbookDAO;
        long newLogbookId;
        public InsertLogbookAsyncTask(LogbookDAO logbookDAO) {
            this.logbookDAO = logbookDAO;
        }

        @Override
        protected Void doInBackground(Logbook... logbooks) {
            newLogbookId = logbookDAO.insert(logbooks[0]);
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

    public  long insertUserWithLogbook(User user){
        InsertUserWithLogbookAsyncTask task = new InsertUserWithLogbookAsyncTask(userDAO,logbookDAO);
         task.execute(user);
         return task.newUserId;
    }
    private static class InsertUserWithLogbookAsyncTask extends AsyncTask<User,Void,Long> {
        UserDAO userDAO;
        LogbookDAO logbookDAO;
        long newUserId;
        public InsertUserWithLogbookAsyncTask(UserDAO userDAO,LogbookDAO logbookDAO) {
            this.userDAO = userDAO;
            this.logbookDAO = logbookDAO;
        }

        @Override
        protected Long doInBackground(User... users) {
            Log.d("gwyd",users[0].getId() + "");
            newUserId = userDAO.insert(users[0]);
            Log.d("gwyd",newUserId + "");
            Logbook logbook = new Logbook(newUserId);
            Log.d("gwyd",logbook.getId() + "");
            long newLbId = logbookDAO.insert(logbook);
            Log.d("gwyd",newLbId + "");
            return  newUserId;
        }
    }

    //endregion

    //region [Updates]
    public  void updateAnnualGoal(GoalAnnual goalAnnual){
        new UpdateGoalAnnualAsyncTask(goalAnnualDAO).execute(goalAnnual);
    }
    private static  class UpdateGoalAnnualAsyncTask extends AsyncTask<GoalAnnual,Void,Void> {
        GoalAnnualDAO anGoalDao;
        public UpdateGoalAnnualAsyncTask(GoalAnnualDAO goalAnnualDAO) {
            this.anGoalDao = goalAnnualDAO;
        }

        @Override
        protected Void doInBackground(GoalAnnual... annuals) {
            anGoalDao.update(annuals[0]);
            return  null;
        }
    }

    public  void updateSeasonalGoal(GoalSeasonal goalSeasonal){
        new UpdateGoalSeasonalAsyncTask(goalSeasonalDAO).execute(goalSeasonal);
    }
    private static  class UpdateGoalSeasonalAsyncTask extends AsyncTask<GoalSeasonal,Void,Void> {
        GoalSeasonalDAO seasonGoalDao;
        public UpdateGoalSeasonalAsyncTask(GoalSeasonalDAO goalSeasonalDAO) {
            this.seasonGoalDao = goalSeasonalDAO;
        }

        @Override
        protected Void doInBackground(GoalSeasonal... annuals) {
            seasonGoalDao.update(annuals[0]);
            return  null;
        }
    }

    public  void updateWeeklyGoal(GoalWeekly goalWeekly){
        new UpdateGoalWeeklyAsyncTask(goalWeeklyDAO).execute(goalWeekly);
    }
    private static  class UpdateGoalWeeklyAsyncTask extends AsyncTask<GoalWeekly,Void,Void> {
        GoalWeeklyDAO goalWeeklyDAO;
        public UpdateGoalWeeklyAsyncTask(GoalWeeklyDAO goalWeeklyDAO) {
            this.goalWeeklyDAO = goalWeeklyDAO;
        }

        @Override
        protected Void doInBackground(GoalWeekly... weeklyGoal) {
            goalWeeklyDAO.update(weeklyGoal[0]);
            return  null;
        }
    }

    public  void updateLogbook(Logbook logbook){
        new UpdateLogbookAsyncTask(logbookDAO).execute(logbook);
    }
    private static  class UpdateLogbookAsyncTask extends AsyncTask<Logbook,Void,Void> {
        LogbookDAO logbookDAO;
        public UpdateLogbookAsyncTask(LogbookDAO logbookDAO) {
            this.logbookDAO = logbookDAO;
        }

        @Override
        protected Void doInBackground(Logbook... logbooks) {
            logbookDAO.update(logbooks[0]);
            return  null;
        }
    }

    public  void updateRoute(Route route){
        new UpdateRouteAsyncTask(routeDAO).execute(route);
    }
    private static  class UpdateRouteAsyncTask extends AsyncTask<Route,Void,Void> {
        RouteDAO routeDAO;
        public UpdateRouteAsyncTask(RouteDAO routeDAO) {
            this.routeDAO = routeDAO;
        }

        @Override
        protected Void doInBackground(Route... routes) {
            routeDAO.update(routes[0]);
            return  null;
        }
    }

    public  void updateSession(Session session){
        new UpdateSessionAsyncTask(sessionDAO).execute(session);
    }
    private static  class UpdateSessionAsyncTask extends AsyncTask<Session,Void,Void> {
        SessionDAO sessionDAO;
        public UpdateSessionAsyncTask(SessionDAO sessionDAO) {
            this.sessionDAO = sessionDAO;
        }

        @Override
        protected Void doInBackground(Session... sessions) {
            sessionDAO.update(sessions[0]);
            return  null;
        }
    }

    public  void updateUser(User user){
        new UpdateUserAsyncTask(userDAO).execute(user);
    }
    private static class UpdateUserAsyncTask extends AsyncTask<User,Void,Void> {
        UserDAO userDAO;
        public UpdateUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.update(users[0]);
            return  null;
        }
    }

    //endregion

    //region [Deletes]
    public  void deleteAnnualGoal(GoalAnnual goalAnnual){
        new DeleteGoalAnnualAsyncTask(goalAnnualDAO).execute(goalAnnual);
    }
    private static  class DeleteGoalAnnualAsyncTask extends AsyncTask<GoalAnnual,Void,Void> {
        GoalAnnualDAO anGoalDao;
        public DeleteGoalAnnualAsyncTask(GoalAnnualDAO goalAnnualDAO) {
            this.anGoalDao = goalAnnualDAO;
        }

        @Override
        protected Void doInBackground(GoalAnnual... annuals) {
            anGoalDao.delete(annuals[0]);
            return  null;
        }
    }

    public  void deleteSeasonalGoal(GoalSeasonal goalSeasonal){
        new DeleteGoalSeasonalAsyncTask(goalSeasonalDAO).execute(goalSeasonal);
    }
    private static  class DeleteGoalSeasonalAsyncTask extends AsyncTask<GoalSeasonal,Void,Void> {
        GoalSeasonalDAO seasonGoalDao;
        public DeleteGoalSeasonalAsyncTask(GoalSeasonalDAO goalSeasonalDAO) {
            this.seasonGoalDao = goalSeasonalDAO;
        }

        @Override
        protected Void doInBackground(GoalSeasonal... annuals) {
            seasonGoalDao.delete(annuals[0]);
            return  null;
        }
    }

    public  void deleteWeeklyGoal(GoalWeekly goalWeekly){
        new DeleteGoalWeeklyAsyncTask(goalWeeklyDAO).execute(goalWeekly);
    }
    private static  class DeleteGoalWeeklyAsyncTask extends AsyncTask<GoalWeekly,Void,Void> {
        GoalWeeklyDAO goalWeeklyDAO;
        public DeleteGoalWeeklyAsyncTask(GoalWeeklyDAO goalWeeklyDAO) {
            this.goalWeeklyDAO = goalWeeklyDAO;
        }

        @Override
        protected Void doInBackground(GoalWeekly... weeklyGoal) {
            goalWeeklyDAO.delete(weeklyGoal[0]);
            return  null;
        }
    }

    public  void deleteLogbook(Logbook logbook){
        new DeleteLogbookAsyncTask(logbookDAO).execute(logbook);
    }
    private static  class DeleteLogbookAsyncTask extends AsyncTask<Logbook,Void,Void> {
        LogbookDAO logbookDAO;
        public DeleteLogbookAsyncTask(LogbookDAO logbookDAO) {
            this.logbookDAO = logbookDAO;
        }

        @Override
        protected Void doInBackground(Logbook... logbooks) {
            logbookDAO.delete(logbooks[0]);
            return  null;
        }
    }

    public  void deleteRoute(Route route){
        new DeleteRouteAsyncTask(routeDAO).execute(route);
    }
    private static  class DeleteRouteAsyncTask extends AsyncTask<Route,Void,Void> {
        RouteDAO routeDAO;
        public DeleteRouteAsyncTask(RouteDAO routeDAO) {
            this.routeDAO = routeDAO;
        }

        @Override
        protected Void doInBackground(Route... routes) {
            routeDAO.delete(routes[0]);
            return  null;
        }
    }

    public  void deleteSession(Session session){
        new DeleteSessionAsyncTask(sessionDAO).execute(session);
    }
    private static  class DeleteSessionAsyncTask extends AsyncTask<Session,Void,Void> {
        SessionDAO sessionDAO;
        public DeleteSessionAsyncTask(SessionDAO sessionDAO) {
            this.sessionDAO = sessionDAO;
        }

        @Override
        protected Void doInBackground(Session... sessions) {
            sessionDAO.delete(sessions[0]);
            return  null;
        }
    }

    public  void deleteUser(User user){
        new DeleteUserAsyncTask(userDAO).execute(user);
    }
    private static class DeleteUserAsyncTask extends AsyncTask<User,Void,Void> {
        UserDAO userDAO;
        public DeleteUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.delete(users[0]);
            return  null;
        }
    }
    //endregion

    //region [Users Get]
    public LiveData<List<User>> getAllUsers() {
        LiveData<List<User>> users = userDAO.getAllUsers();
        List<User> lit = users.getValue();
        return users;
    }
    public LiveData<User> getUserById(long id) {
        return userDAO.getUserById(id);
    }
    public LiveData<User> getUserByName(String name) {
         return userDAO.getUserByName(name);
    }

    //endregion
    //region [GoalWeekly Get]
    public LiveData<GoalWeekly>getMostRecentGoalWeekly(long userId){
        return goalWeeklyDAO.getMostRecentWeeklyGoalForUser(userId);
    }

    public void closeGoalSetWasWeeklyGoalMet(GoalWeekly goalWeekly){
        new CloseGoalSetWasWeeklyGoalMetAsyncTask(routeDAO,goalWeeklyDAO).execute(goalWeekly);
    }
    private static class CloseGoalSetWasWeeklyGoalMetAsyncTask extends AsyncTask<GoalWeekly,Void,Void>{

        private  GoalWeeklyDAO goalWeeklyDAO;
        private RouteDAO routeDAO;
        public CloseGoalSetWasWeeklyGoalMetAsyncTask(RouteDAO routeDAO, GoalWeeklyDAO goalWeeklyDAO) {
            this.routeDAO = routeDAO;
            this.goalWeeklyDAO = goalWeeklyDAO;
        }

        @Override
        protected Void doInBackground(GoalWeekly... goalWeeklies) {
            Boolean wasComplete = true;
            GoalWeekly gw = goalWeeklies[0];
            List<Route> routesInPeriod = routeDAO.getAllRoutesForUserInPeriod(gw.getUserId(),gw.getDateCreated(),gw.getDateExpires());
            List<Route> sport = new ArrayList<Route>();
            int totalSportScore = 0;
            int highestSport = 0;

            //===========Get Values
            List<Route> boulder = new ArrayList<Route>();
            int totalBoulderScore = 0;
            int highestBoulder = 0;

            for (Route r:routesInPeriod) {
                if (r.getRouteType() == RouteType.BOULDER){
                    boulder.add(r);
                    totalBoulderScore += r.getGradeValue();
                    if (r.getGradeValue() > highestBoulder){
                        highestBoulder = r.getGradeValue();
                    }
                }
                if (r.getRouteType() == RouteType.SPORT){
                    sport.add(r);
                    totalSportScore = r.getGradeValue();
                    if (r.getGradeValue() > highestSport){
                        highestSport = r.getGradeValue();
                    }
                }
            }
            //==========Check Values
            if (sport.size() < gw.getNumberOfSport())
                wasComplete = false;

            if (boulder.size() < gw.getNumberOfBoulder())
                wasComplete =false;

            double avgBoulder = (double) totalBoulderScore / boulder.size();
            if (avgBoulder < gw.getAverageBoulderGrade().getValue())
                wasComplete = false;

            double avgSport = (double) totalSportScore / sport.size();
            if (avgSport < gw.getAverageSportGrade().getValue())
                wasComplete = false;

            //Update Current Goal
            gw.setGoalAchieved(wasComplete);
            goalWeeklyDAO.update(gw);
            return null;
        }
    }
    //endregion

    //region [GoalSeasonal Get]
    public LiveData<GoalSeasonal> getMostRecentGoalSeasonal(long userId){
        return goalSeasonalDAO.getMostRecentSeasonalGoalForUser(userId);
    }
    //region
    //region [GoalAnnual Get]
    public LiveData<GoalAnnual> getMostRecentGoalAnnual(long userId){
        return goalAnnualDAO.getMostRecentAnnualGoalForUser(userId);
    }
    //endregion
}
