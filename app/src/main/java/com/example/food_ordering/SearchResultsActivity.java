package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    RecyclerView searchResultsRecyclerView;
    FirebaseFirestore fStore;
    ArrayList<Dish> datalist;
    MyAdapter adapter;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);

        fStore = FirebaseFirestore.getInstance();
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new MyAdapter(this, datalist);
        searchResultsRecyclerView.setAdapter(adapter);
        // Retrieve the search query from the intent
        String searchQuery = getIntent().getStringExtra("search_query");

        fStore.collection("main-dish")
                .whereEqualTo("main_name", searchQuery)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String dishName = document.getString("main_name");
                        String dishPrice = document.getString("main_price");
                        String dishImage = document.getString("main_image");

                        // Add the retrieved data to the ArrayList
                        datalist.add(new Dish(dishName, dishPrice, dishImage));
                    }
                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle the case where the query was not successful
                }
            }
        });

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query submission
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query text change
                filterList(newText);
                return true;
            }
        });


    }
    public void filterList(String keyword) {
        List<Dish> filteredList = new ArrayList<>();
        for (Dish dish : datalist) {
            // Check if the main_name contains the keyword (case-insensitive)
            if (dish.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(dish);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);// Hide the main content
        }
    }
}

