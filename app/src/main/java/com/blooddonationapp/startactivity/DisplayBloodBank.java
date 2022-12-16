package com.blooddonationapp.startactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.blooddonationapp.startactivity.UserData.bloodBank;

public class DisplayBloodBank extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_blood_bank);

        Intent intent = getIntent();
        bloodBank bloodBank = intent.getParcelableExtra("Blood Bank");
        String name = bloodBank.getName();
        String address = bloodBank.getAddress();

        TextView nameView = findViewById(R.id.DisplayBloodBank_TV_NameTitle);
        TextView addressView = findViewById(R.id.DisplayBloodBank_TV_LocationTitle);

        nameView.setText(name);
        addressView.setText(address);
    }
}