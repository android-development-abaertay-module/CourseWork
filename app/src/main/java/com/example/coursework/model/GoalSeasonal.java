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
    private Grades highestBoulderOnsight;
    @ColumnInfo(name = "highestSportOnsight")
    private Grades highestSportOnsight;
    @ColumnInfo(name = "highestBoulderWorked")
    private Grades highestBoulderWorked;
    @ColumnInfo(name = "highestSportWorked")
    private Grades highestSportWorked;

    public Grades getHighestBoulderOnsight() {
        return highestBoulderOnsight;
    }
    public void setHighestBoulderOnsight(Grades highestBoulderOnsight) {
        this.highestBoulderOnsight = highestBoulderOnsight;
    }

    public Grades getHighestSportOnsight() {
        return highestSportOnsight;
    }
    public void setHighestSportOnsight(Grades highestSportOnsight) {
        this.highestSportOnsight = highestSportOnsight;
    }

    public Grades getHighestBoulderWorked() {
        return highestBoulderWorked;
    }
    public void setHighestBoulderWorked(Grades highestBoulderWorked) {
        this.highestBoulderWorked = highestBoulderWorked;
    }

    public Grades getHighestSportWorked() {
        return highestSportWorked;
    }
    public void setHighestSportWorked(Grades highestSportWorked) {
        this.highestSportWorked = highestSportWorked;
    }

//------------------------------------------------------------------------------------Constructor----------------------------------------------------
public GoalSeasonal(long iDUserFK, Grades highestBoulderOnsight, Grades highestSportOnsight, Grades highestBoulderWorked, Grades highestSportWorked, OffsetDateTime dateCreated)//6 Months
    {
        super(iDUserFK);
        this.goalDuration = 6;// 6 Months

        dateExpires = dateCreated.plusMonths(goalDuration);
        goalAchieved = false;
        this.dateCreated = dateCreated;

        this.highestBoulderOnsight = highestBoulderOnsight;
        this.highestSportOnsight = highestSportOnsight;

        this.highestBoulderWorked = highestBoulderWorked;
        this.highestSportWorked = highestSportWorked;
    }

    public GoalSeasonal() {
    }

    //region[Methods]
    //determine weather goal has been met by comparing it against the goals targets.
    //works for each goal property
    public GoalCheckDTO checkGoalAvgGradeTypeX(Grades averageGradeForType, Grades averageGoalForType, String errorMessage){
        GoalCheckDTO result = new GoalCheckDTO();

        if (averageGradeForType != null){
            result.setOutput(averageGradeForType.toString() + " : " + averageGoalForType.toString());

            if (averageGradeForType.getValue() >= averageGoalForType.getValue())
                result.setIsAchieved(true);
            else
                result.setIsAchieved(false);
        }else{
            //no routes logged in period
            result.setOutput(errorMessage);
            result.setIsAchieved(false);
        }
        return result;
    }
    //endregion
}
