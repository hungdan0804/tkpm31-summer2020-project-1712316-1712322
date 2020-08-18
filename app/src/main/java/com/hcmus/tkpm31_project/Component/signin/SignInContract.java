package com.hcmus.tkpm31_project.Component.signin;

public interface SignInContract {
    interface View {
        void signinSuccess(int totalLifeTime);
        void signinFailure(int error);
        void authSuccess(String email,int totalLifeTime);
    }
    interface Presenter{
        void handleSignIn(String username,String password);
        void handleAuth(String email,String phoneNumber);
    }
}
