package com.hcmus.tkpm31_project.Receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.tkpm31_project.Component.habitHome.HabitHomeActivity;
import com.hcmus.tkpm31_project.Component.habitSumary.HabitSumaryActivity;
import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.TrainingDays;
import com.hcmus.tkpm31_project.Object.User;
import com.hcmus.tkpm31_project.Util.CurrentUser;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_TRACKING = "ACTION_TRACKING";
    public static final String ACTION_DISMISS = "ACTION_DISMISS";


    @Override
    public void onReceive(final Context context, Intent intent){
        final FirebaseDatabase database =FirebaseDatabase.getInstance();
        final DatabaseReference ref=database.getReference();
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
                    final CurrentUser currentUser = new CurrentUser(context);

                    try {
                        Date start = format.parse(temp.get_startingTime());
                        Date end = format.parse(temp.get_endingTime());
                        final long diff = end.getTime() - start.getTime(); //diff in milisecond
                        currentUser.setTodaylifetime(currentUser.getTodayLifeTime()+diff);
                        currentUser.setTotallifetime(currentUser.getTotalLifeTime()+diff);
                        ref.child("user").orderByChild("username").equalTo(currentUser.getCurrentUser()).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot d : dataSnapshot.getChildren()){
                                    User temp = d.getValue(User.class);
                                    if(temp.getUsername().equals(currentUser.getCurrentUser())){
                                        d.getRef().child("totalLifeTime").setValue(currentUser.getTotalLifeTime()+diff);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(HabitHomeActivity.active){
                        HabitHomeActivity.updateUI();
                    }
                    if(HabitSumaryActivity.active){
                        HabitSumaryActivity.reloadUI();
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
