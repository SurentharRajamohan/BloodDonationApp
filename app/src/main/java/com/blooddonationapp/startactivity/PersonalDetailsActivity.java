package com.blooddonationapp.startactivity;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class PersonalDetailsActivity extends AppCompatActivity {


    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    //firebase variables (database)
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    //View instances
    ImageView uploadPhoto, invisiblePhoto;
    Button submitButton;
    Spinner bloodGroupInput, genderInput;
    EditText dateOfBirthInput, firstNameInput, lastNameInput, addressInput, phoneNumberInput, regionCountryInput;
//    EditText emailAddressInput;
    //datePicker instance
    private DatePickerDialog datePickerDialog;

    //Method instance to choose photo from gallery
    ActivityResultLauncher<String> choosePhoto;
    // Uri indicates, where the image will be picked from
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        //declaring variables for each element
        dateOfBirthInput = findViewById(R.id.PersonalDetailsPage_ET_dateOfBirth);
        genderInput = findViewById(R.id.PersonalDetailsPage_Spinner_gender);
        bloodGroupInput = findViewById(R.id.PersonalDetailsPage_Spinner_bloodGroup);
        uploadPhoto = findViewById(R.id.PersonalDetailsPage_IV_profilePhoto);
        invisiblePhoto = findViewById(R.id.PersonalDetailsPage_IV_invisibleProfilePicture);
        submitButton = findViewById(R.id.PersonalDetails_BTN_register);
        firstNameInput = findViewById(R.id.PersonalDetailsPage_ET_firstName);
        lastNameInput = findViewById(R.id.PersonalDetailsPage_ET_lastName);
        addressInput = findViewById(R.id.PersonalDetailsPage_ET_address);
        phoneNumberInput = findViewById(R.id.PersonalDetailsPage_ET_phoneNumber);
//        emailAddressInput = findViewById(R.id.PersonalDetailsPage_ET_email);
        regionCountryInput = findViewById(R.id.PersonalDetailsPage_ET_country);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        //setting up spinners

        //1) Gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender,
                android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderInput.setAdapter(genderAdapter);
        genderInput.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        //2) Blood group spinner
        ArrayAdapter<CharSequence> bloodGroupAdapter = ArrayAdapter.createFromResource(this,
                R.array.bloodGroup,
                android.R.layout.simple_spinner_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupInput.setAdapter(bloodGroupAdapter);
        bloodGroupInput.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        //Method to choose photo from gallery
        choosePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        filePath = result;
                        ViewGroup.LayoutParams layoutParams = uploadPhoto.getLayoutParams();
                        layoutParams.width = 300;
                        layoutParams.height = 300;
                        uploadPhoto.setLayoutParams(layoutParams);
                        uploadPhoto.setImageURI(result);
                    }
                }
        );

        //onClickListeners

        //1) Pop up calendar when date picker is selected
        dateOfBirthInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date picker dialog
                datePickerDialog = new DatePickerDialog(PersonalDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                        dateOfBirthInput.setText(Day + "/" + (Month + 1) + "/" + Year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        //2) Upload Photo
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto.launch("image/*");
            }
        });

        //3) Submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });

    }


    // UploadImage method
    private void uploadData() {

        final String firstName = firstNameInput.getText().toString();
        final String lastName = lastNameInput.getText().toString();
//        final String email = emailAddressInput.getText().toString();
        final String dateOfBirth = dateOfBirthInput.getText().toString();
        final String address = addressInput.getText().toString();
        final String phoneNumber = phoneNumberInput.getText().toString();
        final String country = regionCountryInput.getText().toString();
        final String gender = genderInput.getSelectedItem().toString();
        final String bloodGroup = bloodGroupInput.getSelectedItem().toString();

        Intent intent = getIntent();
        final String username = intent.getExtras().getString("Username");
        final String email = intent.getExtras().getString("Email");
        final String password = intent.getExtras().getString("Password");
        final String isAdmin = intent.getExtras().getString("isAdmin");

    //Pattern for phone number
        Pattern phoneNumberPattern = Pattern.compile("(011[0-9]{8}|015[0-9]{8}|01[0-9]{8}|0[0-9]{9})");

        //exceptions
//        if (email.isEmpty())
//            Toast.makeText(PersonalDetailsActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
        if (firstName.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
        else if (username.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "No username", Toast.LENGTH_SHORT).show();
        else if (lastName.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
        else if (dateOfBirth.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "Please enter your date of birth", Toast.LENGTH_SHORT).show();
        else if (address.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
        else if (phoneNumber.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        else if (country.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "Please enter your country/region", Toast.LENGTH_SHORT).show();
        else if (gender.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "Please enter your gender", Toast.LENGTH_SHORT).show();
        else if (bloodGroup.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "Please enter your blood group", Toast.LENGTH_SHORT).show();
        else if (filePath == null)
            Toast.makeText(PersonalDetailsActivity.this, "Please upload your picture", Toast.LENGTH_SHORT).show();
        else if(!(phoneNumberPattern.matcher(phoneNumber).matches()))
            Toast.makeText(PersonalDetailsActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        else {

            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UUID uuid = UUID.randomUUID();
                    String userID = uuid.toString();

                    //check for duplicate email address
                    databaseReference.child("users").child(username).child("Email").setValue(email);
                    databaseReference.child("users").child(username).child("userID").setValue(userID);
                    databaseReference.child("users").child(username).child("Password").setValue(password);
                    databaseReference.child("users").child(username).child("isAdmin").setValue(false);
                    databaseReference.child("users").child(username).child("firstName").setValue(firstName);
                    databaseReference.child("users").child(username).child("lastName").setValue(lastName);
                    databaseReference.child("users").child(username).child("dateOfBirth").setValue(dateOfBirth);
                    databaseReference.child("users").child(username).child("address").setValue(getLocationFromAddress(getApplicationContext(),address));
//                    databaseReference.child("users").child(username).child("address").setValue(address);
                    databaseReference.child("users").child(username).child("phoneNumber").setValue(phoneNumber);
                    databaseReference.child("users").child(username).child("country").setValue(country);
                    databaseReference.child("users").child(username).child("gender").setValue(gender);
                    databaseReference.child("users").child(username).child("bloodGroup").setValue(bloodGroup);
                    databaseReference.child("users").child(username).child("points").setValue(0);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            //Upload image to storage
            //If no URI is set (basically no image)
            if (filePath != null) {

                // Code for showing progressDialog while uploading
                ProgressDialog progressDialog
                        = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                // Defining the child of storageReference
                StorageReference ref
                        = storageReference
                        .child(
                                "images/"
                                        + username);

                // adding listeners on upload
                // or failure of image
                ref.putFile(filePath)
                        .addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(
                                            UploadTask.TaskSnapshot taskSnapshot) {

                                        // Image uploaded successfully
                                        // Dismiss dialog
                                        progressDialog.dismiss();
                                        Toast
                                                .makeText(PersonalDetailsActivity.this,
                                                        "Image Uploaded!!",
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                        Intent questionnaire_page = new Intent(PersonalDetailsActivity.this, Questionnaires.class);
                                        questionnaire_page.putExtra("username", username);
                                        startActivity(questionnaire_page);
                                    }
                                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast
                                        .makeText(PersonalDetailsActivity.this,
                                                "Failed " + e.getMessage(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {

                                    // Progress Listener for loading
                                    // percentage on the dialog box
                                    @Override
                                    public void onProgress(
                                            UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress
                                                = (100.0
                                                * taskSnapshot.getBytesTransferred()
                                                / taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage(
                                                "Uploaded "
                                                        + (int) progress + "%");
                                    }
                                });
            }
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
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}

