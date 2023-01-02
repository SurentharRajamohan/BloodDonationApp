package com.blooddonationapp.startactivity.Utils;


import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

// DAO IS DATA ACCESS OBJECT FOR CRUD OPERATIONS!!!!!!!! DONT ASK ME WHOEVER ASKED ME I WILL PUKUL
public class DAOBloodBank {
    private DatabaseReference databaseReference;

    public DAOBloodBank() {
        // TO INITIALIZE CONNECTION TO MY DATABASE
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://blood-donation-app-c6d0c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = db.getReference(bloodBank.class.getSimpleName());
    }

    public Task<Void> add(bloodBank bloodBankObject){
        return databaseReference.push().setValue(bloodBankObject);
    }

    public Query get(){
        return databaseReference.orderByKey();
    }

    public Query getSpecificData(String childData){
        return databaseReference
                .orderByChild(childData);
    }
}
