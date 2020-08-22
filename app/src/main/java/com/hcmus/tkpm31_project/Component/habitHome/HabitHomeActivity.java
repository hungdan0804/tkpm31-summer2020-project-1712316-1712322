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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcmus.tkpm31_project.Adapter.ViewPagerAdapter;
import com.hcmus.tkpm31_project.Component.Intro.FlashIntroActivity;
import com.hcmus.tkpm31_project.Component.habitSumary.HabitSumaryActivity;
import com.hcmus.tkpm31_project.Component.initializeHabit.InitializeHabitActivity;
import com.hcmus.tkpm31_project.Component.usageHome.PhoneUsageFragment;
import com.hcmus.tkpm31_project.Component.usageHome.PhoneUsageRanking;
import com.hcmus.tkpm31_project.Object.Habit;
import com.hcmus.tkpm31_project.Object.PhoneUsage;
import com.hcmus.tkpm31_project.R;
import com.hcmus.tkpm31_project.Receiver.AlarmReceiver;
import com.hcmus.tkpm31_project.Util.AlarmHelper;
import com.hcmus.tkpm31_project.Util.CurrentUser;
import com.hcmus.tkpm31_project.Util.DateHelper;
import com.hcmus.tkpm31_project.Util.UtilPhoneUsage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HabitHomeActivity extends AppCompatActivity implements HabitHomeContract.View{

    private BottomNavigationView navigation;
    private Toolbar toolbar;
    private CoordinatorLayout container;
    private LinearLayout main;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout sumary_box;
    private ImageView imageView;
    private SearchView searchView;
    private TextView app_title;
    private FloatingActionButton btn_insert;
    private ViewPager viewPager;
    private ImageButton btn_sumary;
    private ImageButton btn_signout;
    private ImageButton btn_top_10;
    private HabitHomePresenter presenter;
    private static HabitDataRecievedListener recievedListener;
    private static TextView totalLifeTime_box;
    private static TextView todayLifeTime_box;
    private static CurrentUser curUser;
    private String searchText ="";
    public static boolean active = false;
    private Context context = this;
    private float DEFAULT_HEIGHT_APPBAR;


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
    protected void onResume() {
        super.onResume();
        container.requestFocus();
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText=newText;
                recievedListener.onFilterData(newText);
                return false;
            }
        });


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset*-1 > appBarLayout.getTotalScrollRange()/10){
                    searchView.setVisibility(View.INVISIBLE);
                }else{
                    searchView.setVisibility(View.VISIBLE);
                    if(navigation.getMenu().findItem(R.id.navigation_spending).isChecked()){
                        searchView.setVisibility(View.GONE);
                        float heightDp = appBarLayout.getResources().getDisplayMetrics().heightPixels/6;
                        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
                        lp.height = (int)heightDp;
                    }
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
                    case 0: navigation.getMenu().findItem(R.id.navigation_habit).setChecked(true);
                    btn_sumary.setVisibility(View.VISIBLE);btn_top_10.setVisibility(View.GONE);
                    btn_insert.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                    CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
                    lp.height = (int)DEFAULT_HEIGHT_APPBAR;
                    updateUI();
                    break;
                    case 1: navigation.getMenu().findItem(R.id.navigation_spending).setChecked(true);
                    btn_sumary.setVisibility(View.GONE);btn_top_10.setVisibility(View.VISIBLE);
                    btn_insert.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    updateUI_Usage();
                    break;
                }
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });

       btn_sumary.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), HabitSumaryActivity.class);
               startActivity(intent);
           }
       });

       btn_signout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               curUser.setCurrentUser(null);
               curUser.setTodaylifetime(0);
               curUser.setTotallifetime(0);
               curUser.setFlatEverydayService(false);
               removeMyService();
               finish();
               Intent intent = new Intent(getApplicationContext(), FlashIntroActivity.class);
               startActivity(intent);
           }

           private void removeMyService() {
               AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
               Intent intent = new Intent(context, AlarmReceiver.class);
               PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 99999, intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
               alarmManager.cancel(pendingIntent);
           }
       });
       btn_top_10.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                startActivity(new Intent(HabitHomeActivity.this,PhoneUsageRanking.class));
           }
       });
    }


    private void initView() {
        main= (LinearLayout)findViewById(R.id.frame_container);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        toolbar.bringToFront();
        appBarLayout =(AppBarLayout) findViewById(R.id.appbar);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        DEFAULT_HEIGHT_APPBAR = lp.height;
        container =(CoordinatorLayout) findViewById(R.id.coordinate);
        sumary_box =(LinearLayout) findViewById(R.id.total_box);
        imageView = (ImageView)findViewById(R.id.icon_app);
        searchView = (SearchView)findViewById(R.id.search_view);
        app_title=(TextView)findViewById(R.id.app_title);
        totalLifeTime_box = (TextView)findViewById(R.id.sub_content_total);
        todayLifeTime_box = (TextView)findViewById(R.id.sub_content_today);
        btn_sumary = (ImageButton)findViewById(R.id.btn_sumary);
        btn_signout = (ImageButton)findViewById(R.id.btn_signout);
        btn_top_10 = (ImageButton)findViewById(R.id.btn_top_10);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
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
        PhoneUsageFragment phoneUsageFragment=new PhoneUsageFragment(getApplicationContext());
        adapter.addFragment(habitFragment,"Habit");
        adapter.addFragment(phoneUsageFragment,"Usage");





        presenter.loadData(this);
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
            recievedListener.onDataReceived(data,searchText);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateUI_Usage()  {
        PhoneUsageFragment phoneUsageFragment=new PhoneUsageFragment(context);
        //get created date of current user
        String createdDate=curUser.getCreatedDate();
        System.out.println("Created date:"+createdDate);
        String[] separated = createdDate.split("/");
        Calendar calendar=Calendar.getInstance();
        System.out.println("Month created:"+Integer.valueOf(separated[1]));

        calendar.set(Calendar.YEAR,Integer.valueOf(separated[2]));
        calendar.set(Calendar.MONTH,Integer.valueOf(separated[1])-1);

        calendar.set(Calendar.DATE,Integer.valueOf(separated[0]));
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);




        long startTime=calendar.getTimeInMillis();

        //now
        Calendar now=Calendar.getInstance();
        long endTime=now.getTimeInMillis();

        ArrayList<PhoneUsage>listUsage=phoneUsageFragment.getListUsage(startTime,endTime);
        long total=0;
        for(int i=0;i<listUsage.size();i++)
        {
            total+=listUsage.get(i).get_time();

        }
        String totalTimeUsage=UtilPhoneUsage.getDurationBreakdown(total);

        totalLifeTime_box.setText(totalTimeUsage);


        String todayTimeUsage=UtilPhoneUsage.getDurationBreakdown(phoneUsageFragment.getTotalTimeToDay());

        todayLifeTime_box.setText(todayTimeUsage);
    }
    public static void updateUI() {
        long todayLifeTime = curUser.getTodayLifeTime();
        long totalLifeTime = curUser.getTotalLifeTime();
        String str= DateHelper.TimeToString(todayLifeTime);
        String str2 =DateHelper.TimeToString(totalLifeTime);
        todayLifeTime_box.setText(str);
        totalLifeTime_box.setText(str2);
    }


}
