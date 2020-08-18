package com.hcmus.tkpm31_project.Object;

import android.graphics.drawable.Drawable;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class PhoneUsage {

    protected int _phoneUsageID;
    protected String _packetName;
    protected Date _createdDate;
    protected Drawable _thumbnail;
    protected String _nameApp;
    protected long _time;

    public PhoneUsage() {
    }

    public PhoneUsage(String _packetName) {
        this._packetName = _packetName;
        this._createdDate=new Date();
    }

    public PhoneUsage( Drawable _thumbnail,String _nameApp, long _time, String _packetName) {
        this._packetName = _packetName;
        this._thumbnail = _thumbnail;
        this._nameApp = _nameApp;
        this._time = _time;
    }

    public PhoneUsage(int _phoneUsageID, String _packetName, Date _createdDate, Drawable _thumbnail, String _nameApp, long _time) {
        this._phoneUsageID = _phoneUsageID;
        this._packetName = _packetName;
        this._createdDate = _createdDate;
        this._thumbnail = _thumbnail;
        this._nameApp = _nameApp;
        this._time = _time;
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

    public Drawable get_thumbnail() {
        return _thumbnail;
    }

    public void set_thumbnail(Drawable _thumbnail) {
        this._thumbnail = _thumbnail;
    }

    public String get_nameApp() {
        return _nameApp;
    }

    public void set_nameApp(String _nameApp) {
        this._nameApp = _nameApp;
    }

    public long get_time() {
        return _time;
    }

    public void set_time(long _time) {
        this._time = _time;
    }


    public static boolean existedAppUsage(String nameApp, ArrayList<PhoneUsage> list)
    {
        for(PhoneUsage a: list)
        {
            if(a.get_nameApp()==nameApp)
            {
                return true;
            }
        }
        return false;
    }
    public static PhoneUsage getAppUsage(String nameApp, ArrayList<PhoneUsage>list)
    {
        PhoneUsage res=new PhoneUsage();
        for(PhoneUsage a: list)
        {
            if(a.get_nameApp()==nameApp)
            {
                res=a;
            }
        }
        return res;
    }

    /*Comparator for sorting the list by time*/
    public static Comparator<PhoneUsage> AppUsageTimeComparator = new Comparator<PhoneUsage>() {

        public int compare(PhoneUsage s1, PhoneUsage s2) {
            long time1 = s1.get_time();
            long time2 = s2.get_time();

            //ascending order
            //return time1.compareTo(time2);

            //descending order
            int res=(int)time2-(int)time1;
            return res;
        }};
}
