package com.blooddonationapp.startactivity.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import com.blooddonationapp.startactivity.AddAdminActivity;
import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.SearchActivity;
import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.blooddonationapp.startactivity.Utils.CardView_RVAdapter;
import com.blooddonationapp.startactivity.Utils.DAOBloodBank;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    CardView_RVAdapter adapter;
    DAOBloodBank dao;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //declaring floatingAction Menu and its buttons
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton addRequestButton, registerAdminButton;


    private Spinner bloodTypeSpinner, malaysianStateSpinner;
    private Button searchButton;
    private FloatingActionButton floatingActionButton;

    DatabaseReference databaseReference;
    SearchView searchView;


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




        //floatingActionButton visibility for the admin users
        floatingActionMenu = view.findViewById(R.id.HomePage_FB_Main);
        registerAdminButton = view.findViewById(R.id.HomePage_FB_registerAdmin);
        addRequestButton = view.findViewById(R.id.HomePage_FB_addBloodRequest);


        //DONT FUCKING CHANGE THIS
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userCredentials",0);
        final boolean isAdmin = sharedPref.getBoolean("isAdmin", false);


        if(!isAdmin){
            floatingActionMenu.setVisibility(View.GONE);
//            floatingActionButton.setVisibility(View.GONE);
        }



        // Initialize the state spinner
        malaysianStateSpinner = (Spinner) view.findViewById(R.id.fragmentHome_spinner_state);
        ArrayAdapter<CharSequence> adapterMalaysianStateSpinner = ArrayAdapter.createFromResource(getActivity(), R.array.states, android.R.layout.simple_spinner_item);
        adapterMalaysianStateSpinner.setDropDownViewResource(R.layout.home_fragment_custom_spinner_item);
        malaysianStateSpinner.setAdapter(adapterMalaysianStateSpinner);

        // Initialize the blood type spinner
        bloodTypeSpinner = (Spinner) view.findViewById(R.id.fragmentHome_spinner_bloodType);
        ArrayAdapter<CharSequence> adapterBloodTypeSpinner = ArrayAdapter.createFromResource(getActivity(), R.array.bloodGroup, android.R.layout.simple_spinner_dropdown_item);
        adapterBloodTypeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeSpinner.setAdapter(adapterBloodTypeSpinner);

        // redirection for floating button
        addRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add donor request", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                replaceFragment(new developer_tools());
            }
        });

        // register admin page redirection
        registerAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddAdminActivity.class));
            }
        });


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

        return view;
    }

    private void loadData() {
        dao.get().addValueEventListener(new ValueEventListener() {
            // They get the data from bloodBankViewHolder class
            // To populate the card list view with the blood bank requirements
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<bloodBank> tempBloodBankObject = new ArrayList<>();
                // snapshot variable is to access the database

                for(DataSnapshot data : snapshot.getChildren()){
                    bloodBank bloodBanks = data.getValue(bloodBank.class);
                    tempBloodBankObject.add(bloodBanks);
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

    private void queryData() {

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MainActivity_NHF_fragmentContainer, fragment);
        fragmentTransaction.commit();
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