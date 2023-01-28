package com.blooddonationapp.startactivity.Fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.Request;
import com.blooddonationapp.startactivity.Utils.CardView_RequestAdapter;
import com.blooddonationapp.startactivity.Utils.RecyclerViewInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestPendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestPendingFragment extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference databaseReference;
    private CardView_RequestAdapter adapter;
    private RecyclerView recyclerView;
    private FrameLayout frameLayout;
    private ArrayList<Request> tempRequest;
    private String key;
    private String name;
    private boolean isAdmin;
    private String donor;

    public RequestPendingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestPendingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestPendingFragment newInstance(String param1, String param2) {
        RequestPendingFragment fragment = new RequestPendingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_pending, container, false);


        // Code for the card list view

        recyclerView = (RecyclerView) view.findViewById(R.id.RequestPendingFragment_RV_pendingRequestList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        adapter = new CardView_RequestAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        loadData("pending");

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userCredentials", 0);
        isAdmin = sharedPreferences.getBoolean("isAdmin", true);
        if (isAdmin) {
            helper.attachToRecyclerView(recyclerView);
        }

        return view;
    }

    public void loadData(String path) {

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userCredentials", 0);
        String user = sharedPreferences.getString("username", "");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");

        databaseReference = firebaseDatabase.getReference("request").child(user).child(path);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempRequest = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Request pending = dataSnapshot.getValue(Request.class);
                    pending.setKey(dataSnapshot.getKey());
                    tempRequest.add(pending);
                    key = dataSnapshot.getKey();
                }
                adapter.setItems(tempRequest);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Donation completed?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userCredentials", 0);
                            String user = sharedPreferences.getString("username", "");
                            copyFirebaseData(user);


//                            copyFirebaseData(user,donor);
//                            deleteFirebaseData(donor,key);


                            adapter.notifyDataSetChanged();


                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    };

    public void copyFirebaseData(String Admin, String DonorName) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("request").child(Admin).child("pending");
        Query selectedQuery = databaseReference.orderByChild("donor").equalTo(DonorName);
        final DatabaseReference toDonor = firebaseDatabase.getReference("request").child(DonorName).child("completed");

        selectedQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot complete : dataSnapshot.getChildren()) {
                    String completeKey = complete.getKey();
                    String date = complete.child("date").getValue(String.class);
                    String name = complete.child("name").getValue(String.class);
                    String time = complete.child("time").getValue(String.class);
                    String donor = complete.child("donor").getValue(String.class);
                    toDonor.child(completeKey).child("date").setValue(date);
                    toDonor.child(completeKey).child("name").setValue(name);
                    toDonor.child(completeKey).child("time").setValue(time);
                    toDonor.child(completeKey).child("status").setValue("Donation Completed");
                    toDonor.child(completeKey).child("donor").setValue(donor);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void copyFirebaseData(String user) {


        Query selectedQuery = databaseReference.orderByChild("status").equalTo("Request accepted");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");

        databaseReference = firebaseDatabase.getReference("request").child(user).child("pending");
        final DatabaseReference toCompleted = firebaseDatabase.getReference("request").child(user).child("completed");

        selectedQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot complete : dataSnapshot.getChildren()) {
                    String completeKey = complete.getKey();
                    String date = complete.child("date").getValue(String.class);
                    name = complete.child("name").getValue(String.class);
                    donor = complete.child("donor").getValue(String.class);
                    String time = complete.child("time").getValue(String.class);
                    toCompleted.child(completeKey).child("date").setValue(date);
                    toCompleted.child(completeKey).child("name").setValue(name);
                    toCompleted.child(completeKey).child("time").setValue(time);
                    toCompleted.child(completeKey).child("status").setValue("Donation Completed");

                    copyFirebaseData(user, donor);
                    deleteFirebaseData(user,completeKey);
                    deleteFirebaseData(donor,completeKey);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteFirebaseData(String user, String key) {

<<<<<<< Updated upstream
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userCredentials", 0);
        String user = sharedPreferences.getString("username", "");
=======
>>>>>>> Stashed changes

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");

        databaseReference = firebaseDatabase.getReference("request").child(user).child("pending").child(key);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot delete : dataSnapshot.getChildren()) {
                    delete.getRef().removeValue();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        if (!isAdmin) {
            androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
            builder1.setMessage("Accept Request?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userCredentials", 0);
                            String user = sharedPreferences.getString("username", "");

                            String key = tempRequest.get(position).getKey();
                            String name = tempRequest.get(position).getName();

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
                            databaseReference = firebaseDatabase.getReference("request").child(user).child("pending").child(key).child("status");
                            databaseReference.setValue("Request accepted");
                            DatabaseReference databaseReference2 = firebaseDatabase.getReference("request").child(name).child("pending").child(key).child("status");
                            databaseReference2.setValue("Request accepted");


                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }
}