package com.hcmus.tkpm31_project.Component.habitHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hcmus.tkpm31_project.R;

public class HabitHomeActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private Toolbar toolbar;
    private CoordinatorLayout container;
    private LinearLayout main;
    private AppBarLayout appBarLayout;
    private LinearLayout sumary_box;
    private ImageView imageView;
    private SearchView searchView;
    private TextView app_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_home);

        initView();
        initVariable();
        registerListener();
    }

    private void initVariable() {
        loadFragment(new HabitFragment(getApplicationContext()));
    }

    private void registerListener() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset*-1 > appBarLayout.getTotalScrollRange()/10){
                    searchView.setVisibility(View.INVISIBLE);
                }else{
                    searchView.setVisibility(View.VISIBLE);
                }
                if(verticalOffset == 0){
                    Transition transition = new Fade();
                    transition.setDuration(400);
                    TransitionManager.beginDelayedTransition(sumary_box,transition);
                    sumary_box.setVisibility(View.VISIBLE);
                    app_title.setText(R.string.app_name);
                }else{
                    sumary_box.setVisibility(View.INVISIBLE);
                    app_title.setText("Today: 0");
                }
                if(verticalOffset*-1 < appBarLayout.getTotalScrollRange()){
                    container.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    main.setBackground(getResources().getDrawable(R.drawable.decor_main_content));
                }else{
                    main.setBackgroundColor(Color.WHITE);
                }
            }
        });
    }

    private void initView() {
        main= (LinearLayout)findViewById(R.id.frame_container);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        toolbar.bringToFront();
        appBarLayout =(AppBarLayout) findViewById(R.id.appbar);
        container =(CoordinatorLayout) findViewById(R.id.coordinate);
        sumary_box =(LinearLayout) findViewById(R.id.total_box);
        imageView = (ImageView)findViewById(R.id.icon_app);
        searchView = (SearchView)findViewById(R.id.search_view);
        app_title=(TextView)findViewById(R.id.app_title);
        Resources res = getResources();
        Bitmap src = BitmapFactory.decodeResource(res, R.drawable.icon_app);
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
        imageView.setImageDrawable(dr);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    fragment = new HabitFragment(getApplicationContext());
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_gifts:
                    fragment = new SpedingFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
