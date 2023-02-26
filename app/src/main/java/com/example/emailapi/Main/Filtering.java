package com.example.emailapi.Main;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.emailapi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Filtering extends AppCompatActivity {
    TextView dataFilter, DogName;
    Button b1, getRequest;
    EditText EnterDogName;

    String outputSim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);

        b1 = findViewById(R.id.toHome);

        getRequest();


        b1.setOnClickListener(v -> {
            goHome();
        });
    }

    private void goHome() {
        Bundle bundle2 = new Bundle();
        Intent intent2 = new Intent(Filtering.this, SecondActivity.class);
        intent2.putExtras(bundle2);
        startActivity(intent2);
    }

    private void getRequest() {
        try {
            String baseUrl = "http://10.0.2.2:8000/getRecommendation/";

            getRequest = findViewById(R.id.GetRequest);
            EnterDogName = findViewById(R.id.Similar);
            DogName = findViewById(R.id.DogName);

            getRequest.setOnClickListener(v -> {
                String queryParameter = EnterDogName.getText().toString(); // Takes Data from Edit Text
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
}

