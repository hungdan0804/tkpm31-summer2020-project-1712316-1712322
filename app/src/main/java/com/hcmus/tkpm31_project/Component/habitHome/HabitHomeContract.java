package com.hcmus.tkpm31_project.Component.habitHome;

import android.content.Context;

import com.hcmus.tkpm31_project.Object.Habit;

import java.util.List;

public interface HabitHomeContract {
    interface View{
        void updateUI_Habit(List<Habit> data);
        void updateUI_Habit_Total(int totalLifeTime);
    }
    interface Presenter{
        void loadData(Context context);
        void loadCurUser();
    }
}
