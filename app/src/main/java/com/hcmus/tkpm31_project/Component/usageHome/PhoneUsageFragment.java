package com.hcmus.tkpm31_project.Component.usageHome;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.tkpm31_project.Adapter.PhoneUsageAdapter;
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
    private Button btnDo;
    private Spinner spnCategory;
    private Context context;
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static final String TAG = UStats.class.getSimpleName();

    public PhoneUsageFragment(Context context){
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_speding, container, false);
        recyclerView=rootView.findViewById(R.id.recyclerview);
        spnCategory=rootView.findViewById(R.id.spnCategory);
        btnDo=rootView.findViewById(R.id.btnDo);
        listAppUsage=new ArrayList<>();

        //cho 3 loai category: day, week, month
        List<String> category=new ArrayList<>();
        category.add("Day");
        category.add("Week");
        category.add("Month");

        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item,category);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnCategory.setAdapter(adapter);
        //Check if permission enabled
        if (UStats.getUsageStatsList(context).isEmpty()){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        loadRecyclerViewDataDay(context);
        btnDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //category
                String category=spnCategory.getSelectedItem().toString();
                switch (category)
                {
                    case "Day":
                        loadRecyclerViewDataDay(context);
                        break;
                    case "Week":
                        loadRecyclerViewDataWeek(context);
                        break;
                    case "Month":
                        loadRecyclerViewDataMonth(context);
                        break;
                }

            }
        });
        return rootView;

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
}
