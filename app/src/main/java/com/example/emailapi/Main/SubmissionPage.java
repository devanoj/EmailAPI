package com.example.emailapi.Main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.example.emailapi.Entity.Animal;
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
import com.google.firebase.storage.StorageTask;



public class SubmissionPage extends AppCompatActivity {
    TextView nameDog, display, displaytime, inputName1, inputAge1, inputBreed, inputEnergy, meeting1, displayDateEnd;
    Button dateButton, cMail, deleteAnimal, updateAnimal, mButtonChooseImage, backToHome, inputAnimal, EndTime, testMail;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dr = database.getReference("Animal");
    DatabaseReference drUser = database.getReference("User");
    FirebaseUser currentUser = mAuth.getCurrentUser();

    public static final int AMNT_PICK_IMAGE = 1;
    Uri mImageUri;
    StorageReference mStorageRef;
    StorageTask mUploadTask;

    boolean isVisible = false;
    String rEmail = "Anything@gmail.com";
    String cUser;

    {
        assert currentUser != null;
        cUser = currentUser.getUid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_layout);

        nameDog = findViewById(R.id.NameB);
        display = findViewById(R.id.date);
        displaytime = findViewById(R.id.time);
        inputName1 = findViewById(R.id.NameEdit1);
        inputAge1 = findViewById(R.id.AgeEdit1);
        inputBreed = findViewById(R.id.breed);
        inputEnergy = findViewById(R.id.energyLevel);
        meeting1 = findViewById(R.id.Meeting);
        displayDateEnd = findViewById(R.id.displayEndDate);

        dateButton = findViewById(R.id.button);
        cMail = findViewById(R.id.mail);
        deleteAnimal = findViewById(R.id.delete);
        updateAnimal = findViewById(R.id.update);
        backToHome = findViewById(R.id.goExplore);
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        inputAnimal = findViewById(R.id.submitButton1);
        EndTime = findViewById(R.id.endTime);
        
        testMail = findViewById(R.id.testMail);
        testMail.setOnClickListener(v->{
            testMail1();
        });

        userPermissions();
        displayDogName();

        backToHome.setOnClickListener(v -> {
            goBackToFindActivity();
        });

        deleteAnimal.setOnClickListener(v -> {
            confirmDialog();
        });

        updateAnimal.setOnClickListener(v3 -> {
            showUpdate();
        });


        dateButton.setOnClickListener(view -> {
            openDatePickerDialog();
            openTimePickerDialog();
            openDateFinish();
        });

        cMail.setOnClickListener(v1 -> {
            getMailFromFirebase();
            goBackToHome();
        });

    }

    private void testMail1() {
        try {
            String baseUrl = "http://10.0.2.2:8000/send/";
            String email = "devanojose4@gmail.com";

            // Construct the complete URL
            String link = baseUrl + email;

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Process the response content as needed
                            Toast.makeText(getApplicationContext(), "Email sent. Response: " + response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error Handling", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Add the request to the RequestQueue
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Email not sent", Toast.LENGTH_SHORT).show();
        }
    }

    private void goBackToFindActivity() {
        Bundle bundle2 = new Bundle();
        Intent intent2 = new Intent(SubmissionPage.this, FindActivity.class);
        intent2.putExtras(bundle2);
        startActivity(intent2);
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to proceed?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setUpDeleteButton();
                        goBackToHome();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void displayDogName() {
        Bundle getBundle = this.getIntent().getExtras();
        String n = getBundle.getString("Name");
        nameDog.setText(n);
    }

    private void userPermissions() {
        drUser.child(cUser).child("organisation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean org = Boolean.TRUE.equals(snapshot.getValue(boolean.class));
                if (org) {
                    isOrganisation();
                } else {
                    isUser();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {Toast.makeText(getApplicationContext(), "Organisation not found", Toast.LENGTH_SHORT).show();}
        });
    }

    private void isUser() {
        deleteAnimal.setVisibility(View.INVISIBLE);
        updateAnimal.setVisibility(View.INVISIBLE);

    }

    private void isOrganisation() {
        dateButton.setVisibility(View.INVISIBLE);
        cMail.setVisibility(View.INVISIBLE);
        meeting1.setVisibility(View.INVISIBLE);
        EndTime.setVisibility(View.INVISIBLE);
    }

    private void goBackToHome() {
        Bundle bundle2 = new Bundle();
        Intent intent2 = new Intent(SubmissionPage.this, Home.class);
        intent2.putExtras(bundle2);
        startActivity(intent2);
    }


    private void showUpdate() {
        Bundle getBundle = this.getIntent().getExtras();
        String idUser = getBundle.getString("idFromUser");

        if (isVisible) {
            inputName1.setVisibility(View.INVISIBLE);
            inputAge1.setVisibility(View.INVISIBLE);
            inputBreed.setVisibility(View.INVISIBLE);
            inputEnergy.setVisibility(View.INVISIBLE);
            inputAnimal.setVisibility(View.INVISIBLE);
            mButtonChooseImage.setVisibility(View.INVISIBLE);
            isVisible = false;
        } else {
            inputName1.setVisibility(View.VISIBLE);
            inputAge1.setVisibility(View.VISIBLE);
            inputBreed.setVisibility(View.VISIBLE);
            inputEnergy.setVisibility(View.VISIBLE);
            inputAnimal.setVisibility(View.VISIBLE);
            mButtonChooseImage.setVisibility(View.VISIBLE);
            isVisible = true;
        }

        mButtonChooseImage.setOnClickListener(v -> openFileChooser());

        inputAnimal.setOnClickListener(v -> {
            if (cUser.equals(idUser)) {
                if (!inputAge1.getText().toString().matches("(0[1-9]|1[0-2])/(0[1-9]|[1-2][0-9]|3[01])/\\d{4}")) {
                    // If dateOfBirth1 is not null or empty, but it's not in the correct format, show an error message
                    Toast.makeText(getApplicationContext(), "Date format should be in number XX/XX/XXXX & be valid dates", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateAnimalFeatures();
            } else {
                Toast.makeText(this, "You don't the permission", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAnimalFeatures() {
        Bundle getBundle = this.getIntent().getExtras();
        String id = getBundle.getString("Id");

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        dr.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Animal animal = snapshot.getValue(Animal.class);
                if (animal == null) {
                    Toast.makeText(SubmissionPage.this, "Animal not found", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!TextUtils.isEmpty(inputName1.getText().toString())) {
                    animal.setName(inputName1.getText().toString());
                }
                if (!TextUtils.isEmpty(inputAge1.getText().toString())) {
                    animal.setDOB(inputAge1.getText().toString());
                }
                if (!TextUtils.isEmpty(inputBreed.getText().toString())) {
                    animal.setBreed(inputBreed.getText().toString());
                }
                if (!TextUtils.isEmpty(inputEnergy.getText().toString())) {
                    animal.setEnergyLevel(inputEnergy.getText().toString());
                }

                // Upload image if selected
                if (mImageUri != null) {
                    StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
                    mUploadTask = fileReference.putFile(mImageUri)
                            .addOnSuccessListener(taskSnapshot -> {

                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();

                                    animal.setmImageUrl(imageUrl);

                                    // Save updated animal to database
                                    dr.child(id).setValue(animal)
                                            .addOnSuccessListener(aVoid -> Toast.makeText(SubmissionPage.this, "Animal updated successfully", Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener(e -> Toast.makeText(SubmissionPage.this, "Failed to update animal", Toast.LENGTH_SHORT).show());
                                });
                            })
                            .addOnFailureListener(e -> Toast.makeText(SubmissionPage.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    // Save updated animal to database
                    dr.child(id).setValue(animal)
                            .addOnSuccessListener(aVoid -> Toast.makeText(SubmissionPage.this, "Animal updated successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(SubmissionPage.this, "Failed to update animal", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SubmissionPage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, AMNT_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AMNT_PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            //mImageView.setImageURI(mImageUri); Use this to display Image
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void setUpDeleteButton() {
        Bundle getBundle = this.getIntent().getExtras();
        String id = getBundle.getString("Id");
        String idUser = getBundle.getString("idFromUser");

        if (cUser.equals(idUser)) {
            dr.child(id).removeValue()
                    .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Animal deleted successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to delete animal", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "You don't the permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMailFromFirebase() {
        drUser.child(cUser).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rEmail = snapshot.getValue(String.class);
                //confirmAndMail(rEmail);
                removeAnimal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Email Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeAnimal() {
        Bundle getBundle = this.getIntent().getExtras();
        String id = getBundle.getString("Id");

        dr.child(id).removeValue();
        Toast.makeText(this, "Animal Chosen", Toast.LENGTH_SHORT).show();
    }

    private void openTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour, minute) -> displaytime.setText(hour + ":"+ minute), 15, 30, false);
        timePickerDialog.show();
    }

    private void openDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            month = month + 1; // Weird bug where where begining of month is 0 so add 1
            display.setText(year + "."+ month + "."+ day);
        }, 2023, 1, 20);
        datePickerDialog.show();
    }

    private void openDateFinish() {
        EndTime.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
                month = month + 1; // Weird bug where where begining of month is 0 so add 1
                displayDateEnd.setText(year + "."+ month + "."+ day);
            }, 2023, 1, 20);
            datePickerDialog.show();
        });
    }
}
