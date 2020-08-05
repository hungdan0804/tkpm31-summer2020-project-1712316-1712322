package com.hcmus.tkpm31_project.Component.habitInfo;

import android.content.Context;

import com.hcmus.tkpm31_project.ObjectRelationship.HabitWithTraningDays;
import com.hcmus.tkpm31_project.Util.CurrentUser;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;

import java.util.List;

public class HabitInfoPresenter implements HabitInfoContract.Presenter {

    private HabitInfoContract.View mView;
    private DatabaseHelper databaseHelper = null;

    public void setmView(HabitInfoContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void HandleLoadData(Context context,int habitID) {
        databaseHelper = DatabaseHelper.getINSTANCE(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HabitWithTraningDays t = databaseHelper.habitDAO().loadHabitsWithTrainingDays(habitID);
                mView.UpdateUI(t.trainingDays);
            }
        }).start();
    }

}
