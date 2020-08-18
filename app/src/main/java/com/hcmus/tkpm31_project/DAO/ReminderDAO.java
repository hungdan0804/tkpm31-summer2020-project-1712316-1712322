package com.hcmus.tkpm31_project.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hcmus.tkpm31_project.Object.Reminder;

import java.util.List;
@Dao
public interface ReminderDAO {
    @Insert
    long insertOnlySingleReminder (Reminder reminder);
    @Insert
    void insertMultipleReminders (List<Reminder> RemindersList);
    @Query("SELECT * FROM Reminder WHERE _reminderID = :reminderID")
    Reminder fetchOneReminderbyReminderId (int reminderID);
    @Update
    void updateReminder (Reminder reminder);
    @Delete
    void deleteReminder (Reminder reminder);

}
