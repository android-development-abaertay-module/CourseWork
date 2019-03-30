package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.helper.GoalCheckDTO;

import java.time.OffsetDateTime;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "GoalSeasonal",foreignKeys = @ForeignKey(entity = User.class,parentColumns = "id",childColumns = "userId", onDelete = CASCADE))
public class GoalSeasonal extends Goal {
    //-------------------------------------------------------------------------------------Attributes----------------------------------------------------
    @ColumnInfo(name = "highestBoulderOnsight")
    private Grades _highestBoulderOnsight;
    @ColumnInfo(name = "highestSportOnsight")
    private Grades _highestSportOnsight;
    @ColumnInfo(name = "highestBoulderWorked")
    private Grades _highestBoulderWorked;
    @ColumnInfo(name = "highestSportWorked")
    private Grades _highestSportWorked;

    public Grades get_highestBoulderOnsight() {
        return _highestBoulderOnsight;
    }
    public void set_highestBoulderOnsight(Grades _highestBoulderOnsight) {
        this._highestBoulderOnsight = _highestBoulderOnsight;
    }

    public Grades get_highestSportOnsight() {
        return _highestSportOnsight;
    }
    public void set_highestSportOnsight(Grades _highestSportOnsight) {
        this._highestSportOnsight = _highestSportOnsight;
    }

    public Grades get_highestBoulderWorked() {
        return _highestBoulderWorked;
    }
    public void set_highestBoulderWorked(Grades _highestBoulderWorked) {
        this._highestBoulderWorked = _highestBoulderWorked;
    }

    public Grades get_highestSportWorked() {
        return _highestSportWorked;
    }
    public void set_highestSportWorked(Grades _highestSportWorked) {
        this._highestSportWorked = _highestSportWorked;
    }

//------------------------------------------------------------------------------------Constructor----------------------------------------------------
public GoalSeasonal(long iDUserFK, Grades highestBoulderOnsight, Grades highestSportOnsight, Grades highestBoulderWorked, Grades highestSportWorked, OffsetDateTime dateCreated)//6 Months
    {
        super(iDUserFK);
        this.goalDuration = 6;// 6 Months

        dateExpires = dateCreated.plusMonths(goalDuration);
        goalAchieved = false;
        this.dateCreated = dateCreated;

        _highestBoulderOnsight = highestBoulderOnsight;
        _highestSportOnsight = highestSportOnsight;

        _highestBoulderWorked = highestBoulderWorked;
        _highestSportWorked = highestSportWorked;
    }

    public GoalSeasonal() {
    }

    //region[Methods]
    public GoalCheckDTO checkHighestSportOnsightGoal(Grades highestSportOSVal){
        GoalCheckDTO result = new GoalCheckDTO();
        if (highestSportOSVal != null){
            result.setOutput(highestSportOSVal.toString() + " : " + _highestSportOnsight.toString());
            if (highestSportOSVal.getValue() > _highestSportOnsight.getValue())
                result.setIsAchieved(true);
            else
                result.setIsAchieved(false);
        }else {
            //no routes in period
            result.setOutput("No Sport Routes Completed: Onsight");
            result.setIsAchieved(false);
        }
        return result;
    }
    public GoalCheckDTO checkHighestBoulderOnsightGoal(Grades highestBoulderOSVal){
        GoalCheckDTO result = new GoalCheckDTO();
        if (highestBoulderOSVal != null){
            result.setOutput(highestBoulderOSVal.toString() + " : " + _highestBoulderOnsight.toString());
            if (highestBoulderOSVal.getValue() >  _highestBoulderOnsight.getValue())
                result.setIsAchieved(true);
            else
                result.setIsAchieved(false);
        }else{
            //no routes done in period
            result.setOutput("No Boulder Routes Completed: Onsight");
            result.setIsAchieved(false);
        }
        return result;
    }
    public GoalCheckDTO checkHighestSportWorkedGoal(Grades highestSportWorkedVal){
        GoalCheckDTO result = new GoalCheckDTO();
        if (highestSportWorkedVal != null){
            result.setOutput(highestSportWorkedVal.toString() + " : " + _highestSportWorked.toString());

            if (highestSportWorkedVal.getValue() > _highestSportWorked.getValue())
                result.setIsAchieved(true);
            else
                result.setIsAchieved(false);
        }else{
            //no routes logged in period yet
            result.setOutput("No Sport Routes Completed: Worked");
            result.setIsAchieved(false);
        }
        return result;
    }
    public GoalCheckDTO checkHighestBoulderWorkedGoal(Grades highestBoulderWorkedVal){
        GoalCheckDTO result = new GoalCheckDTO();
        if (highestBoulderWorkedVal != null){
            result.setOutput(highestBoulderWorkedVal.toString() + " : " + _highestBoulderWorked.toString());

            if (highestBoulderWorkedVal.getValue() > _highestBoulderWorked.getValue())
                result.setIsAchieved(true);
            else
                result.setIsAchieved(false);
        }else{
            //no routes logged in period
            result.setOutput("No Boulder Routes Completed: Worked");
            result.setIsAchieved(false);
        }
        return result;
    }
    //endregion
}
