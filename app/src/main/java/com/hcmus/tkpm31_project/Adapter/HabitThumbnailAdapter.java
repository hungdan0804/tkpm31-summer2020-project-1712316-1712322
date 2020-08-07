package com.hcmus.tkpm31_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hcmus.tkpm31_project.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HabitThumbnailAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> thumbnails;

    public HabitThumbnailAdapter(Context context){
        this.context=context;
        this.thumbnails=new ArrayList<>();
        loadData();
    }

    private void loadData() {
        thumbnails.add(R.drawable.forest);
        thumbnails.add(R.drawable.img_working);
        thumbnails.add(R.drawable.img_sport);
        thumbnails.add(R.drawable.img_eating);
        thumbnails.add(R.drawable.img_socializing);
        thumbnails.add(R.drawable.img_entertainment);
        thumbnails.add(R.drawable.img_other);
        thumbnails.add(R.drawable.ic_add_black_24dp);
    }

    @Override
    public int getCount() {
        return thumbnails.size();
    }

    @Override
    public Object getItem(int position) {
        return thumbnails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return thumbnails.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = (View) LayoutInflater.from(context)
                .inflate(R.layout.custom_grid_item_habit_thumbnail, parent, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.gridView_item);
        imageView.setImageResource(thumbnails.get(position));
        return view;
    }
}
