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
import android.widget.Toast;

import com.blooddonationapp.startactivity.Fragment.HomeFragment;
import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.blooddonationapp.startactivity.Utils.DAOBloodBank;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DisplayBloodBank extends AppCompatActivity {

    Button search;
    String latitude;
    String longitude;
    String name;

    HomeFragment homeFragmentObject;

    DAOBloodBank dao;

    int bloodNumber_abminus = 0, bloodNumber_abplus = 0, bloodNumber_aminus = 0, bloodNumber_aplus = 0, bloodNumber_bminus, bloodNumber_bplus, bloodNumber_ominus, bloodNumber_oplus, bloodNumber_total = 0;
    static int childrenCountDatabase = 0;

    Button refreshStockButton;

    //blood stock images
    ImageView fragmentDisplayBloodBank_image_abMinusBlood, fragmentDisplayBloodBank_image_abPlusBlood, fragmentDisplayBloodBank_image_aMinusBlood, fragmentDisplayBloodBank_image_aplusBlood, fragmentDisplayBloodBank_image_bMinusBlood,
            fragmentDisplayBloodBank_image_bPlusBlood, fragmentDisplayBloodBank_image_oMinusBlood, fragmentDisplayBloodBank_image_oPlusBlood;

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
        getImage(getApplicationContext(), bloodBankImage);


        search = findViewById(R.id.DisplayBloodBank_Button_Continue);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(latitude, longitude, name);
            }
        });

        // for bloodstock images
        fragmentDisplayBloodBank_image_abMinusBlood = (ImageView) findViewById(R.id.fragmentDisplayBloodBank_image_abMinusBlood);
        fragmentDisplayBloodBank_image_abPlusBlood = (ImageView) findViewById(R.id.fragmentDisplayBloodBank_image_abPlusBlood);
        fragmentDisplayBloodBank_image_aMinusBlood = (ImageView) findViewById(R.id.fragmentDisplayBloodBank_image_aMinusBlood);
        fragmentDisplayBloodBank_image_aplusBlood = (ImageView) findViewById(R.id.fragmentDisplayBloodBank_image_aplusBlood);
        fragmentDisplayBloodBank_image_bMinusBlood = (ImageView) findViewById(R.id.fragmentDisplayBloodBank_image_bMinusBlood);
        fragmentDisplayBloodBank_image_bPlusBlood = (ImageView) findViewById(R.id.fragmentDisplayBloodBank_image_bPlusBlood);
        fragmentDisplayBloodBank_image_oMinusBlood = (ImageView) findViewById(R.id.fragmentDisplayBloodBank_image_oMinusBlood);
        fragmentDisplayBloodBank_image_oPlusBlood = (ImageView) findViewById(R.id.fragmentDisplayBloodBank_image_oPlusBlood);

        refreshStockButton = (Button) findViewById(R.id.fragmentHome_button_refreshStock);
        reloadDataStock();
        refreshStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadDataStock();
                changeImageStock();
                Toast.makeText(getApplicationContext(), "Refreshed Stock!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reloadDataStock() {
        dao = new DAOBloodBank();
        dao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bloodNumber_total = 0;
                bloodNumber_abminus = 0;
                bloodNumber_abplus = 0;
                bloodNumber_aminus = 0;
                bloodNumber_aplus = 0;
                bloodNumber_bminus = 0;
                bloodNumber_bplus = 0;
                bloodNumber_ominus = 0;
                bloodNumber_oplus = 0;

                childrenCountDatabase = (int) snapshot.getChildrenCount();
                for (DataSnapshot data : snapshot.getChildren()) {
                    bloodBank bloodBanks = data.getValue(bloodBank.class);
                    switch (bloodBanks.getBloodRequested()) {
                        case "AB-":
                            bloodNumber_abminus++;
                            bloodNumber_total++;
                            break;
                        case "AB+":
                            bloodNumber_abplus++;
                            bloodNumber_total++;
                        case "A-":
                            bloodNumber_aminus++;
                            bloodNumber_total++;
                            break;
                        case "A+":
                            bloodNumber_aplus++;
                            bloodNumber_total++;
                            break;
                        case "B-":
                            bloodNumber_bminus++;
                            bloodNumber_total++;
                            break;
                        case "B+":
                            bloodNumber_bplus++;
                            bloodNumber_total++;
                            break;
                        case "O-":
                            bloodNumber_ominus++;
                            bloodNumber_total++;
                            break;
                        case "O+":
                            bloodNumber_oplus++;
                            bloodNumber_total++;
                            break;
                    }
                }

                int factor = bloodNumber_total / childrenCountDatabase;
                if (factor != 0) {
                    insaneLogic(factor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void changeImageStock() {
        float tempPercentage = 0;

        tempPercentage = (float) bloodNumber_abminus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if (tempPercentage <= 5) {
            fragmentDisplayBloodBank_image_abMinusBlood.setBackgroundResource(R.drawable.blood_full_abminus);
        } else if (tempPercentage <= 25) {
            fragmentDisplayBloodBank_image_abMinusBlood.setBackgroundResource(R.drawable.blood_high_abminus);
        } else if (tempPercentage <= 50) {
            fragmentDisplayBloodBank_image_abMinusBlood.setBackgroundResource(R.drawable.blood_medium_abminus);
        } else if (tempPercentage <= 75) {
            fragmentDisplayBloodBank_image_abMinusBlood.setBackgroundResource(R.drawable.blood_low_abminus);
        }

        tempPercentage = (float) bloodNumber_abplus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if (tempPercentage <= 5) {
            fragmentDisplayBloodBank_image_abPlusBlood.setBackgroundResource(R.drawable.blood_full_abplus);
        } else if (tempPercentage <= 25) {
            fragmentDisplayBloodBank_image_abPlusBlood.setBackgroundResource(R.drawable.blood_high_abplus);
        } else if (tempPercentage <= 50) {
            fragmentDisplayBloodBank_image_abPlusBlood.setBackgroundResource(R.drawable.blood_medium_abplus);
        } else if (tempPercentage <= 75) {
            fragmentDisplayBloodBank_image_abPlusBlood.setBackgroundResource(R.drawable.blood_low_abplus);
        }

        tempPercentage = (float) bloodNumber_aminus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if (tempPercentage <= 5) {
            fragmentDisplayBloodBank_image_aMinusBlood.setBackgroundResource(R.drawable.blood_full_aminus);
        } else if (tempPercentage <= 25) {
            fragmentDisplayBloodBank_image_aMinusBlood.setBackgroundResource(R.drawable.blood_high_aminus);
        } else if (tempPercentage <= 50) {
            fragmentDisplayBloodBank_image_aMinusBlood.setBackgroundResource(R.drawable.blood_medium_aminus);
        } else if (tempPercentage <= 75) {
            fragmentDisplayBloodBank_image_aMinusBlood.setBackgroundResource(R.drawable.blood_low_aminus);
        }

        tempPercentage = (float) bloodNumber_aplus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if (tempPercentage <= 5) {
            fragmentDisplayBloodBank_image_aplusBlood.setBackgroundResource(R.drawable.blood_full_aplus);
        } else if (tempPercentage <= 25) {
            fragmentDisplayBloodBank_image_aplusBlood.setBackgroundResource(R.drawable.blood_high_aplus);
        } else if (tempPercentage <= 50) {
            fragmentDisplayBloodBank_image_aplusBlood.setBackgroundResource(R.drawable.blood_medium_aplus);
        } else if (tempPercentage <= 75) {
            fragmentDisplayBloodBank_image_aplusBlood.setBackgroundResource(R.drawable.blood_low_aplus);
        }

        tempPercentage = (float) bloodNumber_bminus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if (tempPercentage <= 5) {
            fragmentDisplayBloodBank_image_bMinusBlood.setBackgroundResource(R.drawable.blood_full_bminus);
        } else if (tempPercentage <= 25) {
            fragmentDisplayBloodBank_image_bMinusBlood.setBackgroundResource(R.drawable.blood_high_bminus);
        } else if (tempPercentage <= 50) {
            fragmentDisplayBloodBank_image_bMinusBlood.setBackgroundResource(R.drawable.blood_medium_bminus);
        } else if (tempPercentage <= 75) {
            fragmentDisplayBloodBank_image_bMinusBlood.setBackgroundResource(R.drawable.blood_low_bminus);
        }

        tempPercentage = (float) bloodNumber_bplus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if (tempPercentage <= 5) {
            fragmentDisplayBloodBank_image_bPlusBlood.setBackgroundResource(R.drawable.blood_full_bplus);
        } else if (tempPercentage <= 25) {
            fragmentDisplayBloodBank_image_bPlusBlood.setBackgroundResource(R.drawable.blood_high_bplus);
        } else if (tempPercentage <= 50) {
            fragmentDisplayBloodBank_image_bPlusBlood.setBackgroundResource(R.drawable.blood_medium_bplus);
        } else if (tempPercentage <= 75) {
            fragmentDisplayBloodBank_image_bPlusBlood.setBackgroundResource(R.drawable.blood_low_bplus);
        }

        tempPercentage = (float) bloodNumber_ominus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if (tempPercentage <= 5) {
            fragmentDisplayBloodBank_image_oMinusBlood.setBackgroundResource(R.drawable.blood_full_ominus);
        } else if (tempPercentage <= 25) {
            fragmentDisplayBloodBank_image_oMinusBlood.setBackgroundResource(R.drawable.blood_high_ominus);
        } else if (tempPercentage <= 50) {
            fragmentDisplayBloodBank_image_oMinusBlood.setBackgroundResource(R.drawable.blood_medium_ominus);
        } else if (tempPercentage <= 75) {
            fragmentDisplayBloodBank_image_oMinusBlood.setBackgroundResource(R.drawable.blood_low_ominus);
        }

        tempPercentage = (float) bloodNumber_oplus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if (tempPercentage <= 5) {
            fragmentDisplayBloodBank_image_oPlusBlood.setBackgroundResource(R.drawable.blood_full_oplus);
        } else if (tempPercentage <= 25) {
            fragmentDisplayBloodBank_image_oPlusBlood.setBackgroundResource(R.drawable.blood_high_oplus);
        } else if (tempPercentage <= 50) {
            fragmentDisplayBloodBank_image_oPlusBlood.setBackgroundResource(R.drawable.blood_medium_oplus);
        } else if (tempPercentage <= 75) {
            fragmentDisplayBloodBank_image_oPlusBlood.setBackgroundResource(R.drawable.blood_low_oplus);
        }
    }

    public void insaneLogic(int factor) {
        bloodNumber_total = bloodNumber_total / factor;

        bloodNumber_abminus = bloodNumber_abminus / factor;
        bloodNumber_abplus = bloodNumber_abplus / factor;
        bloodNumber_aminus = bloodNumber_aminus / factor;
        bloodNumber_aplus = bloodNumber_aplus / factor;
        bloodNumber_bminus = bloodNumber_bminus / factor;
        bloodNumber_bplus = bloodNumber_bplus / factor;
        bloodNumber_ominus = bloodNumber_ominus / factor;
        bloodNumber_oplus = bloodNumber_oplus / factor;
    }

    private void sendData(String latitude, String longitude, String name) {
        Intent i = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("latitude", latitude);
        bundle.putString("longitude", longitude);
        bundle.putString("name", name);

        i.putExtras(bundle);
        startActivity(i);

    }

    private void getImage(Context context, ImageView image) {

        FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://blood-donation-applicati-79711.appspot.com/");
        StorageReference ref = mStorage.getReference();
        StorageReference storageRef
                = ref
                .child(
                        "images/"
                                + name + ".jpg");

        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}