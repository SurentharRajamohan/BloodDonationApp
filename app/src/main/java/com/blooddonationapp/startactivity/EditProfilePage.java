package com.blooddonationapp.startactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditProfilePage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner genderSpinner, bloodTypeSpinner;
    private EditText dateChoice, firstName, lastName, address, phoneNumber, email, country; //EditProfile_editText_dateChoice
    final Calendar myCalendar = Calendar.getInstance();
    private Button submit;


    //firebase variables (database)
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.EditProfile_TBMainAct);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        //Shared preference instance
        SharedPreferences sharedPref = getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
        String userNameV = sharedPref.getString("username", "User");
        String firstNameV = sharedPref.getString("firstName", "Null");
        String lastNameV = sharedPref.getString("lastName", "Null");
        String addressV = sharedPref.getString("address", "Null");
        String phoneNumberV = sharedPref.getString("phoneNumber", "Null");
        String emailV = sharedPref.getString("email", "Null");
        String genderV = sharedPref.getString("gender", "Null");
        String dateOfBirthV = sharedPref.getString("dateOfBirth", "Null");
        String countryV = sharedPref.getString("country", "Null");
        String bloodGroupV = sharedPref.getString("bloodGroup", "Null");


        //initialize the edit text
        firstName = findViewById(R.id.EditProfile_editText_firstName);
        lastName = findViewById(R.id.EditProfile_editText_lastName);
        address = findViewById(R.id.EditProfile_editText_houseAddress);
        phoneNumber = findViewById(R.id.EditProfile_editText_phoneNumber);
        email = findViewById(R.id.EditProfile_editText_email);
        country = findViewById(R.id.EditProfile_editText_country);

        //initialize submit
        submit = findViewById(R.id.EditProfile_button_confirmSelection);

        //set data that was earlier saved in the database
        firstName.setText(firstNameV);
        lastName.setText(lastNameV);
        address.setText(addressV);
        phoneNumber.setText(phoneNumberV);
        email.setText(emailV);
        country.setText(countryV);

        // To initialize the spinner
        genderSpinner = findViewById(R.id.EditProfile_spinner_genderChoice);
        ArrayAdapter<CharSequence> adapterGenderSpinner = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapterGenderSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapterGenderSpinner);
        genderSpinner.setOnItemSelectedListener(this);

        //set data in spinners
        if (genderV.equalsIgnoreCase("male")) {
            genderSpinner.setSelection(0);
        } else if (genderV.equalsIgnoreCase("female")) {
            genderSpinner.setSelection(1);
        } else {
            genderSpinner.setSelection(2);
        }

        // Date picker
        dateChoice = findViewById(R.id.EditProfile_editText_dateChoice);
        dateChoice.setText(dateOfBirthV);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        dateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditProfilePage.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // To initialize the spinner
        bloodTypeSpinner = findViewById(R.id.EditProfile_spinner_bloodTypeChoice);
        ArrayAdapter<CharSequence> adapterBloodTypeSpinner = ArrayAdapter.createFromResource(this, R.array.bloodGroup, android.R.layout.simple_spinner_item);
        adapterBloodTypeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeSpinner.setAdapter(adapterBloodTypeSpinner);
        bloodTypeSpinner.setOnItemSelectedListener(this);


        if (bloodGroupV.equalsIgnoreCase("A+")) {
            bloodTypeSpinner.setSelection(0);
        } else if (bloodGroupV.equalsIgnoreCase("A-")) {
            bloodTypeSpinner.setSelection(1);
        } else if (bloodGroupV.equalsIgnoreCase("B+")) {
            bloodTypeSpinner.setSelection(2);
        } else if (bloodGroupV.equalsIgnoreCase("B-")) {
            bloodTypeSpinner.setSelection(3);
        } else if (bloodGroupV.equalsIgnoreCase("O+")) {
            bloodTypeSpinner.setSelection(4);
        } else if (bloodGroupV.equalsIgnoreCase("O-")) {
            bloodTypeSpinner.setSelection(5);
        } else if (bloodGroupV.equalsIgnoreCase("AB+")) {
            bloodTypeSpinner.setSelection(6);
        } else {
            bloodTypeSpinner.setSelection(7);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                Toast.makeText(EditProfilePage.this, "Successfully updated data", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    // UploadImage method
    private void updateData() {

        String firstNameV = firstName.getText().toString();
        String lastNameV = lastName.getText().toString();
        String emailV = email.getText().toString();
        String dateOfBirth = dateChoice.getText().toString();
        String addressV = address.getText().toString();
        String phoneNumberV = phoneNumber.getText().toString();
        String countryV = country.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();
        String bloodGroup = bloodTypeSpinner.getSelectedItem().toString();

        SharedPreferences sharedPref = getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "User");
        //exceptions
        if (emailV.isEmpty())
            Toast.makeText(EditProfilePage.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
        else if (firstNameV.isEmpty())
            Toast.makeText(EditProfilePage.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
        else if (username.isEmpty())
            Toast.makeText(EditProfilePage.this, "No username", Toast.LENGTH_SHORT).show();
        else if (lastNameV.isEmpty())
            Toast.makeText(EditProfilePage.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
        else if (dateOfBirth.isEmpty())
            Toast.makeText(EditProfilePage.this, "Please enter your date of birth", Toast.LENGTH_SHORT).show();
        else if (addressV.isEmpty())
            Toast.makeText(EditProfilePage.this, "Please enter your address", Toast.LENGTH_SHORT).show();
        else if (phoneNumberV.isEmpty())
            Toast.makeText(EditProfilePage.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        else if (countryV.isEmpty())
            Toast.makeText(EditProfilePage.this, "Please enter your country/region", Toast.LENGTH_SHORT).show();
        else if (gender.isEmpty())
            Toast.makeText(EditProfilePage.this, "Please enter your gender", Toast.LENGTH_SHORT).show();
        else if (bloodGroup.isEmpty())
            Toast.makeText(EditProfilePage.this, "Please enter your blood group", Toast.LENGTH_SHORT).show();
        else {

            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //update changes
                    databaseReference.child("users").child(username).child("firstName").setValue(firstNameV);
                    databaseReference.child("users").child(username).child("lastName").setValue(lastNameV);
                    databaseReference.child("users").child(username).child("dateOfBirth").setValue(dateOfBirth);
                    databaseReference.child("users").child(username).child("Email").setValue(emailV);
                    databaseReference.child("users").child(username).child("address").setValue(addressV);
                    databaseReference.child("users").child(username).child("phoneNumber").setValue(phoneNumberV);
                    databaseReference.child("users").child(username).child("country").setValue(countryV);
                    databaseReference.child("users").child(username).child("gender").setValue(gender);
                    databaseReference.child("users").child(username).child("bloodGroup").setValue(bloodGroup);
                    databaseReference.child("users").child(username).child("LatLng").setValue(getLocationFromAddress(getApplicationContext(), addressV));

                    SharedPreferences sharedPref = getSharedPreferences("userCredentials", 0);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    //saving data in SharedPreferences
                    editor.putString("bloodGroup", bloodGroup);
                    editor.putString("firstName", firstNameV);
                    editor.putString("lastName", lastNameV);
                    editor.putString("phoneNumber", phoneNumberV);
                    editor.putString("address", addressV);
                    editor.putString("country", countryV);
                    editor.putString("email", emailV);
                    editor.putString("gender", gender);
                    editor.putString("dateOfBirth", dateOfBirth);
                    editor.putString("latitude", String.valueOf(databaseReference.child("users").child(username).child("LatLng").child("latitude")));
                    editor.putString("longitude", String.valueOf(databaseReference.child("users").child(username).child("LatLng").child("longitude")));

                    editor.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
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

            ex.printStackTrace();
        }

        return p1;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateChoice.setText(dateFormat.format(myCalendar.getTime()));
    }


    // Logic for when the items are selected within the spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        String text = adapterView.getItemAtPosition(i).toString();
//        text += " has been selected.";
//        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}