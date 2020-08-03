package com.hcmus.tkpm31_project.Receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hcmus.tkpm31_project.Component.habitHome.HabitHomeActivity;
import com.hcmus.tkpm31_project.Component.habitHome.HabitHomeContract;
import com.hcmus.tkpm31_project.Component.habitHome.HabitHomePresenter;
import com.hcmus.tkpm31_project.Component.habitHome.HabitUpdateUIListener;
import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.TrainingDays;
import com.hcmus.tkpm31_project.Util.CurrentUser;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_TRACKING = "ACTION_TRACKING";
    public static final String ACTION_DISMISS = "ACTION_DISMISS";
    private HabitUpdateUIListener listener;


    @Override
    public void onReceive(final Context context, Intent intent){
        String action = intent.getAction();
        final long habitID = intent.getLongExtra("habitID",0);
        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final DatabaseHelper databaseHelper = DatabaseHelper.getINSTANCE(context);
        if(ACTION_TRACKING.equals(action)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    databaseHelper.trainingDaysDAO().insertOnlySingleTraningDays((new TrainingDays((int)habitID,true)));
                    Habit temp = databaseHelper.habitDAO().fetchOneHabitbyHabitId((int)habitID);
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    CurrentUser currentUser = new CurrentUser(context);

                    try {
                        Date start = format.parse(temp.get_startingTime());
                        Date end = format.parse(temp.get_endingTime());
                        long diff = end.getTime() - start.getTime(); //diff in milisecond
                        currentUser.setTodaylifetime(currentUser.getTodayLifeTime()+diff);
                        currentUser.setTotallifetime(currentUser.getTotalLifeTime()+diff);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(HabitHomeActivity.active){
                        HabitHomeActivity.updateUI();
                    }

                }
            }).start();
        }else if(ACTION_DISMISS.equals(action)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    databaseHelper.trainingDaysDAO().insertOnlySingleTraningDays((new TrainingDays((int)habitID,false)));
                }
            }).start();
        }

        nMgr.cancel(0);
    }
}
