package com.hcmus.tkpm31_project.Receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;


import com.hcmus.tkpm31_project.Util.AlarmHelper;
import com.hcmus.tkpm31_project.Util.NotificationHelper;

import java.util.Calendar;

public class AlarmReceiver  extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar endingDate = Calendar.getInstance();
        endingDate.setTimeInMillis(intent.getLongExtra("endingDate",0));
        Calendar timeStart = Calendar.getInstance();
        timeStart.setTimeInMillis(intent.getLongExtra("timeStart",0));
        timeStart.add(Calendar.DATE,7);
        long reqCode = intent.getLongExtra("reqCode",0);
        String habitName = intent.getStringExtra("habitName");
        long habitID = intent.getLongExtra("habitID",0);

        NotificationHelper notificationHelper = new NotificationHelper(context,habitID);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(habitName);
        Notification notification = nb.build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        nb.setDefaults(notification.defaults);
        notificationHelper.getManager().notify(0,notification);


        if(timeStart.before(endingDate)){
            AlarmHelper helper = AlarmHelper.getInstanceAlarmHelper();
            helper.scheduleAlarm(context,timeStart,endingDate,timeStart.get(Calendar.DAY_OF_WEEK),reqCode,habitName,habitID);
        }else{

        }
    }
}
