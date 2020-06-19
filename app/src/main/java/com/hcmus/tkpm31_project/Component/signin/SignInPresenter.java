package com.hcmus.tkpm31_project.Component.signin;


public class SignInPresenter implements SignInContract.Presenter {

    private SignInContract.View mView;

    public void setView(SignInContract.View view){
        this.mView=view;
    }

    @Override
    public void handleSignIn(String username, String password) {

    }
}
