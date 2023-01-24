package com.blooddonationapp.startactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //creating variables for each element in the login page

        final EditText emailInput = findViewById(R.id.RegisterPage_ET_emailAddress);
        final EditText usernameInput = findViewById(R.id.RegisterPage_ET_username);
        final EditText passwordInput = findViewById(R.id.RegisterPage_ET_password);
        final EditText confirmPasswordInput = findViewById(R.id.RegisterPage_ET_confirmPassword);
        final Button registerButton = findViewById(R.id.PersonalDetails_BTN_register);
        final TextView loginNow = findViewById(R.id.RegisterPage_TV_loginNow);

        //firebase variables (database)

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();


        //onClickListeners

        //1) Register Button

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailInput.getText().toString();
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                final String confirmPassword = confirmPasswordInput.getText().toString();

                //exceptions
                if(email.isEmpty())
                    Toast.makeText(RegisterActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                else if(username.isEmpty())
                    Toast.makeText(RegisterActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                else if(username.contains(" "))
                    Toast.makeText(RegisterActivity.this, "Do not use space in username", Toast.LENGTH_SHORT).show();
                else if(password.isEmpty())
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                else if(confirmPassword.isEmpty())
                    Toast.makeText(RegisterActivity.this, "Please enter your password again", Toast.LENGTH_SHORT).show();
                else if(!(password.equals(confirmPassword)))
                    Toast.makeText(RegisterActivity.this, "Entered passwords are not matching", Toast.LENGTH_SHORT).show();
                else if(!(password.length() >= 8))
                    Toast.makeText(RegisterActivity.this, "Make sure the password length is >= 8", Toast.LENGTH_SHORT).show();
                else if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches()))
                    Toast.makeText(RegisterActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();

                //write data to database
                else{

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //check for duplicate email address
                            if(snapshot.hasChild(username))
                                Toast.makeText(RegisterActivity.this, "This username has been taken", Toast.LENGTH_SHORT).show();
                            else{
//                                // Generate a random UUID (Universally Unique Identifier)

//                                UUID uuid = UUID.randomUUID();
//
//                                String userID = uuid.toString();

//                                databaseReference.child("users").child(username).child("Email").setValue(email);
//                                databaseReference.child("users").child(username).child("userID").setValue(userID);
//                                databaseReference.child("users").child(username).child("Password").setValue(password);
//                                databaseReference.child("users").child(username).child("isAdmin").setValue(false);



                                Intent intent = new Intent(RegisterActivity.this, PersonalDetailsActivity.class);
                                intent.putExtra("Username", username);
                                intent.putExtra("Password", password);
                                intent.putExtra("Email", email);
                                intent.putExtra("isAdmin", false);
                                startActivity(intent);
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }


            }
        });

        //2) Login Now (Button)

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }




}