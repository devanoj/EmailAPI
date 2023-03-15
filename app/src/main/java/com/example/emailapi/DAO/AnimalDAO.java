package com.example.emailapi.DAO;

import com.example.emailapi.Entity.Animal;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnimalDAO {
    private DatabaseReference databaseReference;

    public AnimalDAO(Animal pet1) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Animal");
        String animalId = databaseReference.push().getKey(); // generate unique animal ID

        pet1.setId(animalId); // set the ID in the animal object
        databaseReference.child(animalId).setValue(pet1);



    }
}
