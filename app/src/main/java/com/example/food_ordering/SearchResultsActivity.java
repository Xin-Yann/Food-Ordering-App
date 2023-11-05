package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    RecyclerView searchResultsRecyclerView;
    FirebaseFirestore fStore;
    ArrayList<Menu> datalist;
    MenuAdapter adapter;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);

        fStore = FirebaseFirestore.getInstance();
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new MenuAdapter(this, datalist);
        searchResultsRecyclerView.setAdapter(adapter);
        // Retrieve the search query from the intent
        String searchQuery = getIntent().getStringExtra("search_query");

        fStore.collection("menu")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String dishName = document.getString("menu_name");
                        String dishPrice = document.getString("menu_price");
                        String dishImage = document.getString("menu_image");
                        String dishDesc = document.getString("menu_detail");

                        // Add the retrieved data to the ArrayList
                        datalist.add(new Menu(dishName, dishPrice, dishImage,dishDesc));
                    }
                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle the case where the query was not successful
                }
            }
        });

        searchView = findViewById(R.id.searchView);
        searchView.setFocusable(true);
        searchView.setIconified(true);
        searchView.requestFocus();
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
        List<Menu> filteredList = new ArrayList<>();

        if (keyword.isEmpty()) {
            // If the query is empty, show all items
            filteredList.addAll(datalist);
        } else {
            for (Menu menu : datalist) {
                if (menu.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(menu);
                }
            }
        }

        // Update the adapter with the filtered or all items
        adapter.setFilteredList(filteredList);

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }


}

