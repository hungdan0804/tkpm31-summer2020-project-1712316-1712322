package com.hcmus.tkpm31_project.Util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hcmus.tkpm31_project.Receiver.AlarmReceiver;

import java.util.Calendar;

public class AlarmHelper {
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private Intent intent;
    private static AlarmHelper INSTANCE = null;

    public static AlarmHelper getInstanceAlarmHelper(){
        if(INSTANCE == null){
            INSTANCE = new AlarmHelper();
        }
        return INSTANCE;
    }


    public void initVariable(Context context,Calendar endingDate,Calendar timeStart,long reqCode,String habitName,long habitID){

        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("endingDate",endingDate.getTimeInMillis());
        intent.putExtra("timeStart",timeStart.getTimeInMillis());
        intent.putExtra("reqCode",reqCode);
        intent.putExtra("habitName",habitName);
        intent.putExtra("habitID",habitID);
        pendingIntent = PendingIntent.getBroadcast(context,(int)reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void scheduleAlarm(Context context, Calendar timeStart,Calendar endingDate,int dayofweek, long reqCode,String habitName,long habitID){
        initVariable(context,endingDate,timeStart,reqCode,habitName,habitID);
        timeStart.setFirstDayOfWeek(dayofweek);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,timeStart.getTimeInMillis(),pendingIntent);
    }
}
