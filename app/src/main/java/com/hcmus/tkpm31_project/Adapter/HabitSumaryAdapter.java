package com.hcmus.tkpm31_project.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hcmus.tkpm31_project.Component.habitInfo.HabitInfoActivity;
import com.hcmus.tkpm31_project.Component.habitSumary.HabitSumaryActivity;
import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.R;
import com.hcmus.tkpm31_project.Util.DateHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HabitSumaryAdapter extends BaseAdapter {

    private List<Habit> habits;
    private List<Integer> thumbnails;
    private List<Integer> colors;
    private Context context;
    private Activity activity;

    public HabitSumaryAdapter(Context context, Activity activity, List<Habit> habits){
        this.context=context;
        this.habits= habits;
        this.thumbnails = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.activity=activity;
        loadData();
    }

    private void loadData() {

        thumbnails.add(R.drawable.ic_business_center_black_192dp);
        thumbnails.add(R.drawable.ic_fitness_center_white_192dp);
        thumbnails.add(R.drawable.ic_room_service_black_24dp);
        thumbnails.add(R.drawable.ic_people_white_192dp);
        thumbnails.add(R.drawable.ic_videogame_asset_white_192dp);
        thumbnails.add(R.drawable.ic_apps_white_192dp);

        colors.add(R.drawable.cool_sky_color);
        colors.add(R.drawable.alihossein_color);
        colors.add(R.drawable.red_sunset_color);
        colors.add(R.drawable.evening_sunshine_color);
        colors.add(R.drawable.quepal_color);
        colors.add(R.drawable.ultra_voilet);
    }

    @Override
    public int getCount() {
        return habits.size();
    }

    @Override
    public Object getItem(int position) {
        return habits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return habits.get(position).get_habitID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_grid_item_habit_type_sumary, parent, false);

        LinearLayout gv_container = (LinearLayout)view.findViewById(R.id.gv_container);
        ImageView gv_thumbnail = (ImageView)view.findViewById(R.id.gv_thumbnail);
        TextView gv_title = (TextView)view.findViewById(R.id.gv_title);
        TextView gv_data = (TextView)view.findViewById(R.id.gv_data);

        Habit temp = habits.get(position);
        switch (temp.get_type()){
                case "Work":
                    gv_container.setBackgroundResource(colors.get(0));
                    gv_thumbnail.setImageResource(thumbnails.get(0));
                    break;
                case "Sport":
                    gv_container.setBackgroundResource(colors.get(1));
                    gv_thumbnail.setImageResource(thumbnails.get(1));
                    break;
                case "Eating":
                    gv_container.setBackgroundResource(colors.get(2));
                    gv_thumbnail.setImageResource(thumbnails.get(2));
                    break;
                case "Socializing":
                    gv_container.setBackgroundResource(colors.get(3));
                    gv_thumbnail.setImageResource(thumbnails.get(3));
                    break;
                case "Entertainment":
                    gv_container.setBackgroundResource(colors.get(4));
                    gv_thumbnail.setImageResource(thumbnails.get(4));
                    break;
                case "Others":
                    gv_container.setBackgroundResource(colors.get(5));
                    gv_thumbnail.setImageResource(thumbnails.get(5));
                    break;
        }
        gv_title.setText(habits.get(position).get_habitName());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date start = format.parse(temp.get_startingTime());
            Date end = format.parse(temp.get_endingTime());
            long diff = end.getTime()-start.getTime();//diff in milisecond
            String str = DateHelper.TimeToString(diff);
            gv_data.setText(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Habit temp = habits.get(position);
                if(temp.isDelete()){
                    Toast.makeText(context, "Habit is not exist !!!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(context, HabitInfoActivity.class);
                    intent.putExtra("habitID",temp.get_habitID());
                    intent.putExtra("transitionName",temp.get_habitName());
                    intent.putExtra("habitThumbnail",temp.getImageUri());
                    intent.putExtra("habitThumbnail_text",temp.get_habitName());
                    activity.startActivity(intent);
                }
            }
        });
        gv_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Habit temp = habits.get(position);
                if(temp.isDelete()){
                    Toast.makeText(context, "Habit is not exist !!!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(context, HabitInfoActivity.class);
                    intent.putExtra("habitID",temp.get_habitID());
                    intent.putExtra("transitionName",temp.get_habitName());
                    intent.putExtra("habitThumbnail",temp.getImageUri());
                    intent.putExtra("habitThumbnail_text",temp.get_habitName());
                    activity.startActivity(intent);
                }
            }
        });
        return view;
    }
}
