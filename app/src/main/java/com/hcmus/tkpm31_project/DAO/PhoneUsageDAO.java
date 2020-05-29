package com.hcmus.tkpm31_project.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.hcmus.tkpm31_project.Object.PhoneUsage;

import java.util.List;
@Dao
public interface PhoneUsageDAO {
    @Insert
    void insertOnlySinglePhoneUsage (PhoneUsage phoneUsage);
    @Insert
    void insertMultiplePhoneUsages (List<PhoneUsage> phoneUsages);
    @Query("SELECT * FROM PhoneUsage WHERE _phoneUsageID = :phoneUsageID")
    PhoneUsage fetchOnePhoneUsagebyPhoneUsageId (int phoneUsageID);
    @Update
    void updatePhoneUsage (PhoneUsage phoneUsage);
    @Delete
    void deletePhoneUsage (PhoneUsage phoneUsage);
}
