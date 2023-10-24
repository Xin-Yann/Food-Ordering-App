package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
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

public class Admin_MainActivity extends AppCompatActivity {
    RecyclerView recyclerView, searchResultsRecyclerView;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    ImageButton logout;
    TextView textView;
    FirebaseUser user;
    ArrayList<User> datalist;
    AdminAdapter adapter;
    SearchView searchView;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);

        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new AdminAdapter(this, datalist);
        recyclerView.setAdapter(adapter);

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

                } else {
                    // Handle the case where the query was not successful
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
        Intent intent = new Intent(this, Admin_MainActivity.class);
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
        Intent intent = new Intent(this, Admin_report.class);
        ImageButton toCartPage = findViewById(R.id.reportPage);
        startActivity(intent);
    }

    public void toAccount(View view){
        Intent intent = new Intent(this, Account_details.class);
        ImageButton toAccount = findViewById(R.id.accountPage);
        startActivity(intent);
    }

}
