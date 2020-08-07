package com.hcmus.tkpm31_project.Component.habitInfo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.Reminder;
import com.hcmus.tkpm31_project.ObjectRelationship.HabitWithReminder;
import com.hcmus.tkpm31_project.ObjectRelationship.HabitWithTraningDays;
import com.hcmus.tkpm31_project.Receiver.AlarmReceiver;
import com.hcmus.tkpm31_project.Util.CurrentUser;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;

import java.util.List;

public class HabitInfoPresenter implements HabitInfoContract.Presenter {

    private HabitInfoContract.View mView;
    private DatabaseHelper databaseHelper = null;

    public void setmView(HabitInfoContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void HandleLoadData(Context context,int habitID) {
        databaseHelper = DatabaseHelper.getINSTANCE(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HabitWithTraningDays t = databaseHelper.habitDAO().loadHabitsWithTrainingDays(habitID);
                mView.UpdateUI(t.trainingDays);
            }
        }).start();
    }

    @Override
    public void HandleDeleteHabit(Context context, int habitID) {
        databaseHelper = DatabaseHelper.getINSTANCE(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Habit deletedHabit = databaseHelper.habitDAO().fetchOneHabitbyHabitId(habitID);
                HabitWithReminder habitWithReminder = databaseHelper.habitDAO().loadHabitsWithReminders(habitID);
                List<Reminder> reminders = habitWithReminder.reminders;
                //cancel all alarm service
                Intent intent = new Intent(context, AlarmReceiver.class);

                for(Reminder r: reminders){
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)r.get_reminderID(), intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
                    AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                }
                deletedHabit.setDelete(true);
                databaseHelper.habitDAO().updateHabit(deletedHabit);
                mView.DeletedSuccess();
            }
        }).start();
    }

    @Override
    public void HandleReloadThumbnail(Context context, int habitID) {
        databaseHelper = DatabaseHelper.getINSTANCE(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Habit habit = databaseHelper.habitDAO().fetchOneHabitbyHabitId(habitID);
                mView.UpdateThumbnail(habit.getImageUri());
            }
        }).start();
    }


}
