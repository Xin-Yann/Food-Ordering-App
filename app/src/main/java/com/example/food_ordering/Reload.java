package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;


public class Reload extends AppCompatActivity{
    private TextView cardNumberView;
    private TextView amountView;
    private RadioGroup reloadMethodGroup;
    private RadioButton creditDebitRadioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reload);

        reloadMethodGroup = findViewById(R.id.reloadMethodGroup);
        creditDebitRadioButton = findViewById(R.id.radioButton1);
    }

    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Button button = findViewById(R.id.home);
        startActivity(intent);
    }

    public void next(View view) {
        // Get the selected radio button ID
        int selectedId = reloadMethodGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radioButton1) {
            // Credit/Debit Card radio button is selected, proceed to Reload_1 activity
            Intent intent = new Intent(this, Reload_1.class);
            startActivity(intent);
        } else if (selectedId == R.id.radioButton2) {
            // Cash radio button is selected, navigate to the Cash Instructions activity
            Intent intent = new Intent(this, Cash_Instruction.class);
            startActivity(intent);
        } else {
            // The user did not select a method, handle it as needed
            Toast.makeText(this, "Please select a reload method to continue.", Toast.LENGTH_SHORT).show();
        }
    }


}
