package com.hcmus.tkpm31_project.ObjectRelationship;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.Reminder;
import com.hcmus.tkpm31_project.Object.TrainingDays;

import java.util.List;

public class HabitWithTraningDays {
    @Embedded
    public Habit habit;

    @Relation(parentColumn = "_habitID",entityColumn = "_habitID",entity = TrainingDays.class)
    public List<TrainingDays> trainingDays;
}
