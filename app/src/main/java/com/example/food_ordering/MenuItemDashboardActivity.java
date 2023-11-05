package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuItemDashboardActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton logout,openDrawer;
    FirebaseAuth auth;
    FirebaseUser user;

    ArrayList<Menu> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item_dashboard);


        // Receive the data passed from the MainActivity
        String menuName = getIntent().getStringExtra("menuName");
        String menuPrice = getIntent().getStringExtra("menuPrice");
        String menuImage = getIntent().getStringExtra("menuImage");
        String menuDetail = getIntent().getStringExtra("menuDetail");

        // Display the data in your UI elements
        TextView nameTextView = findViewById(R.id.menu_name);
        TextView priceTextView = findViewById(R.id.menu_price);
        TextView detailTextView = findViewById(R.id.menu_detail);
        ImageView imageView = findViewById(R.id.menu_image);

        // Set the text of TextViews
        nameTextView.setText(menuName);
        priceTextView.setText(menuPrice);
        detailTextView.setText(menuDetail);

        // Load the image into the ImageView using Picasso or any other image loading library
        Picasso.get().load(menuImage).into(imageView);

    }


    /*menu*/
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

    public void toLoginPage(View view){
        Intent intent = new Intent(this, login.class);
        ImageButton toLoginPage = findViewById(R.id.login);
        startActivity(intent);
    }

    /*Footer*/
    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        ImageButton toHome = findViewById(R.id.home_page);
        startActivity(intent);
    }

    public void toOrder(View view){
        Intent intent = new Intent(this, Order_history_n_upcoming.class);
        ImageButton toOrder = findViewById(R.id.order);
        startActivity(intent);
    }


    public void toAccount(View view){
        Intent intent = new Intent(this, Account_details.class);
        TextView toAccount = findViewById(R.id.accountPage);
        startActivity(intent);
    }

}
