package com.example.food_ordering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class login extends AppCompatActivity {
    Button Loginbtn;
    EditText userEmail, userPass, userId;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.user_email);
        userPass = findViewById(R.id.user_pass);
        Loginbtn = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);


        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, id;
                email = String.valueOf(userEmail.getText());
                password = String.valueOf(userPass.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isAdmin = email.endsWith("@admin.com");
                String hashedPassword = hashPassword(password);

                mAuth.signInWithEmailAndPassword(email, hashedPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    if (isAdmin) {
                                        // Admin login successful, redirect to admin home page
                                        Intent adminIntent = new Intent(getApplicationContext(), Admin_home.class);
                                        startActivity(adminIntent);
                                        finish();
                                    } else {
                                        // user & staff login successful, redirect to home page
                                        Intent userIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(userIntent);
                                        finish();
                                    }
                                } else {
                                    // login fail
                                    Toast.makeText(login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }

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
        });

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, Register.class);
                startActivity(intent);
            }
        });

    }
}
