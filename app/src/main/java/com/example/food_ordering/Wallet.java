package com.example.food_ordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Wallet extends AppCompatActivity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);

        TextView displayAmount = findViewById(R.id.displayAmount);
        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        // Retrieve the total amount from shared preferences
        int totalAmount = preferences.getInt("totalAmount", 0);

        // Display the total amount in the TextView
        displayAmount.setText("Balance: RM" + totalAmount);


    }

    public void toReload(View view){
        Intent intent = new Intent(this, Reload.class);
        Button toReload = findViewById(R.id.reload_btn);
        startActivity(intent);
    }

    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Button button = findViewById(R.id.home);
        startActivity(intent);
    }
}
