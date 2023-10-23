package com.example.food_ordering;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.util.Log;



public class Beverage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beverage);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve data from Firestore
        CollectionReference collectionRef = db.collection("main-dish");
        Query query = collectionRef.orderBy("main_name"); // Change "field_name" to "main_name" to match your Firestore field

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Handle each document (data) here
                        String data = document.getString("main_name"); // Change "field_name" to "main_name" to match your Firestore field
                        // Do something with the data
                    }
                } else {

                }
            }
        });
    }







}
