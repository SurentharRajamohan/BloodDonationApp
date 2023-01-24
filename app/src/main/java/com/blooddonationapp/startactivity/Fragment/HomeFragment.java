package com.blooddonationapp.startactivity.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.SearchActivity;
import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.blooddonationapp.startactivity.Utils.CardView_RVAdapter;
import com.blooddonationapp.startactivity.Utils.DAOBloodBank;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    int bloodNumber_abminus = 0, bloodNumber_aminus = 0, bloodNumber_aplus = 0, bloodNumber_bminus, bloodNumber_bplus, bloodNumber_ominus, bloodNumber_oplus, bloodNumber_total = 0;
    static int childrenCountDatabase = 0;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    CardView_RVAdapter adapter;
    DAOBloodBank dao;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SearchView searchView;
    Button refreshStockButton;

    //blood stock images
    ImageView fragmentHome_image_abMinusBlood, fragmentHome_image_aMinusBlood, fragmentHome_image_aplusBlood,  fragmentHome_image_bMinusBlood,
            fragmentHome_image_bPlusBlood, fragmentHome_image_oMinusBlood, fragmentHome_image_oPlusBlood;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // for bloodstock images
        fragmentHome_image_abMinusBlood = (ImageView) view.findViewById(R.id.fragmentHome_image_abMinusBlood);
        fragmentHome_image_aMinusBlood = (ImageView) view.findViewById(R.id.fragmentHome_image_aMinusBlood);
        fragmentHome_image_aplusBlood = (ImageView) view.findViewById(R.id.fragmentHome_image_aplusBlood);
        fragmentHome_image_bMinusBlood = (ImageView) view.findViewById(R.id.fragmentHome_image_bMinusBlood);
        fragmentHome_image_bPlusBlood = (ImageView) view.findViewById(R.id.fragmentHome_image_bPlusBlood);
        fragmentHome_image_oMinusBlood = (ImageView) view.findViewById(R.id.fragmentHome_image_oMinusBlood);
        fragmentHome_image_oPlusBlood = (ImageView) view.findViewById(R.id.fragmentHome_image_oPlusBlood);

        // DON'T CHANGE THIS!
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userCredentials",0);
        final boolean isAdmin = sharedPref.getBoolean("isAdmin", false);

        // Code for the card list view
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragmentHome_refresh_swipe);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragmentHome_recyclerView_cards);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        adapter = new CardView_RVAdapter(getContext());
        recyclerView.setAdapter(adapter);

        // To access DAO (CRUD database operations) methods
        dao = new DAOBloodBank();
        loadData();

        refreshStockButton = (Button) view.findViewById(R.id.fragmentHome_button_refreshStock);
        refreshStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadDataStock();
                changeImageStock();
                Toast.makeText(getContext(), "Refreshed Stock!", Toast.LENGTH_SHORT).show();
            }
        });
        changeImageStock();
        return view;
    }

    private void loadData() {
        dao.get().addValueEventListener(new ValueEventListener() {
            // They get the data from bloodBankViewHolder class
            // To populate the card list view with the blood bank requirements
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childrenCountDatabase = (int) snapshot.getChildrenCount();
                ArrayList<bloodBank> tempBloodBankObject = new ArrayList<>();
                // snapshot variable is to access the database

                for(DataSnapshot data : snapshot.getChildren()){
                    bloodBank bloodBanks = data.getValue(bloodBank.class);
                    switch (bloodBanks.getBloodRequested()) {
                        case "AB-":
                            bloodNumber_abminus++;
                            bloodNumber_total++;
                            break;
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
                    tempBloodBankObject.add(bloodBanks);
                }

                int factor = bloodNumber_total/childrenCountDatabase;
                if(factor != 0){
                    insaneLogic(factor);
                }
                // adding it to the card view through the adapter
                adapter.setItems(tempBloodBankObject);
                // notifying there is a change in the database to the adapter to update the card view
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void reloadDataStock() {
        dao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bloodNumber_total = 0;
                bloodNumber_abminus = 0;
                bloodNumber_aminus = 0;
                bloodNumber_aplus = 0;
                bloodNumber_bminus = 0;
                bloodNumber_bplus = 0;
                bloodNumber_ominus = 0;
                bloodNumber_oplus = 0;

                childrenCountDatabase = (int) snapshot.getChildrenCount();
                for(DataSnapshot data : snapshot.getChildren()){
                    bloodBank bloodBanks = data.getValue(bloodBank.class);
                    switch (bloodBanks.getBloodRequested()) {
                        case "AB-":
                            bloodNumber_abminus++;
                            bloodNumber_total++;
                            break;
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

                int factor = bloodNumber_total/childrenCountDatabase;
                if(factor != 0){
                    insaneLogic(factor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void insaneLogic(int factor){
        bloodNumber_total = bloodNumber_total / factor;

        bloodNumber_abminus = bloodNumber_abminus / factor;
        bloodNumber_aminus = bloodNumber_aminus / factor;
        bloodNumber_aplus = bloodNumber_aplus / factor;
        bloodNumber_bminus = bloodNumber_bminus / factor;
        bloodNumber_bplus = bloodNumber_bplus / factor;
        bloodNumber_ominus = bloodNumber_ominus /factor;
        bloodNumber_oplus = bloodNumber_oplus / factor;
    }

    public void changeImageStock(){
        float tempPercentage = 0;

        tempPercentage = (float) bloodNumber_abminus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if(tempPercentage <= 5) {
            fragmentHome_image_abMinusBlood.setBackgroundResource(R.drawable.blood_full_abminus);
        } else if(tempPercentage <= 25){
            fragmentHome_image_abMinusBlood.setBackgroundResource(R.drawable.blood_high_abminus);
        } else if(tempPercentage <= 50){
            fragmentHome_image_abMinusBlood.setBackgroundResource(R.drawable.blood_medium_abminus);
        } else if(tempPercentage <= 75){
            fragmentHome_image_abMinusBlood.setBackgroundResource(R.drawable.blood_low_abminus);
        }

        tempPercentage = (float) bloodNumber_aminus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if(tempPercentage <= 5) {
            fragmentHome_image_aMinusBlood.setBackgroundResource(R.drawable.blood_full_aminus);
        } else if(tempPercentage <= 25){
            fragmentHome_image_aMinusBlood.setBackgroundResource(R.drawable.blood_high_aminus);
        } else if(tempPercentage <= 50){
            fragmentHome_image_aMinusBlood.setBackgroundResource(R.drawable.blood_medium_aminus);
        } else if(tempPercentage <= 75){
            fragmentHome_image_aMinusBlood.setBackgroundResource(R.drawable.blood_low_aminus);
        }

        tempPercentage = (float) bloodNumber_aplus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if(tempPercentage <= 5) {
            fragmentHome_image_aplusBlood.setBackgroundResource(R.drawable.blood_full_aplus);
        } else if(tempPercentage <= 25){
            fragmentHome_image_aplusBlood.setBackgroundResource(R.drawable.blood_high_aplus);
        } else if(tempPercentage <= 50){
            fragmentHome_image_aplusBlood.setBackgroundResource(R.drawable.blood_medium_aplus);
        } else if(tempPercentage <= 75){
            fragmentHome_image_aplusBlood.setBackgroundResource(R.drawable.blood_low_aplus);
        }

        tempPercentage = (float) bloodNumber_bminus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if(tempPercentage <= 5) {
            fragmentHome_image_bMinusBlood.setBackgroundResource(R.drawable.blood_full_bminus);
        } else if(tempPercentage <= 25){
            fragmentHome_image_bMinusBlood.setBackgroundResource(R.drawable.blood_high_bminus);
        } else if(tempPercentage <= 50){
            fragmentHome_image_bMinusBlood.setBackgroundResource(R.drawable.blood_medium_bminus);
        } else if(tempPercentage <= 75){
            fragmentHome_image_bMinusBlood.setBackgroundResource(R.drawable.blood_low_bminus);
        }

        tempPercentage = (float) bloodNumber_bplus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if(tempPercentage <= 5) {
            fragmentHome_image_bPlusBlood.setBackgroundResource(R.drawable.blood_full_bplus);
        } else if(tempPercentage <= 25){
            fragmentHome_image_bPlusBlood.setBackgroundResource(R.drawable.blood_high_bplus);
        } else if(tempPercentage <= 50){
            fragmentHome_image_bPlusBlood.setBackgroundResource(R.drawable.blood_medium_bplus);
        } else if(tempPercentage <= 75){
            fragmentHome_image_bPlusBlood.setBackgroundResource(R.drawable.blood_low_bplus);
        }

        tempPercentage = (float) bloodNumber_ominus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if(tempPercentage <= 5) {
            fragmentHome_image_oMinusBlood.setBackgroundResource(R.drawable.blood_full_ominus);
        } else if(tempPercentage <= 25){
            fragmentHome_image_oMinusBlood.setBackgroundResource(R.drawable.blood_high_ominus);
        } else if(tempPercentage <= 50){
            fragmentHome_image_oMinusBlood.setBackgroundResource(R.drawable.blood_medium_ominus);
        } else if(tempPercentage <= 75){
            fragmentHome_image_oMinusBlood.setBackgroundResource(R.drawable.blood_low_ominus);
        }

        tempPercentage = (float) bloodNumber_oplus / bloodNumber_total;
        tempPercentage = tempPercentage * 100;
        if(tempPercentage <= 5) {
            fragmentHome_image_oPlusBlood.setBackgroundResource(R.drawable.blood_full_oplus);
        } else if(tempPercentage <= 25){
            fragmentHome_image_oPlusBlood.setBackgroundResource(R.drawable.blood_high_oplus);
        } else if(tempPercentage <= 50){
            fragmentHome_image_oPlusBlood.setBackgroundResource(R.drawable.blood_medium_oplus);
        } else if(tempPercentage <= 75){
            fragmentHome_image_oPlusBlood.setBackgroundResource(R.drawable.blood_low_oplus);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // setting up the views for fragments for onClickListener methods
        searchView = (SearchView) view.findViewById(R.id.HomePage_SV_searchBloodBank);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
    }

    public boolean isEnabled(int position) {
        if (position == 0) {
            // Disable the first item from Spinner
            // First item will be use for hint
            return false;
        } else {
            return true;
        }
    }
}