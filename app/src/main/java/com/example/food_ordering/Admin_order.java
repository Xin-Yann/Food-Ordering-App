package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Admin_order extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_order);

    }
    public void toHome(View view){
        Intent intent = new Intent(this, Admin_home.class);
        ImageButton toHome = findViewById(R.id.home_page);
        startActivity(intent);
    }

    public void toAddMenu(View view){
        Intent intent = new Intent(this, Add_Menu.class);
        ImageButton toAddMenu = findViewById(R.id.add_menu);
        startActivity(intent);
    }

    public void toOrder(View view){
        Intent intent = new Intent(this, Admin_Order_history_n_upcoming.class);
        ImageButton toOrder = findViewById(R.id.order);
        startActivity(intent);
    }

    public void toReportPage(View view){
        Intent intent = new Intent(this, Admin_menu_list.class);
        ImageButton toCartPage = findViewById(R.id.reportPage);
        startActivity(intent);
    }

    public void toAccount(View view){
        Intent intent = new Intent(this, Account_details.class);
        TextView toAccount = findViewById(R.id.accountPage);
        startActivity(intent);
    }

}
