package com.hcmus.tkpm31_project.Receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;


import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.Reminder;
import com.hcmus.tkpm31_project.ObjectRelationship.HabitWithReminder;
import com.hcmus.tkpm31_project.Util.AlarmHelper;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;
import com.hcmus.tkpm31_project.Util.NotificationHelper;

import java.util.Calendar;
import java.util.List;

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

        if(timeStart.before(endingDate)){
            AlarmHelper helper = AlarmHelper.getInstanceAlarmHelper();
            helper.scheduleAlarm(context,timeStart,endingDate,timeStart.get(Calendar.DAY_OF_WEEK),reqCode,habitName,habitID);
        }
        Calendar now = Calendar.getInstance();
        if(now.after(endingDate)){
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification2(habitName);
            Notification notification = nb.build();
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            nb.setDefaults(notification.defaults);
            notificationHelper.getManager().notify(10000,notification);

            DatabaseHelper databaseHelper = DatabaseHelper.getINSTANCE(context);

            Habit deletedHabit = databaseHelper.habitDAO().fetchOneHabitbyHabitId((int)habitID);
            HabitWithReminder habitWithReminder = databaseHelper.habitDAO().loadHabitsWithReminders((int)habitID);
            List<Reminder> reminders = habitWithReminder.reminders;
            //cancel all alarm service
            Intent intent2 = new Intent(context, AlarmReceiver.class);

            for(Reminder r: reminders){
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)r.get_reminderID(), intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
            deletedHabit.setDelete(true);
            databaseHelper.habitDAO().updateHabit(deletedHabit);
        }


        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(habitName);
        Notification notification = nb.build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        nb.setDefaults(notification.defaults);
        notificationHelper.getManager().notify(0,notification);

    }
}
