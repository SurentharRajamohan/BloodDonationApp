package com.blooddonationapp.startactivity.UserData;

import java.util.ArrayList;

public class User {

    private String firstName,latitude,longitude,bloodGroup, image;
    private int points;
    private boolean isAdmin;
    private double distance;


    public User(String firstName, String latitude, String longitude,String bloodGroup) {
        this.firstName = firstName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bloodGroup = bloodGroup;
    }



    public User( ) {

    }

    public double getDistance() {
        return distance;
    }

    public double setDistance(double distance) {
        this.distance = distance;
        return distance;
    }

    public String getImage() {
        return image;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

}
