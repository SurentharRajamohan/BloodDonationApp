package com.blooddonationapp.startactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class PersonalDetailsActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        //creating variables for each element
        EditText dateOfBirth = findViewById(R.id.PersonalDetailsPage_ET_dateOfBirth);
        Spinner gender = findViewById(R.id.PersonalDetailsPage_Spinner_gender);
        Spinner bloodGroup = findViewById(R.id.PersonalDetailsPage_Spinner_bloodGroup);
        ImageView uploadPhoto = findViewById(R.id.PersonalDetailsPage_IV_profilePhoto);

        //setting up spinners

        //1) Gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender,
                android.R.layout.simple_spinner_item);

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gender.setAdapter(genderAdapter);

        gender.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Toast.makeText(getApplicationContext(), parent.getItemAtPosition(pos).toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        //2) Blood group spinner
        ArrayAdapter<CharSequence> bloodGroupAdapter = ArrayAdapter.createFromResource(this,
                R.array.bloodGroup,
                android.R.layout.simple_spinner_item);

        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bloodGroup.setAdapter(bloodGroupAdapter);

        bloodGroup.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Toast.makeText(getApplicationContext(), parent.getItemAtPosition(pos).toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });











        //onClickListeners

        //1) Pop up calendar when date picker is selected
            dateOfBirth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);

                    //Date picker dialog
                    datePickerDialog = new DatePickerDialog(PersonalDetailsActivity.this , new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                            dateOfBirth.setText(Day + "/" + (Month+1)+"/"+Year);
                        }
                    }, year, month, day);
                    datePickerDialog.show();

                }
            });


        //2) Upload Photo
            uploadPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });












    }
}