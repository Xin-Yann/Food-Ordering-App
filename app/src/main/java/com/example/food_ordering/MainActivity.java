package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView, searchResultsRecyclerView;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    ImageButton logout;
    TextView textView;
    FirebaseUser user;
    ArrayList<Dish> datalist;
    MyAdapter adapter;
    SearchView searchView;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new MyAdapter(this, datalist);
        recyclerView.setAdapter(adapter);

        fStore.collection("main-dish").limit(3).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query text change
                filterList(newText);
                return true;
            }
        });


        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void filterList(String keyword) {
        List<Dish> filteredList = new ArrayList<>();
        for (Dish dish : datalist) {
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

    /*public void toLoginPage(View view){
        Intent intent = new Intent(this, login.class);
        ImageButton toLoginPage = findViewById(R.id.login);
        startActivity(intent);
    }*/

    /*Category*/
    public void toMainDish(View view){
        Intent intent = new Intent(this, Main_dish.class);
        ImageButton toMainPage = findViewById(R.id.main_dish_btn);
        startActivity(intent);
    }

    public void toBeverage(View view){
        Intent intent = new Intent(this, Beverage.class);
        ImageButton toBeverage = findViewById(R.id.beverage_btn);
        startActivity(intent);
    }

    public void toDessert(View view){
        Intent intent = new Intent(this, Dessert.class);
        ImageButton toDessert = findViewById(R.id.dessert_btn);
        startActivity(intent);
    }

    /*Footer*/
    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        ImageButton toHome = findViewById(R.id.home_page);
        startActivity(intent);
    }

    public void toOrder(View view){
        Intent intent = new Intent(this, Order_history_n_upcoming.class);
        ImageButton toOrder = findViewById(R.id.order);
        startActivity(intent);
    }

    public void toCartPage(View view){
        Intent intent = new Intent(this, Cart.class);
        ImageButton toCartPage = findViewById(R.id.cartPage);
        startActivity(intent);
    }

    public void toAccount(View view){
        Intent intent = new Intent(this, Account_details.class);
        ImageButton toAccount = findViewById(R.id.accountPage);
        startActivity(intent);
    }

}
