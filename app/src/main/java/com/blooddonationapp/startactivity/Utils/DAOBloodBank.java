package com.blooddonationapp.startactivity.Utils;

import com.blooddonationapp.startactivity.UserData.bloodBank;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

// DAO is Data Access Object, for CRUD operations to FireBase
public class DAOBloodBank {
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;

    public DAOBloodBank() {
        // To initialize connection to database
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://blood-donation-applicati-79711-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = db.getReference(bloodBank.class.getSimpleName());
        userReference = db.getReference("users");
    }

    public Task<Void> add(bloodBank bloodBankObject) {
        return databaseReference.push().setValue(bloodBankObject);
    }

    public Query get() {
        return databaseReference.orderByKey();
    }

    public Query getUser() {
        return userReference.orderByKey();
    }

}
