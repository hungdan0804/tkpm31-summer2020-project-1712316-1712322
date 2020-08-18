package com.hcmus.tkpm31_project.Component.habitUpdateInfo;

import android.content.Context;

import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.Reminder;
import com.hcmus.tkpm31_project.ObjectRelationship.HabitWithReminder;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;

import java.util.List;

public class HabitUpdateInfoPresenter implements HabitUpdateInfoContract.Presenter {
    private HabitUpdateInfoContract.View mView;
    private DatabaseHelper databaseHelper = null;


    public void setmView(HabitUpdateInfoContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void HandleLoadData(Context context,int habitID) {
        databaseHelper = DatabaseHelper.getINSTANCE(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Habit habit = databaseHelper.habitDAO().fetchOneHabitbyHabitId(habitID);
                HabitWithReminder temp = databaseHelper.habitDAO().loadHabitsWithReminders(habitID);
                mView.updateUI(habit,temp.reminders);
            }
        }).start();
    }

    @Override
    public void HandleUpdateData(Context context, Habit habit) {
        databaseHelper = DatabaseHelper.getINSTANCE(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                databaseHelper.habitDAO().updateHabit(habit);
                mView.updatedSuccess();
            }
        }).start();
    }
}
