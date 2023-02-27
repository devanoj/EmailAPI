package com.example.emailapi.Main;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emailapi.Adapter.newAdapter;
import com.example.emailapi.Entity.Animal;
import com.example.emailapi.R;
import com.example.emailapi.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FindActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference dref;
    FirebaseRecyclerOptions<Animal> options;
    FirebaseRecyclerAdapter<Animal, CategoryViewHolder> adapter;
    EditText editText;
    ArrayList<Animal> arrayList;

    ImageView animalExplore, Profile1, Filter1, Find1, HomeMain1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_layout);

        recyclerView = findViewById(R.id.recyclerview);//
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();

        // EditTexts
        editText = findViewById(R.id.inputVariable);

        // ImageView
        animalExplore = findViewById(R.id.ExploreAnimal);
        Profile1 = findViewById(R.id.Profile);
        Filter1 = findViewById(R.id.Filter);
        Find1 = findViewById(R.id.Find);
        HomeMain1 = findViewById(R.id.HomeMain);

        Profile1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(FindActivity.this, Profile.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        animalExplore.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(FindActivity.this, Create.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        Filter1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(FindActivity.this, Filtering.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });
        HomeMain1.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            Intent intent1 = new Intent(FindActivity.this, Home.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()) {
                    searching(editable.toString());
                }
                else {
                    searching("");
                }
            }
        });

        dref = FirebaseDatabase.getInstance().getReference().child("Animal");

        options = new FirebaseRecyclerOptions.Builder<Animal>()
                .setQuery(dref,Animal.class).build();

        adapter = new FirebaseRecyclerAdapter<Animal, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Animal model) {
                holder.cat.setText(model.getAge());
                holder.dead.setText(model.getBreed());
                holder.desc.setText(model.getEnergyLevel());
                holder.stat.setText(model.getName());
                holder.id.setText(model.getId());
                holder.idfromUser.setText(model.getUsid());

                // Get the reference to the image URL
                DatabaseReference imageUrlRef = FirebaseDatabase.getInstance().getReference().child("Animal").child(model.getId()).child("mImageUrl");

                // Add a value event listener to get the image URL
                imageUrlRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get the image URL value from the dataSnapshot
                        String imageUrl = dataSnapshot.getValue(String.class);

                        // Set the image using Picasso library
                        Picasso.get()
                                .load(imageUrl)
                                .fit()
                                .centerCrop()
                                .into(holder.pictureDog);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                    }
                });


            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.animal_rcv_layout,parent,false);

                return new CategoryViewHolder(v, parent.getContext());
            }


        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }

    private void searching(String editable) {
        Query query = dref.orderByChild("name")
                .startAt(editable).endAt(editable+"\uf8ff");

        Query query1 = dref.orderByChild("energyLevel")
                .startAt(editable).endAt(editable+"\uf8ff");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()) {
                    arrayList.clear();
                    for(DataSnapshot sd: snapshot.getChildren()) {
                        final Animal tlist = sd.getValue(Animal.class);
                        arrayList.add(tlist);
                    }

                    newAdapter newAdapter = new newAdapter(getApplicationContext(), arrayList);
                    recyclerView.setAdapter(newAdapter);
                    newAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()) {
                    arrayList.clear();
                    for(DataSnapshot sd: snapshot.getChildren()) {
                        final Animal tlist = sd.getValue(Animal.class);
                        arrayList.add(tlist);
                    }

                    newAdapter newAdapter = new newAdapter(getApplicationContext(), arrayList);
                    recyclerView.setAdapter(newAdapter);
                    newAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); */
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        if(adapter!=null)
            adapter.startListening();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }


}
