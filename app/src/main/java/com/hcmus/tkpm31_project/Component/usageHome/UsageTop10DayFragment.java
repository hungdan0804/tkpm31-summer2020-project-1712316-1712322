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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.tkpm31_project.Adapter.PhoneUsageAdapter;
import com.hcmus.tkpm31_project.Component.habitHome.HabitHomeActivity;
import com.hcmus.tkpm31_project.Object.PhoneUsage;
import com.hcmus.tkpm31_project.Object.Top10RankingDay;
import com.hcmus.tkpm31_project.R;
import com.hcmus.tkpm31_project.Util.UStats;
import com.hcmus.tkpm31_project.Util.UtilPhoneUsage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UsageTop10DayFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<PhoneUsage> listAppUsage;
    private PhoneUsageAdapter appUsageAdapter;

    private Context context;

    public UsageTop10DayFragment(Context context) {
        this.context = context;
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

        listAppUsage=new ArrayList<>();

        //Check if permission enabled
        if (UStats.getUsageStatsList(context).isEmpty()){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        loadRecyclerViewTop10Day(context);
        return rootView;

    }
    public void loadRecyclerViewTop10Day(Context context)
    {
        UsageStatsManager usm = UtilPhoneUsage.getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();

        long startTime = calendar.getTimeInMillis(); // - 86400000;
        long days = TimeUnit.MILLISECONDS.toDays(startTime);  // day starts at 5.30
        startTime = TimeUnit.DAYS.toMillis(days) ; //+ 66600000; // so add this no to get to other day


        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        listAppUsage.clear();
        Top10RankingDay top10RankingDay=new Top10RankingDay();
        listAppUsage=top10RankingDay.top10Ranking(context);


        appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
        recyclerView.setAdapter(appUsageAdapter);
    }

}
