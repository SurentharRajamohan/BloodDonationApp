package com.blooddonationapp.startactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class AddAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        //creating variables for each element in the login page

        final EditText emailInput = findViewById(R.id.AddAdmin_ET_email);
        final EditText usernameInput = findViewById(R.id.AddAdmin_ET_username);
        final EditText passwordInput = findViewById(R.id.AddAdmin_ET_password);
        final EditText confirmPasswordInput = findViewById(R.id.AddAdmin_ET_confirmPassword);
        final EditText firstNameInput = findViewById(R.id.AddAdmin_ET_firstName);
        final EditText lastNameInput = findViewById(R.id.AddAdmin_ET_lastName);
        final EditText addressInput = findViewById(R.id.AddAdmin_ET_address);
        final EditText countryInput = findViewById(R.id.AddAdmin_ET_country);
        final EditText phoneNumberInput = findViewById(R.id.AddAdmin_ET_phoneNumber);
        final Button registerButton = findViewById(R.id.AddAdmin_BTN_register);


        //firebase variables (database)

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        //Toolbar back button
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.AddAdmin_TBMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //onClickListeners

        //1) Register Button

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailInput.getText().toString();
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                final String confirmPassword = confirmPasswordInput.getText().toString();
                final String firstName = firstNameInput.getText().toString();
                final String lastName = lastNameInput.getText().toString();
                final String address = addressInput.getText().toString();
                final String country = countryInput.getText().toString();
                final String phoneNumber = phoneNumberInput.getText().toString();

                //Pattern for phone number
                Pattern phoneNumberPattern = Pattern.compile("(011[0-9]{8}|015[0-9]{8}|01[0-9]{8}|0[0-9]{9})");

                //exceptions
                if (email.isEmpty())
                    Toast.makeText(AddAdminActivity.this, "Please enter the admin's email address", Toast.LENGTH_SHORT).show();
                else if (username.isEmpty())
                    Toast.makeText(AddAdminActivity.this, "Please enter the admin's username", Toast.LENGTH_SHORT).show();
                else if (password.isEmpty())
                    Toast.makeText(AddAdminActivity.this, "Please enter the admin's password", Toast.LENGTH_SHORT).show();
                else if (confirmPassword.isEmpty())
                    Toast.makeText(AddAdminActivity.this, "Please enter the admin's password", Toast.LENGTH_SHORT).show();
                else if (firstName.isEmpty())
                    Toast.makeText(AddAdminActivity.this, "Please enter the admin's firstname", Toast.LENGTH_SHORT).show();
                else if (lastName.isEmpty())
                    Toast.makeText(AddAdminActivity.this, "Please enter the admin's lastname", Toast.LENGTH_SHORT).show();
                else if (address.isEmpty())
                    Toast.makeText(AddAdminActivity.this, "Please enter the admin's address", Toast.LENGTH_SHORT).show();
                else if (country.isEmpty())
                    Toast.makeText(AddAdminActivity.this, "Please enter the admin's country", Toast.LENGTH_SHORT).show();
                else if (phoneNumber.isEmpty())
                    Toast.makeText(AddAdminActivity.this, "Please enter the admin's phone number", Toast.LENGTH_SHORT).show();
                else if (!(password.equals(confirmPassword)))
                    Toast.makeText(AddAdminActivity.this, "Entered passwords are not matching", Toast.LENGTH_SHORT).show();
                else if (!(password.length() >= 8))
                    Toast.makeText(AddAdminActivity.this, "Make sure the password length is >= 8", Toast.LENGTH_SHORT).show();
                else if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches()))
                    Toast.makeText(AddAdminActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                else if (!(phoneNumberPattern.matcher(phoneNumber).matches()))
                    Toast.makeText(AddAdminActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();

                    //write data to database
                else {

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //check for duplicate email address
                            if (snapshot.hasChild(username))
                                Toast.makeText(AddAdminActivity.this, "This username has been taken", Toast.LENGTH_SHORT).show();
                            else {
//                                // Generate a random UUID (Universally Unique Identifier)
                                UUID uuid = UUID.randomUUID();

                                String userID = uuid.toString();

                                databaseReference.child("users").child(username).child("Email").setValue(email);
                                databaseReference.child("users").child(username).child("userID").setValue(userID);
                                databaseReference.child("users").child(username).child("Password").setValue(password);
                                databaseReference.child("users").child(username).child("isAdmin").setValue(true);
                                databaseReference.child("users").child(username).child("country").setValue(country);
                                databaseReference.child("users").child(username).child("lastName").setValue(lastName);
                                databaseReference.child("users").child(username).child("firstName").setValue(firstName);
                                databaseReference.child("users").child(username).child("phoneNumber").setValue(phoneNumber);

                                databaseReference.child("users").child(username).child("LatLng").setValue(getLocationFromAddress(getApplicationContext(), address));
                                databaseReference.child("users").child(username).child("address").setValue(address);

                                databaseReference.child("users").child(username).child("bloodGroup").setValue("Null");
                                databaseReference.child("users").child(username).child("gender").setValue("Null");
                                databaseReference.child("users").child(username).child("dateOfBirth").setValue("Null");
                                databaseReference.child("users").child(username).child("points").setValue(0);

                                Toast.makeText(AddAdminActivity.this, "Successfully added admin user", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }


            }
        });

    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            Toast.makeText(context, "This address is invalid", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

        return p1;
    }
}