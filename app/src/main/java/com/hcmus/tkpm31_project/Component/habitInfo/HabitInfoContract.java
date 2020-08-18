package com.hcmus.tkpm31_project.Component.habitInfo;

import android.content.Context;

import com.hcmus.tkpm31_project.Object.TrainingDays;

import java.util.List;

public interface HabitInfoContract {
    interface View{
        void UpdateUI(List<TrainingDays> data);
        void DeletedSuccess();
        void UpdateThumbnail(String imageUri);
    }

    interface Presenter{
        void HandleLoadData(Context context,int habitID);
        void HandleDeleteHabit(Context context,int habitID);
        void HandleReloadThumbnail(Context context,int habitID);
    }
}
