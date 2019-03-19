package com.example.coursework.model.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coursework.model.Route;
import com.example.coursework.model.User;
import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.enums.RouteType;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Dao
public interface RouteDAO {
    @Insert
    long  insert(Route route);
    @Update
    void update(Route route);
    @Delete
    void delete(Route route);

    @Query("SELECT * FROM Route WHERE userId ==:userId and timeDone BETWEEN datetime(:periodStart) AND datetime(:periodEnd)")
    List<Route> getAllRoutesForUserInPeriod(long userId, OffsetDateTime periodStart, OffsetDateTime periodEnd);

    @Query("SELECT * FROM Route WHERE userId ==:userId")
    List<Route> getAllRoutesForUser(long userId);

    @Query("SELECT * FROM Route WHERE userId ==:userId ORDER BY timeDone DESC")
    List<Route> getAllRoutesForUserOrderASC(long userId);

    @Query("SELECT * FROM Route WHERE userId ==:userId ORDER BY timeDone DESC LIMIT :numberOfRoutes")
    LiveData<List<Route>> getRecentRoutesForUser(int numberOfRoutes, long userId);

    @Query("SELECT count(id) FROM Route WHERE userId ==:userId " +
            "and timeDone BETWEEN datetime(:periodStart) AND datetime(:periodEnd) " +
            "AND routeType ==:routeType")
    LiveData<Integer> getNumberRoutesInPeriod(long userId, OffsetDateTime periodStart, OffsetDateTime periodEnd, RouteType routeType);

    @Query("SELECT avg(grade) FROM Route WHERE userId ==:userId " +
            "and timeDone BETWEEN datetime(:periodStart) AND datetime(:periodEnd) " +
            "AND routeType ==:routeType")
    LiveData<Grades> getAvgGradeRouteInPeriod(long userId, OffsetDateTime periodStart, OffsetDateTime periodEnd, RouteType routeType);

    @Query("SELECT null FROM Route")
    LiveData<Grades> noGoalSetReturnNull();

    @Query("SELECT 0 FROM Route")
    LiveData<Integer> noGoalSetReturnZeroCount();

    @Query("SELECT MAX(grade) FROM Route WHERE userId ==:userId " +
            "and timeDone BETWEEN datetime(:periodStart) AND datetime(:periodEnd) " +
            "AND routeType ==:routeType")
    LiveData<Grades> getHighestRouteInPeriod(long userId, OffsetDateTime periodStart, OffsetDateTime periodEnd, RouteType routeType);
}
