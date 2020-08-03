package com.hcmus.tkpm31_project.Component.initializeHabit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.Reminder;
import com.hcmus.tkpm31_project.Object.TrainingDays;
import com.hcmus.tkpm31_project.ObjectRelationship.HabitWithReminder;
import com.hcmus.tkpm31_project.Receiver.AlarmReceiver;
import com.hcmus.tkpm31_project.Util.AlarmHelper;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InitializeHabitPresenter implements InitializeHabitContract.Presenter {

    private InitializeHabitContract.View activity;


    public void setView(InitializeHabitContract.View view){this.activity = view;}

    @Override
    public void handleInsertHabit(final Context context, final String habitName, String type, Calendar startingDate, String daysTraining, String thumbnail, final List<Boolean> dayOfWeek, final Calendar timeStart, final Calendar timeEnd, String Description) {

        int dayTr=0;
        int start_hour = timeStart.get(Calendar.HOUR_OF_DAY);
        int start_minute = timeStart.get(Calendar.MINUTE);
        int end_hour = timeEnd.get(Calendar.HOUR_OF_DAY);
        int end_minute = timeEnd.get(Calendar.MINUTE);
        final String start = String.format("%d:%d",start_hour,start_minute);
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
                        long rowID = databaseHelper.habitDAO().insertOnlySingleHabit(newHabit);
                        List<Integer> dayofweek = generateToInt(dayOfWeek);
                        AlarmHelper helper= AlarmHelper.getInstanceAlarmHelper();
                        for(int i:dayofweek){
                            Calendar temp = Calendar.getInstance();
                            temp.setTimeInMillis(timeStart.getTimeInMillis());
                            long reqCode = databaseHelper.reminderDAO().insertOnlySingleReminder(new Reminder(rowID,i));
                            helper.scheduleAlarm(context,temp,endingDate,i,reqCode,habitName,rowID);
                        }
                        if (timeStart.before(Calendar.getInstance())) {
                            timeStart.add(Calendar.DATE, 1);
                        }

                    }

                private List<Integer> generateToInt(List<Boolean> dayOfWeek) {
                        List<Integer> res = new ArrayList<>();
                        for(int i=0;i<dayOfWeek.size();i++){
                            switch (i){
                                case 0: res.add(Calendar.MONDAY);break;
                                case 1: res.add(Calendar.TUESDAY);break;
                                case 2: res.add(Calendar.WEDNESDAY);break;
                                case 3: res.add(Calendar.THURSDAY);break;
                                case 4: res.add(Calendar.FRIDAY);break;
                                case 5: res.add(Calendar.SATURDAY);break;
                                case 6: res.add(Calendar.SUNDAY);break;
                            }
                        }
                        return res;
                }
            }).start();
            activity.insertSuccess();
        }else{
            activity.insertFailure(1);
        }
    }

}
