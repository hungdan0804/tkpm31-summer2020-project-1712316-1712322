package com.hcmus.tkpm31_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.TextView;

import com.hcmus.tkpm31_project.Object.TrainingDays;
import com.hcmus.tkpm31_project.ObjectRelationship.HabitWithReminder;
import com.hcmus.tkpm31_project.ObjectRelationship.HabitWithTraningDays;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private static final String DATABASE_NAME = "Project_Android";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseHelper = Room.databaseBuilder(getApplicationContext(), DatabaseHelper.class, DATABASE_NAME).fallbackToDestructiveMigration()
                .build();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                TrainingDays trainingDays = new TrainingDays(1,true);
//                databaseHelper.trainingDaysDAO().insertOnlySingleTraningDays(trainingDays);
//            }
//        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<HabitWithReminder> habits = databaseHelper.habitDAO().loadHabitsWithReminders();
//                if(habits.size()==0){
//                    textView.setText("Size == 0");
//                }else {
//                    textView.setText("id: " + habits.get(0).reminders.get(0).get_reminderID());
//                }
//            }
//        }).start();
    }
}
