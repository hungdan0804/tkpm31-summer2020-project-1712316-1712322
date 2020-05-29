package com.hcmus.tkpm31_project.ObjectRelationship;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.Reminder;

import java.util.List;

public class HabitWithReminder {
    @Embedded
    public Habit habit;

    @Relation(parentColumn = "_habitID",entityColumn = "_habitID",entity = Reminder.class)
    public List<Reminder> reminders;
}
