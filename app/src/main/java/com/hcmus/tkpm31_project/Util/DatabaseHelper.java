package com.hcmus.tkpm31_project.Util;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.hcmus.tkpm31_project.DAO.HabitDAO;
import com.hcmus.tkpm31_project.DAO.PhoneUsageDAO;
import com.hcmus.tkpm31_project.DAO.ReminderDAO;
import com.hcmus.tkpm31_project.DAO.TrainingDaysDAO;
import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.PhoneUsage;
import com.hcmus.tkpm31_project.Object.Reminder;
import com.hcmus.tkpm31_project.Object.TrainingDays;

@Database(entities = {Habit.class, Reminder.class, TrainingDays.class/*, PhoneUsage.class*/}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DatabaseHelper extends RoomDatabase {
    public abstract HabitDAO habitDAO();
    public abstract ReminderDAO reminderDAO();
    public abstract TrainingDaysDAO trainingDaysDAO();
    //public abstract PhoneUsageDAO phoneUsageDAO();
}
