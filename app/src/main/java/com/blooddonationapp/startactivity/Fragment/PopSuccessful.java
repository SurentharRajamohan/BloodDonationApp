package com.blooddonationapp.startactivity.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

import com.blooddonationapp.startactivity.MainActivity;
import com.blooddonationapp.startactivity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class PopSuccessful extends Activity {

    private DatabaseReference databaseReference;
    private String DonorName;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popsuccessfulrequest);

        DisplayMetrics dn = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dn);

        int width = dn.widthPixels;
        int height = dn.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        Bundle bundle = getIntent().getExtras();
        DonorName = bundle.getString("DonorName");
        SharedPreferences sharedPreferences = getSharedPreferences("userCredentials",0);
        String user = sharedPreferences.getString("username", "");


        copyFirebaseData(user, DonorName);
        goToHome();





    }

    public void copyFirebaseData(String Admin, String DonorName) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("request").child(Admin).child("pending");
        Query selectedQuery = databaseReference.orderByChild("donor").equalTo(DonorName);
        final DatabaseReference toDonor = firebaseDatabase.getReference("request").child(DonorName).child("pending");

        selectedQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot complete : dataSnapshot.getChildren()) {
                    String completeKey = complete.getKey();
                    String date = complete.child("date").getValue(String.class);
                    String name = complete.child("name").getValue(String.class);
                    String time = complete.child("time").getValue(String.class);
                    String status = complete.child("status").getValue(String.class);
                    String donor = complete.child("donor").getValue(String.class);
                    toDonor.child(completeKey).child("date").setValue(date);
                    toDonor.child(completeKey).child("name").setValue(name);
                    toDonor.child(completeKey).child("time").setValue(time);
                    toDonor.child(completeKey).child("status").setValue(status);
                    toDonor.child(completeKey).child("donor").setValue(donor);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void goToHome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(PopSuccessful.this, MainActivity.class);
                startActivity(mainIntent);
                finish();

            }
        }, 2000);
    }

}