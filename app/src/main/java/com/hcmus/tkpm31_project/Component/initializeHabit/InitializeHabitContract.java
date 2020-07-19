package com.hcmus.tkpm31_project.Component.initializeHabit;

import java.io.File;
import java.util.Calendar;

public interface InitializeHabitContract {
    interface View{

    }
    interface Presenter{
        void handleInsertHabit(String habitName, Calendar startingDate, int daysTraining, File thumbnail,int[] dayOfWeek,Calendar timeStart,Calendar timeEnd,String Description);
    }
}
