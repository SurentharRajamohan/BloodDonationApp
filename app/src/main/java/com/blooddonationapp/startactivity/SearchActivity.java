package com.blooddonationapp.startactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.blooddonationapp.startactivity.Fragment.HomeFragment;
import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.blooddonationapp.startactivity.Utils.MyAdapter;
import com.blooddonationapp.startactivity.Utils.RecyclerViewInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements RecyclerViewInterface {

    RecyclerView recyclerView;
    ArrayList<bloodBank> list;
    DatabaseReference databaseReference;
    SearchView searchView;
    MyAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.RV);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("bloodBank");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this,list,this);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    bloodBank bb = dataSnapshot.getValue(bloodBank.class);
                    Double tempLat = Double.parseDouble(bb.getLatitude());
                    Double tempLng = Double.parseDouble(bb.getLongitude());

                    SharedPreferences sharedPreferences = getSharedPreferences("userCredentials", 0);
                    String latitude = sharedPreferences.getString("latitude", "");
                    String longitude = sharedPreferences.getString("longitude", "");

                    final Double distance = getDistance(tempLat, tempLng, Double.parseDouble(latitude), Double.parseDouble(longitude));
                    bb.setDistance(distance);


                    list.add(bb);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.TBMainAct);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        search();
    }

    private void search(){
        searchView = findViewById(R.id.SearchActivity_SV_searchBB);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);

                return false;
            }
        });

    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<bloodBank> filteredlist = new ArrayList<bloodBank>();

        // running a for loop to compare elements.
        for (bloodBank item : list) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
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


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SearchActivity.this, DisplayBloodBank.class);
        intent.putExtra("Blood Bank", list.get(position));
        startActivity(intent);

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