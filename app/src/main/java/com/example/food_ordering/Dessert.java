package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class Dessert extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    ArrayList<Menu> datalist;
    MenuAdapter adapter;
    DrawerLayout drawerLayout;
    ImageButton openDrawer;

    FirebaseAuth auth;
    ImageButton logout;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dessert);

        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.menu);

        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        user = auth.getCurrentUser();

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

        /*display user email if user login */
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        } else {
            TextView text= findViewById(R.id.user_details);
            text.setText(user.getEmail());
            TextView textView = findViewById(R.id.user_email);
            textView.setText(user.getEmail());
        }

        /*fetch dessert data*/
        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.dessert);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist= new ArrayList<>();
        adapter = new MenuAdapter(this, datalist);
        recyclerView.setAdapter(adapter);

        fStore.collection("menu")
                .whereEqualTo("menu_category","Dessert")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String dessertName = document.getString("menu_name");
                                String dessertPrice = document.getString("menu_price");
                                String dessertImage = document.getString("menu_image");
                                String dessertDesc = document.getString("menu_detail");

                                // Add the retrieved data to the ArrayList
                                datalist.add(new Menu(dessertName, dessertPrice, dessertImage,dessertDesc)); // Change to the appropriate data class (BeverageData)
                            }

                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle the case where the query was not successful
                        }
                    }
                });

        adapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Get the selected menu item
                Menu selectedItem = datalist.get(position);

                // Create an Intent to send the selected menu item's details to the MenuItemDashboardActivity
                Intent intent = new Intent(Dessert.this, Menu_item.class);
                intent.putExtra("menuName", selectedItem.getName());
                intent.putExtra("menuPrice", selectedItem.getPrice());
                intent.putExtra("menuImage", selectedItem.getImage());
                intent.putExtra("menuDetail", selectedItem.getDetail());
                startActivity(intent);
            }
        });

    }

    /*menu*/
    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        TextView toHome = findViewById(R.id.home);
        startActivity(intent);
    }
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
