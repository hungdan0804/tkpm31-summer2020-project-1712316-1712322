package com.hcmus.tkpm31_project.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.tkpm31_project.R;

import java.util.ArrayList;
import java.util.List;

public class HabitTypeListViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> titles;
    private List<Integer> thumbnails;

    public HabitTypeListViewAdapter(Context context){
            this.context=context;
            this.titles = new ArrayList<>();
            this.thumbnails=new ArrayList<>();
            loadData();
    }

    private void loadData() {
        titles.add("Work");
        titles.add("Sport");
        titles.add("Eating");
        titles.add("Socializing");
        titles.add("Entertainment");
        titles.add("Others");

        thumbnails.add(R.drawable.ic_business_center_24dp);
        thumbnails.add(R.drawable.ic_fitness_center_24dp);
        thumbnails.add(R.drawable.ic_room_service_24dp);
        thumbnails.add(R.drawable.ic_people_24dp);
        thumbnails.add(R.drawable.ic_videogame_asset_24dp);
        thumbnails.add(R.drawable.ic_others_24dp);
    }


    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_item_habit_types, parent, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.lv_item_image);
        TextView textView = (TextView)view.findViewById(R.id.lv_item_msg);

        imageView.setImageResource(thumbnails.get(position));
        textView.setText(titles.get(position));

        return view;
    }
}
