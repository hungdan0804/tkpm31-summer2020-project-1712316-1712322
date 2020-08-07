package com.hcmus.tkpm31_project.Object;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "Habit")
public class Habit implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int _habitID;
    private String _habitName;
    private String _type;
    private Date _startingDate;
    private Date _endingDate;
    private int _daysTraining;
    private String satisfidedRating;
    private String _startingTime;
    private String _endingTime;
    private String imageUri;
    private String description;
    private boolean isDelete;

    public Habit(String _habitName, String _type, Date _startingDate, Date _endingDate,int _daysTraining, String _startingTime, String _endingTime,String imageUri,String description) {
        this._habitName = _habitName;
        this._type = _type;
        this._startingDate = _startingDate;
        this._endingDate = _endingDate;
        this._daysTraining = _daysTraining;
        this._startingTime = _startingTime;
        this._endingTime = _endingTime;
        this.satisfidedRating = "";
        this.imageUri = imageUri;
        this.description =description;
        isDelete=false;

    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public Date get_endingDate() {
        return _endingDate;
    }

    public void set_endingDate(Date _endingDate) {
        this._endingDate = _endingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void set_habitID(@NonNull int _habitID) {
        this._habitID = _habitID;
    }

    public void set_habitName(String _habitName) {
        this._habitName = _habitName;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public void set_startingDate(Date _startingDate) {
        this._startingDate = _startingDate;
    }

    public void set_daysTraining(int _daysTraining) {
        this._daysTraining = _daysTraining;
    }

    public void setSatisfidedRating(String satisfidedRating) {
        this.satisfidedRating = satisfidedRating;
    }

    public void set_startingTime(String _startingTime) {
        this._startingTime = _startingTime;
    }

    public void set_endingTime(String _endingTime) {
        this._endingTime = _endingTime;
    }

    @NonNull
    public int get_habitID() {
        return _habitID;
    }

    public String get_habitName() {
        return _habitName;
    }

    public String get_type() {
        return _type;
    }

    public Date get_startingDate() {
        return _startingDate;
    }

    public int get_daysTraining() {
        return _daysTraining;
    }

    public String getSatisfidedRating() {
        return satisfidedRating;
    }

    public String get_startingTime() {
        return _startingTime;
    }

    public String get_endingTime() {
        return _endingTime;
    }
}
