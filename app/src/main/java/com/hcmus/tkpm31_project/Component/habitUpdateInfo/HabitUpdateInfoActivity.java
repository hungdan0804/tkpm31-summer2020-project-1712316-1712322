package com.hcmus.tkpm31_project.Component.habitUpdateInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.hcmus.tkpm31_project.Adapter.HabitThumbnailAdapter;
import com.hcmus.tkpm31_project.Adapter.HabitTypeListViewAdapter;
import com.hcmus.tkpm31_project.Component.initializeHabit.InitializeHabitActivity;
import com.hcmus.tkpm31_project.Component.initializeHabit.InitializeHabitPresenter;
import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.Reminder;
import com.hcmus.tkpm31_project.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HabitUpdateInfoActivity extends AppCompatActivity implements HabitUpdateInfoContract.View{

    private final int DAY_OF_WEEK = 7;
    private TextView edt_habit_name;
    private TextView txt_type;
    private TextView btn_type;
    private TextView txt_startingDate;
    private TextView txt_dayTrainning;
    private ImageButton btn_thumbnail;
    private TextView txt_repetition_day;
    private TextView txt_start_repetition_time;
    private TextView txt_end_repetition_time;
    private EditText edt_description;
    private ImageButton btn_update;
    private ImageButton btn_back;
    private int habitID;
    private HabitUpdateInfoPresenter presenter;
    private Habit curHabit = null;
    private Context context;
    private String thumbnail="";
    private int RESULT_LOAD_IMG = 200;
    private final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_update_info);


        initVariable();
        initView();
        registerListener();
    }

    private void registerListener() {
        btn_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createHabitTypeDialog(HabitUpdateInfoActivity.this);
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
                        txt_type.setText(msg);
                        curHabit.set_type(msg);
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });
                HabitTypeListViewAdapter adapter=new HabitTypeListViewAdapter(context);
                lv_habit_types.setAdapter(adapter);
                dialog.show();
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
                            curHabit.setImageUri(thumbnail);
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
       btn_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
       btn_update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String description = edt_description.getText().toString();
               curHabit.setDescription(description);
               presenter.HandleUpdateData(context,curHabit);
           }
       });
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
            curHabit.setImageUri(thumbnail);
        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void initVariable() {
        Intent intent = getIntent();
        habitID = intent.getIntExtra("habitID",0);
        presenter = new HabitUpdateInfoPresenter();
        presenter.setmView(this);
        context = this;
    }

    private void initView() {
        edt_habit_name = (TextView)findViewById(R.id.edt_habit_name);
        txt_type = (TextView)findViewById(R.id.type_value);
        btn_type = (TextView)findViewById(R.id.type_change);
        txt_startingDate = (TextView)findViewById(R.id.startingDay_value);
        txt_dayTrainning = (TextView)findViewById(R.id.daysTrainning_value);
        btn_thumbnail = (ImageButton)findViewById(R.id.thumbnail);
        txt_repetition_day = (TextView)findViewById(R.id.repetition_days);
        txt_start_repetition_time =(TextView)findViewById(R.id.start_repetition);
        txt_end_repetition_time = (TextView)findViewById(R.id.end_repetition);
        edt_description = (EditText)findViewById(R.id.edt_description);
        btn_update = (ImageButton)findViewById(R.id.btn_update);
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        presenter.HandleLoadData(getApplicationContext(),habitID);
    }

    @Override
    public void updateUI(Habit habit, List<Reminder> reminders) {
        curHabit =habit;
        edt_habit_name.setText(curHabit.get_habitName());
        txt_type.setText(curHabit.get_type());
        TimeZone timeZoneUTC = TimeZone.getDefault();
        int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi","VN"));
        txt_startingDate.setText(simpleFormat.format(curHabit.get_startingDate()));
        txt_dayTrainning.setText(curHabit.get_daysTraining()+" Days");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String []path = habit.getImageUri().split(" ");
                if(habit.getImageUri().split(" ")[0].equals("Drawable")){
                    int imgDrawable = Integer.parseInt(path[1]);
                    Picasso.get().load(imgDrawable).resize(1024,768).into(btn_thumbnail);
                }else{
                    File imgFile = new  File(habit.getImageUri());
                    if(imgFile.exists()){
                        Picasso.get().load(Uri.fromFile(imgFile)).resize(1024,768).into(btn_thumbnail);
                    }else {
                        Toast.makeText(getApplicationContext(), "Can't open file", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if(reminders.size() == 7){
            txt_repetition_day.setText("Whole week");
        }else{
            String res ="";
            for(Reminder r :reminders){
                switch (r.get_daysOfWeek()){
                    case Calendar.MONDAY: res+="M,";break;
                    case Calendar.TUESDAY: res+="T,";break;
                    case Calendar.WEDNESDAY: res+="W,";break;
                    case Calendar.THURSDAY: res+="Th,";break;
                    case Calendar.FRIDAY: res+="Fr,";break;
                    case Calendar.SATURDAY: res+="St,";break;
                    case Calendar.SUNDAY: res+="Sn,";break; }
            }
            res=res.substring(0,res.length()-1); //remove ',' in last index
            txt_repetition_day.setText(res);
        }

        txt_start_repetition_time.setText(habit.get_startingTime());
        txt_end_repetition_time.setText(habit.get_endingTime());
        edt_description.setText(habit.getDescription());
    }

    @Override
    public void updatedSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
