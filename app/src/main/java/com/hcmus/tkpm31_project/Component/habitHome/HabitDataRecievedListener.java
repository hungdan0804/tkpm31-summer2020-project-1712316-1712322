package com.hcmus.tkpm31_project.Component.habitHome;

import com.hcmus.tkpm31_project.Object.Habit;

import java.util.List;

public interface HabitDataRecievedListener {
    void onDataReceived(List<Habit> model,String searchText);
    void onFilterData(String newText);
}
