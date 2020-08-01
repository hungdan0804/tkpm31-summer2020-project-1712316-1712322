package com.hcmus.tkpm31_project.Receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hcmus.tkpm31_project.Object.TrainingDays;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_TRACKING = "ACTION_TRACKING";
    public static final String ACTION_DISMISS = "ACTION_DISMISS";

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
