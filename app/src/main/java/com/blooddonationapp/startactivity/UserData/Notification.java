package com.blooddonationapp.startactivity.UserData;

public class Notification {

    String date, title, description;



    public Notification(){}

    public Notification(String date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
