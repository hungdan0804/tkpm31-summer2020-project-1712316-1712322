package com.hcmus.tkpm31_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hcmus.tkpm31_project.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HabitSumaryTypeAdapter extends BaseAdapter {
    private List<String> titles;
    private List<String> data;
    private List<Integer> thumbnails;
    private List<Integer> colors;
    private Context context;

    public HabitSumaryTypeAdapter(Context context,List<String> data){
        this.context = context;
        this.titles = new ArrayList<>();
        this.thumbnails = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.data=data;

        loadData();
    }

    private void loadData() {
        titles.add("Work");
        titles.add("Sport");
        titles.add("Eating");
        titles.add("Socializing");
        titles.add("Entertainment");
        titles.add("Others");

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
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_grid_item_habit_type_sumary, parent, false);

        LinearLayout gv_container = (LinearLayout)view.findViewById(R.id.gv_container);
        ImageView gv_thumbnail = (ImageView)view.findViewById(R.id.gv_thumbnail);
        TextView gv_title = (TextView)view.findViewById(R.id.gv_title);
        TextView gv_data = (TextView)view.findViewById(R.id.gv_data);

        gv_container.setBackgroundResource(colors.get(position));
        gv_thumbnail.setImageResource(thumbnails.get(position));
        gv_title.setText(titles.get(position));
        gv_data.setText(data.get(position));
        return view;
    }
}
