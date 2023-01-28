package com.blooddonationapp.startactivity;

import android.content.Intent;
import android.os.Bundle;

import com.blooddonationapp.startactivity.UserData.Notification;
import com.blooddonationapp.startactivity.UserData.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;

import com.blooddonationapp.startactivity.databinding.ActivityAddEventBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddEventActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAddEventBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //firebase variables (database)


        //Toolbar back button
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.AddEvent_TB_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //declaring the variables
        final EditText eventNameInput = findViewById(R.id.AddEvent_ET_eventName);
        final EditText locationInput = findViewById(R.id.AddEvent_ET_location);
        final EditText dateInput = findViewById(R.id.AddEvent_ET_date);
        final EditText timeInput = findViewById(R.id.AddEvent_ET_time);
        final Button submit = findViewById(R.id.AddEvent_BTN_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String eventName = eventNameInput.getText().toString();
                final String location = locationInput.getText().toString();
                final String date = dateInput.getText().toString();
                final String time = timeInput.getText().toString();

                if (eventName.isEmpty() || location.isEmpty() || date.isEmpty() || time.isEmpty()) {
                    Toast.makeText(AddEventActivity.this, "Please complete all details", Toast.LENGTH_SHORT).show();
                } else {
                    Notification notification = new Notification(date, eventName, "Location: " + location + " " + "Time: " + time);
                    loadUser(notification);
                    Toast.makeText(AddEventActivity.this, "Succesfully added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddEventActivity.this, MainActivity.class));
                }


            }
        });


    }

    private void addEvent(Notification notification, ArrayList<String> list) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");

        for (int i = 0; i < list.size(); i++) {
//
            databaseReference = firebaseDatabase.getReference("notification").child(list.get(i));
            databaseReference.push().setValue(notification);
        }


    }


    private void loadUser(Notification notification) {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("users");

        ArrayList<String> list = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);


                    list.add(user.getUserID());


                }
                addEvent(notification, list);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}