package com.hcmus.tkpm31_project.Component.Intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.hcmus.tkpm31_project.Component.habitHome.HabitHomeActivity;
import com.hcmus.tkpm31_project.Component.signin.SignInActivity;
import com.hcmus.tkpm31_project.Component.signup.SignUpActivity;
import com.hcmus.tkpm31_project.R;
import com.hcmus.tkpm31_project.Util.CurrentUser;

public class FlashIntroActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_signin;
    private Button btn_signup;
    private CurrentUser curUser;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
        if(curUser.getCurrentUser()!=null){
            finish();
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        initView();
        registerListener();
    }

    private void initVariable() {
        curUser=new CurrentUser(this);
        intent = new Intent(this, HabitHomeActivity.class);
    }

    private void registerListener() {
        btn_signin.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
    }

    private void initView() {
        btn_signin=(Button)findViewById(R.id.btn_signin);
        btn_signup=(Button)findViewById(R.id.btn_signup);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup: startActivity(new Intent(this, SignUpActivity.class));break;
            case R.id.btn_signin: startActivity(new Intent(this, SignInActivity.class));break;
            default: break;
        }
    }
}
