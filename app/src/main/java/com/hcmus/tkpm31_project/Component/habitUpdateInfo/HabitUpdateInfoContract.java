package com.hcmus.tkpm31_project.Component.habitUpdateInfo;

import android.content.Context;

import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.Reminder;

import java.util.List;

public interface HabitUpdateInfoContract {
    interface View{
        void updateUI(Habit habit, List<Reminder> reminders);
        void updatedSuccess();
    }

    interface Presenter{
        void HandleLoadData(Context context, int habitID);
        void HandleUpdateData(Context context,Habit habit);
    }
}
