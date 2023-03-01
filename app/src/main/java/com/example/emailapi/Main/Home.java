package com.example.emailapi.Main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emailapi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Home extends AppCompatActivity {
    ImageView animalExplore, Profile1, Filter1, Find1, HomeMain1;
    TextView nameUser;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference drUser = database.getReference("User");
    String cUser;
    {
        assert currentUser != null;
        cUser = currentUser.getUid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        sideNavMenu();
        getWelcomeName();
        new GetFactsTask().execute();
    }

    private class GetFactsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Set up the API endpoint URL
                String apiUrl = "https://dog-api.kinduff.com/api/facts";

                // Set up the request
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // Read the response
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Parse the response
                JSONObject json = new JSONObject(result.toString());
                JSONArray facts = json.getJSONArray("facts");

                // Build the text
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < facts.length(); i++) {
                    text.append(facts.get(i)).append("\n");
                }
                return text.toString();

            }catch (MalformedURLException e) {
                    return "Error: Invalid URL";
            } catch (IOException e) {
                    return "Error: IO Exception";
            } catch (JSONException e) {
                    return "Error: JSON Exception";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            TextView textView = findViewById(R.id.dogfact);
            textView.setText(result);
            }
        }



    private void getWelcomeName() {
        nameUser = findViewById(R.id.nameDisplay);
        String cUser = currentUser.getUid();

        drUser.child(cUser).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String x = snapshot.getValue(String.class);
                nameUser.setText(x);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Name not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sideNavMenu() {
        animalExplore = findViewById(R.id.ExploreAnimal);
        Profile1 = findViewById(R.id.Profile);
        Filter1 = findViewById(R.id.Filter);
        Find1 = findViewById(R.id.Find);
        HomeMain1 = findViewById(R.id.HomeMain);

        makeCreateVisible();

        Profile1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Home.this, Profile.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        animalExplore.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Home.this, Create.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        Filter1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Home.this, Filtering.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        Find1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Home.this, FindActivity.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
    }

    private void makeCreateVisible() {
        drUser.child(cUser).child("organisation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean org = Boolean.TRUE.equals(snapshot.getValue(boolean.class));
                if (org) {
                    animalExplore.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {Toast.makeText(getApplicationContext(), "Organisation not found", Toast.LENGTH_SHORT).show();}
        });
    }
}
