package com.hcmus.tkpm31_project.DAO;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hcmus.tkpm31_project.Object.Habit;

import java.util.List;

public interface HabitDAO {

    @Insert
    void insertOnlySingleHabit (Habit habit);
    @Insert
    void insertMultipleHabits (List<Habit> HabitsList);
    @Query("SELECT * FROM Habit WHERE _habitID = :habitID")
    Habit fetchOneHabitbyHabitId (int habitID);
    @Update
    void updateHabit (Habit habit);
    @Delete
    void deleteHabit (Habit habit);
}
