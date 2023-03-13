package com.example.emailapi.Main;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailapi.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LogIn extends AppCompatActivity {
    EditText email1, password1;
    TextView forgotPswd;
    Button bLogin, SignUp;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        email1 = findViewById(R.id.username);
        password1 = findViewById(R.id.password);

        click();
    }

    private void click() {
        SignUp = findViewById(R.id.SignUp);
        bLogin = findViewById(R.id.loginbtn);
        forgotPswd = findViewById(R.id.forgotPassword);

        forgotPswd.setOnClickListener(v -> {
            String email = email1.getText().toString();
            Bundle bundle1 = new Bundle();
            bundle1.putString("email", email);
            Intent intent1 = new Intent(LogIn.this, ForgotPassword.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });


        SignUp.setOnClickListener(view -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(LogIn.this, SignUp.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
            Toast.makeText(LogIn.this, "Register Page", Toast.LENGTH_SHORT).show();
        });

        bLogin.setOnClickListener(view -> {
            String email = email1.getText().toString();
            String password = password1.getText().toString();
            loginUser(email, password);
        });
    }

    private void loginUser(String email, String password) {
        {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogIn.this, "User signed in", Toast.LENGTH_SHORT).show();

                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            bundle.putString("password", password);

                            Intent intent = new Intent(LogIn.this, Home.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (task.getException() instanceof FirebaseAuthInvalidUserException ||
                                task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(LogIn.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("MySignIn", "SignInUserWithEmail:failure", task.getException());
                            Toast.makeText(LogIn.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }
}
