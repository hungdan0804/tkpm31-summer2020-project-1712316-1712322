package com.hcmus.tkpm31_project.Component.signin;

public interface SignInContract {
    interface View {
        void signinSuccess();
        void signinFailure(int error);
    }
    interface Presenter{
        void handleSignIn(String username,String password);
    }
}
