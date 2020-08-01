package com.hcmus.tkpm31_project.Component.initializeHabit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Receiver.AlarmReceiver;
import com.hcmus.tkpm31_project.Util.AlarmHelper;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class InitializeHabitPresenter implements InitializeHabitContract.Presenter {

    private InitializeHabitContract.View activity;


    public void setView(InitializeHabitContract.View view){this.activity = view;}

    @Override
    public void handleInsertHabit(final Context context, String habitName, String type, Calendar startingDate, String daysTraining, String thumbnail, List<Boolean> dayOfWeek, final Calendar timeStart, final Calendar timeEnd, String Description) {

        int dayTr=0;
        int start_hour = timeStart.get(Calendar.HOUR);
        int start_minute = timeStart.get(Calendar.MINUTE);
        int start_second = timeStart.get(Calendar.SECOND);
        int end_hour = timeEnd.get(Calendar.HOUR);
        int end_minute = timeEnd.get(Calendar.MINUTE);
        String start = String.format("%d:%d",start_hour,start_minute);
        String end = String.format("%d:%d",end_hour,end_minute);
        if(!habitName.isEmpty() && !daysTraining.isEmpty() && !start.equals(end)){
            dayTr = Integer.parseInt(daysTraining);
            final Calendar endingDate = Calendar.getInstance();
            endingDate.setTimeInMillis(startingDate.getTimeInMillis());
            endingDate.add(Calendar.DATE,dayTr);
            final Habit newHabit = new Habit(habitName,type,startingDate.getTime(),endingDate.getTime(),dayTr,start,end,thumbnail,Description);
            final DatabaseHelper databaseHelper = DatabaseHelper.getINSTANCE(context);
            new Thread(new Runnable() {
                    @Override
                    public void run() {
                        databaseHelper.habitDAO().insertOnlySingleHabit(newHabit);
                        if (timeStart.before(Calendar.getInstance())) {
                            timeStart.add(Calendar.DATE, 1);
                        }
                        AlarmHelper helper= AlarmHelper.getInstanceAlarmHelper();
                        helper.scheduleAlarm(context,timeStart,endingDate,2);
                    }}).start();
            activity.insertSuccess();
        }else{
            activity.insertFailure(1);
        }
    }

}
