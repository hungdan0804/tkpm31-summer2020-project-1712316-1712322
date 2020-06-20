package com.hcmus.tkpm31_project.Component.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hcmus.tkpm31_project.Component.signin.SignInActivity;
import com.hcmus.tkpm31_project.R;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View{

    private LinearLayout linearLayout;
    private TextInputEditText edt_username;
    private TextInputEditText edt_password;
    private TextInputEditText edt_conf_password;
    private TextInputEditText edt_email;
    private TextInputEditText edt_phoneNum;
    private MaterialButton btn_signup_submit;
    private TextView txt_error;
    private SignUpPresenter signUpPresenter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
        registerListener();
        initPresenter();
    }

    private void initPresenter() {
        signUpPresenter= new SignUpPresenter();
        signUpPresenter.setView(this);
    }

    private void registerListener() {
        btn_signup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_error.setText("");
                progressBar.setVisibility(View.VISIBLE);
                signup();
            }
        });
    }

    private void signup() {
       String username = edt_username.getText().toString();
       String password = edt_password.getText().toString();
       String conf_password = edt_conf_password.getText().toString();
       String email = edt_email.getText().toString();
       String phoneNum = edt_phoneNum.getText().toString();
       if(username.isEmpty() || password.isEmpty()|| conf_password.isEmpty()|| email.isEmpty()||
       phoneNum.isEmpty()){
            signupFailure(1);
       }else{

            signUpPresenter.handleSignUp(username,password,conf_password,email,phoneNum);
       }
    }

    private void initView() {
        edt_username=(TextInputEditText)findViewById(R.id.edt_username);
        edt_password=(TextInputEditText)findViewById(R.id.edt_password);
        edt_conf_password=(TextInputEditText)findViewById(R.id.edt_conf_password);
        edt_email=(TextInputEditText)findViewById(R.id.edt_email);
        edt_phoneNum=(TextInputEditText)findViewById(R.id.edt_phoneNum);
        btn_signup_submit=(MaterialButton)findViewById(R.id.btn_signup_submit);
        txt_error=(TextView)findViewById(R.id.txt_error);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        progressBar.bringToFront();
        linearLayout=(LinearLayout)findViewById(R.id.signup_form);
        linearLayout.bringToFront();
    }



    @Override
    public void signupSuccess() {
        progressBar.setVisibility(View.INVISIBLE);
        finish();
        startActivity(new Intent(this, SignInActivity.class));
    }

    @Override
    public void signupFailure(int error) {
        progressBar.setVisibility(View.INVISIBLE);
        switch (error){
            case 1: checkEmptyField();txt_error.setText(R.string.msg_error_1);break;
            case 2: edt_conf_password.setError("CONFIRM PASSWORD DOES NOT MATCH"); txt_error.setText(R.string.msg_error_2);break;
            case 3: edt_password.setError("PASSWORD LESS THAN 7 CHARACTERS");txt_error.setText(R.string.msg_error_3);break;
            case 4: edt_email.setError("INVALID EMAIL ADDRESS");txt_error.setText(R.string.msg_erorr_4);break;
            case 5: edt_username.setError("USERNAME LESS THAN 7 CHARACTERS");txt_error.setText(R.string.msg_error_5);break;
            case 6: edt_username.setError("USERNAME ALREADY EXISTS");txt_error.setText(R.string.msg_error_6); break;
            default:break;
        }
    }

    private void checkEmptyField() {
        if(edt_username.getText().toString().isEmpty()){
            edt_username.setError("PLEASE FILL ALL FIELDS");
        }
        if(edt_password.getText().toString().isEmpty()){
            edt_password.setError("PLEASE FILL ALL FIELDS");
        }
        if(edt_conf_password.getText().toString().isEmpty()){
            edt_conf_password.setError("PLEASE FILL ALL FIELDS");
        }
        if(edt_email.getText().toString().isEmpty()){
            edt_email.setError("PLEASE FILL ALL FIELDS");
        }
        if(edt_phoneNum.getText().toString().isEmpty()){
            edt_phoneNum.setError("PLEASE FILL ALL FIELDS");
        }
    }
}
