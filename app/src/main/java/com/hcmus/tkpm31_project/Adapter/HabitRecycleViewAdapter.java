package com.hcmus.tkpm31_project.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.tkpm31_project.Component.habitHome.HabitHomeActivity;
import com.hcmus.tkpm31_project.Component.habitInfo.HabitInfoActivity;
import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HabitRecycleViewAdapter extends RecyclerView.Adapter<HabitRecycleViewAdapter.MyViewHolder> {
    private List<Habit> mDataset;
    private Context context;
    private HabitHomeActivity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public TextView imgView;
        public LinearLayout linearLayout;
        public TextView spendingTimeTxt;
        public ImageButton btn_favorite;
        public ImageButton btn_expand;
        public View view;
        public MyViewHolder(View v) {
            super(v);
            textView = (TextView)v.findViewById(R.id.txt);
            imgView = (TextView)v.findViewById(R.id.img);
            linearLayout = (LinearLayout)v.findViewById(R.id.description);
            spendingTimeTxt=(TextView)v.findViewById(R.id.spendingTime);
            btn_favorite = (ImageButton)v.findViewById(R.id.btn_favorite);
            btn_expand=(ImageButton)v.findViewById(R.id.btn_expand);
            view=v;

        }
    }
    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RecyclerView.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HabitRecycleViewAdapter(List<Habit> myDataset, Context context, Activity activity) {
        this.mDataset = myDataset;
        this.context = context;
        this.activity =(HabitHomeActivity) activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HabitRecycleViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_layout_habit, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ImageView img = new ImageView(context);
        holder.imgView.setText(mDataset.get(position).get_habitName());
        String []path = mDataset.get(position).getImageUri().split(" ");
        if(mDataset.get(position).getImageUri().split(" ")[0].equals("Drawable")){
            int imgDrawable = Integer.parseInt(path[1]);
            Picasso.get().load(imgDrawable).resize(2048,1152).into(img, new Callback() {
                @Override
                public void onSuccess() {
                    holder.imgView.setBackground(img.getDrawable());
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("Error");
                }
            });
        }else{
            File imgFile = new  File(mDataset.get(position).getImageUri());
            if(imgFile.exists()){
                Picasso.get().load(Uri.fromFile(imgFile)).resize(2048,1152).into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.imgView.setBackground(img.getDrawable());
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println("Error");
                    }
                });
            }else {
                Toast.makeText(context, "Can't open file", Toast.LENGTH_SHORT).show();
            }
        }

        holder.textView.setText(mDataset.get(position).getDescription());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        int hours = 0;
        int mins = 0;
        try {
            Date start = format.parse(mDataset.get(position).get_startingTime());
            Date end = format.parse(mDataset.get(position).get_endingTime());
            long diff = end.getTime() - start.getTime(); //diff in milisecond
            diff /= 1000; // diff in second

            hours =(int) diff / 3600;
            int remainder = (int) diff - hours *3600;
            mins = remainder / 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String str ="";
        if(hours > 0){
            str += "+ " + hours+"h "+mins+"m Lifetime";
        }else{
            str += "+ " +mins+"m Lifetime";
        }
        holder.spendingTimeTxt.setText(str);


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.linearLayout.getVisibility() == View.GONE){
                    holder.btn_expand.setImageResource(R.drawable.ic_expand_black_24dp);
                    expand(holder.linearLayout);
                }else{
                    holder.btn_expand.setImageResource(R.drawable.ic_expand_more_24dp);
                    collapse(holder.linearLayout);
                }
            }
        });

        String transitionName = mDataset.get(position).get_habitName();
        ViewCompat.setTransitionName(holder.imgView,transitionName);

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context,HabitInfoActivity.class);
                intent.putExtra("habitID",mDataset.get(position).get_habitID());
                intent.putExtra("transitionName",transitionName);
                intent.putExtra("habitThumbnail",mDataset.get(position).getImageUri());
                intent.putExtra("habitThumbnail_text",mDataset.get(position).get_habitName());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,(View)holder.imgView,ViewCompat.getTransitionName(holder.imgView));
                activity.startActivity(intent,options.toBundle());
                return true;
            }
        });

        holder.btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}