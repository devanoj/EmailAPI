package com.example.emailapi.Main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emailapi.DAO.SafetyDAO;
import com.example.emailapi.Entity.Animal;
import com.example.emailapi.Entity.Safety;
import com.example.emailapi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateSafety extends AppCompatActivity {
    Button nextPage;
    Spinner spinnerAdult, gardenSpinner;
    EditText leftAlone, pType;
    CheckBox criminal, car;
    Boolean criminal1 = false; //boolean is more memory efficient than Boolean, however Boolean can be null
    Boolean car1 = false;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dr = database.getReference("Safety");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_safety_layout);
        spinnerAdult = findViewById(R.id.my_spinner);
        gardenSpinner = findViewById(R.id.garden_spinner);
        leftAlone = findViewById(R.id.leftAlone);
        pType = findViewById(R.id.pType);

        handleCheckBox();
        handleSpinner();
        updateButton();
    }

    private void handleCheckBox() {
        criminal = findViewById(R.id.my_checkbox1);
        car = findViewById(R.id.car_checkbox);

        criminal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            criminal1 = isChecked;
        });

        car.setOnCheckedChangeListener((buttonView, isChecked) -> {
            car1 = isChecked;
        });
    }

    private void updateButton() {
        nextPage = findViewById(R.id.nextPage);
        nextPage.setOnClickListener(v->{getIdforSafety();});}

    private void getIdforSafety() {
        assert mUser != null;
        String ussid = mUser.getUid();


        dr.orderByChild("ussid").equalTo(ussid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot sSnapshot = snapshot.getChildren().iterator().next();

                String x = sSnapshot.child("safetyId").getValue(String.class);
                Log.d("TAGXXX", "usid for current user: " + x);
                updateSafetyNode(x);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateSafetyNode(String safetyID) {
        dr.child(safetyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Safety sInfo = snapshot.getValue(Safety.class);
                if(sInfo == null) {
                    Toast.makeText(UpdateSafety.this, "Safety Info Not Found", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isEmpty(leftAlone.getText().toString())) {
                    sInfo.setHoursAlone(leftAlone.getText().toString());
                }
                if (!TextUtils.isEmpty(pType.getText().toString())) {
                    sInfo.setProperty(pType.getText().toString());
                }

                String adult = spinnerAdult.getSelectedItem().toString();
                String garden = gardenSpinner.getSelectedItem().toString();
                sInfo.setAdult(adult);
                sInfo.setGarden(garden);
                sInfo.setCar(car1);
                sInfo.setCriminal(criminal1);

                dr.child(safetyID).setValue(sInfo)
                        .addOnSuccessListener(aVoid -> Toast.makeText(UpdateSafety.this, "Safety Info updated successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(UpdateSafety.this, "Safety Info didn't update", Toast.LENGTH_SHORT).show());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void handleSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.my_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdult.setAdapter(adapter);

        ArrayAdapter<CharSequence> aGardenSpinner = ArrayAdapter.createFromResource(this,
                R.array.garden_array, android.R.layout.simple_spinner_item);
        aGardenSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gardenSpinner.setAdapter(aGardenSpinner);
    }

    private void goToProfile() {
        Bundle bundle1 = new Bundle();
        Intent intent1 = new Intent(UpdateSafety.this, Profile.class);
        intent1.putExtras(bundle1);
        startActivity(intent1);
    }
}
