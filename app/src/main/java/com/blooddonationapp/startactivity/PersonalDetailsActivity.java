package com.blooddonationapp.startactivity;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.Calendar;
import java.util.UUID;

public class PersonalDetailsActivity extends AppCompatActivity {


    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    //firebase variables (database)
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    //View instances
    ImageView uploadPhoto;
    Button submitButton;
    Spinner bloodGroupInput, genderInput;
    EditText dateOfBirthInput, firstNameInput, lastNameInput, addressInput, phoneNumberInput, emailAddressInput, regionCountryInput;

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
        submitButton = findViewById(R.id.PersonalDetails_BTN_register);
        firstNameInput = findViewById(R.id.PersonalDetailsPage_ET_firstName);
        lastNameInput = findViewById(R.id.PersonalDetailsPage_ET_lastName);
        addressInput = findViewById(R.id.PersonalDetailsPage_ET_address);
        phoneNumberInput = findViewById(R.id.PersonalDetailsPage_ET_phoneNumber);
        emailAddressInput = findViewById(R.id.PersonalDetailsPage_ET_email);
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
        final String email = emailAddressInput.getText().toString();
        final String dateOfBirth = dateOfBirthInput.getText().toString();
        final String address = addressInput.getText().toString();
        final String phoneNumber = phoneNumberInput.getText().toString();
        final String country = regionCountryInput.getText().toString();
        final String gender = genderInput.getSelectedItem().toString();
        final String bloodGroup = bloodGroupInput.getSelectedItem().toString();

        Intent intent = getIntent();
        final String username = intent.getExtras().getString("Username");
        //exceptions
        if (email.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
        else if (firstName.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
        else if (username.isEmpty())
            Toast.makeText(PersonalDetailsActivity.this, "Username Illeda", Toast.LENGTH_SHORT).show();
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
        else {

            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //check for duplicate email address

                    databaseReference.child("users").child(username).child("firstName").setValue(firstName);
                    databaseReference.child("users").child(username).child("lastName").setValue(lastName);
                    databaseReference.child("users").child(username).child("dateOfBirth").setValue(dateOfBirth);
                    databaseReference.child("users").child(username).child("address").setValue(address);
                    databaseReference.child("users").child(username).child("firstName").setValue(firstName);
                    databaseReference.child("users").child(username).child("lastName").setValue(lastName);
                    databaseReference.child("users").child(username).child("dateOfBirth").setValue(dateOfBirth);
                    databaseReference.child("users").child(username).child("address").setValue(address);
                    databaseReference.child("users").child(username).child("phoneNumber").setValue(phoneNumber);
                    databaseReference.child("users").child(username).child("country").setValue(country);
                    databaseReference.child("users").child(username).child("gender").setValue(gender);
                    databaseReference.child("users").child(username).child("bloodGroup").setValue(bloodGroup);
//                    finish();


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
                                        + UUID.randomUUID().toString());

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
}


//IGNORE I NEED THIS FOR LATER USE
//     Select Image method
//    private void SelectImage() {
//
//        // Defining Implicit Intent to mobile gallery
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//
//        startActivityForResult(
//                Intent.createChooser(
//                        intent,
//                        "Select Image from here..."),
//                PICK_IMAGE_REQUEST);
//    }

//     Override onActivityResult method
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode,
//                resultCode,
//                data);
//
//        // checking request code and result code
//        // if request code is PICK_IMAGE_REQUEST and
//        // resultCode is RESULT_OK
//        // then set image in the image view
//        if (requestCode == PICK_IMAGE_REQUEST
//                && resultCode == RESULT_OK
//                && data != null
//                && data.getData() != null) {
//
//            // Get the Uri of data
//            filePath = data.getData();
//            try {
//
//                // Setting image on image view using Bitmap
//                Bitmap bitmap = MediaStore
//                        .Images
//                        .Media
//                        .getBitmap(
//                                getContentResolver(),
//                                filePath);
//                uploadPhoto.setImageBitmap(bitmap);
//
//                uploadPhoto.setMinimumWidth(300);
//                uploadPhoto.setMinimumHeight(300);
//            }
//
//            catch (IOException e) {
//                // Log the exception
//                e.printStackTrace();
//            }
//        }
//    }