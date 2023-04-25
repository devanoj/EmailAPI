package com.example.emailapi.Main;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnimalRUD extends AppCompatActivity {
    DatabaseReference dref;
    FirebaseRecyclerOptions<Animal> options;
    FirebaseRecyclerAdapter<Animal, CategoryViewHolder> adapter;
    ArrayList<Animal> arrayList;
    RecyclerView recyclerView;
    EditText search;

    ImageView BackToCreate;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rud_layout);


        BackToCreate = findViewById(R.id.backToCreate);


        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();


        BackToCreate.setOnClickListener(v->{
            Bundle bundle2 = new Bundle();
            Intent intent2 = new Intent(AnimalRUD.this, Create.class);
            intent2.putExtras(bundle2);
            startActivity(intent2);
        });


        dref = FirebaseDatabase.getInstance().getReference().child("Animal");
        /**
        options = new FirebaseRecyclerOptions.Builder<Animal>()
                .setQuery(dref,Animal.class).build(); **/

        Query query = dref.orderByChild("usid").equalTo(currentUser.getUid());
        options = new FirebaseRecyclerOptions.Builder<Animal>()
                .setQuery(query, Animal.class).build();

        adapter = new FirebaseRecyclerAdapter<Animal, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Animal model) {
                holder.cat.setText(model.getDOB());
                holder.dead.setText(model.getBreed());
                holder.desc.setText(model.getEnergyLevel());
                holder.stat.setText(model.getName());
                holder.fromOrg.setText(model.getFrom());
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
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                if (adapter.getItemCount() == 0) {
                    Log.d("Fix", "No data to be inputed");
                }
            }
        });
        recyclerView.setAdapter(adapter);
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




