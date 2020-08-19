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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PhoneUsageFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<PhoneUsage> listAppUsage;
    private PhoneUsageAdapter appUsageAdapter;
    private HabitHomeActivity mActivity;
    private OnFragmentInteractionListener mListener;

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

    public void loadRecyclerViewDataDay(Context context)
    {
        UsageStatsManager usm = UtilPhoneUsage.getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();

        long startTime = calendar.getTimeInMillis(); // - 86400000;
        long days = TimeUnit.MILLISECONDS.toDays(startTime);  // day starts at 5.30
        startTime = TimeUnit.DAYS.toMillis(days) ; //+ 66600000; // so add this no to get to other day


        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        listAppUsage.clear();
        for (UsageStats u : usageStatsList) {

            try {

                if ((u.getTotalTimeInForeground() > 0) && !u.getPackageName().equals("com.google.android.deskclock") && (!u.getPackageName().equals("com.google.android.googlequicksearchbox"))) {
                    Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                            + u.getTotalTimeInForeground());


                    Drawable icon;
                    PackageManager packageManager = context.getPackageManager();

                    icon = packageManager.getApplicationIcon(u.getPackageName());
                    PhoneUsage appUsage = new PhoneUsage(icon, UtilPhoneUsage.getAppNameFromPkgName(context, u.getPackageName()), u.getTotalTimeInForeground(), u.getPackageName());
                    if(PhoneUsage.existedAppUsage(appUsage.get_nameApp(),listAppUsage))
                    {
                        PhoneUsage existed=PhoneUsage.getAppUsage(appUsage.get_nameApp(),listAppUsage);
                        long timeNew=u.getTotalTimeInForeground()+existed.get_time();
                        appUsage.set_time(timeNew);
                        listAppUsage.remove(existed);


                    }

                    listAppUsage.add(appUsage);
                }
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //sort by time
        Collections.sort(listAppUsage,PhoneUsage.AppUsageTimeComparator);

        appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
        recyclerView.setAdapter(appUsageAdapter);
    }

    public void loadRecyclerViewDataWeek(Context context)
    {

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
        listAppUsage.clear();

        for (UsageStats u : usageStatsList) {

            try {

                if ((u.getTotalTimeInForeground() > 0) && !u.getPackageName().equals("com.google.android.deskclock") && (!u.getPackageName().equals("com.google.android.googlequicksearchbox"))) {
                    Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                            + u.getTotalTimeInForeground());


                    Drawable icon;
                    PackageManager packageManager = context.getPackageManager();
                    icon = packageManager.getApplicationIcon(u.getPackageName());
                    PhoneUsage appUsage = new PhoneUsage(icon, UtilPhoneUsage.getAppNameFromPkgName(context, u.getPackageName()), u.getTotalTimeInForeground(), u.getPackageName());
                    if(PhoneUsage.existedAppUsage(appUsage.get_nameApp(),listAppUsage))
                    {
                        PhoneUsage existed=PhoneUsage.getAppUsage(appUsage.get_nameApp(),listAppUsage);
                        long timeNew=u.getTotalTimeInForeground()+existed.get_time();
                        appUsage.set_time(timeNew);
                        listAppUsage.remove(existed);


                    }

                    listAppUsage.add(appUsage);
                }
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //sort by time
        Collections.sort(listAppUsage,PhoneUsage.AppUsageTimeComparator);

        appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
        recyclerView.setAdapter(appUsageAdapter);
    }


    public void loadRecyclerViewDataMonth(Context context)
    {

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

        listAppUsage.clear();

        for (UsageStats u : usageStatsList) {

            try {

                if ((u.getTotalTimeInForeground() > 0) && !u.getPackageName().equals("com.google.android.deskclock") && (!u.getPackageName().equals("com.google.android.googlequicksearchbox"))) {
                    Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                            + u.getTotalTimeInForeground());


                    Drawable icon;
                    PackageManager packageManager = context.getPackageManager();
                    icon = packageManager.getApplicationIcon(u.getPackageName());
                    PhoneUsage appUsage = new PhoneUsage(icon, UtilPhoneUsage.getAppNameFromPkgName(context, u.getPackageName()), u.getTotalTimeInForeground(), u.getPackageName());
                    if(PhoneUsage.existedAppUsage(appUsage.get_nameApp(),listAppUsage))
                    {
                        PhoneUsage existed=PhoneUsage.getAppUsage(appUsage.get_nameApp(),listAppUsage);
                        long timeNew=u.getTotalTimeInForeground()+existed.get_time();
                        appUsage.set_time(timeNew);
                        listAppUsage.remove(existed);

                    }

                    listAppUsage.add(appUsage);

                }
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //sort by time
        Collections.sort(listAppUsage,PhoneUsage.AppUsageTimeComparator);

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
