package com.hcmus.tkpm31_project.Object;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class PhoneUsage {
    @NonNull
    @PrimaryKey (autoGenerate = true)
    protected int _phoneUsageID;
    protected String _packetName;
    protected Date _createdDate;

    public PhoneUsage(String _packetName) {
        this._packetName = _packetName;
        this._createdDate=new Date();
    }

    public int get_phoneUsageID() {
        return _phoneUsageID;
    }

    public void set_phoneUsageID(int _phoneUsageID) {
        this._phoneUsageID = _phoneUsageID;
    }

    public String get_packetName() {
        return _packetName;
    }

    public void set_packetName(String _packetName) {
        this._packetName = _packetName;
    }

    public Date get_createdDate() {
        return _createdDate;
    }

    public void set_createdDate(Date _createdDate) {
        this._createdDate = _createdDate;
    }
}
