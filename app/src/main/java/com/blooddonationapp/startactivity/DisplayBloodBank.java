package com.blooddonationapp.startactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DisplayBloodBank extends AppCompatActivity  {

    Button search;
    String latitude;
    String longitude;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_blood_bank);

        Intent intent = getIntent();
        bloodBank bloodBank = intent.getParcelableExtra("Blood Bank");
        name = bloodBank.getName();
        String address = bloodBank.getAddress();
        longitude = bloodBank.getLongitude();
        latitude = bloodBank.getLatitude();




        TextView nameView = findViewById(R.id.DisplayBloodBank_TV_NameTitle);
        TextView addressView = findViewById(R.id.DisplayBloodBank_TV_LocationTitle);
        ImageView bloodBankImage = findViewById(R.id.DisplayBloodBank_ImageView_bloodBankImage);

        nameView.setText(name);
        addressView.setText(address);
        getImage(getApplicationContext(),bloodBankImage);


        search = findViewById(R.id.DisplayBloodBank_Button_Continue);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(latitude,longitude,name);
            }
        });




    }

    private void sendData(String latitude, String longitude, String name){
        Intent i = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("latitude", latitude);
        bundle.putString("longitude", longitude);
        bundle.putString("name",name);

        i.putExtras(bundle);
        startActivity(i);

    }

    private void getImage(Context context, ImageView image){

        FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://blood-donation-applicati-79711.appspot.com/");
        StorageReference ref = mStorage.getReference();
        StorageReference storageRef
                = ref
                .child(
                        "images/"
                                + name + ".jpg");

        try{
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)         //ALL or NONE as your requirement
                            .thumbnail(Glide.with(context).load(R.drawable.ic_image_loading))
                            .error(R.drawable.ic_image_error).into(image);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Glide.with(context)
                            .load(R.drawable.ic_image_error)
                            .fitCenter().into(image);

                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }

    }




}