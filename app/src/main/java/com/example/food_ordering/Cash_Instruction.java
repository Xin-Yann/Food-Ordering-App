package com.example.food_ordering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cash_Instruction extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_instruction);
    }

    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Button button = findViewById(R.id.home);
        startActivity(intent);
    }
}
