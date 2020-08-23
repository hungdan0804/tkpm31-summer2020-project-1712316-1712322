package com.hcmus.tkpm31_project.Component.habitSumary;

import android.content.Context;

import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.TrainingDays;
import com.hcmus.tkpm31_project.ObjectRelationship.TrainningDaysWithHabitType;
import com.hcmus.tkpm31_project.Util.DatabaseHelper;
import com.hcmus.tkpm31_project.Util.DateHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HabitSumaryPresenter implements HabitSumaryContract.Presenter {

    private HabitSumaryContract.View mView;
    private DatabaseHelper databaseHelper = null;
    private final int MAX_NUMBERS_TYPES =6;

    public void setmView(HabitSumaryContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadData(Context context, Calendar dateChooser) {
        databaseHelper = DatabaseHelper.getINSTANCE(context);
        Calendar dayen =(Calendar) dateChooser.clone();
        dayen.set(Calendar.HOUR_OF_DAY,23);
        dayen.set(Calendar.MINUTE,59);
        dayen.set(Calendar.SECOND,59);
        new Thread(new Runnable() {
            private List<Habit> habits = new ArrayList<>();

            @Override
            public void run() {
                List<TrainingDays> l = databaseHelper.trainingDaysDAO().fetchAllTrainingDaysbyDate(dateChooser.getTime(),dayen.getTime());
                List<TrainningDaysWithHabitType> list_type = getTimeAllType(l);
                List<String> data = getTimeToString(list_type);
                mView.updateUI(habits,data);
            }

            private List<String> getTimeToString(List<TrainningDaysWithHabitType> list_type) {
                List<String> res = new ArrayList<>();
                for(TrainningDaysWithHabitType t : list_type){
                    String temp = DateHelper.TimeToString(t.getTotalTime());
                    res.add(temp);
                }
                return res;
            }

            private List<TrainningDaysWithHabitType> getTimeAllType(List<TrainingDays> l) {
                List<TrainningDaysWithHabitType> res =  initializeDefaultData();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                for(TrainingDays t:l){
                    if(t.is_isMark()){
                        Habit temp = databaseHelper.habitDAO().fetchOneHabitbyHabitId(t.get_habitID());
                        Date start = null;
                        Date end= null;
                        if(temp !=null) {
                            try {
                                start = format.parse(temp.get_startingTime());
                                end = format.parse(temp.get_endingTime());
                                long diff = end.getTime() - start.getTime(); //diff in milisecond
                                switch (temp.get_type()) {
                                    case "Work":
                                        res.get(0).setTotalTime(diff);
                                        break;
                                    case "Sport":
                                        res.get(1).setTotalTime(diff);
                                        break;
                                    case "Eating":
                                        res.get(2).setTotalTime(diff);
                                        break;
                                    case "Socializing":
                                        res.get(3).setTotalTime(diff);
                                        break;
                                    case "Entertainment":
                                        res.get(4).setTotalTime(diff);
                                        break;
                                    case "Others":
                                        res.get(5).setTotalTime(diff);
                                        break;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            habits.add(temp);
                        }
                    }
                }
                return res;
            }

            private List<TrainningDaysWithHabitType> initializeDefaultData() {
                List<TrainningDaysWithHabitType> res = new ArrayList<>();
                for(int i=0 ;i <MAX_NUMBERS_TYPES;i++){
                    String typeName="";
                    switch (i){
                        case 0: typeName = "Work";break;
                        case 1: typeName = "Sport";break;
                        case 2: typeName = "Eating";break;
                        case 3: typeName= "Socializing";break;
                        case 4: typeName = "Entertainment";break;
                        case 5: typeName = "Others";break;
                    }
                    TrainningDaysWithHabitType temp = new TrainningDaysWithHabitType(typeName,0);
                    res.add(temp);
                }
                return res;
            }
        }).start();
    }
}
