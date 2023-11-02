package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Menu_item extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton logout,openDrawer;
    FirebaseAuth auth;
    FirebaseUser user;

    int quantity = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item);

        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.menu);

        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        user = auth.getCurrentUser();

        String searchQuery = getIntent().getStringExtra("search_query");
        /*open menu*/
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerMenu))) {
                    drawerLayout.closeDrawer(findViewById(R.id.drawerMenu));
                } else {
                    drawerLayout.openDrawer(findViewById(R.id.drawerMenu));
                }
            }
        });

        /*display user email if user login */
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        } else {

            TextView text= findViewById(R.id.user_details);
            text.setText(user.getEmail());
            TextView textView = findViewById(R.id.user_email);
            textView.setText(user.getEmail());
        }

        /*logout*/
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });


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
    public void toHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        TextView toHome = findViewById(R.id.home);
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

}
