package com.example.emailapi.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emailapi.DAO.SafetyDAO;
import com.example.emailapi.DAO.UserDAO;
import com.example.emailapi.Entity.Safety;
import com.example.emailapi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddSafetyInfo extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    Button nextPage;
    Spinner spinnerAdult, gardenSpinner;
    EditText leftAlone, pType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info_layout);

        spinnerAdult = findViewById(R.id.my_spinner);
        gardenSpinner = findViewById(R.id.garden_spinner);
        leftAlone = findViewById(R.id.leftAlone);
        pType = findViewById(R.id.pType);


        handleSpinner();
        addToFirebase();
    }

    private void addToFirebase() {
        nextPage = findViewById(R.id.nextPage);
        nextPage.setOnClickListener(v->{
            String adult = spinnerAdult.getSelectedItem().toString();
            String garden = gardenSpinner.getSelectedItem().toString();
            String hrsAlone = leftAlone.getText().toString();
            String property = pType.getText().toString();

            assert mUser != null;
            String ussid = mUser.getUid();
            String safetyId = null;


            Safety add1 = new Safety(safetyId, ussid, adult, garden, hrsAlone, property);
            SafetyDAO sDAO = new SafetyDAO(add1);
            goToLogin();

        });


        //            Log.d("ItemChosenXXX", "Selected item 1: " + adult);
        //            Log.d("ItemChosenXXX", "Selected item 2: " + garden);
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

    private void goToLogin() {
        Bundle bundle1 = new Bundle();
        Intent intent1 = new Intent(AddSafetyInfo.this, SignUp.class);
        intent1.putExtras(bundle1);
        startActivity(intent1);
    }
}
