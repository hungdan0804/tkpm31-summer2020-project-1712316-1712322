package com.hcmus.tkpm31_project.Component.habitInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.hcmus.tkpm31_project.Object.TrainingDays;
import com.hcmus.tkpm31_project.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HabitInfoActivity extends AppCompatActivity implements HabitInfoContract.View{

   private TextView share_img;
   private CalendarView calendarView;
   private List<EventDay> mEventDays = new ArrayList<>();
   private HabitInfoPresenter presenter;
   private int habitID;
   private String transitionName;
   private String imageUri;
   private String imageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_info);

        initVariable();
        initView();

        presenter.HandleLoadData(this,habitID);
    }

    private void initVariable() {
        presenter = new HabitInfoPresenter();
        presenter.setmView(this);
        Intent intent = getIntent();
        habitID = intent.getIntExtra("habitID",0);
        transitionName = intent.getStringExtra("transitionName");
        imageUri = intent.getStringExtra("habitThumbnail");
        imageText = intent.getStringExtra("habitThumbnail_text");


    }

    private void initView() {
        share_img = (TextView)findViewById(R.id.img);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        share_img.setTransitionName(transitionName);
        updateThumbnail();

    }

    private void updateThumbnail() {
        final ImageView img = new ImageView(this);
        String []path = imageUri.split(" ");
        share_img.setText(imageText);
        if(path[0].equals("Drawable")){
            int imgDrawable = Integer.parseInt(path[1]);
            Picasso.get().load(imgDrawable).noFade().resize(2048,1152).into(img, new Callback() {
                @Override
                public void onSuccess() {
                    share_img.setBackground(img.getDrawable());
                    supportStartPostponedEnterTransition();
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("Error");
                    supportStartPostponedEnterTransition();
                }
            });
        }else{
            File imgFile = new  File(imageUri);
            if(imgFile.exists()){
                Picasso.get().load(Uri.fromFile(imgFile)).noFade().resize(2048,1152).into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        share_img.setBackground(img.getDrawable());
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println("Error");
                        supportStartPostponedEnterTransition();
                    }
                });
            }else {
                Toast.makeText(this, "Can't open file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void UpdateUI(List<TrainingDays> data) {
        for(TrainingDays t : data){
            EventDay eventDay;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(t.get_createdDate());
            if(t.is_isMark()){
                eventDay = new EventDay(calendar,R.drawable.ic_radio_button_checked_black_24dp);
            }else{
                eventDay = new EventDay(calendar,R.drawable.ic_radio_button_unchecked_24dp);
            }
            mEventDays.add(eventDay);
        }
        calendarView.setEvents(mEventDays);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
