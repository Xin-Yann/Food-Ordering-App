package com.example.food_ordering;

import android.os.Bundle;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Beverage extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    ArrayList<BeverageData> berverageList;
    BeverageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beverage);

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.beverage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        berverageList = new ArrayList<>();
        adapter = new BeverageAdapter(this, berverageList);
        recyclerView.setAdapter(adapter);

        fStore.collection("beverage").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String beverageName = document.getString("beverage_name");
                        String beveragePrice = document.getString("beverage_price");
                        String beverageImage = document.getString("beverage_image");

                        // Add the retrieved data to the ArrayList
                        berverageList.add(new BeverageData(beverageName, beveragePrice, beverageImage)); // Change to the appropriate data class (BeverageData)
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    // Handle the case where the query was not successful
                }
            }
        });
    }
}
