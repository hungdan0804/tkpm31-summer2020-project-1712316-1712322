package com.hcmus.tkpm31_project.Component.usageHome;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hcmus.tkpm31_project.Adapter.PhoneUsageAdapter;
import com.hcmus.tkpm31_project.Object.PhoneUsage;
import com.hcmus.tkpm31_project.Object.Top10RankingDay;
import com.hcmus.tkpm31_project.Object.Top10RankingMonth;
import com.hcmus.tkpm31_project.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PhoneUsageRanking extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<PhoneUsage> listAppUsage;
    private PhoneUsageAdapter appUsageAdapter;
    Button btnTop10Day,btnTop10Month;
    TextView txtTime;
    ImageButton btn_prev,btnDataPre,btnDataNext;

    private Calendar currentDay;
    private Calendar currentMonth;
    private Context context=this;

    Top10RankingDay top10RankingDay=new Top10RankingDay();
    Top10RankingMonth top10RankingMonth=new Top10RankingMonth();
    long endDate,startDate,endTimeMonth,startTimeMonth;
    private boolean flag=true;//true la day, false la month
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_usage_ranking);

        recyclerView=findViewById(R.id.recyclerview);
        txtTime=findViewById(R.id.txtTime);
        btnTop10Month=findViewById(R.id.btnTopMonth);
        btnTop10Day=findViewById(R.id.btnTopDay);
        btn_prev = findViewById(R.id.btn_prev);
        btnDataPre=findViewById(R.id.date_chooser_prev);
        btnDataNext=findViewById(R.id.date_chooser_next);



        listAppUsage=new ArrayList<>();



        btnTop10Month.setBackgroundColor(getColor(R.color.white));
        btnTop10Day.setBackgroundColor(getColor(R.color.bluewhite));
        btnTop10Month.setTextColor(getColor(R.color.black));
        btnTop10Day.setTextColor(getColor(R.color.white));



        //get time today
        //now
        currentDay = Calendar.getInstance();
        SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
        String strToday=format1.format(new Date(currentDay.getTimeInMillis()));

        endDate=currentDay.getTimeInMillis();
        //start time of today
        currentDay.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        currentDay.clear(Calendar.MINUTE);
        currentDay.clear(Calendar.SECOND);
        currentDay.clear(Calendar.MILLISECOND);
        startDate=currentDay.getTimeInMillis();


        //get time this month
        currentMonth=Calendar.getInstance();
        SimpleDateFormat format2=new SimpleDateFormat("MM/yyyy");
        endTimeMonth=currentMonth.getTimeInMillis();
        //get first day of month
        currentMonth.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        currentMonth.clear(Calendar.MINUTE);
        currentMonth.clear(Calendar.SECOND);
        currentMonth.clear(Calendar.MILLISECOND);
        // get start of the month
        currentMonth.set(Calendar.DAY_OF_MONTH, 1);
        startTimeMonth = currentMonth.getTimeInMillis();



        txtTime.setText(strToday);
        //get usage time today
        listAppUsage=top10RankingDay.top10Ranking(context,startDate,endDate);
        appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
        recyclerView.setAdapter(appUsageAdapter);
        btnDataPre.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //lui ngay
                if(flag==true)
                {
                    currentDay.add(Calendar.DATE,-1);
                    txtTime.setText(format1.format(new Date(currentDay.getTimeInMillis())));
                    endDate=startDate;
                    startDate=currentDay.getTimeInMillis();
                    System.out.println("Start day:"+format1.format(new Date(startDate)));
                    System.out.println("End day:"+format1.format(new Date(endDate)));

                    //reload data
                    loadDataTop10Day();

                }
                else //lui thang
                {
                    currentMonth.add(Calendar.MONTH,-1);
                    txtTime.setText(format2.format(new Date(currentMonth.getTimeInMillis())));
                    endTimeMonth=startTimeMonth;
                    startTimeMonth=currentMonth.getTimeInMillis();
                    System.out.println("Start day:"+format1.format(new Date(startTimeMonth)));
                    System.out.println("End day:"+format1.format(new Date(endTimeMonth)));
                    //reload data
                    loadDataTop10Month();
                }
            }
        });
        btnDataNext.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(flag==true)//tang ngay
                {
                    currentDay.add(Calendar.DATE,1);
                    txtTime.setText(format1.format(new Date(currentDay.getTimeInMillis())));
                    startDate=currentDay.getTimeInMillis();

                    currentDay.add(Calendar.DATE,1);
                    endDate=currentDay.getTimeInMillis();

                    currentDay.add(Calendar.DATE,-1);

                    System.out.println("Start day:"+format1.format(new Date(startDate)));
                    System.out.println("End day:"+format1.format(new Date(endDate)));
                    //reload data
                    loadDataTop10Day();


                }
                else //tang thang
                {
                    currentMonth.add(Calendar.MONTH,1);
                    txtTime.setText(format2.format(new Date(currentMonth.getTimeInMillis())));
                    startTimeMonth=currentMonth.getTimeInMillis();
                    format2.format(new Date(currentMonth.getTimeInMillis()));

                    currentMonth.add(Calendar.MONTH,1);


                    endTimeMonth=currentMonth.getTimeInMillis();

                    currentMonth.add(Calendar.MONTH,-1);

                    System.out.println("Start day:"+format1.format(new Date(startTimeMonth)));
                    System.out.println("End day:"+format1.format(new Date(endTimeMonth)));
                    //reload data
                    loadDataTop10Month();


                }
            }
        });
        btnTop10Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                loadDataTop10Day();
                txtTime.setText(format1.format(new Date(currentDay.getTimeInMillis())));
                btnTop10Month.setBackgroundColor(getColor(R.color.white));
                btnTop10Day.setBackgroundColor(getColor(R.color.bluewhite));
                btnTop10Month.setTextColor(getColor(R.color.black));
                btnTop10Day.setTextColor(getColor(R.color.white));
            }
        });

        btnTop10Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=false;
                loadDataTop10Month();
                txtTime.setText(format2.format(new Date(currentMonth.getTimeInMillis())));
                btnTop10Day.setBackgroundColor(getColor(R.color.white));
                btnTop10Month.setBackgroundColor(getColor(R.color.bluewhite));
                btnTop10Day.setTextColor(getColor(R.color.black));
                btnTop10Month.setTextColor(getColor(R.color.white));
            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void loadDataTop10Day()
    {
        listAppUsage.clear();
        listAppUsage=top10RankingDay.top10Ranking(context,startDate,endDate);
        appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
        recyclerView.setAdapter(appUsageAdapter);
    }

    public void loadDataTop10Month()
    {
        listAppUsage.clear();

        listAppUsage=top10RankingDay.top10Ranking(context,startTimeMonth,endTimeMonth);
        appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
        recyclerView.setAdapter(appUsageAdapter);
    }

    public boolean checkDate(Calendar date)
    {
        Calendar now=Calendar.getInstance();
        long dayDate = TimeUnit.MILLISECONDS.toDays(date.getTimeInMillis());
        long dayNow=TimeUnit.MILLISECONDS.toDays(now.getTimeInMillis());
        if(dayDate>dayNow)
            return false;
        return true;
    }
}
