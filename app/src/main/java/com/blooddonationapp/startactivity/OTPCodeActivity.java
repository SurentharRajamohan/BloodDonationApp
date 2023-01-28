package com.blooddonationapp.startactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OTPCodeActivity extends AppCompatActivity implements NewPasswordDialogBox.NewPasswordDialogBoxListener {

    Button confirm;
    PinView pinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_code);


        confirm = findViewById(R.id.OTPPage_BTN_Confirm);
        pinView = findViewById(R.id.OTPPage_PINVIEW_Pincode);

        Intent intent = getIntent();
        final String tacCode = intent.getExtras().getString("TACCode");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tacCode.equals(pinView.getText().toString())) {
                    NewPasswordDialogBox newPasswordDialogBox = new NewPasswordDialogBox();
                    newPasswordDialogBox.show(getSupportFragmentManager(), "New Password Dialog");

                }
            }
        });

    }

    @Override
    public void setNewPassword(String newPassword) {
        //firebase variables (database)
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        SharedPreferences sharedPref = getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
        String userName = sharedPref.getString("username", "User");

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //update changes
                databaseReference.child("users").child(userName).child("Password").setValue(newPassword);
                Toast.makeText(OTPCodeActivity.this, "Password has been changed successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(OTPCodeActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}