package com.blooddonationapp.startactivity.UserData;

public class User {

    private String name,latitude,longitude,bloodType;
    private int points;

    public User(String name, String latitude, String longitude,String bloodType) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bloodType = bloodType;
    }



    public User( ) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

}
