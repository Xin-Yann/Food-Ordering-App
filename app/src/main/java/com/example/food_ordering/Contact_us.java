package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Contact_us extends AppCompatActivity {

    EditText inputUsername, inputSubject, inputMessage;
    Button submitBtn;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        db = FirebaseFirestore.getInstance();
        inputUsername = findViewById(R.id.inputUsername);
        inputSubject = findViewById(R.id.inputSubject);
        inputMessage = findViewById(R.id.inputMessage);
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = inputUsername.getText().toString();
                String Subject = inputSubject.getText().toString();
                String Message = inputMessage.getText().toString();
                Map<String,Object> contact = new HashMap<>();
                contact.put("Username", Username);
                contact.put("Subject", Subject);
                contact.put("Message", Message);

                db.collection("contact")
                        .add(contact)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(Contact_us.this,"Successful",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Contact_us.this,"Failed",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Button button = findViewById(R.id.home);
        startActivity(intent);
    }
}
