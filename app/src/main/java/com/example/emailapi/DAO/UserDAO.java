package com.example.emailapi.DAO;

import com.example.emailapi.Entity.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDAO {
    private DatabaseReference databaseReference;

    public UserDAO(User person1, String uid) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("User");

        person1.setUid(uid);
        databaseReference.child(uid).setValue(person1);

    }
}
