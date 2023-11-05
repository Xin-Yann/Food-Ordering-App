package com.example.food_ordering;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;

public class Admin_manage_beverage extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    ArrayList<AdminMenu> datalist;
    AdminMenuAdapter adapter;
    ImageButton openDrawer;
    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    ImageButton logout;
    FirebaseUser admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_manage_beverage);

        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        admin = auth.getCurrentUser();

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.beverage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist= new ArrayList<>();
        adapter = new AdminMenuAdapter(this, datalist);
        recyclerView.setAdapter(adapter);
        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.menu);

        /*fetch beverage data*/
        fStore.collection("menu")
                .whereEqualTo("menu_category","Beverage")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String beverageName = document.getString("menu_name");
                                String beveragePrice = document.getString("menu_price");
                                String beverageImage = document.getString("menu_image");
                                String beverageDesc = document.getString("menu_detail");
                                String beverageId = document.getString("menu_id");

                                // Add the retrieved data to the ArrayList
                                datalist.add(new AdminMenu(beverageName, beveragePrice, beverageImage, beverageDesc, beverageId)); // Change to the appropriate data class (BeverageData)
                            }

                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle the case where the query was not successful
                        }
                    }
                });

        adapter.setOnItemClickListener(new AdminMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Get the selected menu item
                AdminMenu selectedItem = datalist.get(position);

                // Create an Intent to send the selected menu item's details to the MenuItemDashboardActivity
                Intent intent = new Intent(Admin_manage_beverage.this, Menu_item.class);
                intent.putExtra("menuName", selectedItem.getName());
                intent.putExtra("menuPrice", selectedItem.getPrice());
                intent.putExtra("menuImage", selectedItem.getImage());
                intent.putExtra("menuDetail", selectedItem.getDetail());
                intent.putExtra("menuId", selectedItem.getId());
                startActivity(intent);
            }

            @Override
            public void onEditClick(int position) {
                AdminMenu selectedItem = datalist.get(position);
                Intent intent = new Intent(Admin_manage_beverage.this, Edit_Menu.class);
            }

            @Override
            public void onDeleteClick(int position) {
                // Handle delete button click
                AdminMenu selectedItem = datalist.get(position);
                String menuItemId = selectedItem.getId();

                // Implement the code to delete the beverage item here
                // For example, you can use menuId to delete the item from the Firestore database
                fStore.collection("menu").document(menuItemId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Item deleted successfully
                                datalist.remove(position);
                                adapter.notifyItemRemoved(position);
                                Toast.makeText(getApplicationContext(), "Deleted successfully", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to delete", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });


        /*display user email if user login */
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

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}