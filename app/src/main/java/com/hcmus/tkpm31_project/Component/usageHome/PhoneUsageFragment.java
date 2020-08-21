package com.hcmus.tkpm31_project.Component.usageHome;


import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.tkpm31_project.Adapter.PhoneUsageAdapter;
import com.hcmus.tkpm31_project.Component.habitHome.HabitHomeActivity;
import com.hcmus.tkpm31_project.Object.PhoneUsage;
import com.hcmus.tkpm31_project.R;
import com.hcmus.tkpm31_project.Util.UStats;
import com.hcmus.tkpm31_project.Util.UtilPhoneUsage;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;

public class PhoneUsageFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<PhoneUsage> listAppUsage;
    private PhoneUsageAdapter appUsageAdapter;
    private HabitHomeActivity mActivity;
    private OnFragmentInteractionListener mListener;

    private static long totalTime;
    private Context context;
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static final String TAG = UStats.class.getSimpleName();

    public PhoneUsageFragment(Context context){
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=(HabitHomeActivity)getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_speding, container, false);
        recyclerView=rootView.findViewById(R.id.recyclerview);

        listAppUsage=new ArrayList<>();

        //Check if permission enabled
        if (UStats.getUsageStatsList(context).isEmpty()){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        loadRecyclerViewDataDay(context);
        System.out.println("Total time:"+ this.getTotalTimeString());

        return rootView;

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public ArrayList<PhoneUsage> getListUsage(long startTime,long endTime)
    {
        UsageStatsManager usm = UtilPhoneUsage.getUsageStatsManager(context);
        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        ArrayList<PhoneUsage>res=new ArrayList<>();
        totalTime=0;
        for (UsageStats u : usageStatsList) {

            try {

                if ((u.getTotalTimeInForeground() > 0) && !u.getPackageName().equals("com.google.android.deskclock") && (!u.getPackageName().equals("com.google.android.googlequicksearchbox"))) {
                    Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                            + u.getTotalTimeInForeground());


                    Drawable icon;
                    PackageManager packageManager = context.getPackageManager();

                    icon = packageManager.getApplicationIcon(u.getPackageName());
                    PhoneUsage appUsage = new PhoneUsage(icon, UtilPhoneUsage.getAppNameFromPkgName(context, u.getPackageName()), u.getTotalTimeInForeground(), u.getPackageName());
                    if(PhoneUsage.existedAppUsage(appUsage.get_nameApp(),res))
                    {
                        PhoneUsage existed=PhoneUsage.getAppUsage(appUsage.get_nameApp(),res);
                        long timeNew=u.getTotalTimeInForeground()+existed.get_time();
                        appUsage.set_time(timeNew);
                        res.remove(existed);


                    }
                    totalTime+=u.getTotalTimeInForeground();
                    res.add(appUsage);
                }
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //sort by time
        Collections.sort(res,PhoneUsage.AppUsageTimeComparator);
        //them stt
        ArrayList<PhoneUsage>cpyList=new ArrayList<>();
        for(int i=0;i<res.size();i++)
        {
            PhoneUsage a=res.get(i);
            a.set_stt(i+1);
            cpyList.add(a);
        }
        res.clear();
        for(PhoneUsage a:cpyList)
        {
            res.add(a);
        }
        return res;
    }
    public void loadRecyclerViewDataDay(Context context)
    {
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        //now

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.SECOND);
        today.clear(Calendar.MILLISECOND);
        long startTime = today.getTimeInMillis();


        listAppUsage.clear();

        totalTime=0;
        listAppUsage=getListUsage(startTime,endTime);
        ArrayList<Long> listTimeWeek=getTotalTimeEachDayWeek(context);
        for(long a:listTimeWeek)
        {
            System.out.println("Day time in week:"+a);
        }
        appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
        recyclerView.setAdapter(appUsageAdapter);


    }

    public ArrayList<Long>getTotalTimeEachDayWeek(Context context)
    {
        ArrayList<Long> res=new ArrayList<>();
        //first day of week
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        startDate.clear(Calendar.MINUTE);
        startDate.clear(Calendar.SECOND);
        startDate.clear(Calendar.MILLISECOND);
        // get start of this week in milliseconds
        startDate.set(Calendar.DAY_OF_WEEK, startDate.getFirstDayOfWeek());
        long startTime=startDate.getTimeInMillis();


        //today
        //now

        Calendar today = Calendar.getInstance();
        long endTime=today.getTimeInMillis();
        today.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.SECOND);
        today.clear(Calendar.MILLISECOND);
        long startEndTime = today.getTimeInMillis();

        UsageStatsManager usm = UtilPhoneUsage.getUsageStatsManager(context);
        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        ArrayList<PhoneUsage>listPhoneUsage=new ArrayList<>();
        long totalTimeOneDay=0;
        //total time truoc today
        for(long i=startTime;i!=startEndTime;i=i+86400000)
        {


            listPhoneUsage.clear();
            listPhoneUsage=getListUsage(i,i+86400000);
            totalTimeOneDay=0;
            for (UsageStats u : usageStatsList) {

                try {

                    if ((u.getTotalTimeInForeground() > 0) && !u.getPackageName().equals("com.google.android.deskclock") && (!u.getPackageName().equals("com.google.android.googlequicksearchbox"))) {


                        Drawable icon;
                        PackageManager packageManager = context.getPackageManager();
                        icon = packageManager.getApplicationIcon(u.getPackageName());
                        PhoneUsage appUsage = new PhoneUsage(icon, UtilPhoneUsage.getAppNameFromPkgName(context, u.getPackageName()), u.getTotalTimeInForeground(), u.getPackageName());
                        totalTimeOneDay+=u.getTotalTimeInForeground();

                        if(PhoneUsage.existedAppUsage(appUsage.get_nameApp(),listPhoneUsage))
                        {
                            PhoneUsage existed=PhoneUsage.getAppUsage(appUsage.get_nameApp(),listPhoneUsage);
                            long timeNew=u.getTotalTimeInForeground()+existed.get_time();
                            appUsage.set_time(timeNew);
                            listPhoneUsage.remove(existed);


                        }
                        listPhoneUsage.add(appUsage);

                    }
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.out.println("Total time in for:"+totalTimeOneDay);

            res.add(totalTimeOneDay);
        }
        listPhoneUsage.clear();
        listPhoneUsage=getListUsage(startEndTime,endTime);

        totalTimeOneDay=0;
        for (UsageStats u : usageStatsList) {

            try {

                if ((u.getTotalTimeInForeground() > 0) && !u.getPackageName().equals("com.google.android.deskclock") && (!u.getPackageName().equals("com.google.android.googlequicksearchbox"))) {


                    Drawable icon;
                    PackageManager packageManager = context.getPackageManager();
                    icon = packageManager.getApplicationIcon(u.getPackageName());
                    PhoneUsage appUsage = new PhoneUsage(icon, UtilPhoneUsage.getAppNameFromPkgName(context, u.getPackageName()), u.getTotalTimeInForeground(), u.getPackageName());
                    totalTimeOneDay+=u.getTotalTimeInForeground();

                    if(PhoneUsage.existedAppUsage(appUsage.get_nameApp(),listPhoneUsage))
                    {
                        PhoneUsage existed=PhoneUsage.getAppUsage(appUsage.get_nameApp(),listPhoneUsage);
                        long timeNew=u.getTotalTimeInForeground()+existed.get_time();
                        appUsage.set_time(timeNew);
                        listPhoneUsage.remove(existed);


                    }

                    listPhoneUsage.add(appUsage);

                }
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.println("Total time in for:"+totalTimeOneDay);

        res.add(totalTimeOneDay);
        for(int i=0;i<res.size();i++)
        {
            System.out.println("Time total:"+UtilPhoneUsage.getDurationBreakdown(res.get(i)));
        }
        return res;

    }
    public void loadRecyclerViewDataWeek(Context context)
    {

        //first day of week
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        startDate.clear(Calendar.MINUTE);
        startDate.clear(Calendar.SECOND);
        startDate.clear(Calendar.MILLISECOND);
        // get start of this week in milliseconds
        startDate.set(Calendar.DAY_OF_WEEK, startDate.getFirstDayOfWeek());
        long startTime=startDate.getTimeInMillis();

        Calendar endDate=Calendar.getInstance();
        long endTime = endDate.getTimeInMillis();





        listAppUsage.clear();

        listAppUsage=getListUsage(startTime,endTime);
        appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
        recyclerView.setAdapter(appUsageAdapter);
    }

    public long getTotalTime()
    {
        return totalTime;
    }

    public String getTotalTimeString()
    {
        long time=getTotalTime();
        return UtilPhoneUsage.getDurationBreakdown(time);

    }
    public void loadRecyclerViewDataMonth(Context context)
    {

        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();

        //get first day of month
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        // get start of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long startTime = calendar.getTimeInMillis(); // - 86400000;



        listAppUsage.clear();

        listAppUsage=getListUsage(startTime,endTime);
        appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
        recyclerView.setAdapter(appUsageAdapter);
    }

    public static ArrayList<PhoneUsage>listUsageDay(Context context)
    {
        ArrayList<PhoneUsage> res=new ArrayList<>();
        UsageStatsManager usm = UtilPhoneUsage.getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();

        long startTime = calendar.getTimeInMillis(); // - 86400000;
        long days = TimeUnit.MILLISECONDS.toDays(startTime);  // day starts at 5.30
        startTime = TimeUnit.DAYS.toMillis(days) ; //+ 66600000; // so add this no to get to other day


        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        for (UsageStats u : usageStatsList) {

            try {

                if ((u.getTotalTimeInForeground() > 0) && !u.getPackageName().equals("com.google.android.deskclock") && (!u.getPackageName().equals("com.google.android.googlequicksearchbox"))) {
                    Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                            + u.getTotalTimeInForeground());


                    Drawable icon;
                    PackageManager packageManager = context.getPackageManager();
                    icon = packageManager.getApplicationIcon(u.getPackageName());
                    PhoneUsage appUsage = new PhoneUsage(icon, UtilPhoneUsage.getAppNameFromPkgName(context, u.getPackageName()), u.getTotalTimeInForeground(), u.getPackageName());
                    if(PhoneUsage.existedAppUsage(appUsage.get_nameApp(),res))
                    {
                        PhoneUsage existed=PhoneUsage.getAppUsage(appUsage.get_nameApp(),res);
                        long timeNew=u.getTotalTimeInForeground()+existed.get_time();
                        appUsage.set_time(timeNew);
                        res.remove(existed);

                    }

                    res.add(appUsage);

                }
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //sort by time
        Collections.sort(res,PhoneUsage.AppUsageTimeComparator);

        return res;

    }

    public static ArrayList<PhoneUsage>listUsageWeek(Context context)
    {
        ArrayList<PhoneUsage> res=new ArrayList<>();

        UsageStatsManager usm = UtilPhoneUsage.getUsageStatsManager(context);
        //first day of week
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        startDate.clear(Calendar.MINUTE);
        startDate.clear(Calendar.SECOND);
        startDate.clear(Calendar.MILLISECOND);
        // get start of this week in milliseconds
        startDate.set(Calendar.DAY_OF_WEEK, startDate.getFirstDayOfWeek());
        long startTime=startDate.getTimeInMillis();

        Calendar endDate=Calendar.getInstance();
        long endTime = endDate.getTimeInMillis();





        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        for (UsageStats u : usageStatsList) {

            try {

                if ((u.getTotalTimeInForeground() > 0) && !u.getPackageName().equals("com.google.android.deskclock") && (!u.getPackageName().equals("com.google.android.googlequicksearchbox"))) {
                    Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                            + u.getTotalTimeInForeground());


                    Drawable icon;
                    PackageManager packageManager = context.getPackageManager();
                    icon = packageManager.getApplicationIcon(u.getPackageName());
                    PhoneUsage appUsage = new PhoneUsage(icon, UtilPhoneUsage.getAppNameFromPkgName(context, u.getPackageName()), u.getTotalTimeInForeground(), u.getPackageName());
                    if(PhoneUsage.existedAppUsage(appUsage.get_nameApp(),res))
                    {
                        PhoneUsage existed=PhoneUsage.getAppUsage(appUsage.get_nameApp(),res);
                        long timeNew=u.getTotalTimeInForeground()+existed.get_time();
                        appUsage.set_time(timeNew);
                        res.remove(existed);

                    }

                    res.add(appUsage);

                }
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //sort by time
        Collections.sort(res,PhoneUsage.AppUsageTimeComparator);

        return res;

    }

    public static ArrayList<PhoneUsage>listUsageMonth(Context context)
    {

        ArrayList<PhoneUsage> res=new ArrayList<>();

        UsageStatsManager usm = UtilPhoneUsage.getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();

        //get first day of month
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        // get start of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long startTime = calendar.getTimeInMillis(); // - 86400000;


        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);


        for (UsageStats u : usageStatsList) {

            try {

                if ((u.getTotalTimeInForeground() > 0) && !u.getPackageName().equals("com.google.android.deskclock") && (!u.getPackageName().equals("com.google.android.googlequicksearchbox"))) {
                    Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                            + u.getTotalTimeInForeground());


                    Drawable icon;
                    PackageManager packageManager = context.getPackageManager();
                    icon = packageManager.getApplicationIcon(u.getPackageName());
                    PhoneUsage appUsage = new PhoneUsage(icon, UtilPhoneUsage.getAppNameFromPkgName(context, u.getPackageName()), u.getTotalTimeInForeground(), u.getPackageName());
                    if(PhoneUsage.existedAppUsage(appUsage.get_nameApp(),res))
                    {
                        PhoneUsage existed=PhoneUsage.getAppUsage(appUsage.get_nameApp(),res);
                        long timeNew=u.getTotalTimeInForeground()+existed.get_time();
                        appUsage.set_time(timeNew);
                        res.remove(existed);

                    }

                    res.add(appUsage);

                }
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //sort by time
        Collections.sort(res,PhoneUsage.AppUsageTimeComparator);

        return res;

    }
}
