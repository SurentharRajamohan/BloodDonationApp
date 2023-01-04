package com.blooddonationapp.startactivity.UserData;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Request implements Serializable {
    String name,date,time,status;
    @Exclude
    private String key;

    public Request(){}

    public Request(String name, String date, String time, String status) {

        this.name = name;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public String getKey() {
        return key;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
