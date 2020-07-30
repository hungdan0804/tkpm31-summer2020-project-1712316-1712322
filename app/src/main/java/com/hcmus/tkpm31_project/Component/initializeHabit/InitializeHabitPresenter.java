package com.hcmus.tkpm31_project.Component.initializeHabit;

import android.content.Context;

import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class InitializeHabitPresenter implements InitializeHabitContract.Presenter {

    private InitializeHabitContract.View activity;

    public void setView(InitializeHabitContract.View view){this.activity = view;}

    @Override
    public void handleInsertHabit(Context context, String habitName, String type, Calendar startingDate, String daysTraining, String thumbnail, List<Boolean> dayOfWeek, Calendar timeStart, Calendar timeEnd, String Description) {

        int dayTr=0;
        int start_hour = timeStart.get(Calendar.HOUR);
        int start_minute = timeStart.get(Calendar.MINUTE);
        int end_hour = timeEnd.get(Calendar.HOUR);
        int end_minute = timeEnd.get(Calendar.MINUTE);
        String start = String.format("%d:%d",start_hour,start_minute);
        String end = String.format("%d:%d",end_hour,end_minute);
        if(!habitName.isEmpty() && !daysTraining.isEmpty() && !start.equals(end)){
            dayTr = Integer.parseInt(daysTraining);
            final Habit newHabit = new Habit(habitName,type,startingDate.getTime(),dayTr,start,end,thumbnail,Description);
            final DatabaseHelper databaseHelper = DatabaseHelper.getINSTANCE(context);
            new Thread(new Runnable() {
                    @Override
                    public void run() {
                        databaseHelper.habitDAO().insertOnlySingleHabit(newHabit);
                    }}).start();
            activity.insertSuccess();
        }else{
            activity.insertFailure(1);
        }
    }
}
