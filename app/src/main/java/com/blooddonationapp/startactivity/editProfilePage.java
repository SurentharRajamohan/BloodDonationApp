package com.blooddonationapp.startactivity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class editProfilePage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner genderSpinner, bloodTypeSpinner;
    private EditText dateChoice; //EditProfile_editText_dateChoice
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        // To initialize the spinner
        genderSpinner = (Spinner) findViewById(R.id.EditProfile_spinner_genderChoice);
        ArrayAdapter<CharSequence> adapterGenderSpinner = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapterGenderSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapterGenderSpinner);
        genderSpinner.setOnItemSelectedListener(this);

        // Date picker
        dateChoice = (EditText) findViewById(R.id.EditProfile_editText_dateChoice);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        dateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(editProfilePage.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // To initialize the spinner
        bloodTypeSpinner = (Spinner) findViewById(R.id.EditProfile_spinner_bloodTypeChoice);
        ArrayAdapter<CharSequence> adapterBloodTypeSpinner = ArrayAdapter.createFromResource(this, R.array.bloodGroup, android.R.layout.simple_spinner_item);
        adapterBloodTypeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeSpinner.setAdapter(adapterBloodTypeSpinner);
        bloodTypeSpinner.setOnItemSelectedListener(this);
    }

    private void updateLabel() {
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateChoice.setText(dateFormat.format(myCalendar.getTime()));
    }


    // Logic for when the items are selected within the spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        text += " has been selected.";
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}