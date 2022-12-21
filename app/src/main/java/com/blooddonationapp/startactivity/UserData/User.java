package com.blooddonationapp.startactivity.UserData;

public class User {

    private String name,latitude,longitude,bloodtype;

    public User(String name, String latitude, String longitude,String bloodtype) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bloodtype = bloodtype;
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

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBloodType() {
        return bloodtype;
    }

    public void setBloodType(String bloodType) {
        this.bloodtype = bloodType;
    }
}
