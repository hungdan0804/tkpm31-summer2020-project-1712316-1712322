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
    private List<Integer> data_thumbnail;

    public HabitThumbnailAdapter(Context context){
        this.context=context;
        this.data_thumbnail=new ArrayList<>();
        loadData();
    }

    private void loadData() {
        data_thumbnail.add(R.drawable.forest);
        data_thumbnail.add(R.drawable.img_working);
        data_thumbnail.add(R.drawable.img_sport);
        data_thumbnail.add(R.drawable.img_eating);
        data_thumbnail.add(R.drawable.img_socializing);
        data_thumbnail.add(R.drawable.img_entertainment);
        data_thumbnail.add(R.drawable.img_other);
        data_thumbnail.add(R.drawable.ic_add_black_24dp);
    }

    @Override
    public int getCount() {
        return data_thumbnail.size();
    }

    @Override
    public Object getItem(int position) {
        return data_thumbnail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data_thumbnail.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = (View) LayoutInflater.from(context)
                .inflate(R.layout.custom_grid_item_habit_thumbnail, parent, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.gridView_item);
        imageView.setImageResource(data_thumbnail.get(position));
        return view;
    }
}
