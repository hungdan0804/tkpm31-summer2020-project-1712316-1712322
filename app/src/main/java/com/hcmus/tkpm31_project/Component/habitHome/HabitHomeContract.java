package com.hcmus.tkpm31_project.Component.habitHome;

import android.content.Context;

import com.hcmus.tkpm31_project.Object.Habit;

import java.util.List;

public interface HabitHomeContract {
    interface View{
        void updateUI_Habit(List<Habit> data);

    }
    interface Presenter{
        void loadData(Context context);

    }
}
