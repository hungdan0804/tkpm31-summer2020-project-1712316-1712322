package com.hcmus.tkpm31_project.Component.habitSumary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.hcmus.tkpm31_project.Adapter.HabitSumaryAdapter;
import com.hcmus.tkpm31_project.Adapter.HabitSumaryTypeAdapter;
import com.hcmus.tkpm31_project.Component.habitInfo.HabitInfoActivity;
import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HabitSumaryActivity extends AppCompatActivity implements HabitSumaryContract.View{

    private final int MAX_NUMBERS_TYPES =6;
    private GridView gv_type_sumary;
    private GridView gv_sumary;
    private TextView txt_date_chooser;
    private HabitSumaryTypeAdapter typeAdapter;
    private HabitSumaryAdapter mAdapter;
    private List<String> data;
    private static HabitSumaryPresenter presenter;
    private SimpleDateFormat sdf;
    private MaterialDatePicker.Builder timePickerBuilder;
    private MaterialDatePicker materialDatePicker;
    private static Calendar dateChooser;
    private ImageButton dateChooser_prev;
    private ImageButton dateChooser_next;
    private ImageButton btn_prev;
    private List<Habit> habits;
    public static boolean active = false;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_sumary);


        initVariable();
        initView();
        registerListener();
        dateChooser.set(Calendar.HOUR_OF_DAY,0);
        dateChooser.set(Calendar.MINUTE,0);
        dateChooser.set(Calendar.SECOND,0);
        presenter.loadData(getApplicationContext(),dateChooser);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.loadData(getApplicationContext(),dateChooser);
    }

    @Override
    protected void onStart() {
        super.onStart();
        active=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active=false;
    }

    private void registerListener() {
        txt_date_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                Date date = new Date((long)selection + offsetFromUTC);
                txt_date_chooser.setText(sdf.format(date));
                dateChooser.setTime(date);
                presenter.loadData(getApplicationContext(),dateChooser);
            }
        });
        dateChooser_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChooser.add(Calendar.DATE,-1);
                txt_date_chooser.setText(sdf.format(dateChooser.getTime()));
                presenter.loadData(getApplicationContext(),dateChooser);
            }
        });
        dateChooser_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChooser.add(Calendar.DATE,1);
                txt_date_chooser.setText(sdf.format(dateChooser.getTime()));
                presenter.loadData(getApplicationContext(),dateChooser);
            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initVariable() {
        context=this;
        data= new ArrayList<>();
        habits = new ArrayList<>();
        loadDefaultData();
        typeAdapter = new HabitSumaryTypeAdapter(getApplicationContext(),data);
        mAdapter = new HabitSumaryAdapter(getApplicationContext(),this,habits);
        presenter = new HabitSumaryPresenter();
        presenter.setmView(this);
        sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi","VN"));
        timePickerBuilder =MaterialDatePicker.Builder.datePicker();
        timePickerBuilder.setTitleText("Select a date");
        materialDatePicker =timePickerBuilder.build();
        dateChooser = Calendar.getInstance();
    }

    private void loadDefaultData() {
        for(int i=0;i<MAX_NUMBERS_TYPES;i++){
            String temp = "0m";
            data.add(temp);
        }
    }

    private void initView() {
        gv_type_sumary = (GridView)findViewById(R.id.gv_type_sumary);
        gv_type_sumary.setAdapter(typeAdapter);
        gv_sumary=(GridView)findViewById(R.id.gv_sumary);
        gv_sumary.setAdapter(mAdapter);
        txt_date_chooser = (TextView)findViewById(R.id.date_chooser);
        txt_date_chooser.setText(sdf.format(dateChooser.getTime()));
        dateChooser_prev = (ImageButton)findViewById(R.id.date_chooser_prev);
        dateChooser_next = (ImageButton)findViewById(R.id.date_chooser_next);
        btn_prev = (ImageButton)findViewById(R.id.btn_prev);
    }

    @Override
    public void updateUI(List<Habit> habits,List<String> data) {
        this.data.clear();
        this.data.addAll(data);
        this.habits.clear();
        this.habits.addAll(habits);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                typeAdapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public static void reloadUI(){
        presenter.loadData(context,dateChooser);
    }
}
