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
import android.widget.TextView;

import com.hcmus.tkpm31_project.Adapter.PhoneUsageAdapter;
import com.hcmus.tkpm31_project.Object.PhoneUsage;
import com.hcmus.tkpm31_project.Object.Top10RankingDay;
import com.hcmus.tkpm31_project.Object.Top10RankingMonth;
import com.hcmus.tkpm31_project.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PhoneUsageRanking extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<PhoneUsage> listAppUsage;
    private PhoneUsageAdapter appUsageAdapter;
    Button btnTop10Day,btnTop10Month;
    TextView txtTime;
    private Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_usage_ranking);

        recyclerView=findViewById(R.id.recyclerview);
        txtTime=findViewById(R.id.txtTime);
        btnTop10Month=findViewById(R.id.btnTopMonth);
        btnTop10Day=findViewById(R.id.btnTopDay);
        listAppUsage=new ArrayList<>();
        Top10RankingDay top10RankingDay=new Top10RankingDay();
        Top10RankingMonth top10RankingMonth=new Top10RankingMonth();
        listAppUsage=top10RankingDay.top10Ranking(context);
        appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
        recyclerView.setAdapter(appUsageAdapter);

        btnTop10Month.setBackgroundColor(getColor(R.color.white));
        btnTop10Day.setBackgroundColor(getColor(R.color.bluewhite));
        btnTop10Month.setTextColor(getColor(R.color.black));
        btnTop10Day.setTextColor(getColor(R.color.white));

        txtTime.setText(getCurrentDate());
        btnTop10Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAppUsage=top10RankingDay.top10Ranking(context);
                appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
                recyclerView.setAdapter(appUsageAdapter);
                txtTime.setText(getCurrentDate());
                btnTop10Month.setBackgroundColor(getColor(R.color.white));
                btnTop10Day.setBackgroundColor(getColor(R.color.bluewhite));
                btnTop10Month.setTextColor(getColor(R.color.black));
                btnTop10Day.setTextColor(getColor(R.color.white));
            }
        });

        btnTop10Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAppUsage=top10RankingMonth.top10Ranking(context);
                appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
                recyclerView.setAdapter(appUsageAdapter);
                txtTime.setText(getCurrentMonthYear());
                btnTop10Day.setBackgroundColor(getColor(R.color.white));
                btnTop10Month.setBackgroundColor(getColor(R.color.bluewhite));
                btnTop10Day.setTextColor(getColor(R.color.black));
                btnTop10Month.setTextColor(getColor(R.color.white));
            }
        });

    }

    public String getCurrentDate()
    {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        return currentDate;
    }
    public String getCurrentMonthYear()
    {
        String currentDate = new SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(new Date());

        return currentDate;
    }
}
