package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import com.example.coursework.model.enums.Grades;
import com.example.coursework.model.helper.GoalCheckDTO;

import java.time.OffsetDateTime;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "GoalWeekly",foreignKeys = @ForeignKey(entity = User.class,parentColumns = "id",childColumns = "userId", onDelete = CASCADE))
public class GoalWeekly extends Goal {
//---------------------------------------------------------------------Attributes----------------------------------------------------
    @ColumnInfo(name = "numberOfSport")
    private int numberOfSport;
    @ColumnInfo(name = "numberOfBoulder")
    private int numberOfBoulder;
    @ColumnInfo(name = "averageSportGrade")
    private Grades averageSportGrade;
    @ColumnInfo(name = "sportGoalValue")
    private int sportGoalValue;
    @ColumnInfo(name = "averageBoulderGrade")
    private Grades averageBoulderGrade;
    @ColumnInfo(name = "boulderGoalValue")
    private int boulderGoalValue;

    public int getNumberOfSport() {
        return numberOfSport;
    }
    public void setNumberOfSport(int numberOfSport) {
        this.numberOfSport = numberOfSport;
    }

    public int getNumberOfBoulder() {
        return numberOfBoulder;
    }
    public void setNumberOfBoulder(int numberOfBoulder) {
        this.numberOfBoulder = numberOfBoulder;
    }

    public Grades getAverageSportGrade() {
        return averageSportGrade;
    }
    public void setAverageSportGrade(Grades averageSportGrade) {
        this.averageSportGrade = averageSportGrade;
    }

    public int getSportGoalValue() {
        return sportGoalValue;
    }
    public void setSportGoalValue(int sportGoalValue) {
        this.sportGoalValue = sportGoalValue;
    }

    public Grades getAverageBoulderGrade() {
        return averageBoulderGrade;
    }
    public void setAverageBoulderGrade(Grades averageBoulderGrade) {
        this.averageBoulderGrade = averageBoulderGrade;
    }

    public int getBoulderGoalValue() {
        return boulderGoalValue;
    }
    public void setBoulderGoalValue(int boulderGoalValue) {
        this.boulderGoalValue = boulderGoalValue;
    }

    //--------------------------------------------------------------------------Constructor----------------------------------------------------
    public GoalWeekly(long iDUserFK, int numberOfSport, int numberOfBoulder, Grades averageSportGrade, Grades averageBoulderGrade, OffsetDateTime dateCreated)
        {
            super(iDUserFK);
            goalDuration = 7;//7 Days
            dateExpires = dateCreated.plusDays(goalDuration);
            goalAchieved = false;
            this.dateCreated = dateCreated;

            this.numberOfSport = numberOfSport;
            this.numberOfBoulder = numberOfBoulder;
            this.averageSportGrade = averageSportGrade;
            this.sportGoalValue = this.averageSportGrade.getValue();
            this.averageBoulderGrade = averageBoulderGrade;
            this.boulderGoalValue = this.averageBoulderGrade.getValue();
        }
    public GoalWeekly()
        {
            super();
        }

//-----------------------------------------------------------------------------Methods-----------------------------------------------------
    public int checkNumberOfRoutesForTypeGoalPercentage(int numRoutesTypeXAchievedVal, int targetNumberOfRoutesTypeX){
        int result;
        if (numRoutesTypeXAchievedVal >= targetNumberOfRoutesTypeX) {
            result = 100;
        }else{
            float fraction = (float) numRoutesTypeXAchievedVal / targetNumberOfRoutesTypeX;
            result = (int) Math.floor(fraction * 100);
        }
        return result;
    }

    public GoalCheckDTO checkAverageGradeForRouteTypeXGoal(Grades averageGradeForType, Grades averageGoalForType, String noRoutesMessage){
        GoalCheckDTO result = new GoalCheckDTO();

        if (averageGradeForType != null){
            result.setOutput(averageGradeForType.toString() + " : " + averageGoalForType.toString());

            if (averageGradeForType.getValue() > averageGoalForType.getValue())
                result.setIsAchieved(true);
            else
                result.setIsAchieved(false);
        }else{
            //no routes logged in period
            result.setOutput(noRoutesMessage);
            result.setIsAchieved(false);
        }
        return result;
    }
}