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

import com.hcmus.tkpm31_project.Adapter.PhoneUsageAdapter;
import com.hcmus.tkpm31_project.Object.PhoneUsage;
import com.hcmus.tkpm31_project.Object.Top10RankingDay;
import com.hcmus.tkpm31_project.Object.Top10RankingMonth;
import com.hcmus.tkpm31_project.R;

import java.util.ArrayList;

public class PhoneUsageRanking extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<PhoneUsage> listAppUsage;
    private PhoneUsageAdapter appUsageAdapter;
    Button btnTop10Day,btnTop10Month;
    private Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_usage_ranking);
        getSupportActionBar().setTitle("TOP RANKING");
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //background white
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffffff")));
        //center title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.activity_phone_usage_ranking);

        recyclerView=findViewById(R.id.recyclerview);

        listAppUsage=new ArrayList<>();
        Top10RankingDay top10RankingDay=new Top10RankingDay();
        Top10RankingMonth top10RankingMonth=new Top10RankingMonth();
        listAppUsage=top10RankingDay.top10Ranking(context);
        appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
        recyclerView.setAdapter(appUsageAdapter);
        btnTop10Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAppUsage=top10RankingDay.top10Ranking(context);
                appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
                recyclerView.setAdapter(appUsageAdapter);
            }
        });

        btnTop10Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAppUsage=top10RankingMonth.top10Ranking(context);
                appUsageAdapter=new PhoneUsageAdapter(context,listAppUsage);
                recyclerView.setAdapter(appUsageAdapter);
            }
        });

    }
}
