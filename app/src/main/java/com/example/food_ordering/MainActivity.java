package com.example.food_ordering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    Button Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void toTestingPage(View view){
        Intent intent = new Intent(this, Testing.class);
        Button toAccountPage = findViewById(R.id.test);
        startActivity(intent);
    }
}