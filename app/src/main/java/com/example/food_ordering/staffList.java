package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class staffList extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseUser staff;
    ArrayList<User> datalist;
    FirebaseUser admin;
    AdminAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_list);

        auth = FirebaseAuth.getInstance();
        admin = auth.getCurrentUser();
        user = auth.getCurrentUser();
        staff = auth.getCurrentUser();

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new AdminAdapter(this, datalist);
        recyclerView.setAdapter(adapter);

        // Display staff list
        fStore.collection("staffs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String username = document.getString("name");
                        String staffId = document.getString("id");
                        String staffEmail = document.getString("email");
                        String staffContact = document.getString("contact");
                        String staffPassword = document.getString("password");

                        // Add the retrieved data to the ArrayList
                        datalist.add(new User(username, staffId, staffEmail, staffContact, staffPassword));
                    }

                    adapter.notifyDataSetChanged();

                }
            }
        });
    }

    public void toHomePage(View view){
        Intent intent = new Intent(this, Admin_home.class);
        ImageButton toHomePage = findViewById(R.id.backBtn);
        startActivity(intent);
    }
}
