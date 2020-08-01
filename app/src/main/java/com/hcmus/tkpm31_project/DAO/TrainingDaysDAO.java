package com.hcmus.tkpm31_project.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.hcmus.tkpm31_project.Object.TrainingDays;

import java.util.List;
@Dao
public interface TrainingDaysDAO {
    @Insert
    void insertOnlySingleTraningDays(TrainingDays td);
    @Insert
    void insertMultipleTrainingDays(List<TrainingDays> TrainingDaysList);
    @Query("SELECT * FROM TrainingDays")
    List<TrainingDays> fetchAllTrainingDaysbyTrainingDaysId();
    @Query("SELECT * FROM TrainingDays WHERE _tdID = :tdID")
    TrainingDays fetchOneTrainingDaysbyTrainingDaysId(int tdID);
    @Update
    void updateTrainingDays(TrainingDays td);
    @Delete
    void deleteTrainingDays(TrainingDays td);

}
