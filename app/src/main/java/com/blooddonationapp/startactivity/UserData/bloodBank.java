package com.blooddonationapp.startactivity.UserData;

import android.os.Parcel;
import android.os.Parcelable;

public class bloodBank implements Parcelable {

    private String name, address;

    public bloodBank(){}

    public bloodBank(String name, String address) {
        this.name = name;
        this.address = address;
    }

    protected bloodBank(Parcel in) {
        name = in.readString();
        address = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
    }
}
