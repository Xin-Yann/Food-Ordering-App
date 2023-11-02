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

    private TextView displayAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);

        // Initialize SharedPreferences
        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        // Initialize the displayAmount TextView
        displayAmount = findViewById(R.id.displayAmount);

        // Retrieve and display the wallet amount from SharedPreferences
        String walletAmount = preferences.getString("walletAmount", "0");
        displayAmount.setText("RM " + walletAmount);

        // Replace "userId" with the actual user's identifier
        String userId = "user123"; // Replace with the actual user's identifier

        // Set the wallet balance for the current user
        preferences = getSharedPreferences("Wallet_" + userId, MODE_PRIVATE);
    }

    public void toReload(View view){
        Intent intent = new Intent(this, Reload.class);
        Button toReload = findViewById(R.id.reload_btn);
        startActivity(intent);
    }

    // Handle the result from Reload_1 activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Get the updated amount from Reload_1
                String updatedAmount = data.getStringExtra("updatedAmount");

                // Update the displayed amount in the Wallet activity
                displayAmount.setText(updatedAmount);
            }
        }
    }

    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Button button = findViewById(R.id.home);
        startActivity(intent);
    }

    public void toMainDish(View view){
        Intent intent = new Intent(this, Main_dish.class);
        ImageButton toMainPage = findViewById(R.id.main_dish_btn);
        startActivity(intent);
    }

    public void toBeverage(View view){
        Intent intent = new Intent(this, Beverage.class);
        ImageButton toBeverage = findViewById(R.id.beverage_btn);
        startActivity(intent);
    }

    public void toDessert(View view){
        Intent intent = new Intent(this, Dessert.class);
        ImageButton toDessert = findViewById(R.id.dessert_btn);
        startActivity(intent);
    }
    public void toPrivacy(View view){
        Intent intent = new Intent(this, Privacy_policy.class);
        TextView toPrivacy= findViewById(R.id.privacy);
        startActivity(intent);
    }

    public void toPickup(View view){
        Intent intent = new Intent(this, Pickup_info.class);
        TextView toPickup= findViewById(R.id.pickup);
        startActivity(intent);
    }

    public void toContact(View view){
        Intent intent = new Intent(this, Contact_us.class);
        TextView toContact= findViewById(R.id.contact);
        startActivity(intent);
    }

    public void toAccount(View view){
        Intent intent = new Intent(this, Account_details.class);
        TextView toAccount = findViewById(R.id.accountPage);
        startActivity(intent);
    }

    public void toCart(View view){
        Intent intent = new Intent(this, Cart.class);
        TextView toCart = findViewById(R.id.cart);
        startActivity(intent);
    }
}