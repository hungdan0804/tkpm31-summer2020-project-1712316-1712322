package com.hcmus.tkpm31_project.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hcmus.tkpm31_project.Util.AlarmHelper;
import com.hcmus.tkpm31_project.Util.CurrentUser;

import java.util.Calendar;

public class MyServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        CurrentUser currentUser = new CurrentUser(context);
        currentUser.setTodaylifetime(0);

        //Initial new alarm service
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        AlarmHelper alarmHelper = AlarmHelper.getInstanceAlarmHelper();
        alarmHelper.scheduleMyService(context,calendar);
    }
}
