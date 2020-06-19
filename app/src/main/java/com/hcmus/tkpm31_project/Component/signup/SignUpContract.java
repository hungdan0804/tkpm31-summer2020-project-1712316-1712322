package com.hcmus.tkpm31_project.Component.signup;

public interface SignUpContract {
    interface View{
        void signupSuccess();
        void signupFailure(int error);
    }
    interface Presenter{
        void handleSignUp(String username,String password,String conf_password, String email, String phoneNumber);

    }
}
