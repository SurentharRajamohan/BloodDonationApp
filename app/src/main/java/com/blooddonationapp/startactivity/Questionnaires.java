package com.blooddonationapp.startactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Questionnaires extends AppCompatActivity {

    //firebase variables (database)
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaires);

        CheckBox TNC = findViewById(R.id.QuestionnairePage_CB_TNC);
        Button BTNbecomeADonor = findViewById(R.id.QuestionnairePage_BTN_BecomeADonor);
        RadioGroup radioGroup1_diabetes = findViewById(R.id.QuestionnairePage_RG_Diabetes);
        RadioGroup radioGroup2_heart = findViewById(R.id.QuestionnairePage_RG_Heart);
        RadioGroup radioGroup3_covid = findViewById(R.id.QuestionnairePage_RG_Covid);
        RadioGroup radioGroup4_AIDS = findViewById(R.id.QuestionnairePage_RG_AIDS);
        RadioGroup radioGroup5_cancer = findViewById(R.id.QuestionnairePage_RG_Cancer);
        RadioGroup radioGroup6_vaccine = findViewById(R.id.QuestionnairePage_RG_Vaccine);

        TNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TNC.isChecked()){
                    BTNbecomeADonor.setBackgroundResource(R.drawable.button_shape);
                }else{

                    BTNbecomeADonor.setBackgroundResource(R.drawable.unavailable_button_shape);
                }
            }
        });

        BTNbecomeADonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(TNC.isChecked())){
                    Toast.makeText(Questionnaires.this, "Make sure you agree to the Terms and conditions before proceeding", Toast.LENGTH_SHORT).show();
                }else{
                    //exceptions
                    if(radioGroup1_diabetes.getCheckedRadioButtonId() == -1 || radioGroup2_heart.getCheckedRadioButtonId() == -1
                        || radioGroup3_covid.getCheckedRadioButtonId() == -1 || radioGroup4_AIDS.getCheckedRadioButtonId() == -1
                        || radioGroup5_cancer.getCheckedRadioButtonId() == -1 || radioGroup6_vaccine.getCheckedRadioButtonId() == -1){
                        Toast.makeText(Questionnaires.this, "Please answer all the questions", Toast.LENGTH_SHORT).show();
                    }else{
                        RadioButton question1 = findViewById(radioGroup1_diabetes.getCheckedRadioButtonId());
                        RadioButton question2 = findViewById(radioGroup2_heart.getCheckedRadioButtonId());
                        RadioButton question3 = findViewById(radioGroup3_covid.getCheckedRadioButtonId());
                        RadioButton question4 = findViewById(radioGroup4_AIDS.getCheckedRadioButtonId());
                        RadioButton question5 = findViewById(radioGroup5_cancer.getCheckedRadioButtonId());
                        RadioButton question6 = findViewById(radioGroup6_vaccine.getCheckedRadioButtonId());


                        String diabetes = question1.getText().toString();
                        String heartOrLungProblem = question2.getText().toString();
                        String covid19 = question3.getText().toString();
                        String AIDS = question4.getText().toString();
                        String cancer = question5.getText().toString();
                        String vaccine = question6.getText().toString();


                        Intent intent = getIntent();
                        final String username = intent.getExtras().getString("username");

                        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                databaseReference.child("users").child(username).child("diabetes").setValue(diabetes);
                                databaseReference.child("users").child(username).child("heartOrLungProblem").setValue(heartOrLungProblem);
                                databaseReference.child("users").child(username).child("covid19").setValue(covid19);
                                databaseReference.child("users").child(username).child("AIDS").setValue(AIDS);
                                databaseReference.child("users").child(username).child("cancer").setValue(cancer);
                                databaseReference.child("users").child(username).child("vaccine").setValue(vaccine);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        startActivity(new Intent(Questionnaires.this, MainActivity.class));
                    }

                }
            }
        });
    }
}
