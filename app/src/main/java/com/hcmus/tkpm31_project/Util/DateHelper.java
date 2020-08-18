package com.hcmus.tkpm31_project.Util;

import com.hcmus.tkpm31_project.Object.Habit;

public class DateHelper {

    public static String TimeToString(long diff){
        diff /= 1000; //diff in second
        int hour = (int)diff/3600;
        int remainder= (int)diff - hour*3600;
        int min = remainder/60;
        String str="";
        if(diff > 0) {
            str += hour + "h " + min + "m";
        }else{
            str += min + "m";
        }
        return str;
    }
}
