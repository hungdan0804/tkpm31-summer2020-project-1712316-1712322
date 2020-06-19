package com.hcmus.tkpm31_project.Object;

import java.util.HashMap;
import java.util.Map;

public class User {
    protected String username;
    protected String password;
    protected String email;
    protected String phoneNum;

    public User(){

    }

    public User(String username, String password, String email, String phoneNum) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> res=new HashMap<>();
        res.put("username",this.username);
        res.put("password",this.password);
        res.put("email",this.email);
        res.put("phoneNumber",this.phoneNum);
        return res;
    }
}
