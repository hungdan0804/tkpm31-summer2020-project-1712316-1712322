package com.hcmus.tkpm31_project.Component.initializeHabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.hcmus.tkpm31_project.Adapter.HabitThumbnailAdapter;
import com.hcmus.tkpm31_project.Adapter.HabitTypeListViewAdapter;
import com.hcmus.tkpm31_project.BuildConfig;
import com.hcmus.tkpm31_project.Component.habitHome.HabitHomeActivity;
import com.hcmus.tkpm31_project.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class InitializeHabitActivity extends AppCompatActivity implements InitializeHabitContract.View {


    private final int DAY_OF_WEEK = 7;
    private TextInputEditText edt_habit_name;
    private TextView typeValue;
    private TextView typeChange;
    private TextView startingDays_value;
    private TextView startingDays_change;
    private Slider edt_tranning_Days;
    private ImageButton btn_thumbnail;
    private TextView repetition_days;
    private TextView repetition_days_change;
    private TextView start_repetition;
    private TextView start_repetition_change;
    private TextView end_repetition;
    private TextView end_repetition_change;
    private EditText edt_description;
    private ImageButton btn_insert;
    private ImageButton btn_back;
    private CalendarConstraints.Builder calendarConstraint;
    private MaterialDatePicker.Builder timePickerBuilder;
    private MaterialDatePicker materialDatePicker;
    private Calendar startingDate;
    private Calendar start_repetition_time;
    private Calendar end_repetition_time;
    private int RESULT_LOAD_IMG = 200;
    private final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private Context context = this;
    private int dayofweek;
    private List<Boolean> checkDay;
    private InitializeHabitPresenter presenter;
    private String thumbnail = "";
    private TextView daysTrainning_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize_habit);

        initView();
        initVariable();
        registerListener();
    }

    private void initVariable() {

        presenter = new InitializeHabitPresenter();
        presenter.setView(this);

        calendarConstraint= new CalendarConstraints.Builder();
        timePickerBuilder =MaterialDatePicker.Builder.datePicker();

        calendarConstraint.setValidator(DateValidatorPointForward.from(new Date().getTime()-24*60*60*1000));
        timePickerBuilder.setCalendarConstraints(calendarConstraint.build());
        timePickerBuilder.setTitleText("Select a date");



        materialDatePicker =timePickerBuilder.build();
        startingDate = Calendar.getInstance();
        start_repetition_time = Calendar.getInstance();
        end_repetition_time = Calendar.getInstance();

        checkDay = new ArrayList<>();
        //initialize
        for(int i=0;i< DAY_OF_WEEK;i++) {
            checkDay.add(true);
        }
    }

    private void registerListener() {
        typeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createHabitTypeDialog(InitializeHabitActivity.this);
            }

            private void createHabitTypeDialog(Context context) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_habit_type);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final ListView lv_habit_types= dialog.findViewById(R.id.lv_habit_type);
                lv_habit_types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String msg = (String)lv_habit_types.getItemAtPosition(position);
                        typeValue.setText(msg);
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });
                HabitTypeListViewAdapter adapter=new HabitTypeListViewAdapter(getBaseContext());
                lv_habit_types.setAdapter(adapter);
                dialog.show();
            }
        });
        startingDays_change.setOnClickListener(new View.OnClickListener() {
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
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi","VN"));
                Date date = new Date((long)selection + offsetFromUTC);
                startingDays_value.setText(simpleFormat.format(date));
                startingDate.setTime(date);
            }
        });
        btn_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createHabitThumbnailDialog();

            }

            private void createHabitThumbnailDialog() {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_thumbnail_chooser);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final GridView gridView =(GridView) dialog.findViewById(R.id.gridView);
                HabitThumbnailAdapter adapter = new HabitThumbnailAdapter(context);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position != gridView.getLastVisiblePosition()){
                            Picasso.get().load((int)gridView.getItemAtPosition(position)).resize(1024,768).into(btn_thumbnail);
                            thumbnail = "Drawable "+(int)gridView.getItemAtPosition(position);
                            dialog.cancel();
                            dialog.dismiss();
                        }else{
                            if (ContextCompat.checkSelfPermission(
                                    getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_GRANTED) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                            } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                        MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
                            } else {
                                requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                        MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();

            }
        });
        repetition_days_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_day_of_week);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final CheckBox cb1 = (CheckBox)dialog.findViewById(R.id.checkbox_1);
                final CheckBox cb2 = (CheckBox)dialog.findViewById(R.id.checkbox_2);
                final CheckBox cb3 = (CheckBox)dialog.findViewById(R.id.checkbox_3);
                final CheckBox cb4 = (CheckBox)dialog.findViewById(R.id.checkbox_4);
                final CheckBox cb5 = (CheckBox)dialog.findViewById(R.id.checkbox_5);
                final CheckBox cb6 = (CheckBox)dialog.findViewById(R.id.checkbox_6);
                final CheckBox cb7 = (CheckBox)dialog.findViewById(R.id.checkbox_7);

                if(repetition_days.getText().equals("Whole week")){
                    cb1.setChecked(true);
                    cb2.setChecked(true);
                    cb3.setChecked(true);
                    cb4.setChecked(true);
                    cb5.setChecked(true);
                    cb6.setChecked(true);
                    cb7.setChecked(true);
                    dayofweek = 7;
                }else{
                    String [] tokens = repetition_days.getText().toString().split(",");
                    for(int i=0;i<tokens.length;i++){
                        switch (tokens[i]){
                            case "M": cb1.setChecked(true);break;
                            case "T": cb2.setChecked(true);break;
                            case "W": cb3.setChecked(true);break;
                            case "Th": cb4.setChecked(true);break;
                            case "Fr": cb5.setChecked(true);break;
                            case "St": cb6.setChecked(true);break;
                            case "Sn": cb7.setChecked(true);break;
                        }
                    }
                }
                cb1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateRepetitionDay(cb1,0,"M");
                    }
                });
                cb2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateRepetitionDay(cb2,1,"T");
                    }
                });
                cb3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateRepetitionDay(cb3,2,"W");
                    }
                });
                cb4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateRepetitionDay(cb4,3,"Th");
                    }
                });
                cb5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateRepetitionDay(cb5,4,"Fr");
                    }
                });
                cb6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateRepetitionDay(cb6,5,"St");
                    }
                });
                cb7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateRepetitionDay(cb7,6,"Sn");
                    }
                });


                dialog.show();
            }
        });

        start_repetition_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimepicker(start_repetition,start_repetition_time);
            }
        });

        end_repetition_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimepicker(end_repetition,end_repetition_time);
            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String habitName = edt_habit_name.getText().toString();
                String type = typeValue.getText().toString();
                float dateTrainning = edt_tranning_Days.getValue();
                String description = edt_description.getText().toString();

                //set starting date
                start_repetition_time.set(Calendar.YEAR,startingDate.get(Calendar.YEAR));
                start_repetition_time.set(Calendar.MONTH,startingDate.get(Calendar.MONTH));
                start_repetition_time.set(Calendar.DAY_OF_MONTH,startingDate.get(Calendar.DAY_OF_MONTH));
                //set starting date
                end_repetition_time.set(Calendar.YEAR,startingDate.get(Calendar.YEAR));
                end_repetition_time.set(Calendar.MONTH,startingDate.get(Calendar.MONTH));
                end_repetition_time.set(Calendar.DAY_OF_MONTH,startingDate.get(Calendar.DAY_OF_MONTH));

                presenter.handleInsertHabit(getApplicationContext(),habitName,type,startingDate,dateTrainning,thumbnail,checkDay,start_repetition_time,end_repetition_time,description);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edt_tranning_Days.setOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value) {
                int num = (int)value;
                daysTrainning_value.setText(num +  " Days");
            }
        });
    }


    private void showTimepicker(final TextView textView, final Calendar time){
        int Hour=time.get(Calendar.HOUR);
        final int min=time.get(Calendar.MINUTE);
        int second = time.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textView.setText(hourOfDay+":"+minute);
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);
                time.set(Calendar.SECOND, 0);
            }
        },Hour,min,true);
        timePickerDialog.show();
    }

    private void updateRepetitionDay(CheckBox cb,int dayofweek_int,String dayofweek_String){
        if(cb.isChecked()){
            dayofweek++;
            checkDay.set(dayofweek_int,true);
            if(dayofweek==7){
                repetition_days.setText("Whole week");
            }else{
                if(dayofweek == 1){
                    repetition_days.setText(dayofweek_String);
                }else{
                    repetition_days.setText(repetition_days.getText()+","+dayofweek_String);
                }
            }
        }else{
            dayofweek--;
            checkDay.set(dayofweek_int,false);
            if(dayofweek==0){
                dayofweek++;
                cb.setChecked(true);
                checkDay.set(dayofweek_int,true);
                return;
            }
            String str=removeDay(dayofweek_int);
            repetition_days.setText(str);
        }
    }


    private String removeDay(int index){
        String res ="";
        for(int i=0;i<DAY_OF_WEEK;i++){
            if(checkDay.get(i)){
                switch (i){
                    case 0: res+="M,";break;
                    case 1: res+="T,";break;
                    case 2: res+="W,";break;
                    case 3: res+="Th,";break;
                    case 4: res+="Fr,";break;
                    case 5: res+="St,";break;
                    case 6: res+="Sn,";break;
                }
            }
        }
        res=res.substring(0,res.length()-1); //remove ',' in last index
        return res;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == -1){
            final Uri imageUri = data.getData();
            thumbnail = imageUri.getLastPathSegment();
            Picasso.get().load(imageUri).resize(1024,768).into(btn_thumbnail);
        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        edt_habit_name=(TextInputEditText)findViewById(R.id.edt_habit_name);
        typeValue = (TextView)findViewById(R.id.type_value);
        typeChange = (TextView)findViewById(R.id.type_change);
        startingDays_value = (TextView)findViewById(R.id.startingDay_value);
        startingDays_change = (TextView)findViewById(R.id.startingDay_change);
        edt_tranning_Days = (Slider) findViewById(R.id.daysTrainning);
        btn_thumbnail = (ImageButton)findViewById(R.id.thumbnail);
        repetition_days = (TextView)findViewById(R.id.repetition_days);
        repetition_days_change = (TextView)findViewById(R.id.repetition_days_change);
        start_repetition = (TextView)findViewById(R.id.start_repetition);
        start_repetition_change  = (TextView)findViewById(R.id.start_repetition_change);
        end_repetition = (TextView)findViewById(R.id.end_repetition);
        end_repetition_change = (TextView)findViewById(R.id.end_repetition_change);
        edt_description = (EditText)findViewById(R.id.edt_description);
        btn_insert = (ImageButton)findViewById(R.id.btn_insert);
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        daysTrainning_value = (TextView)findViewById(R.id.daysTrainning_value);
    }

    @Override
    public void insertSuccess() {
        Toast.makeText(this, "insert success", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void insertFailure(int error) {
        switch (error){
            case 1:  Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();break;
        }
    }
}
