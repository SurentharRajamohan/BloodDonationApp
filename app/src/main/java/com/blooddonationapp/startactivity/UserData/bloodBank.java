package com.blooddonationapp.startactivity.UserData;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class bloodBank implements Parcelable {

    private String name, address, bloodRequested;
    private String longitude, latitude;

    // added by Faris for homepage
    private String date, time, state;

    public bloodBank(){}

    public bloodBank(String name, String address, String longitude, String latitude, String date, String time, String bloodRequested, String state) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
        this.bloodRequested = bloodRequested;
        this.state = state;
    }

    protected bloodBank(Parcel in) {
        name = in.readString();
        address = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static final Creator<bloodBank> CREATOR = new Creator<bloodBank>() {
        @Override
        public bloodBank createFromParcel(Parcel in) {
            return new bloodBank(in);
        }

        @Override
        public bloodBank[] newArray(int size) {
            return new bloodBank[size];
        }
    };

    public String getBloodRequested() {
        return bloodRequested;
    }

    public void setBloodRequested(String bloodRequested) {
        this.bloodRequested = bloodRequested;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(longitude);
        parcel.writeString(latitude);

    }
}
