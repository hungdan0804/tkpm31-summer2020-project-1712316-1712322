package com.hcmus.tkpm31_project.Component.habitHome;

import android.content.Context;

import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Util.CurrentUser;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class HabitHomePresenter implements HabitHomeContract.Presenter{
    private HabitHomeContract.View mView;
    private Context context;
    private CurrentUser currentUser;

    public void setView(HabitHomeContract.View view){
        this.mView = view;
    }
    public void setCurrentUser(CurrentUser user){
        this.currentUser =currentUser;
    }


    @Override
    public void loadData(final Context context) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHelper db= DatabaseHelper.getINSTANCE(context);
                List<Habit> res = db.habitDAO().fetchAllHabit();
                mView.updateUI_Habit(res);
            }
        }).start();
    }

}
