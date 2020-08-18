package com.hcmus.tkpm31_project.Component.initializeHabit;

import android.content.Context;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public interface InitializeHabitContract {
    interface View{
        void insertSuccess();
        void insertFailure(int error);
    }
    interface Presenter{
        void handleInsertHabit(Context context,String habitName, String type , Calendar startingDate, float daysTraining, String thumbnail, List<Boolean> dayOfWeek, Calendar timeStart, Calendar timeEnd, String Description);
    }
}
