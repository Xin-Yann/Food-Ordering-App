package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Account_details extends AppCompatActivity {
    FirebaseAuth auth;
    TextView textView;
    FirebaseUser user , staffs;
    FirebaseFirestore fStore;

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_details);

        // Initialize FirebaseAuth and get the current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        staffs= auth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        if (user == null) {
            // If the user is not logged in, redirect them to the login screen
            Intent intent = new Intent(Account_details.this, login.class);
            startActivity(intent);
            finish();
        } else {
            // Query Firestore to retrieve additional user data
            fStore.collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String userName = document.getString("name");
                                    String userId = document.getString("id");
                                    String userPass = document.getString("password");
                                    String userContact = document.getString("contact");
                                    String userEmail = document.getString("email");
                                    // You can retrieve other user data here
                                    textView = findViewById(R.id.inputUsername);
                                    textView.setText(userName);
                                    textView = findViewById(R.id.inputDarpaId);
                                    textView.setText(userId);
                                    textView = findViewById(R.id.inputPassword);
                                    textView.setText(userPass);
                                    textView = findViewById(R.id.inputContactNo);
                                    textView.setText(userContact);
                                    textView = findViewById(R.id.inputEmail);
                                    textView.setText(userEmail);

                                } else if (staffs == null) {
                                    // If the user is not logged in, redirect them to the login screen
                                    Intent intent = new Intent(Account_details.this, login.class);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    // Query Firestore to retrieve additional user data
                                    fStore.collection("staffs")
                                            .document(staffs.getUid())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            String staffName = document.getString("name");
                                                            String staffId = document.getString("id");
                                                            String staffPass = document.getString("password");
                                                            String staffContact = document.getString("contact");
                                                            String staffEmail = document.getString("email");
                                                            // You can retrieve other user data here
                                                            textView = findViewById(R.id.inputUsername);
                                                            textView.setText(staffName);
                                                            textView = findViewById(R.id.inputDarpaId);
                                                            textView.setText(staffId);
                                                            textView = findViewById(R.id.inputPassword);
                                                            textView.setText(staffPass);
                                                            textView = findViewById(R.id.inputContactNo);
                                                            textView.setText(staffContact);
                                                            textView = findViewById(R.id.inputEmail);
                                                            textView.setText(staffEmail);


                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
        }

        saveButton = findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newContact = ((TextInputEditText) findViewById(R.id.inputContactNo)).getText().toString();
                String newEmail = ((TextInputEditText) findViewById(R.id.inputEmail)).getText().toString();
                String newPass = ((TextInputEditText) findViewById(R.id.inputPassword)).getText().toString();

                updateProfile(newContact, newEmail, newPass);
            }
        });
    }

    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        ImageButton toHome = findViewById(R.id.home_page);
        startActivity(intent);
    }

    public void updateProfile(String newContact, String newEmail, String newPass) {
        if(user!=null) {
            // Get the document reference for the user's data
            DocumentReference userDocRef = fStore.collection("users").document(user.getUid());

            // Create a map with the updated data
            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("contact", newContact);
            updatedData.put("email", newEmail);
            updatedData.put("pass", newPass);

            // Update the data in Firestore
            userDocRef.update(updatedData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Account_details.this, "Data updated successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Account_details.this, "Failed to update data", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        if(staffs != null){
            // Get the document reference for the user's data
            DocumentReference userDocRef = fStore.collection("staffs").document(user.getUid());

            // Create a map with the updated data
            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("contact", newContact);
            updatedData.put("email", newEmail);
            updatedData.put("pass", newPass);

            // Update the data in Firestore
            userDocRef.update(updatedData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Account_details.this, "Data updated successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Account_details.this, "Failed to update data", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }

}






