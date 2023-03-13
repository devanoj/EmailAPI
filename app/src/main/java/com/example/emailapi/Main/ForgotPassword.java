package com.example.emailapi.Main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emailapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {
    Button changePswd, backButton;
    EditText Email1, Email2;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pswd_layout);

        Email2 = findViewById(R.id.newPswd2);
        Email1 = findViewById(R.id.newPswd);
        changePswd = findViewById(R.id.enterPswd);
        backButton = findViewById(R.id.back);

        changePswd.setOnClickListener(v->{
            EmailMatch();
        });

        backButton.setOnClickListener(v->{
            goBack();
        });
    }

    private void goBack() {
        Bundle bundle1 = new Bundle();
        Intent intent1 = new Intent(ForgotPassword.this, LogIn.class);
        intent1.putExtras(bundle1);
        startActivity(intent1);
    }

    private void EmailMatch() {
        if (Email1.getText().toString().matches(Email2.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Match", Toast.LENGTH_SHORT).show();
            EmailChange(Email1.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Email doesn't match", Toast.LENGTH_SHORT).show();
        }
    }

    private void EmailChange(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (email != null) {
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "Error Email doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }
}
