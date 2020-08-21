package com.hcmus.tkpm31_project.Component.signin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hcmus.tkpm31_project.Component.habitHome.HabitHomeActivity;
import com.hcmus.tkpm31_project.Object.User;
import com.hcmus.tkpm31_project.R;
import com.hcmus.tkpm31_project.Util.AlarmHelper;
import com.hcmus.tkpm31_project.Util.CurrentUser;

import java.util.Calendar;

public class SignInActivity extends AppCompatActivity implements SignInContract.View, View.OnClickListener {
    private LinearLayout linearLayout;
    private TextInputEditText edt_username;
    private TextInputEditText edt_password;
    private MaterialButton btn_signin;
    private MaterialButton btn_google_signin;
    private SignInPresenter presenter;
    private TextView txt_error;
    private ProgressBar progressBar;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 200;
    private FirebaseAuth mAuth;
    private CurrentUser curUser;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
        setContentView(R.layout.activity_sign_in);

        initView();
        registerListener();
        initPresenter();
    }



    private void initVariable() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        curUser=new CurrentUser(this);
        intent = new Intent(this,HabitHomeActivity.class);

    }

    private void initPresenter() {
        presenter = new SignInPresenter();
        presenter.setView(this);
    }

    private void registerListener() {
        btn_signin.setOnClickListener(this);
        btn_google_signin.setOnClickListener(this);
    }

    private void initView() {
        linearLayout=(LinearLayout)findViewById(R.id.signin_form);
        linearLayout.bringToFront();
        edt_username= (TextInputEditText)findViewById(R.id.edt_username);
        edt_password= (TextInputEditText)findViewById(R.id.edt_password);
        btn_signin = (MaterialButton)findViewById(R.id.btn_signin_submit);
        btn_google_signin=(MaterialButton)findViewById(R.id.btn_signin_google);
        txt_error=(TextView)findViewById(R.id.txt_error);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        progressBar.bringToFront();
    }

    @Override
    public void signinSuccess(int totalLifeTime,String createdDate) {
        progressBar.setVisibility(View.INVISIBLE);
        String username = edt_username.getText().toString();
        curUser.setCurrentUser(username);
        curUser.setTotallifetime(totalLifeTime);
        curUser.setFlatEverydayService(true);
        startMyService();
        finish();
        startActivity(intent);
    }

    private void startMyService() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        AlarmHelper alarmHelper = AlarmHelper.getInstanceAlarmHelper();
        alarmHelper.scheduleMyService(getApplicationContext(),calendar);
    }

    @Override
    public void signinFailure(int error) {
        progressBar.setVisibility(View.INVISIBLE);
        switch (error){
            case 1: txt_error.setText(R.string.msg_error_1);break;
            case 2: txt_error.setText(R.string.msg_error_7);break;
            case 3: txt_error.setText(R.string.msg_error_8);break;
        }
    }

    @Override
    public void authSuccess(String email,int totalLifeTime,String createdDate) {
        progressBar.setVisibility(View.INVISIBLE);
        curUser.setCurrentUser(email);
        curUser.setTotallifetime(totalLifeTime);
        curUser.setFlatEverydayService(true);
        finish();
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        txt_error.setText("");
        progressBar.setVisibility(View.VISIBLE);
        switch (v.getId()){
            case R.id.btn_signin_submit: signin();break;
            case R.id.btn_signin_google:signinWithGoogle(); break;
            default: break;
        }
    }

    private void signinWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== RC_SIGN_IN && resultCode == Activity.RESULT_OK) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }catch (Exception e) {
               Log.w("Google sign in fail","Google sign in failed",e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                presenter.handleAuth(user.getEmail(),user.getPhoneNumber());
                            }

                        } else {
                            txt_error.setText(R.string.msg_error_7);
                        }


                    }
                });
    }

    private void signin() {

        String username= edt_username.getText().toString();
        String password= edt_password.getText().toString();
        if(username.isEmpty() || password.isEmpty()){
            signinFailure(1);
        }else{
            presenter.handleSignIn(username,password);
        }
    }
}
