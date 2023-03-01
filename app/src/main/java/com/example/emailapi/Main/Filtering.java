package com.example.emailapi.Main;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Filtering extends AppCompatActivity {
    TextView DogName, InfoAPI;
    Button getRequest;
    EditText EnterDogName;
    ImageView animalExplore, Profile1, Filter1, Find1, HomeMain1;

    String outputSim;
    String x;

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
        setContentView(R.layout.filter_layout);

        getRequest = findViewById(R.id.GetRequest);
        EnterDogName = findViewById(R.id.Similar);



        sideNavMenu();
        //getRequest();
        executeDogAPI();

    }

    private void executeDogAPI() {
        getRequest.setOnClickListener(v->{
            x = EnterDogName.getText().toString();
            new DogAPI(x).execute();
            makeRequest(x);
        });

    }

    private class DogAPI extends AsyncTask<Void, Void, String> {
        private String x;

        public DogAPI(String x) {
            this.x = x;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                // Set the API endpoint and API key
                String link = "https://api.api-ninjas.com/v1/dogs?name=";
                String nameOfBreed = "briard";
                String endpoint = link + this.x;
                String apiKey = "BI+Alyvlya8ICTKjNAny1w==zTNZj4XYZz6ESIVw";

                // Make the API call with the API key as a request header
                URL url = new URL(endpoint);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("X-Api-Key", apiKey);
                InputStream inputStream = urlConnection.getInputStream();

                // Parse the JSON response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String name = jsonObject.getString("name");
                        int goodWithChildren = jsonObject.getInt("good_with_children");
                        int goodWithOtherDogs = jsonObject.getInt("good_with_other_dogs");
                        int shedding = jsonObject.getInt("shedding");
                        int grooming = jsonObject.getInt("grooming");
                        int drooling = jsonObject.getInt("drooling");
                        int coatLength = jsonObject.getInt("coat_length");
                        int goodWithStrangers = jsonObject.getInt("good_with_strangers");
                        int playfulness = jsonObject.getInt("playfulness");
                        int protectiveness = jsonObject.getInt("protectiveness");
                        int trainability = jsonObject.getInt("trainability");
                        int energy = jsonObject.getInt("energy");
                        int barking = jsonObject.getInt("barking");
                        int minLifeExpectancy = jsonObject.getInt("min_life_expectancy");
                        int maxLifeExpectancy = jsonObject.getInt("max_life_expectancy");


                        TextView textView = findViewById(R.id.DogAPI);
                        String text = "Information about dog breed" + "\n" +
                                "Name: " + name + "\n" +
                                "Good with children: " + goodWithChildren + "\n" +
                                "Good with other dogs: " + goodWithOtherDogs + "\n" +
                                "Shedding: " + shedding + "\n" +
                                "Grooming: " + grooming + "\n" +
                                "Drooling: " + drooling + "\n" +
                                "Coat length: " + coatLength + "\n" +
                                "Good with strangers: " + goodWithStrangers + "\n" +
                                "Playfulness: " + playfulness + "\n" +
                                "Protectiveness: " + protectiveness + "\n" +
                                "Trainability: " + trainability + "\n" +
                                "Energy: " + energy + "\n" +
                                "Barking: " + barking + "\n" +
                                "Minimum life expectancy: " + minLifeExpectancy + "\n" +
                                "Maximum life expectancy: " + maxLifeExpectancy;
                        textView.setText(text);
                    } else {
                        TextView textView = findViewById(R.id.DogAPI);
                        String text = "No results found.";
                        textView.setText(text);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void makeRequest(String y) {
        try {
            String baseUrl = "http://10.0.2.2:8000/getRecommendation/";


            DogName = findViewById(R.id.DogName);

                //String queryParameter = EnterDogName.getText().toString();
                String link = baseUrl + y;
                RequestQueue queue = Volley.newRequestQueue(this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, link, null, response -> {
                            try {
                                if (response.has("sim")) {
                                    JSONObject z = response.getJSONObject("sim");
                                    outputSim = z.toString();


                                    // Remove the brackets and quotes
                                    String cleanInput = outputSim.replace("{", "").replace("}", "");

                                    // Split the input into breed names and scores
                                    String[] breedScores = cleanInput.split(",");

                                    // Initialize a StringBuilder to store the breed names and scores
                                    StringBuilder resultBuilder = new StringBuilder();

                                    // Loop through each breed score and convert the decimal to a percentage
                                    for (String breedScore : breedScores) {
                                        String[] parts = breedScore.split(":");
                                        String breedName = parts[0];
                                        double score = Double.parseDouble(parts[1]);
                                        int percentage = (int) (score * 100);
                                        String formattedScore = percentage + "%";
                                        String breedResult = breedName + ": " + formattedScore;
                                        resultBuilder.append(breedResult).append("\n");
                                    }

                                    // Get the final result as a string
                                    String result = resultBuilder.toString();
                                    DogName.setText(result);
                                    //DogName.setText(x.toString());

                                } else {Toast.makeText(Filtering.this, "Key 'good_with_children' not found in response", Toast.LENGTH_SHORT).show();}
                            } catch (JSONException e) {Toast.makeText(Filtering.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();}
                        }, error -> {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                String message = data.getString("message");
                                Toast.makeText(Filtering.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(Filtering.this, "Dog doesn't exist in the database", Toast.LENGTH_SHORT).show();
                            } catch (UnsupportedEncodingException e) {
                                Toast.makeText(Filtering.this, "Unsupported encoding: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                queue.add(jsonObjectRequest);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Info not found", Toast.LENGTH_SHORT).show();
        }

    }

    private void getRequest() {
        try {
            String baseUrl = "http://10.0.2.2:8000/getRecommendation/";


            DogName = findViewById(R.id.DogName);

            getRequest.setOnClickListener(v -> {
                String queryParameter = EnterDogName.getText().toString();
                String link = baseUrl + queryParameter;
                RequestQueue queue = Volley.newRequestQueue(this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, link, null, response -> {
                            try {
                                if (response.has("sim")) {
                                    JSONObject x = response.getJSONObject("sim");
                                    outputSim = x.toString();


                                    // Remove the brackets and quotes
                                    String cleanInput = outputSim.replace("{", "").replace("}", "");

                                    // Split the input into breed names and scores
                                    String[] breedScores = cleanInput.split(",");

                                    // Initialize a StringBuilder to store the breed names and scores
                                    StringBuilder resultBuilder = new StringBuilder();

                                    // Loop through each breed score and convert the decimal to a percentage
                                    for (String breedScore : breedScores) {
                                        String[] parts = breedScore.split(":");
                                        String breedName = parts[0];
                                        double score = Double.parseDouble(parts[1]);
                                        int percentage = (int) (score * 100);
                                        String formattedScore = percentage + "%";
                                        String breedResult = breedName + ": " + formattedScore;
                                        resultBuilder.append(breedResult).append("\n");
                                    }

                                    // Get the final result as a string
                                    String result = resultBuilder.toString();
                                    DogName.setText(result);
                                    //DogName.setText(x.toString());

                                } else {Toast.makeText(Filtering.this, "Key 'good_with_children' not found in response", Toast.LENGTH_SHORT).show();}
                            } catch (JSONException e) {Toast.makeText(Filtering.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();}
                        }, error -> {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                String message = data.getString("message");
                                Toast.makeText(Filtering.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(Filtering.this, "Dog doesn't exist in the database", Toast.LENGTH_SHORT).show();
                            } catch (UnsupportedEncodingException e) {
                                Toast.makeText(Filtering.this, "Unsupported encoding: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                queue.add(jsonObjectRequest);

            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Info not found", Toast.LENGTH_SHORT).show();
        }

    }

    private void sideNavMenu() {
        // ImageView
        animalExplore = findViewById(R.id.ExploreAnimal);
        Profile1 = findViewById(R.id.Profile);
        Filter1 = findViewById(R.id.Filter);
        Find1 = findViewById(R.id.Find);
        HomeMain1 = findViewById(R.id.HomeMain);

        makeCreateVisible();

        Profile1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Filtering.this, Profile.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        animalExplore.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Filtering.this, Create.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        Find1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Filtering.this, FindActivity.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        HomeMain1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(Filtering.this, Home.class);
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

