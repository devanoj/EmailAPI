package com.example.emailapi.Main;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.emailapi.DAO.UserDAO;
import com.example.emailapi.Entity.Safety;
import com.example.emailapi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddSafetyInfo extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    Button nextPage, back;
    Spinner spinnerAdult, gardenSpinner, oSpinner1;
    EditText leftAlone, pType;
    CheckBox criminal, car;
    Boolean criminal1 = false; //boolean is more memory efficient than Boolean, however Boolean can be null
    Boolean car1 = false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference drUser = database.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info_layout);

        spinnerAdult = findViewById(R.id.my_spinner);
        gardenSpinner = findViewById(R.id.garden_spinner);
        oSpinner1 = findViewById(R.id.other_spinner);
        leftAlone = findViewById(R.id.leftAlone);
        pType = findViewById(R.id.pType);

        handleCheckBox();
        handleSpinner();
        addToFirebase();
        backButton();
    }

    private void backButton() {
        back = findViewById(R.id.back);
        back.setOnClickListener(v->{
            goToLogin();
        });

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

    private void addToFirebase() {
        nextPage = findViewById(R.id.nextPage);
        nextPage.setOnClickListener(v->{
            if (criminal1) {
                Toast.makeText(this, "Criminals will have a harder time fostering", Toast.LENGTH_SHORT).show();
            }

                String adult = spinnerAdult.getSelectedItem().toString();
                String garden = gardenSpinner.getSelectedItem().toString();
                String otherS = oSpinner1.getSelectedItem().toString();
                String hrsAlone = leftAlone.getText().toString();
                String property = pType.getText().toString();

                assert mUser != null;
                String ussid = mUser.getUid();
                String safetyId = null;

                Safety add1 = new Safety(safetyId, car1, criminal1, adult, garden, hrsAlone, property, otherS, ussid);
                SafetyDAO sDAO = new SafetyDAO(add1, ussid);
                goToLogin();



        });
    }

    private void handleSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.my_array, R.layout.simple_spinner_item_one);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdult.setAdapter(adapter);

        ArrayAdapter<CharSequence> aGardenSpinner = ArrayAdapter.createFromResource(this,
                R.array.garden_array, R.layout.simple_spinner_item_one);
        aGardenSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gardenSpinner.setAdapter(aGardenSpinner);

        ArrayAdapter<CharSequence> oSpinner = ArrayAdapter.createFromResource(this,
                R.array.other_array, R.layout.simple_spinner_item_one);
        oSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oSpinner1.setAdapter(oSpinner);
    }

    private void goToLogin() {
        Bundle bundle1 = new Bundle();
        Intent intent1 = new Intent(AddSafetyInfo.this, LogIn.class);
        intent1.putExtras(bundle1);
        startActivity(intent1);
    }
    
    
}
