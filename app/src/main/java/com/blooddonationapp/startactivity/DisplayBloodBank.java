package com.blooddonationapp.startactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blooddonationapp.startactivity.UserData.bloodBank;

public class DisplayBloodBank extends AppCompatActivity  {

    Button search;
    String latitude;
    String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_blood_bank);

        Intent intent = getIntent();
        bloodBank bloodBank = intent.getParcelableExtra("Blood Bank");
        String name = bloodBank.getName();
        String address = bloodBank.getAddress();
        latitude = bloodBank.getLatitude();
        longitude = bloodBank.getLongitude();

        TextView nameView = findViewById(R.id.DisplayBloodBank_TV_NameTitle);
        TextView addressView = findViewById(R.id.DisplayBloodBank_TV_LocationTitle);

        nameView.setText(name);
        addressView.setText(address);

        search = findViewById(R.id.DisplayBloodBank_Button_Continue);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(latitude,longitude);
            }
        });




    }

    private void showMap() {

        Intent intent = new Intent(DisplayBloodBank.this, MapsActivity.class);
        startActivity(intent);
    }

    private void sendData(String latitude, String longitude){
        Intent i = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("latitude", latitude);
        bundle.putString("longitude", longitude);

        i.putExtras(bundle);
        startActivity(i);

    }

//    public String getLatitude() {
//        Intent intent = getIntent();
//        bloodBank bloodBank = intent.getParcelableExtra("Blood Bank");
//
//        String latitude = bloodBank.getLatitude();
//        return latitude;
//
//    }
//    public String getLongitude() {
//        Intent intent = getIntent();
//        bloodBank bloodBank = intent.getParcelableExtra("Blood Bank");
//
//        String longitude = bloodBank.getLongitude();
//        return longitude;
//
//    }
}