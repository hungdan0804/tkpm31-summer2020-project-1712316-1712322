package com.hcmus.tkpm31_project.Component.habitHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcmus.tkpm31_project.Adapter.ViewPagerAdapter;
import com.hcmus.tkpm31_project.Component.initializeHabit.InitializeHabitActivity;
import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.R;
import com.hcmus.tkpm31_project.Util.CurrentUser;

import java.util.List;

public class HabitHomeActivity extends AppCompatActivity implements HabitHomeContract.View{

    private BottomNavigationView navigation;
    private Toolbar toolbar;
    private CoordinatorLayout container;
    private LinearLayout main;
    private AppBarLayout appBarLayout;
    private LinearLayout sumary_box;
    private ImageView imageView;
    private SearchView searchView;
    private TextView app_title;
    private FloatingActionButton btn_insert;
    private ViewPager viewPager;
    private HabitHomePresenter presenter;
    private HabitDataRecievedListener recievedListener;
    private static TextView totalLifeTime_box;
    private static TextView todayLifeTime_box;
    private static CurrentUser curUser;
    public static boolean active = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_home);

        initVariable();
        initView();
        registerListener();
        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        active=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active=false;
    }



    public void setDataListener(HabitDataRecievedListener listener) {
        this.recievedListener = listener;
    }

    private void initVariable() {
        presenter = new HabitHomePresenter();
        presenter.setView(this);
        curUser=new CurrentUser(this);
        presenter.setCurrentUser(curUser);

    }


    private void registerListener() {
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
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), InitializeHabitActivity.class));
            }
        });
       navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_habit:
                        viewPager.setCurrentItem(0);

                        return true;
                    case R.id.navigation_spending:
                        viewPager.setCurrentItem(1);

                        return true;
                }
                return false;
            }
        });
       viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {
                switch (position){
                    case 0: navigation.getMenu().findItem(R.id.navigation_habit).setChecked(true);break;
                    case 1: navigation.getMenu().findItem(R.id.navigation_spending).setChecked(true);break;
                }
           }

           @Override
           public void onPageScrollStateChanged(int state) {

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
        totalLifeTime_box = (TextView)findViewById(R.id.sub_content_total);
        todayLifeTime_box = (TextView)findViewById(R.id.sub_content_today);
        Resources res = getResources();
        Bitmap src = BitmapFactory.decodeResource(res, R.drawable.icon_app);
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
        imageView.setImageDrawable(dr);
        btn_insert = (FloatingActionButton)findViewById(R.id.btn_insert_habit);
        viewPager =(ViewPager)findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        HabitFragment habitFragment = new HabitFragment(getApplicationContext());
        adapter.addFragment(habitFragment,"Habit");
        presenter.loadData(this);
        adapter.addFragment(new SpedingFragment(),"Habit");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.loadData(this);
    }


    @Override
    public void updateUI_Habit(List<Habit> data) {
        try {
            Thread.sleep(500);
            recievedListener.onDataReceived(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void updateUI() {
        long todayLifeTime = curUser.getTodayLifeTime();
        long totalLifeTime = curUser.getTotalLifeTime();
        todayLifeTime /= 1000; // diff in second
        totalLifeTime /= 1000;
        int hours_today =(int) todayLifeTime / 3600;
        int remainder_today = (int) todayLifeTime- hours_today *3600;
        int mins_today = remainder_today / 60;
        int hours_total =(int) totalLifeTime / 3600;
        int remainder_total = (int) totalLifeTime- hours_today *3600;
        int mins_total = remainder_today / 60;
        String str="";
        if(hours_today > 0) {
            str += hours_today + "h " + mins_today + "m";
        }else{
            str += mins_today + "m";
        }
        String str2="";
        if(hours_total > 0) {
            str2 += hours_total + "h " + mins_total + "m";
        }else{
            str2 += mins_total + "m";
        }
        todayLifeTime_box.setText(str);
        totalLifeTime_box.setText(str2);
    }
}
