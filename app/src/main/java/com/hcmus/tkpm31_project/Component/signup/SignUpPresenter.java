package com.hcmus.tkpm31_project.Component.signup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.tkpm31_project.Object.User;

import java.util.HashMap;
import java.util.Map;

public class SignUpPresenter implements SignUpContract.Presenter{

    private SignUpContract.View mView;

    public void setView(SignUpContract.View view){
        this.mView=view;
    }


    @Override
    public void handleSignUp(final String username, final String password, String conf_password, final String email, final String phoneNumber) {
        final FirebaseDatabase database =FirebaseDatabase.getInstance();
        final DatabaseReference ref=database.getReference();
        if(password.compareTo(conf_password)!=0){
            mView.signupFailure(2);
        }
        if(password.length() < 7){
            mView.signupFailure(3);
        }
        if(!email.contains("@")){
            mView.signupFailure(4);
        }
        if(username.length() < 7){
            mView.signupFailure(5);
        }
        try {

            ref.child("user").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue() != null){
                        mView.signupFailure(6);

                    }else{

                        String key = ref.push().getKey();
                        User newUser= new User(username,password,email,phoneNumber);
                        Map<String,Object> userValue= newUser.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/user/" +key, userValue);
                        ref.updateChildren(childUpdates);
                        mView.signupSuccess();

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
