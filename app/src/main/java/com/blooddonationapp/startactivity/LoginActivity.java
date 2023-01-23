package com.blooddonationapp.startactivity;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends AppCompatActivity implements ForgotPasswordDialogBox.ForgotPasswordDialogListener {


int TACcode;
String emailAddress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //creating variables for each element in the login page

        final EditText usernameInput = findViewById(R.id.LoginPage_ET_username);
        final EditText passwordInput = findViewById(R.id.LoginPage_ET_password);
        final Button loginButton = findViewById(R.id.LoginPage_BTN_login);
        final TextView registerNow = findViewById(R.id.LoginPage_TV_registerNow);
        final TextView forgotPassword = findViewById(R.id.LoginPage_TV_forgotPassword);



        //firebase variables (database)
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        //SharedPreference to pass username to other activities
        SharedPreferences sharedPref = this.getSharedPreferences("userCredentials", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        //onClickListeners

        //1) Login Button

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                //exceptions
                if(username.isEmpty())
                    Toast.makeText(LoginActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                else if(password.isEmpty())
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                else{

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //check if this username exist
                            if(snapshot.hasChild(username)){
                                //username present
                                //check if password is right
                                final String databasePassword = snapshot.child(username).child("Password").getValue(String.class);
                                final boolean isAdmin = (boolean) snapshot.child(username).child("isAdmin").getValue();
                                final String bloodGroup = snapshot.child(username).child("bloodGroup").getValue(String.class);
                                final String userID = snapshot.child(username).child("userID").getValue(String.class);
                                final int points = snapshot.child(username).child("points").getValue(Integer.class);
                                final String firstName = snapshot.child(username).child("firstName").getValue(String.class);
                                final String lastName = snapshot.child(username).child("lastName").getValue(String.class);
                                final String address = snapshot.child(username).child("address").getValue(String.class);

                                final double latitude = snapshot.child(username).child("LatLng").child("latitude").getValue(double.class);
                                final double longitude = snapshot.child(username).child("LatLng").child("longitude").getValue(double.class);

                                final String country= snapshot.child(username).child("country").getValue(String.class);
                                final String email = snapshot.child(username).child("Email").getValue(String.class);
                                final String gender = snapshot.child(username).child("gender").getValue(String.class);
                                final String dateOfBirth = snapshot.child(username).child("dateOfBirth").getValue(String.class);
                                final String phoneNumber = snapshot.child(username).child("phoneNumber").getValue(String.class);


                                if(databasePassword.equals(password)){
                                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                                        //saving data in SharedPreferences
                                        editor.putString("username", username);
                                        editor.putBoolean("isAdmin", isAdmin);
                                        editor.putString("bloodGroup", bloodGroup);
                                        editor.putString("userID", userID);
                                        editor.putString("firstName", firstName);
                                        editor.putString("lastName", lastName);
                                        editor.putString("phoneNumber", phoneNumber);
                                        editor.putString("address",  address);
                                        editor.putString("latitude", String.valueOf(latitude));
                                        editor.putString("longitude", String.valueOf(longitude));

                                        editor.putString("country", country);
                                        editor.putString("email", email);
                                        editor.putString("gender", gender);
                                        editor.putString("dateOfBirth", dateOfBirth);
                                        editor.putInt("points",points);
                                        editor.commit();


                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("username",username);
                                        intent.putExtra("isAdmin",isAdmin);
                                        startActivity(intent);
                                        finish();

                                }else{
                                    Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "Wrong username", Toast.LENGTH_SHORT).show();
                            }


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        //2) Register Now (Button)

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        //3) Forgot password (Button)
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });






    }

    public void openDialog(){
        ForgotPasswordDialogBox forgotPasswordDialogBox = new ForgotPasswordDialogBox();
        forgotPasswordDialogBox.show(getSupportFragmentManager(), "Forgot Password Dialog");
    }

    @Override
    public void sendTacCode(String userName) {

        //SharedPreference to pass username to other activities
        SharedPreferences sharedPref = this.getSharedPreferences("userCredentials", 0);
        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putString("username", userName);
        editor.commit();


        Random random = new Random();
        TACcode = random.nextInt(8999)+1000;


        //firebase variables (database)
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        //check if this username exist
        if(snapshot.hasChild(userName)){
            Toast.makeText(LoginActivity.this, "Surenthar Email : "+snapshot.child(userName).child("Email").getValue(String.class), Toast.LENGTH_SHORT).show();
            emailAddress = snapshot.child(userName).child("Email").getValue(String.class);
            String url = "https://blooddonationappsurenthar.000webhostapp.com/sendEmail.php";
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, OTPCodeActivity.class);
                    intent.putExtra("TACCode", String.valueOf(TACcode));
                    startActivity(intent);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", emailAddress);
                    params.put("code", ""+ String.valueOf(TACcode));
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
    }
}