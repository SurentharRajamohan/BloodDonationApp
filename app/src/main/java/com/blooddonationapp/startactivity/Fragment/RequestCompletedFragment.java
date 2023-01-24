package com.blooddonationapp.startactivity.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.Request;
import com.blooddonationapp.startactivity.Utils.CardView_RequestAdapter;
import com.blooddonationapp.startactivity.Utils.RecyclerViewInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestCompletedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestCompletedFragment extends Fragment implements RecyclerViewInterface {

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
    private int points;


    public RequestCompletedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestCompletedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestCompletedFragment newInstance(String param1, String param2) {
        RequestCompletedFragment fragment = new RequestCompletedFragment();
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
        View view = inflater.inflate(R.layout.fragment_request_completed, container, false);

        // Code for the card list view

        recyclerView = (RecyclerView) view.findViewById(R.id.RequestPendingFragment_RV_completedRequestList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        adapter = new CardView_RequestAdapter(getContext(),this);
        recyclerView.setAdapter(adapter);

        loadData("completed");

        return view;
    }

    public void loadData(String path) {


       SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userCredentials",0);
       String user = sharedPreferences.getString("username", "");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("request").child(user).child(path);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Request> tempRequest = new ArrayList<>();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Request completed = dataSnapshot.getValue(Request.class);
                    tempRequest.add(completed);



                }
                adapter.setItems(tempRequest);
                adapter.notifyDataSetChanged();
                //add points for donor
                points = tempRequest.size()*10;
//                setPoints(user,points);
                setPoints(user, points);







            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setPoints(String userKey, int point){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("users").child(userKey).child("points");

        databaseReference.setValue(point);


    }

    @Override
    public void onItemClick(int position) {

    }
}