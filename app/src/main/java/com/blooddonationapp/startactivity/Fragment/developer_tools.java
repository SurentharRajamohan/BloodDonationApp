package com.blooddonationapp.startactivity.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.blooddonationapp.startactivity.MainActivity;
import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.Notification;
import com.blooddonationapp.startactivity.UserData.Request;
import com.blooddonationapp.startactivity.UserData.User;
import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.blooddonationapp.startactivity.Utils.DAOBloodBank;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link developer_tools#newInstance} factory method to
 * create an instance of this fragment.
 */
public class developer_tools extends Fragment {

    // BUTTONS
    private Button goBackHomeBtn, submitDataBtn;

    // EDIT TEXT FIELDS
    private EditText ETBloodBankName, ETBloodBankAddress, ETBloodBankLongitude, ETBloodBankLatitude;

    // FOR GETTING CURRENT DATE
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    // TIME STUFF
    private TextClock TCCurrentTime;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference databaseReference;
    private ArrayList<User> tempUser;

    public developer_tools() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment developer_tools.
     */
    // TODO: Rename and change types and number of parameters
    public static developer_tools newInstance(String param1, String param2) {
        developer_tools fragment = new developer_tools();
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
        // TODO: CLEAN UP CODE IN onCreateView!
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_developer_tools, container, false);

        // GO BACK HOMEMEMMEEEMME
        goBackHomeBtn = (Button) view.findViewById(R.id.fragmentDeveloperTools_button_goBackHome);
        goBackHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        // EDIT TEXT
        ETBloodBankName = (EditText) view.findViewById(R.id.fragmentDeveloperTools_editText_bloodBankName);
        ETBloodBankAddress = (EditText) view.findViewById(R.id.fragmentDeveloperTools_editText_bloodBankAddress);
        ETBloodBankLongitude = (EditText) view.findViewById(R.id.fragmentDeveloperTools_editText_bloodBankLongitude);
        ETBloodBankLatitude = (EditText) view.findViewById(R.id.fragmentDeveloperTools_editText_bloodBankLatitude);

        // GETTING CURRENT DATE
        calendar = Calendar.getInstance();
        dateTimeDisplay = (TextView) view.findViewById(R.id.fragmentDeveloperTools_textView_currentDateDisplay);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);

        // GETTING CURRENT TIME
        TCCurrentTime = (TextClock) view.findViewById(R.id.fragmentDeveloperTools_textClock_currentTime);

        // CREATE DAO OBJECT TO ACCESS CRUD OPERATIONS
        DAOBloodBank dao = new DAOBloodBank();

        // Initialize the blood type spinner
        Spinner bloodTypeSpinner = (Spinner) view.findViewById(R.id.fragmentDeveloperTools_spinner_bloodType);
        ArrayAdapter<CharSequence> adapterBloodTypeSpinner = ArrayAdapter.createFromResource(getActivity(), R.array.bloodGroup, android.R.layout.simple_spinner_dropdown_item);
        adapterBloodTypeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeSpinner.setAdapter(adapterBloodTypeSpinner);

        // Initialize the state spinner
        Spinner malaysianStateSpinner = (Spinner) view.findViewById(R.id.fragmentDeveloperTools_spinner_state);
        ArrayAdapter<CharSequence> adapterMalaysianStateSpinner = ArrayAdapter.createFromResource(getActivity(), R.array.states, android.R.layout.simple_spinner_item);
        adapterMalaysianStateSpinner.setDropDownViewResource(R.layout.home_fragment_custom_spinner_item);
        malaysianStateSpinner.setAdapter(adapterMalaysianStateSpinner);

        // SUBMIT DATA TO FIREBASE
        submitDataBtn = (Button) view.findViewById(R.id.fragmentDeveloperTools_button_submitData);
        submitDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                String bloodBankName = ETBloodBankName.getText().toString();
                String bloodBankAddress = ETBloodBankAddress.getText().toString();
                LatLng address = getLocationFromAddress(getContext(), bloodBankAddress);

                Double bloodBankLongitudeRaw = address.longitude;
                Double bloodBankLatitudeRaw = address.latitude;
                String bloodBankLongitude = bloodBankLatitudeRaw.toString();
                String bloodBankLatitude = bloodBankLongitudeRaw.toString();


                String currentTime = TCCurrentTime.getText().toString();
                String currentDate = date;
                String wantedBlood = bloodTypeSpinner.getSelectedItem().toString();
                String state = malaysianStateSpinner.getSelectedItem().toString();
                bloodBank bloodBankObject = new bloodBank(bloodBankName, bloodBankAddress, bloodBankLongitude,
                        bloodBankLatitude, currentDate, currentTime, wantedBlood, state);
                dao.add(bloodBankObject).addOnSuccessListener(suc -> {
                    Toast.makeText(getContext(), "Record is inserted", Toast.LENGTH_SHORT).show();
                });



                Notification notification = new Notification(currentDate, bloodBankName + " requires an emergency donor!", "Low on " + wantedBlood + " blood" );
                loadUser(notification);

            }
        });
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MainActivity_NHF_fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
        
    private void loadUser(Notification notification){


            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
            databaseReference = firebaseDatabase.getReference("users");

            ArrayList<String> list = new ArrayList<>();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        final boolean isAdmin = dataSnapshot.child("isAdmin").getValue(boolean.class);
                        if (isAdmin == false) {
                            list.add(user.getFirstName());
                        }


                    }
                    addNotification(notification, list);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }









    private void addNotification(Notification notification, ArrayList<String> list){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");





        for(int i = 0; i< list.size(); i++){
//            Toast.makeText(getContext(), list.get(i).getFirstName(), Toast.LENGTH_SHORT).show();
            databaseReference = firebaseDatabase.getReference("notification").child(list.get(i));
            databaseReference.push().setValue(notification);
        }





    }
}