package com.example.emailapi.Main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import com.example.emailapi.R;

public class AddInfo extends AppCompatActivity {

    Button nextPage;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info_layout);

        spinner = findViewById(R.id.my_spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.my_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        goToLogin();

    }

    private void goToLogin() {
        nextPage = findViewById(R.id.nextPage);
        nextPage.setOnClickListener(v->{
            String selectedItem = spinner.getSelectedItem().toString();
            Log.d("ItemChosenXXX", "Selected item: " + selectedItem);
        });
    }
}
