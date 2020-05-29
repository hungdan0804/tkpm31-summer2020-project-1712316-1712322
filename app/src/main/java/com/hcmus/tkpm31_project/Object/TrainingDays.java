package com.hcmus.tkpm31_project.Object;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class TrainingDays  {
    @NonNull
    @PrimaryKey (autoGenerate = true)
    private int _tdID;
    private int _habitID;
    private Date _createdDate;
    private boolean _isMark;

    public TrainingDays(int _habitID,boolean _isMark) {
        this._habitID =_habitID;
        this._isMark = _isMark;
        _createdDate=new Date();
    }

    public int get_tdID() {
        return _tdID;
    }

    public void set_tdID(int _tdID) {
        this._tdID = _tdID;
    }

    public Date get_createdDate() {
        return _createdDate;
    }

    public void set_createdDate(Date _createdDate) {
        this._createdDate = _createdDate;
    }

    public boolean is_isMark() {
        return _isMark;
    }

    public void set_isMark(boolean _isMark) {
        this._isMark = _isMark;
    }

    public int get_habitID() {
        return _habitID;
    }

    public void set_habitID(int _habitID) {
        this._habitID = _habitID;
    }
}
