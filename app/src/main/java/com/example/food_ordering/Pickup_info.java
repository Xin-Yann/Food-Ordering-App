package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Pickup_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickup_info);
    }

    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Button button = findViewById(R.id.home);
        startActivity(intent);
    }
}
