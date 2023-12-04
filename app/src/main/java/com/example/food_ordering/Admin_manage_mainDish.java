package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Admin_manage_mainDish extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    ArrayList<AdminMenu> datalist;
    AdminMenuAdapter adapter;
    ImageButton logout,openDrawer;
    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    FirebaseUser admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_manage_main_dish);

        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        admin = auth.getCurrentUser();

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.mainDish);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist= new ArrayList<>();
        adapter = new AdminMenuAdapter(this, datalist);
        recyclerView.setAdapter(adapter);
        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.menu);

        /*fetch main-dish data*/
        fStore.collection("menu")
                .whereEqualTo("menu_category","Main Dish")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String mainDishName = document.getString("menu_name");
                                String mainDishPrice = document.getString("menu_price");
                                String mainDishImage = document.getString("menu_image");
                                String mainDishDesc = document.getString("menu_detail");
                                String mainDishId = document.getString("menu_id");

                                datalist.add(new AdminMenu(mainDishName, mainDishPrice, mainDishImage, mainDishDesc, mainDishId)); // Change to the appropriate data class (BeverageData)
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        adapter.setOnItemClickListener(new AdminMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onEditClick(int position) {
                AdminMenu selectedItem = datalist.get(position);
                String itemId = selectedItem.getId();

                // Start the Edit_Menu activity with the item's ID
                Intent intent = new Intent(Admin_manage_mainDish.this, Edit_Menu.class);
                intent.putExtra("itemId", itemId);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                AdminMenu selectedItem = datalist.get(position);
                String menuItemId = selectedItem.getId();

                fStore.collection("menu")
                        .whereEqualTo("menu_id", menuItemId)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String documentId = document.getId();
                                    Log.d("Delete", "Found document with ID: " + documentId);

                                    document.getReference().delete()
                                            .addOnCompleteListener(deleteTask -> {
                                                if (deleteTask.isSuccessful()) {
                                                    Log.d("Delete", "Document deleted successfully");
                                                    datalist.remove(position);
                                                    adapter.notifyItemRemoved(position);
                                                    adapter.notifyItemRangeChanged(position, datalist.size());
                                                } else {
                                                    Log.e("Delete", "Error deleting document", deleteTask.getException());
                                                }
                                            });
                                }
                            } else {
                                Log.e("Delete", "Error getting documents: ", task.getException());
                            }
                        });
            }
        });

        /*display user email if user login*/
        if (admin == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        } else {
            TextView text = findViewById(R.id.admin_details);
            text.setText(admin.getEmail());

        }

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

    public void toLoginPage(View view){
        Intent intent = new Intent(this, login.class);
        ImageButton toLoginPage = findViewById(R.id.login);
        startActivity(intent);
    }

    /*Footer*/
    public void toHome(View view){
        Intent intent = new Intent(this, Admin_home.class);
        ImageButton toHome = findViewById(R.id.home_page);
        startActivity(intent);
    }

    public void toAddMenu(View view){
        Intent intent = new Intent(this, Add_Menu.class);
        ImageButton toAddMenu = findViewById(R.id.add_menu);
        startActivity(intent);
    }

    public void toOrder(View view){
        Intent intent = new Intent(this, Admin_order.class);
        ImageButton toOrder = findViewById(R.id.order);
        startActivity(intent);
    }

    public void toReportPage(View view){
        Intent intent = new Intent(this, Admin_menu_list.class);
        ImageButton toCartPage = findViewById(R.id.reportPage);
        startActivity(intent);
    }

    public void toAccount(View view){
        Intent intent = new Intent(this, Account_details.class);
        TextView toAccount = findViewById(R.id.accountPage);
        startActivity(intent);
    }

    public void toMainDish(View view){
        Intent intent = new Intent(this, Admin_manage_mainDish.class);
        TextView toMainDish = findViewById(R.id.mainDishPage);
        startActivity(intent);
    }

    public void toBeverage(View view){
        Intent intent = new Intent(this, Admin_manage_beverage.class);
        TextView toBeverage = findViewById(R.id.beveragePage);
        startActivity(intent);
    }

    public void toDessert(View view){
        Intent intent = new Intent(this, Admin_manage_dessert.class);
        TextView toDessert = findViewById(R.id.dessertPage);
        startActivity(intent);
    }
}