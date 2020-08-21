package com.hcmus.tkpm31_project.Component.signin;

public interface SignInContract {
    interface View {
        void signinSuccess(int totalLifeTime,String createdDate);
        void signinFailure(int error);
        void authSuccess(String email,int totalLifeTime,String createdDate);
    }
    interface Presenter{
        void handleSignIn(String username,String password);
        void handleAuth(String email,String phoneNumber);
    }
}
