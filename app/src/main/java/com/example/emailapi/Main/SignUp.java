package com.example.emailapi.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emailapi.DAO.UserDAO;
import com.example.emailapi.Entity.User;
import com.example.emailapi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

public class SignUp extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser;
    Button createAcc, goBack;
    EditText email2, password2, pwRetype, Name, dateOfBirth, phoneNo, eircode;
    CheckBox myCheckbox;
    Boolean organisation=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);

        createAcc = findViewById(R.id.createAccount);
        goBack = findViewById(R.id.backButton);
        email2 = findViewById(R.id.email);
        password2 = findViewById(R.id.password3);
        pwRetype = findViewById(R.id.password4);
        Name = findViewById(R.id.Name);
        dateOfBirth = findViewById(R.id.DateOfBirth);
        myCheckbox = findViewById(R.id.my_checkbox);
        phoneNo = findViewById(R.id.phoneNo);
        eircode = findViewById(R.id.eircode);

        myCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            organisation = isChecked;
            if (isChecked) {
                dateOfBirth.setVisibility(View.GONE);
            } else {
                dateOfBirth.setVisibility(View.VISIBLE);
            }
        });

        createAcc.setOnClickListener(view2 -> {
            createAccount();
        });

        goBack.setOnClickListener(view -> {
            goToLogIn();
        });
    }

    private void goToLogIn() {
        Toast.makeText(SignUp.this, "Login Page", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), LogIn.class);
        startActivity(intent);
    }

    private void createAccount() {
        String email = email2.getText().toString();
        String password = password2.getText().toString();
        String name1 = Name.getText().toString();
        String dateOfBirth1 = dateOfBirth.getText().toString();
        String phoneNo1 = phoneNo.getText().toString();
        String eircode1 = eircode.getText().toString();

        if (organisation) {
            // If the user is an organisation, set dateOfBirth1 to an empty string
            dateOfBirth1 = "";
        } else if (dateOfBirth1 == null || dateOfBirth1.isEmpty()) {
            // If the user is not an organisation and dateOfBirth1 is null or empty, show an error message
            Toast.makeText(this, "Date of birth is required.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!dateOfBirth1.matches("(0[1-9]|1[0-2])/(0[1-9]|[1-2][0-9]|3[01])/\\d{4}")) {
            // If dateOfBirth1 is not null or empty, but it's not in the correct format, show an error message
            Toast.makeText(getApplicationContext(), "Date format should be in number XX/XX/XXXX & be valid dates", Toast.LENGTH_SHORT).show();
            return;
        }else {

            // Check if the user is above 18 years of age based on date of birth
            String[] dobParts = dateOfBirth1.split("/");
            int day = Integer.parseInt(dobParts[0]);
            int month = Integer.parseInt(dobParts[1]);
            int year = Integer.parseInt(dobParts[2]);

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            currentDate.add(Calendar.YEAR, -18);
            Calendar dob = Calendar.getInstance();
            dob.set(year, month - 1, day);

            if (dob.after(currentDate)) {
                Toast.makeText(getApplicationContext(), "You must be at least 18 years old", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        registerUser(email, password, name1, dateOfBirth1, organisation, phoneNo1, eircode1);
    }

    private void registerUser(String email, String password, String name1, String dateOfBirth1, Boolean organisation, String phoneNo1, String eircode1) {
        String passwordRetype = pwRetype.getText().toString();

        // Check if password2 and pwRetype are the same
        if (!password.equals(passwordRetype)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUp.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("myActivity", "createUserWithEmail:success");
                        mUser = mAuth.getCurrentUser();
                        String uid = mUser.getUid();


                        User person1 = new User(name1, dateOfBirth1, phoneNo1, eircode1, email, uid, organisation, null);
                        UserDAO uDAO = new UserDAO(person1, uid);

                        if (organisation) {
                            Intent intent = new Intent(getApplicationContext(), LogIn.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), AddSafetyInfo.class);
                            startActivity(intent);
                        }
                    } else {

                        // If sign in fails, display a message to the user.
                        Log.w("myActivity", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUp.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
