package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import com.example.coursework.model.enums.Grades;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "GoalAnnual",foreignKeys = @ForeignKey(entity = User.class,parentColumns = "id",childColumns = "userId", onDelete = CASCADE))
public class GoalAnnual extends Goal{
    //------------------------------------------Attributes-------------------------
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

    //--------Constructor
    public GoalAnnual(long iDUserFK, Grades highestBoulderOnsight, Grades highestSportOnsight, Grades highestBoulderWorked, Grades highestSportWorked, OffsetDateTime dateCreated)
    {
            super(iDUserFK);
            this.goalDuration = 1;//1 Year
            dateExpires = dateCreated.plusYears(goalDuration);
            goalAchieved = false;
            this.dateCreated = dateCreated;

            this.highestBoulderOnsight = highestBoulderOnsight;
            this.highestSportOnsight = highestSportOnsight;

            this.highestBoulderWorked = highestBoulderWorked;
            this.highestSportWorked = highestSportWorked;
    }

    public GoalAnnual()
    {
    }

}
