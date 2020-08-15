package com.hcmus.tkpm31_project.Util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.widget.CalendarView;

import androidx.core.app.NotificationCompat;

import com.hcmus.tkpm31_project.R;
import com.hcmus.tkpm31_project.Receiver.NotificationReceiver;

import java.util.Calendar;
import java.util.Date;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    public static final String ACTION_TRACKING = "ACTION_TRACKING";
    public static final String ACTION_DISMISS = "ACTION_DISMISS";
    private NotificationManager mManager;
    private Intent intent;
    private Intent trackingIntent;
    private Intent dismissIntent;
    private PendingIntent dismissPendingIntent;
    private PendingIntent trackingPendingIntent;
    private PendingIntent pendingIntent;
    public NotificationHelper(Context base,long habitID) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent=new Intent(base, CalendarView.class);

            trackingIntent = new Intent(base, NotificationReceiver.class);
            trackingIntent.setAction(ACTION_TRACKING);
            trackingIntent.putExtra("habitID",habitID);
            trackingPendingIntent = PendingIntent.getBroadcast(base,9999,trackingIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            dismissIntent = new Intent(base, NotificationReceiver.class);
            dismissIntent.setAction(ACTION_DISMISS);
            dismissIntent.putExtra("habitID",habitID);
            dismissPendingIntent = PendingIntent.getBroadcast(base,9999,dismissIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar c=Calendar.getInstance();
            c.setTime(new Date());
            intent.putExtra("date",c.getTimeInMillis());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent=PendingIntent.getActivity(base,0,intent,0);

            createChannel();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }


    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String habitName) {

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(habitName)
                .setContentText("It's training time now")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH)
                .addAction(R.drawable.ic_check_black_24dp,getString(R.string.msg_tracking),trackingPendingIntent)
                .addAction(R.drawable.ic_close_black_24dp,getString(R.string.msg_dismiss),dismissPendingIntent)
                .setOngoing(true)
                .setContentIntent(pendingIntent);

    }

    public NotificationCompat.Builder getChannelNotification2(String habitName) {

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(habitName)
                .setContentText("Congratulation !!! You have finished your trainning")
                .setSmallIcon(R.drawable.ic_whatshot_black_24dp)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

    }

}
