package com.blooddonationapp.startactivity.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.blooddonationapp.startactivity.R;
import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.blooddonationapp.startactivity.Utils.DAOBloodBank;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_developer_tools, container, false);

        // GO BACK HOMEMEMMEEEMME
        goBackHomeBtn = (Button) view.findViewById(R.id.fragmentDeveloperTools_button_goBackHome);
        goBackHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HomeFragment());
            }
        });

        // EDIT TEXT SHIT
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

        // SUBMIT DATA TO FIREBASE
        submitDataBtn = (Button) view.findViewById(R.id.fragmentDeveloperTools_button_submitData);
        submitDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bloodBankName = ETBloodBankName.getText().toString();
                String bloodBankAddress = ETBloodBankAddress.getText().toString();
                String bloodBankLongitude = ETBloodBankLongitude.getText().toString();
                String bloodBankLatitude = ETBloodBankLatitude.getText().toString();
                String currentTime = TCCurrentTime.getText().toString();
                String currentDate = date;
                bloodBank bloodBankObject = new bloodBank(bloodBankName, bloodBankAddress, bloodBankLongitude,
                        bloodBankLatitude, currentDate, currentTime);
                dao.add(bloodBankObject).addOnSuccessListener(suc -> {
                    Toast.makeText(getContext(), "Record is inserted", Toast.LENGTH_SHORT).show();
                });
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
}