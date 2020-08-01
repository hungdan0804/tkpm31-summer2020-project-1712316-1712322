package com.hcmus.tkpm31_project.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.ObjectRelationship.HabitWithReminder;
import com.hcmus.tkpm31_project.ObjectRelationship.HabitWithTraningDays;

import java.util.List;

@Dao
public interface HabitDAO {

    @Insert
    long insertOnlySingleHabit (Habit habit);
    @Insert
    void insertMultipleHabits (List<Habit> HabitsList);
    @Query("SELECT * FROM Habit")
    List<Habit> fetchAllHabit();
    @Query("SELECT * FROM Habit WHERE _habitID = :habitID")
    Habit fetchOneHabitbyHabitId (int habitID);
    @Update
    void updateHabit (Habit habit);
    @Delete
    void deleteHabit (Habit habit);
    @Transaction
    @Query("SELECT * FROM Habit")
    public List<HabitWithReminder> loadHabitsWithReminders();

    @Transaction
    @Query("SELECT * FROM Habit")
    public List<HabitWithTraningDays> loadHabitsWithTrainingDays();
}
