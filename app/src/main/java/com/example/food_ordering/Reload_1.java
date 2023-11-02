package com.example.food_ordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Reload_1 extends AppCompatActivity{
    private TextView cardNumberView;
    private SharedPreferences preferences;
    private TextView amountView;
    EditText inputDarpaId, inputCardNumber, inputAmount;
    Button reloadBtn;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reload_1);

        db = FirebaseFirestore.getInstance();
        inputDarpaId = findViewById(R.id.inputDarpaId);
        inputCardNumber = findViewById(R.id.inputCardNumber);
        inputAmount = findViewById(R.id.inputAmount);
        reloadBtn = findViewById(R.id.reloadBtn);

        amountView = findViewById(R.id.inputAmount);
        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String InputDarpaId = inputDarpaId.getText().toString();
                String InputCardNumber = inputCardNumber.getText().toString();
                String InputAmount = inputAmount.getText().toString();
                Map<String,Object> wallet = new HashMap<>();
                wallet.put("Id",InputDarpaId);
                wallet.put("Card Number",InputCardNumber);
                wallet.put("Amount",InputAmount);
                String InputAmountStr = inputAmount.getText().toString();

                db.collection("wallet")
                        .add(wallet)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(Reload_1.this,"Successful",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(Reload_1.this,"Failed",Toast.LENGTH_SHORT).show();
                            }
                        });

                if (InputAmount.isEmpty()) {
                    // Handle the case when the input amount is empty (show an error message)
                    Toast.makeText(Reload_1.this, "Amount is required", Toast.LENGTH_SHORT).show();
                } else {
                    // Parse the input amount
                    int inputAmount = Integer.parseInt(InputAmount);

                    // Calculate the updated amount by adding InputAmount to the current amount
                    String currentAmountStr = preferences.getString("walletAmount", "0");
                    int currentAmount = Integer.parseInt(currentAmountStr);
                    int updatedAmount = currentAmount + inputAmount;

                    // Store the updated amount in SharedPreferences
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("walletAmount", String.valueOf(updatedAmount));
                    editor.apply();

                    // Store the updated amount in Firestore (if needed)
                    // ...

                    // Create an intent to pass the updated amount back to Wallet.java
                    Intent resultIntent = new Intent(Reload_1.this, Wallet.class);
                    resultIntent.putExtra("updatedAmount", String.valueOf(updatedAmount));

                    // Set the result code and data
                    setResult(RESULT_OK, resultIntent);

                    // Finish the Reload_1 activity and go back to Wallet.java
                    finish();
                }
            }
        });
    }

    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Button button = findViewById(R.id.home);
        startActivity(intent);
    }




}