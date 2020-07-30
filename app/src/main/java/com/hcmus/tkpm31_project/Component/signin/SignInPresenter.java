package com.hcmus.tkpm31_project.Component.signin;


import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.tkpm31_project.Object.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SignInPresenter implements SignInContract.Presenter {

    private SignInContract.View mView;

    public void setView(SignInContract.View view){
        this.mView=view;
    }

    @Override
    public void handleSignIn(String username, final String password) {
        final FirebaseDatabase database =FirebaseDatabase.getInstance();
        final DatabaseReference ref=database.getReference();
        try {

            ref.child("user").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null){
                        mView.signinFailure(2);

                    }else{
                        User user = new User();
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            user = data.getValue(User.class);
                        }
                        if (user != null) {
                            if(BCrypt.checkpw(password,user.getPassword())){

                                mView.signinSuccess(user.getTotalLifeTime());
                            }else{
                                mView.signinFailure(3);
                            }
                        }
                    }
                    ref.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Log.e("Insert",e.getMessage());
        }
    }

    @Override
    public void handleAuth(final String email,final String phoneNumber) {
        final FirebaseDatabase database =FirebaseDatabase.getInstance();
        final DatabaseReference ref=database.getReference();
        try {

            ref.child("user").orderByChild("username").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue() != null){
                        User user =(User) dataSnapshot.getValue();
                        mView.authSuccess(email,user.getTotalLifeTime());
                    }else{

                        String key = ref.push().getKey();
                        User newUser= new User(email, BCrypt.hashpw("1234567",BCrypt.gensalt()),email,phoneNumber,0);
                        Map<String,Object> userValue= newUser.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/user/" +key, userValue);
                        ref.updateChildren(childUpdates);
                        mView.authSuccess(email,0);

                    }
                    ref.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            Log.e("Insert",e.getMessage());
        }
    }
}
