package com.example.coursework.model.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.coursework.model.GoalAnnual;
import com.example.coursework.model.GoalSeasonal;
import com.example.coursework.model.GoalWeekly;
import com.example.coursework.model.Route;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.RouteType;
import com.example.coursework.model.enums.StyleDone;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class DaoRepository {
    private GoalAnnualDAO goalAnnualDAO;
    private GoalSeasonalDAO goalSeasonalDAO;
    private GoalWeeklyDAO goalWeeklyDAO;
    private RouteDAO routeDAO;
    private SessionDAO sessionDAO;
    private UserDAO userDAO;

    public DaoRepository(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        goalAnnualDAO = appDatabase.goalAnnualDAO();
        goalSeasonalDAO = appDatabase.goalSeasonalDAO();
        goalWeeklyDAO = appDatabase.goalWeeklyDAO();
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

    public  long insertUser(User user){
        InsertUserAsyncTask task = new InsertUserAsyncTask(userDAO);
         task.execute(user);
         return task.newUserId;
    }
    private static class InsertUserAsyncTask extends AsyncTask<User,Void,Long> {
        UserDAO userDAO;
        long newUserId;
        public InsertUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Long doInBackground(User... users) {
            Log.d("gwyd",users[0].getId() + " - no user id generated at this stage");
            newUserId = userDAO.insert(users[0]);
            Log.d("gwyd",newUserId + " - user insert ran, we have this id");

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
           int numRowsUpdated = sessionDAO.update(sessions[0]);
           Log.d("gwyd","number of rows updated " + numRowsUpdated);
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

    public void closeGoalSetWasSeasonalGoalMet(GoalSeasonal seasonalGoal) {
        new CloseGoalSetWasSeasonalGoalMetAsyncTask(routeDAO,goalSeasonalDAO).execute(seasonalGoal);
    }
    private static class CloseGoalSetWasSeasonalGoalMetAsyncTask extends AsyncTask<GoalSeasonal,Void,Void>{
        private GoalSeasonalDAO goalSeasonalDAO;
        private RouteDAO routeDAO;
        public CloseGoalSetWasSeasonalGoalMetAsyncTask(RouteDAO routeDAO,GoalSeasonalDAO goalSeasonalDAO) {
            this.routeDAO = routeDAO;
            this.goalSeasonalDAO = goalSeasonalDAO;
        }

        @Override
        protected Void doInBackground(GoalSeasonal... goalSeasonals) {
            Boolean wasComplete = true;
            //============Get Data
            GoalSeasonal gs = goalSeasonals[0];
            List<Route> routesInPeriod = routeDAO.getAllRoutesForUserInPeriod(gs.getUserId(),gs.getDateCreated(),gs.getDateExpires());

            int highestBoulderOSValue = 0;
            int highestBoulderWorkedValue = 0;
            int highestSportOSValue = 0;
            int highestSportWorkedValue = 0;
            //organize Routes
            for (Route r:routesInPeriod) {
                //Sport Routes
                if (r.getRouteType() == RouteType.SPORT){
                    if (r.getStyleDone() == StyleDone.Onsight){
                        if (r.getGrade().getValue() > highestSportOSValue) {
                            highestSportOSValue = r.getGrade().getValue();
                        }
                    }
                    if(r.getStyleDone() == StyleDone.Worked){
                        if (r.getGrade().getValue() > highestSportWorkedValue){
                            highestSportWorkedValue = r.getGrade().getValue();
                        }
                    }
                }
                //Boulder Routes
                else if(r.getRouteType() == RouteType.BOULDER){
                    if (r.getStyleDone() == StyleDone.Onsight){
                        if (r.getGrade().getValue() > highestBoulderOSValue){
                            highestBoulderOSValue = r.getGrade().getValue();
                        }
                    }
                    if(r.getStyleDone() == StyleDone.Worked){
                        if (r.getGrade().getValue() >highestBoulderWorkedValue){
                            highestBoulderWorkedValue = r.getGrade().getValue();
                        }
                    }
                }
            }
            //check goal progress
            if (highestBoulderOSValue < gs.getHighestBoulderOnsight().getValue()) {
                wasComplete = false;
                Log.d("gwyd","highest Boulder OS Goal Was NOT met");
            }
            if (highestBoulderWorkedValue < gs.getHighestBoulderWorked().getValue()) {
                wasComplete = false;
                Log.d("gwyd","highest Boulder Worked Goal Was NOT met");

            }
            if (highestSportOSValue < gs.getHighestSportOnsight().getValue()) {
                wasComplete = false;
                Log.d("gwyd","highest Sport OS Goal Was NOT met");
            }
            if (highestSportWorkedValue < gs.getHighestSportWorked().getValue()) {
                wasComplete = false;
                Log.d("gwyd","highest Sport Worked Goal Was NOT met");
            }
            //Update Current Goal
            gs.setGoalAchieved(wasComplete);
            goalSeasonalDAO.update(gs);
            return null;
        }
    }
    //endregion

    //region [GoalAnnual Get]
    public LiveData<GoalAnnual> getMostRecentGoalAnnual(long userId){
        return goalAnnualDAO.getMostRecentAnnualGoalForUser(userId);
    }

    public void closeGoalSetWasAnnualGoalMet(GoalAnnual goalAnnual) {
        new CloseGoalSetWasAnnualGoalMetAsyncTask(routeDAO,goalAnnualDAO).execute(goalAnnual);
    }
    private static class CloseGoalSetWasAnnualGoalMetAsyncTask extends AsyncTask<GoalAnnual,Void,Void>{
        private GoalAnnualDAO goalAnnualDAO;
        private RouteDAO routeDAO;
        public CloseGoalSetWasAnnualGoalMetAsyncTask(RouteDAO routeDAO,GoalAnnualDAO goalAnnualDAO) {
            this.routeDAO = routeDAO;
            this.goalAnnualDAO = goalAnnualDAO;
        }

        @Override
        protected Void doInBackground(GoalAnnual... goalAnnuals) {
            Boolean wasComplete = true;
            //============Get Data
            GoalAnnual ga = goalAnnuals[0];
            List<Route> routesInPeriod = routeDAO.getAllRoutesForUserInPeriod(ga.getUserId(),ga.getDateCreated(),ga.getDateExpires());

            int highestBoulderOSValue = 0;
            int highestBoulderWorkedValue = 0;
            int highestSportOSValue = 0;
            int highestSportWorkedValue = 0;
            //organize Routes
            for (Route r:routesInPeriod) {
                //Sport Routes
                if (r.getRouteType() == RouteType.SPORT){
                    if (r.getStyleDone() == StyleDone.Onsight){
                        if (r.getGrade().getValue() > highestSportOSValue) {
                            highestSportOSValue = r.getGrade().getValue();
                        }
                    }
                    if(r.getStyleDone() == StyleDone.Worked){
                        if (r.getGrade().getValue() > highestSportWorkedValue){
                            highestSportWorkedValue = r.getGrade().getValue();
                        }
                    }
                }
                //Boulder Routes
                else if(r.getRouteType() == RouteType.BOULDER){
                    if (r.getStyleDone() == StyleDone.Onsight){
                        if (r.getGrade().getValue() > highestBoulderOSValue){
                            highestBoulderOSValue = r.getGrade().getValue();
                        }
                    }
                    if(r.getStyleDone() == StyleDone.Worked){
                        if (r.getGrade().getValue() >highestBoulderWorkedValue){
                            highestBoulderWorkedValue = r.getGrade().getValue();
                        }
                    }
                }
            }
            //check goal progress
            if (highestBoulderOSValue < ga.getHighestBoulderOnsight().getValue()) {
                wasComplete = false;
                Log.d("gwyd","highest Boulder OS Goal Was NOT met");
            }
            if (highestBoulderWorkedValue < ga.getHighestBoulderWorked().getValue()) {
                wasComplete = false;
                Log.d("gwyd","highest Boulder Worked Goal Was NOT met");

            }
            if (highestSportOSValue < ga.getHighestSportOnsight().getValue()) {
                wasComplete = false;
                Log.d("gwyd","highest Sport OS Goal Was NOT met");
            }
            if (highestSportWorkedValue < ga.getHighestSportWorked().getValue()) {
                wasComplete = false;
                Log.d("gwyd","highest Sport Worked Goal Was NOT met");
            }
//Update Current Goal
            ga.setGoalAchieved(wasComplete);
            goalAnnualDAO.update(ga);
            return null;
        }
    }
    //endregion

    //region [Routes Get]
    public LiveData<List<Route>> getRecentRoutesForUser(int numberOfRoutes, long userId) {
        return routeDAO.getRecentRoutesForUser(numberOfRoutes, userId);
    }
    public LiveData<Integer> getNumberRoutesInPeriod(long userId, OffsetDateTime periodStart, OffsetDateTime periodEnd, RouteType routeType) {
        return routeDAO.getNumberRoutesInPeriod(userId, periodStart,periodEnd,routeType);
    }

    public LiveData<Grades> getAvgGradeRouteInPeriod(long userId, OffsetDateTime periodStart, OffsetDateTime periodEnd, RouteType routeType) {
        return routeDAO.getAvgGradeRouteInPeriod(userId, periodStart,periodEnd,routeType);
    }
    public LiveData<Grades> getHighestRouteInPeriod(long userId, OffsetDateTime periodStart, OffsetDateTime periodEnd, RouteType routeType, StyleDone styleDone){
        return routeDAO.getHighestRouteInPeriod(userId,periodStart,periodEnd,routeType, styleDone);
    }
    public LiveData<Grades> noGoalSetReturnNull(){
        return routeDAO.noGoalSetReturnNull();
    }
    public LiveData<Integer> noGoalSetReturnZeroCount(){
        return routeDAO.noGoalSetReturnZeroCount();
    }
    //endregion

    //region [Session Get]
    public LiveData<List<Session>> getRecentSessionsForUser(int numberOfSessions, long userId) {
        return sessionDAO.getRecentSessionsForUser(numberOfSessions,userId);
    }
    public LiveData<List<Session>> getRecentSessionsWithLocationForUser(int numberOfSessions, long userId) {
        return sessionDAO.getRecentSessionsWithLocationForUser(numberOfSessions,userId);
    }
    public LiveData<Session> getCurrentSession(long userId) {
        return sessionDAO.getCurrentSessionForUser(userId);
    }


    //endregion
}
