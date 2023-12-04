package com.example.food_ordering;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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


import java.io.File;
import java.util.ArrayList;

public class Admin_home extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    ImageButton logout,openDrawer;
    TextView textView;
    FirebaseUser user;
    FirebaseUser staff;
    ArrayList<User> datalist;
    FirebaseUser admin;
    AdminAdapter adapter;
    DrawerLayout drawerLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);

        auth = FirebaseAuth.getInstance();
        admin = auth.getCurrentUser();

        logout = findViewById(R.id.logout);
        textView = findViewById(R.id.admin_details);
        user = auth.getCurrentUser();
        staff = auth.getCurrentUser();

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new AdminAdapter(this, datalist);
        recyclerView.setAdapter(adapter);
        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.menu);

        // Display user list
        fStore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String username = document.getString("name");
                        String userId = document.getString("id");
                        String userEmail = document.getString("email");
                        String userContact = document.getString("contact");
                        String userPassword = document.getString("password");

                        // Add the retrieved data to the ArrayList
                        datalist.add(new User(username, userId, userEmail, userContact, userPassword));
                    }

                    adapter.notifyDataSetChanged();

                }
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
            TextView textView = findViewById(R.id.user_email);
            textView.setText(user.getEmail());

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
        Intent intent = new Intent(this, Admin_Order_history_n_upcoming.class);
        ImageButton toOrder = findViewById(R.id.order);
        startActivity(intent);
    }

    public void toReportPage(View view){
        Intent intent = new Intent(this, Report.class);
        ImageButton toReportPage = findViewById(R.id.reportPage);
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

    public void toStaffList(View view){
        Intent intent = new Intent(this, staffList.class);
        Button toStaffList = findViewById(R.id.viewStaffBtn);
        startActivity(intent);
    }

}
