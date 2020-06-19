package com.hcmus.tkpm31_project.Component.signin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.hcmus.tkpm31_project.R;

public class SignInActivity extends AppCompatActivity implements SignInContract.View {
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initView();
    }

    private void initView() {
        linearLayout=(LinearLayout)findViewById(R.id.signin_form);
        linearLayout.bringToFront();
    }

    @Override
    public void signinSuccess() {

    }

    @Override
    public void signinFailure(int error) {

    }
}
