package com.example.food_ordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class Reload extends AppCompatActivity{
    private TextView cardNumberView;
    private TextView amountView;
    private RadioGroup reloadMethodGroup;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reload);

        cardNumberView = findViewById(R.id.inputCardNumber);
        amountView = findViewById(R.id.inputAmount);
        reloadMethodGroup = findViewById(R.id.reloadMethodGroup);
        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
    }

    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Button button = findViewById(R.id.home);
        startActivity(intent);
    }

    public void reload(View view) {
        String cardNumber = cardNumberView.getText().toString();
        String amountStr = amountView.getText().toString();
        int amount = Integer.parseInt(amountStr);

        int selectedRadioButtonId = reloadMethodGroup.getCheckedRadioButtonId();
        String reloadMethod = "";

        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            reloadMethod = selectedRadioButton.getText().toString();
        }

        int currentTotalAmount = preferences.getInt("totalAmount", 0);
        currentTotalAmount += amount;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("totalAmount", currentTotalAmount);
        editor.apply();

        // Now, you have cardNumber, amount, and reloadMethod
        // You can pass these values to the Wallet activity or display them as needed.

        // You can pass these values to the Wallet activity
        Intent intent = new Intent(this, Wallet.class);
        intent.putExtra("cardNumber", cardNumber);
        intent.putExtra("reloadAmount", amount);
        intent.putExtra("reloadMethod", reloadMethod);
        startActivity(intent);
    }
}
