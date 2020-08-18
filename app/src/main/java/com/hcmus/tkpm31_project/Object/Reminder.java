package com.hcmus.tkpm31_project.Object;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Reminder {
    @NonNull
    @PrimaryKey (autoGenerate = true)
    private int _reminderID;
    private long _habitID;
    private int _daysOfWeek;

    public Reminder(long _habitID, int _daysOfWeek) {
        this._habitID = _habitID;
        this._daysOfWeek = _daysOfWeek;
    }

    public int get_reminderID() {
        return _reminderID;
    }

    public void set_reminderID(int _reminderID) {
        this._reminderID = _reminderID;
    }

    public long get_habitID() {
        return _habitID;
    }

    public void set_habitID(long _habitID) {
        this._habitID = _habitID;
    }

    public int get_daysOfWeek() {
        return _daysOfWeek;
    }

    public void set_daysOfWeek(int _daysOfWeek) {
        this._daysOfWeek = _daysOfWeek;
    }
}
