package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
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

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    ImageButton logout,openDrawer;
    TextView textView;
    FirebaseUser user;
    ArrayList<Menu> datalist;
    MenuAdapter adapter;
    SearchView searchView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        user = auth.getCurrentUser();

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new MenuAdapter(this, datalist);
        recyclerView.setAdapter(adapter);
        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.menu);
        searchView = findViewById(R.id.searchView);
        searchView.setFocusable(true);
        searchView.setIconified(true);
        searchView.requestFocus();

        // Check if the user is null or an admin
        if (user == null || user.getEmail().endsWith("@admin.com")) {
            Intent intent = new Intent(getApplicationContext(), Admin_home.class);
            startActivity(intent);
            finish();
        } else {
            fStore = FirebaseFirestore.getInstance();
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            datalist = new ArrayList<>();
            adapter = new MenuAdapter(this, datalist);
            recyclerView.setAdapter(adapter);
            drawerLayout = findViewById(R.id.drawerLayout);
            openDrawer = findViewById(R.id.menu);
            searchView = findViewById(R.id.searchView);
            searchView.setFocusable(true);
            searchView.setIconified(true);
            searchView.requestFocus();

            /*fetch main dish data*/
            fStore.collection("menu")
                    .limit(3)
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
                                    datalist.add(new Menu(dishName, dishPrice, dishImage, dishDesc));
                                }

                                adapter.notifyDataSetChanged();

                            }
                        }
                    });

            adapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Menu selectedItem = datalist.get(position);

                    Intent intent = new Intent(MainActivity.this, Menu_item.class);
                    intent.putExtra("menuName", selectedItem.getName());
                    intent.putExtra("menuPrice", selectedItem.getPrice());
                    intent.putExtra("menuImage", selectedItem.getImage());
                    intent.putExtra("menuDetail", selectedItem.getDetail());
                    startActivity(intent);
                }
            });

            /*search*/
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("MainActivity", "Search submitted with query: " + query);
                    filterList(query);
                    Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                    intent.putExtra("search_query", query);
                    startActivity(intent);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Handle search query text change
                    Log.d("MainActivity", "Search text changed to: " + newText);
                    filterList(newText);
                    return true;
                }
            });

            /*display user email if user login */
            TextView text = findViewById(R.id.user_details);
            text.setText(user.getEmail());
            TextView textView = findViewById(R.id.user_email);
            textView.setText(user.getEmail());

            /*logout*/
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                    finish();
                }
            });

            /*open menu*/
            openDrawer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerMenu))) {
                        drawerLayout.closeDrawer(findViewById(R.id.drawerMenu));
                    } else {
                        drawerLayout.openDrawer(findViewById(R.id.drawerMenu));
                    }
                }
            });
        }
    }

    public void filterList(String keyword) {
        List<Menu> filteredList = new ArrayList<>();

        if (keyword.isEmpty()) {
            filteredList.addAll(datalist);
        } else {
            for (Menu menu : datalist) {
                if (menu.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(menu);
                }
            }
        }

        Log.d("FilterList", "Filtered list size: " + filteredList.size());

        adapter.setFilteredList(filteredList);

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    /*menu*/
    public void toPrivacy(View view){
        Intent intent = new Intent(this, Privacy_policy.class);
        TextView toPrivacy= findViewById(R.id.privacy);
        startActivity(intent);
    }

    public void toPickup(View view){
        Intent intent = new Intent(this, Pickup_info.class);
        TextView toPickup= findViewById(R.id.pickup);
        startActivity(intent);
    }

    public void toContact(View view){
        Intent intent = new Intent(this, Contact_us.class);
        TextView toContact= findViewById(R.id.contact);
        startActivity(intent);
    }

    public void toLoginPage(View view){
        Intent intent = new Intent(this, login.class);
        ImageButton toLoginPage = findViewById(R.id.login);
        startActivity(intent);
    }

    public void toWallet(View view){
        Intent intent = new Intent(this, Wallet.class);
        ImageButton toWallet = findViewById(R.id.wallet);
        startActivity(intent);
    }

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

    public void toAccount(View view){
        Intent intent = new Intent(this, Account_details.class);
        TextView toAccount = findViewById(R.id.accountPage);
        startActivity(intent);
    }

    public void toCart(View view){
        Intent intent = new Intent(this, Cart.class);
        TextView toCart = findViewById(R.id.cart);
        startActivity(intent);
    }

}
