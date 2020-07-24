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
    private List<String> data_msg;
    private List<Integer> data_thumbnail;

    public HabitTypeListViewAdapter(Context context){
            this.context=context;
            this.data_msg = new ArrayList<>();
            this.data_thumbnail=new ArrayList<>();
            loadData();
    }

    private void loadData() {
        data_msg.add("Work");
        data_msg.add("Sport");
        data_msg.add("Eating");
        data_msg.add("Socializing");
        data_msg.add("Entertainment");
        data_msg.add("Others");

        data_thumbnail.add(R.drawable.ic_business_center_24dp);
        data_thumbnail.add(R.drawable.ic_fitness_center_24dp);
        data_thumbnail.add(R.drawable.ic_room_service_24dp);
        data_thumbnail.add(R.drawable.ic_people_24dp);
        data_thumbnail.add(R.drawable.ic_videogame_asset_24dp);
        data_thumbnail.add(R.drawable.ic_others_24dp);
    }


    @Override
    public int getCount() {
        return data_msg.size();
    }

    @Override
    public Object getItem(int position) {
        return data_msg.get(position);
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

        imageView.setImageResource(data_thumbnail.get(position));
        textView.setText(data_msg.get(position));

        return view;
    }
}
