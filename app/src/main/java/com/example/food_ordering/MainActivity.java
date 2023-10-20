package com.example.food_ordering;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    TextView mainName, mainPrice;
    ImageView mainImage;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home); // Replace with the layout for your main dish activity.

        fStore = FirebaseFirestore.getInstance();
        mainName = findViewById(R.id.main_name); // Replace with the actual ID of your TextView for dish name
        mainPrice = findViewById(R.id.main_price); // Replace with the actual ID of your TextView for dish price
        mainImage = findViewById(R.id.main_Image); // Replace with the actual ID of your ImageView for dish image

        fStore.collection("main-dish").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String dishName = document.getString("main_name");
                        String dishPrice = document.getString("main_price");
                        String dishImage = document.getString("main_image");

                        // Display text data
                        mainName.setText(dishName);
                        mainPrice.setText(dishPrice);

                        // Load and display the image using Picasso
                        Picasso.get()
                                .load(dishImage)
                                .placeholder(R.drawable.vegetables) // Replace with your placeholder image
                                .error(R.drawable.logo) // Replace with your error image
                                .into(mainImage);
                    }
                } else {
                    // Handle the case where the query was not successful
                }
            }
        });
    }
}
