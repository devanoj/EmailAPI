package com.example.emailapi.Main;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.emailapi.DAO.AnimalDAO;
import com.example.emailapi.Entity.Animal;
import com.example.emailapi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

public class Explore extends AppCompatActivity {
    Button inputAnimal, infoButton1, goFilter, mButtonChooseImage, aOptions;
    EditText inputName1, inputAge1, inputBreed, inputEnergy;
    TextView data;
    ImageView animalExplore, Profile1, Filter1, Find1, HomeMain1;
    boolean isVisible = false;

    StorageTask mUploadTask;
    Uri mImageUri;
    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef; // Just a test later put the reference into reference

    private static final int AMNT_PICK_IMAGE = 1;
    String email1;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_layout);

        inputAnimal = findViewById(R.id.submitButton1);
        infoButton1 = findViewById(R.id.addInfoButton1);
        goFilter = findViewById(R.id.GoToFilter);
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        aOptions = findViewById(R.id.AnimalOptions);
        // EditText
        inputName1 = findViewById(R.id.NameEdit1);
        inputAge1 = findViewById(R.id.AgeEdit1);
        inputBreed = findViewById(R.id.breed);
        inputEnergy = findViewById(R.id.energyLevel);
        data = findViewById(R.id.animal_fact);
        // ImageView
        animalExplore = findViewById(R.id.ExploreAnimal);
        Profile1 = findViewById(R.id.Profile);
        Filter1 = findViewById(R.id.Filter);
        Find1 = findViewById(R.id.Find);
        HomeMain1 = findViewById(R.id.HomeMain);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads"); // Maybe delete later

        Profile1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Explore.this, Profile.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        Filter1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Explore.this, Filtering.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        Find1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Explore.this, FindActivity.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        HomeMain1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Explore.this, Home.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });

        aOptions.setOnClickListener(v0 -> {
            Bundle bundle2 = new Bundle();
            Intent intent2 = new Intent(Explore.this, AnimalRUD.class);
            intent2.putExtras(bundle2);
            startActivity(intent2);
        });

        mButtonChooseImage.setOnClickListener(v -> openFileChooser());
        goFilter.setOnClickListener(v -> {
            Bundle bundle2 = new Bundle();
            Intent intent2 = new Intent(Explore.this, Filtering.class);
            intent2.putExtras(bundle2);
            startActivity(intent2);
        });


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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email1 = currentUser.getEmail();
            uid = currentUser.getUid();
        }

        inputAnimal.setOnClickListener(view -> {
            if (mImageUri != null) {

                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

                mUploadTask = fileReference.putFile(mImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String id = null;


                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        Animal pet1 = new Animal(uid, id, inputName1.getText().toString(), inputAge1.getText().toString(), inputBreed.getText().toString(), inputEnergy.getText().toString(),
                                                email1, imageUrl);
                                        AnimalDAO aDAO = new AnimalDAO(pet1);
                                        Toast.makeText(Explore.this, "Entered Data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(Explore.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(Explore.this, "No file selected", Toast.LENGTH_SHORT).show();
            }

        });

        String url = "https://catfact.ninja/fact";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //response = request.get(url, headers={'X-Api-Key': 'YOUR_API_KEY'}
                            //String x = response.getString("facts");

                            //JSONObject foo = response;
                            //String facts = foo.get("facts").toString();
                            //String datetime = response.getString("datetime");
                            //String date = datetime.split("T")[0];

                            //Log.e("onResponse", String.valueOf(response));

                            String x = response.getString("fact");

                            /*
                            JSONObject jsonObject = new JSONObject((Map) response);
                            String x = jsonObject.getString("facts");*/


                            data.setText(x);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorVolley", error.toString());
                Toast.makeText(Explore.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(request);


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
