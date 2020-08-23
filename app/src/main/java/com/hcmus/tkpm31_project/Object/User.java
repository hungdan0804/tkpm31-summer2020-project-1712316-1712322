package com.hcmus.tkpm31_project.Object;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class User {
    protected String username;
    protected String password;
    protected String email;
    protected String phoneNumber;
    protected int totalLifeTime;
    protected String createdDate;

    public User(){

    }

    public User(String username, String password, String email, String phoneNumber,int totalLifeTime) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.totalLifeTime=totalLifeTime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        createdDate = sdf.format(calendar.getTime());

    }

    public int getTotalLifeTime() {
        return totalLifeTime;
    }

    public void setTotalLifeTime(int totalLifeTime) {
        this.totalLifeTime = totalLifeTime;
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
        return phoneNumber;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNumber = phoneNum;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> res=new HashMap<>();
        res.put("username",this.username);
        res.put("password",this.password);
        res.put("email",this.email);
        res.put("phoneNumber",this.phoneNumber);
        res.put("totalLifeTime",this.totalLifeTime);
        res.put("createdDate",this.createdDate);
        return res;
    }
}
