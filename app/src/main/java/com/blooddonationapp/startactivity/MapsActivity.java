package com.blooddonationapp.startactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.blooddonationapp.startactivity.Fragment.PopDonorDetails;
import com.blooddonationapp.startactivity.Fragment.PopSuccessful;
import com.blooddonationapp.startactivity.UserData.User;
import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.blooddonationapp.startactivity.Utils.MyAdapter;
import com.blooddonationapp.startactivity.Utils.RecyclerViewInterface;
import com.blooddonationapp.startactivity.Utils.UserAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.blooddonationapp.startactivity.databinding.ActivityMapsBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, RecyclerViewInterface {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ConstraintLayout mBottomSheetLayout;
    private BottomSheetBehavior sheetBehavior;
    private ImageView header_Arrow_Image;
    private RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private UserAdapter adapter;
    private ArrayList<User> list;
    private Button B;
    private Button A;
    private Button O;
    private String DonorName;
    private String latitude, longitude;
    private final double MAX_DISTANCE = 30.0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());







//         Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);






        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
        header_Arrow_Image = findViewById(R.id.bottom_sheet_arrow);

        header_Arrow_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                header_Arrow_Image.setRotation(slideOffset * 180);
            }
        });

        filterBloodType();

        Toolbar toolbar = (Toolbar) findViewById(R.id.MapsActivity_TB_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, DisplayBloodBank.class);
                startActivity(intent);

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
       // DisplayBloodBank bb = new DisplayBloodBank();

        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getString("latitude");
        longitude = bundle.getString("longitude");
        String name = bundle.getString("name");



        mMap = googleMap;

        LatLng bloodBankLatLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(bloodBankLatLng);
        markerOptions.title(name);

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bloodBankLatLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bloodBankLatLng, 10.0f));



        getNearbyMarkers();

    }


    @Override
    public void onItemClick(int position) {

        SharedPreferences sharedPreferences = getSharedPreferences("userCredentials",0);
        boolean isAdmin = sharedPreferences.getBoolean("isAdmin",false);

        if(isAdmin) {



            Intent intent = new Intent(MapsActivity.this, PopSuccessful.class);
            Bundle bundle2 = new Bundle();
            bundle2.putString("DonorName", DonorName);

            intent.putExtras(bundle2);
            startActivity(intent);
        }
    }

    private void getNearbyMarkers() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("users");
        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this,list,this);
        recyclerView.setAdapter(adapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User users = dataSnapshot.getValue(User.class);
                    final Double tempLat = dataSnapshot.child("LatLng").child("latitude").getValue(double.class);
                    final Double tempLng = dataSnapshot.child("LatLng").child("longitude").getValue(double.class);
                    final boolean isAdmin = dataSnapshot.child("isAdmin").getValue(boolean.class);
                    DonorName = users.getFirstName();


                    final Double distance = getDistance(tempLat, tempLng, Double.parseDouble(latitude), Double.parseDouble(longitude));
                    users.setDistance(distance/1000);

                    if(!isAdmin && users.getDistance() < MAX_DISTANCE) {



                        LatLng allLatLang = new LatLng(tempLat, tempLng);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(allLatLang);

                        mMap.addMarker(markerOptions);


                        list.add(users);


                    }
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void filterBloodType(){
         B= findViewById(R.id.BtypeButton);
        A= findViewById(R.id.ATypeButton);
        O= findViewById(R.id.OTypeButton);

        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               filter("B");
            }
        });
        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter("A");

            }
        });
        O.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter("O");

            }
        });



    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<User> filteredlist = new ArrayList<User>();

        // running a for loop to compare elements.
        for (User item : list) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getBloodGroup().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    private double getDistance(Double donorlat,Double donorlng, Double lat, Double lng){

        Location startPoint=new Location("donorLocation");
        startPoint.setLatitude(donorlat);
        startPoint.setLongitude(donorlng);


        Location endPoint=new Location("locationA");
        endPoint.setLatitude(lat);
        endPoint.setLongitude(lng);

        double distance=startPoint.distanceTo(endPoint);

        return distance;





    }
}