package com.example.coursework.model;

import com.example.coursework.model.enums.Grades;

import java.time.LocalDateTime;

public class GoalAnnual extends Goal{
        //------------------------------------------Attributes-------------------------

        private Grades _highestBoulderOnsight;

        private Grades _highestSportOnsight;

        private Grades _highestBoulderWorked;

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

        //--------Constructor
        public GoalAnnual(int iDUserFK, Grades highestBoulderOnsight, Grades highestSportOnsight, Grades highestBoulderWorked, Grades highestSportWorked, LocalDateTime dateCreated)
        {
                super(iDUserFK);
                this._goalDruation = 1;//1 Year
                _dateExpires = dateCreated.plusYears(_goalDruation);
                _goalAcheaved = false;
                _dateCreated = dateCreated;

                _highestBoulderOnsight = highestBoulderOnsight;
                _highestSportOnsight = highestSportOnsight;

                _highestBoulderWorked = highestBoulderWorked;
                _highestSportWorked = highestSportWorked;
        }

        public GoalAnnual()
        {
        }

}
