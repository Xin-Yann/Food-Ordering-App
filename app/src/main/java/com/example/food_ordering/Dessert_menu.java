package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Dessert_menu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dessert_menu);
    }

    public void toDashboard(View view) {
        Intent intent = new Intent(this, MenuItemDashboardActivity.class);
        startActivity(intent);

    }

}
