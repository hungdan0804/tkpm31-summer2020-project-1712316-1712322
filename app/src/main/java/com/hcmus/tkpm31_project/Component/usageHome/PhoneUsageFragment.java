package com.hcmus.tkpm31_project.Component.usageHome;


import android.Manifest;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hcmus.tkpm31_project.Adapter.PhoneUsageAdapter;
import com.hcmus.tkpm31_project.Component.habitHome.HabitHomeActivity;
import com.hcmus.tkpm31_project.Object.PhoneUsage;
import com.hcmus.tkpm31_project.R;
import com.hcmus.tkpm31_project.Util.UStats;
import com.hcmus.tkpm31_project.Util.UtilPhoneUsage;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;

public class PhoneUsageFragment extends Fragment implements OnChartValueSelectedListener {
    private RecyclerView recyclerView;
    private ArrayList<PhoneUsage> listAppUsage;
    private PhoneUsageAdapter appUsageAdapter;
    private HabitHomeActivity mActivity;
    private OnFragmentInteractionListener mListener;
    private CombinedChart mChart;
    private static long totalTime;
    private Context context;
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static final String TAG = UStats.class.getSimpleName();
    private int RESULT_USAGE_ACCESS = 200;
    private final int MY_PERMISSION_REQUEST_ACCESS_USAGE = 100;

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
        mChart = rootView.findViewById(R.id.combinedChart);

        createChart();
        listAppUsage=new ArrayList<>();

        //Check if permission enabled
        if (UStats.getUsageStatsList(context).isEmpty()){
            requestPermissions(new String[] { Manifest.permission.PACKAGE_USAGE_STATS },
                    MY_PERMISSION_REQUEST_ACCESS_USAGE);
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_ACCESS_USAGE:
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivityForResult(intent,RESULT_USAGE_ACCESS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_USAGE_ACCESS){
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
            listAppUsage.addAll(getListUsage(startTime,endTime));
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    appUsageAdapter.notifyDataSetChanged();
                }
            });
            createChart();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void createChart()
    {
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setOnChartValueSelectedListener(this);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        ArrayList<String> xValue = new ArrayList<>();
        xValue=getXLabel();

        final ArrayList<String>xLabel=new ArrayList<>();
        for(int i=0;i<xValue.size();i++)
        {
            xLabel.add(xValue.get(i));
        }
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int) value % xLabel.size());
            }
        });
        CombinedData data = new CombinedData();
        LineData lineDatas = new LineData();
        lineDatas.addDataSet((ILineDataSet) dataChart());

        data.setData(lineDatas);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mChart.setData(data);
        mChart.invalidate();


    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(context, "Value: "
                + e.getY()
                + ", index: "
                + h.getX()
                + ", DataSet index: "
                + h.getDataSetIndex(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    private  DataSet dataChart() {

        LineData d = new LineData();
        ArrayList<Long>dataLong=getTotalTimeEachDayWeek(context);
        ArrayList<Float>dataFloat=new ArrayList<>();
        for(int i=0;i<dataLong.size();i++)
        {
            long time=dataLong.get(i);
            dataFloat.add(millisecondToTime(time));

        }
        //int[] data = new int[] { 1, 2, 2, 1, 1, 1, 2, 1, 1, 2, 1, 9 };

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < dataFloat.size(); index++) {
            entries.add(new Entry(index, dataFloat.get(index)));
        }

        LineDataSet set = new LineDataSet(entries, "Usage time by hours");
        set.setColor(Color.GREEN);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.GREEN);
        set.setCircleRadius(5f);
        set.setFillColor(Color.GREEN);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.GREEN);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }

    public float millisecondToTime(long time)
    {
        long days = TimeUnit.MILLISECONDS.toDays(time);
        time -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(time);
        time -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        time -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);

        float minutesFloat = (float) minutes;
        float hoursFloat   = (float) hours;
        float res=hoursFloat+minutesFloat/60;
        return res;
    }

    public ArrayList<String>getXLabel()
    {
        ArrayList<Long>listTotalTimeUsageDayInWeek=getTotalTimeEachDayWeek(context);
        ArrayList<String>res=new ArrayList<>();

        switch(listTotalTimeUsageDayInWeek.size())
        {
            case 1:
                res.add("Monday");
                break;

            case 2:
                res.add("Monday");
                res.add("Tuesday");
                break;

            case 3:
                res.add("Monday");
                res.add("Tuesday");
                res.add("Wednesday");
                break;

            case 4:
                res.add("Monday");
                res.add("Tuesday");
                res.add("Wednesday");
                res.add("Thurday");
                break;

            case 5:
                res.add("Monday");
                res.add("Tuesday");
                res.add("Wednesday");
                res.add("Thurday");
                res.add("Friday");
                break;

            case 6:
                res.add("Monday");
                res.add("Tuesday");
                res.add("Wednesday");
                res.add("Thurday");
                res.add("Friday");
                res.add("Saturday");
                break;

            case 7:
                res.add("Monday");
                res.add("Tuesday");
                res.add("Wednesday");
                res.add("Thurday");
                res.add("Friday");
                res.add("Saturday");
                res.add("Sunday");
                break;
        }
        return res;
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

    //load du lieu recycler
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

        ArrayList<PhoneUsage>listPhoneUsage=new ArrayList<>();
        long totalTimeOneDay=0;
        //total time truoc today
        for(long i=startTime;i!=startEndTime;i=i+86400000)
        {

        // Create a DateFormatter object for displaying date in specified format.
           /* SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            System.out.println("Start date:"+formatter.format(new Date(i)));
            System.out.println("End date:"+formatter.format(new Date(i+86400000)));
*/
            listPhoneUsage.clear();
            listPhoneUsage=getListUsage(i,i+86400000);
            totalTimeOneDay=0;
            for(int index=0;index<listPhoneUsage.size();index++)
            {
                totalTimeOneDay+=listPhoneUsage.get(index).get_time();
            }
            res.add(totalTimeOneDay);
        }
        totalTimeOneDay=0;

        //total time today
        listPhoneUsage.clear();
        listPhoneUsage=getListUsage(startEndTime,endTime);
        for(int index=0;index<listPhoneUsage.size();index++)
        {
            totalTimeOneDay+=listPhoneUsage.get(index).get_time();
        }


        res.add(totalTimeOneDay);

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

    public long getTotalTimeToDay()
    {
        ArrayList<PhoneUsage>listUsage=listUsageDay(context);
        long res=0;
        for(int i=0;i<listUsage.size();i++)
        {
            res+=listUsage.get(i).get_time();
        }


        return res;

    }

    public String getTotalTimeString()
    {
        long time=getTotalTimeToDay();
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

    public ArrayList<PhoneUsage>listUsageDay(Context context)
    {
        ArrayList<PhoneUsage> res=new ArrayList<>();
        UsageStatsManager usm = UtilPhoneUsage.getUsageStatsManager(context);
        //today
        //now

        Calendar today = Calendar.getInstance();
        long endTime=today.getTimeInMillis();
        today.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.SECOND);
        today.clear(Calendar.MILLISECOND);
        long startTime = today.getTimeInMillis();

        res=getListUsage(startTime,endTime);

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
