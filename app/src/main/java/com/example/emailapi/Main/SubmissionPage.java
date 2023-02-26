package com.example.emailapi.Main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
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

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SubmissionPage extends AppCompatActivity {
    TextView nameDog, display, displaytime, inputName1, inputAge1, inputBreed, inputEnergy;
    Button dateButton, cMail, deleteAnimal, updateAnimal, mButtonChooseImage, backToExplore, inputAnimal;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dr = database.getReference("Animal");
    DatabaseReference drUser = database.getReference("User");
    FirebaseUser currentUser = mAuth.getCurrentUser();

    public static final int AMNT_PICK_IMAGE = 1;
    Uri mImageUri;
    StorageReference mStorageRef;
    StorageTask mUploadTask;

    String x = "Hi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity);

        nameDog = findViewById(R.id.NameB);
        display = findViewById(R.id.date);
        displaytime = findViewById(R.id.time);
        inputName1 = findViewById(R.id.NameEdit1);
        inputAge1 = findViewById(R.id.AgeEdit1);
        inputBreed = findViewById(R.id.breed);
        inputEnergy = findViewById(R.id.energyLevel);

        dateButton = findViewById(R.id.button);
        cMail = findViewById(R.id.mail);
        deleteAnimal = findViewById(R.id.delete);
        updateAnimal = findViewById(R.id.update);
        backToExplore = findViewById(R.id.goExplore);
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        inputAnimal = findViewById(R.id.submitButton1);

        backToExplore.setOnClickListener(v -> {
            goBackToExplore();
        });

        deleteAnimal.setOnClickListener(v -> {
            setUpDeleteButton();
            goBackToExplore();
        });

        updateAnimal.setOnClickListener(v3 -> {
            showUpdate();
        });

        nameDog.setText("Meeting date for: ");
        dateButton.setOnClickListener(view -> {
            openDatePickerDialog();
            openTimePickerDialog();
        });

        cMail.setOnClickListener(v1 -> {
            getMailFromFirebase();
            goBackToExplore();
        });

    }

    private void goBackToExplore() {
        Bundle bundle2 = new Bundle();
        Intent intent2 = new Intent(SubmissionPage.this, Explore.class);
        intent2.putExtras(bundle2);
        startActivity(intent2);
    }


    private void showUpdate() {
        Bundle getBundle = this.getIntent().getExtras();
        String idUser = getBundle.getString("idFromUser");
        String cUser = currentUser.getUid();

        inputName1.setVisibility(View.VISIBLE);
        inputAge1.setVisibility(View.VISIBLE);
        inputBreed.setVisibility(View.VISIBLE);
        inputEnergy.setVisibility(View.VISIBLE);
        inputAnimal.setVisibility(View.VISIBLE);
        mButtonChooseImage.setVisibility(View.VISIBLE);

        mButtonChooseImage.setOnClickListener(v -> openFileChooser());

        inputAnimal.setOnClickListener(v -> {
            if (cUser.equals(idUser)) {
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
                    animal.setAge(inputAge1.getText().toString());
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
        String cUser = currentUser.getUid();

        if (cUser.equals(idUser)) {
            dr.child(id).removeValue()
                    .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Animal deleted successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to delete animal", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "You don't the permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMailFromFirebase() {
        String cUser = currentUser.getUid();
        
        drUser.child(cUser).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                x = snapshot.getValue(String.class);
                confirmAndMail(x);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Email Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmAndMail(String stringReceiverEmail) {
        String time = displaytime.getText().toString();
        String date = display.getText().toString();
        Bundle getBundle = this.getIntent().getExtras();
        String name = getBundle.getString("Name");

        try {
            String stringSenderEmail = "devanojose4@gmail.com";
            String stringPasswordSenderEmail = "hfllnbeydaxihzez";
            //String stringPasswordSenderEmail = "Xbjj7866";
            //String stringReceiverEmail = "devanojos@gmail.com";

            String stringHost = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
            mimeMessage.setSubject("RE: Animal Fostering");
            mimeMessage.setText("Hello User, \n\nWe are delighted to announce you are fostering a dog name " + name + ". " +
                    "When: " + time + " on the " + date + "." +
                    "\n\nCheers!\nAniFost");

            Thread thread = new Thread(() -> {
                try {Transport.send(mimeMessage);} catch (MessagingException e) {e.printStackTrace();}
            });
            thread.start();

            Toast.makeText(getApplicationContext(), "Confirmation Email sent", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {e.printStackTrace();}
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
}
