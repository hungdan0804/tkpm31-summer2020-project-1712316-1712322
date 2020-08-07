package com.hcmus.tkpm31_project.Component.habitSumary;

import android.content.Context;

import com.hcmus.tkpm31_project.Object.Habit;

import java.util.Calendar;
import java.util.List;

public interface HabitSumaryContract {
    interface View{
        void updateUI(List<Habit>habits,List<String> data);
    }
    interface Presenter{
        void loadData(Context context, Calendar dateChooser);
    }
}
