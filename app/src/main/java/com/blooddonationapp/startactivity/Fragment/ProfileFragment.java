package com.blooddonationapp.startactivity.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blooddonationapp.startactivity.LogoutScreen;
import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.EditProfilePage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    Button editProfileBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //declaring elements
    TextView username, bloodType, userID, title, pointsDisplay;
    Button logOut, editProfile;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        editProfileBtn = view.findViewById(R.id.fragmentProfile_button_editProfile);


        SharedPreferences sharedPref = getActivity().getSharedPreferences("userCredentials",Context.MODE_PRIVATE);
        String userName = sharedPref.getString("username", "User");
        boolean isAdmin = sharedPref.getBoolean("isAdmin",false);
        String bloodGroup = sharedPref.getString("bloodGroup","No Blood Group");
        String userid = sharedPref.getString("userID","No ID");
        int points = sharedPref.getInt("points",0);

        if(isAdmin){
            editProfileBtn.setVisibility(View.GONE);}

            editProfileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), EditProfilePage.class);
                    startActivity(intent);
                }
            });
        logOut = view.findViewById(R.id.fragmentProfile_button_logout);
        editProfile = view.findViewById(R.id.fragmentProfile_button_editProfile);
        username = view.findViewById(R.id.fragmentProfile_textView_bloodDonorName);
        bloodType = view.findViewById(R.id.fragmentProfile_textView_bloodDonorBloodType);

        userID = view.findViewById(R.id.fragmentProfile_textView_bloodDonorID);
        title = view.findViewById(R.id.fragmentProfile_textView_bloodDonorLabel);
        pointsDisplay = view.findViewById(R.id.fragmentProfile_textView_points);


        username.setText(userName);
        bloodType.setText(bloodGroup);
        userID.setText(userid);
        pointsDisplay.setText(Integer.toString(points));
        if(isAdmin){
            title.setText("Admin");

        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get a SharedPreferences object
                SharedPreferences preferences = getActivity().getSharedPreferences("userCredentials",Context.MODE_PRIVATE);

                // Get an editor for the SharedPreferences object
                SharedPreferences.Editor editor = preferences.edit();

                // Clear all data from the SharedPreferences
                editor.clear();

                // Apply the changes
                editor.apply();

                Intent intent = new Intent(getActivity(), LogoutScreen.class);
                startActivity(intent);
            }
        });
        return view;
    }


}