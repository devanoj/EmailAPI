package com.example.emailapi.DAO;


import com.example.emailapi.Entity.Safety;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SafetyDAO {
    private DatabaseReference dr;
    public SafetyDAO(Safety add1) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dr = db.getReference("Safety");
        String safetyId = dr.push().getKey(); // generate unique animal ID

        add1.setSafetyId(safetyId); // set the ID in the animal object
        dr.child(safetyId).setValue(add1);

    }
}
