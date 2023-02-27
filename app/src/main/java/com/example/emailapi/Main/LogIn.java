package com.example.emailapi.Main;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LogIn extends AppCompatActivity {
    EditText email1, password1;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button bLogin, SignUp;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        bLogin = findViewById(R.id.loginbtn);
        email1 = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        SignUp = findViewById(R.id.SignUp);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        mAuth = FirebaseAuth.getInstance();


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                Intent intent1 = new Intent(LogIn.this, SignUp.class);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                Toast.makeText(LogIn.this, "Register Page", Toast.LENGTH_SHORT).show();
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                String email = email1.getText().toString();
                String password = password1.getText().toString();
                loginUser(email, password);
            }
        });


    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                //task.getResult(ApiException.class);
                //navigateToSecondActivity();

                GoogleSignInAccount account = task.getResult(ApiException.class);
                String email = account.getEmail();
                navigateToSecondActivity(email);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Whoops", Toast.LENGTH_SHORT).show();
        }

    }
    void navigateToSecondActivity(String email){
        finish();
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        Intent intent = new Intent(LogIn.this, Profile.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void loginUser(String email, String password) {
        {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LogIn.this, "User signed in", Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putString("email", email);
                                bundle.putString("password", password);

                                Intent intent = new Intent(LogIn.this, Home.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                Log.w("MySignIn", "SignInUserWithEmail:failure", task.getException());
                                Toast.makeText(LogIn.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
    }
}
