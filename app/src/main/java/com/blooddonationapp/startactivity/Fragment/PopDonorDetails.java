package com.blooddonationapp.startactivity.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.Request;
import com.blooddonationapp.startactivity.UserData.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PopDonorDetails extends Activity {

    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private String user;
    private boolean isAdmin;
    private Calendar calendar;

    //DATE VARIABLES
    private SimpleDateFormat dateFormat;
    private String date;

    //TIME VARIABLE
    private String currentTime;

    private String DonorName;

    private Request newRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popdonordetailswindow);

        DisplayMetrics dn= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dn);

        int width=dn.widthPixels;
        int height=dn.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        Button btn= findViewById(R.id.requestbutton);





        sharedPreferences = getSharedPreferences("userCredentials", 0);
        isAdmin = sharedPreferences.getBoolean("isAdmin", false);

        Intent intent = getIntent();
        User donor = intent.getParcelableExtra("User");
        DonorName = donor.getFirstName();

        btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //write data to database
//                      Bundle bundle = getIntent().getExtras();
//                      DonorName = bundle.getString("DonorName");
                        user = sharedPreferences.getString("username", "");

                        addNewRequest(DonorName,user);


                         Intent intent = new Intent(PopDonorDetails.this, PopSuccessful.class);
                         Bundle bundle2 = new Bundle();
                        bundle2.putString("DonorName", DonorName);

                        intent.putExtras(bundle2);
                         startActivity(intent);







                }
            });
        }


    private String getCurrentDate() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        return date;
    }

    private void addNewRequest(String donor, String hospital) {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("request").child(hospital).child("pending");

        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String key = databaseReference.push().getKey();



        newRequest = new Request(hospital, getCurrentDate(),currentTime, "Request Pending",donor);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.child(key).child("name").setValue(newRequest.getName());
                databaseReference.child(key).child("date").setValue(newRequest.getDate());
                databaseReference.child(key).child("time").setValue(newRequest.getTime());
                databaseReference.child(key).child("status").setValue(newRequest.getStatus());
                databaseReference.child(key).child("donor").setValue(newRequest.getDonor());


            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }





}
