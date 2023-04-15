package com.example.emailapi.Main;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emailapi.DAO.AnimalDAO;
import com.example.emailapi.Entity.Animal;
import com.example.emailapi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;

public class Create extends AppCompatActivity {
    Button inputAnimal, infoButton1, mButtonChooseImage, aOptions;
    EditText inputName1, inputAge1, inputBreed, inputEnergy;
    ImageView animalExplore, Profile1, Filter1, Find1, HomeMain1;
    boolean isVisible = false;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference drUser = database.getReference("User");

    StorageTask mUploadTask;
    Uri mImageUri;
    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef; // Just a test later put the reference into reference

    private static final int AMNT_PICK_IMAGE = 1; //Change manually to 1
    String email1;
    String uid;
    String from;
    String fromValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_layout);

        inputAnimal = findViewById(R.id.submitButton1);
        infoButton1 = findViewById(R.id.addInfoButton1);
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        aOptions = findViewById(R.id.AnimalOptions);

        inputName1 = findViewById(R.id.NameEdit1);
        inputAge1 = findViewById(R.id.AgeEdit1);
        inputBreed = findViewById(R.id.breed);
        inputEnergy = findViewById(R.id.energyLevel);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads"); // Maybe delete later

        sideNavMenu();
        setVisibilityForInfoBttn();
        inputDog();

        aOptions.setOnClickListener(v0 -> {
            Bundle bundle2 = new Bundle();
            Intent intent2 = new Intent(Create.this, AnimalRUD.class);
            intent2.putExtras(bundle2);
            startActivity(intent2);
        });

        mButtonChooseImage.setOnClickListener(v -> openFileChooser());

    }

    private void inputDog() {

        if (currentUser != null) {
            email1 = currentUser.getEmail();
            uid = currentUser.getUid();
        }

        //from = getFrom();

        drUser.child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fromValue = snapshot.getValue(String.class);;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {Toast.makeText(getApplicationContext(), "Organisation not found", Toast.LENGTH_SHORT).show();}
        });

        inputAnimal.setOnClickListener(view -> {
            if (!inputAge1.getText().toString().matches("(0[1-9]|1[0-2])/(0[1-9]|[1-2][0-9]|3[01])/\\d{4}")) {
                // If dateOfBirth1 is not null or empty, but it's not in the correct format, show an error message
                Toast.makeText(getApplicationContext(), "Date format should be in number XX/XX/XXXX & be valid dates", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mImageUri != null) {

                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

                mUploadTask = fileReference.putFile(mImageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            String id = null;

                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();

                                Animal pet1 = new Animal(uid, id, inputName1.getText().toString(), inputAge1.getText().toString(), inputBreed.getText().toString(), inputEnergy.getText().toString(),
                                        email1, imageUrl, fromValue);
                                AnimalDAO aDAO = new AnimalDAO(pet1);
                                Toast.makeText(Create.this, "Entered Data", Toast.LENGTH_SHORT).show();
                            });
                        })
                        .addOnFailureListener(e -> Toast.makeText(Create.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(Create.this, "No file selected", Toast.LENGTH_SHORT).show();
            }

        });
    }

    /**
    private String getFrom() {

        if (currentUser != null) {
            uid = currentUser.getUid();
        }
        return fromValue;
    } **/

    private void setVisibilityForInfoBttn() {
        infoButton1.setOnClickListener(view -> {
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
        });
    }

    private void sideNavMenu() {
        // ImageView
        animalExplore = findViewById(R.id.ExploreAnimal);
        Profile1 = findViewById(R.id.Profile);
        Filter1 = findViewById(R.id.Filter);
        Find1 = findViewById(R.id.Find);
        HomeMain1 = findViewById(R.id.HomeMain);

        Profile1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Create.this, Profile.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        Filter1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Create.this, Filtering.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        Find1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Create.this, FindActivity.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        HomeMain1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Create.this, Home.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
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
}
