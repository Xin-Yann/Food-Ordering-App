package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Button Registerbtn;
    EditText userEmail, userPass, userId, userContact, userName;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();
        userId = findViewById(R.id.user_id);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPass = findViewById(R.id.user_pass);
        userContact = findViewById(R.id.user_contact);
        Registerbtn = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        fStore = FirebaseFirestore.getInstance();


        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, id, name, contact;
                email = String.valueOf(userEmail.getText());
                password = String.valueOf(userPass.getText());
                id = String.valueOf(userId.getText());
                name = String.valueOf(userName.getText());
                contact = String.valueOf(userContact.getText());

                String hashedPassword = hashPassword(password);

                if (TextUtils.isEmpty(email)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(id)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter your id", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(contact)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter contact no", Toast.LENGTH_SHORT).show();
                    return;
                }

                //set id format
                if (email.endsWith("@student.com")) {
                    if (!id.startsWith("P")) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Register.this, "Student ID must start with 'P'", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (email.endsWith("@staff.com")) {
                    if (!id.startsWith("S")) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Register.this, "Staff ID must start with 'S'", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (email.endsWith("@admin.com")) {
                    if (!id.startsWith("A")) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Register.this, "Admin ID must start with 'A'", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                checkIfIdExists(id, new OnIdCheckListener() {
                    @Override
                    public void onIdCheckComplete(boolean isRegistered) {
                        if (isRegistered) {
                            Toast.makeText(Register.this, "ID is already registered", Toast.LENGTH_SHORT).show();
                        } else {

                        boolean isAdmin = email.endsWith("@admin.com");
                        boolean isUser = email.endsWith("@student.com");
                        boolean isStaff = email.endsWith("@staff.com");

                        mAuth.createUserWithEmailAndPassword(email, hashedPassword)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            // User registration successful
                                            Toast.makeText(Register.this, "Account Created.", Toast.LENGTH_SHORT).show();

                                            // Store user data in Firestore
                                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                            if (firebaseUser != null) {

                                                // Create a map to store user data
                                                Map<String, Object> userData = new HashMap<>();
                                                userData.put("id", id);
                                                userData.put("name", name);
                                                userData.put("email", email);
                                                userData.put("password", hashedPassword); // Store the hashed password
                                                userData.put("contact", contact);

                                                // Get a reference to Firestore
                                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                                if (isAdmin) {

                                                    // Save admin data in the "admins" collection
                                                    db.collection("admins")
                                                            .document(firebaseUser.getUid())
                                                            .set(userData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(Register.this, "Admin data saved.", Toast.LENGTH_SHORT).show();

                                                                    Intent intent = new Intent(getApplicationContext(), Admin_home.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(Register.this, "Failed to save admin data.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }

                                                if (isUser) {
                                                    // Create a new document in the "users" collection with the user's UID as the document ID
                                                    db.collection("users")
                                                            .document(firebaseUser.getUid())
                                                            .set(userData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    // User data successfully saved in Firestore
                                                                    Toast.makeText(Register.this, "User data saved.", Toast.LENGTH_SHORT).show();

                                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }

                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // Failed to save user data in Firestore
                                                                    Toast.makeText(Register.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }

                                                if (isStaff) {
                                                    // Create a new document in the "users" collection with the user's UID as the document ID
                                                    db.collection("staffs")
                                                            .document(firebaseUser.getUid())
                                                            .set(userData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // User data successfully saved in Firestore
                                                                    Toast.makeText(Register.this, "Staff data saved.", Toast.LENGTH_SHORT).show();

                                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // Failed to save user data in Firestore
                                                                    Toast.makeText(Register.this, "Failed to save staff data.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }

                                        } else {
                                            // Registration failed
                                            Toast.makeText(Register.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        }
                    }
                });
            }
        });
    }
    public interface OnIdCheckListener {
        void onIdCheckComplete(boolean isRegistered);
    }

    private void checkIfIdExists(String id, OnIdCheckListener listener) {
        fStore.collection("users")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // ID already exists in the database
                                Log.d("Register", "ID already exists: " + id);
                                listener.onIdCheckComplete(true);
                                return; // Exit the loop as we've found a matching ID
                            }
                            // ID is not registered
                            Log.d("Register", "ID is not registered: " + id);
                            listener.onIdCheckComplete(false);
                        } else {
                            // Handle the error
                            Log.e("Register", "Error checking ID: " + task.getException());
                            listener.onIdCheckComplete(false); // Assume ID is not registered if an error occurs
                        }
                    }
                });
    }



    // Hashing function (You can use a proper password hashing library)
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(hashedBytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            Log.e("Register", "Failed to hash password: " + e.getMessage());
            return null;
        }
    }
}

