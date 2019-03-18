package com.example.coursework.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import com.example.coursework.model.enums.Grades;

import java.time.LocalDateTime;
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
}
