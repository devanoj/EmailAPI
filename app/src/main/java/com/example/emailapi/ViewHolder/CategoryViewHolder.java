package com.example.emailapi.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emailapi.Main.SubmissionPage;
import com.example.emailapi.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView cat, dead, desc, stat, id, idfromUser, fromOrg;
    public Button select;
    public ImageView pictureDog;
    private Context context;
    public String animalID;

    public CategoryViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        cat = itemView.findViewById(R.id.categoryEditTV1);
        desc = itemView.findViewById(R.id.descEditTV1);
        dead = itemView.findViewById(R.id.deadlineEditTV1);
        stat = itemView.findViewById(R.id.statusEditTV1);
        id = itemView.findViewById(R.id.idAnimal);
        idfromUser = itemView.findViewById(R.id.idUser);
        fromOrg = itemView.findViewById(R.id.fromEditTV1);

        pictureDog = itemView.findViewById(R.id.pictureOfDog);
        select = itemView.findViewById(R.id.selectAnimal);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(itemView.getContext(), SubmissionPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Bundle bundle = new Bundle();
                bundle.putString("Name", stat.getText().toString());
                bundle.putString("Age", desc.getText().toString());
                bundle.putString("Id", id.getText().toString());
                bundle.putString("idFromUser", idfromUser.getText().toString());

                intent.putExtras(bundle);

                context.startActivity(intent);

            }
        });
    }


}
