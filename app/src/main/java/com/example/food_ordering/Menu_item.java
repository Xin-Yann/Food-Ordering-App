package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Menu_item extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton logout,openDrawer;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView quantityTextView;
    EditText remarks;
    Button cart;
    FirebaseFirestore db;

    int quantity = 1;
    String menuName; // Add variable to hold menu name
    String menuPrice; // Add variable to hold menu price
    String menuImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item);
        db = FirebaseFirestore.getInstance();
        remarks = findViewById(R.id.remarks);
        cart = findViewById(R.id.cart);

        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.menu);

        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        user = auth.getCurrentUser();

        quantityTextView = findViewById(R.id.quantity_text_view);

        Button incrementButton = findViewById(R.id.increment);
        Button decrementButton = findViewById(R.id.decrement);

        menuName = getIntent().getStringExtra("menuName");
        menuPrice = getIntent().getStringExtra("menuPrice");
        menuImage = getIntent().getStringExtra("menuImage");

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Remarks = remarks.getText().toString();
                Map<String,Object> cart = new HashMap<>();
                cart.put("cart_remarks", Remarks);
                cart.put("cart_name", menuName);
                cart.put("cart_price", menuPrice);
                cart.put("cart_quantity", quantity);
                cart.put("cart_image", menuImage);
                cart.put("user_email", user.getEmail());

                db.collection("cart")
                        .add(cart)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(Menu_item.this,"Successful",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Menu_item.this,"Failed",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuantity();
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity();
            }
        });

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

    // Function to increment the quantity
    private void incrementQuantity() {
        quantity++;
        updateQuantityTextView();
    }

    // Function to decrement the quantity
    private void decrementQuantity() {
        if (quantity > 1) {
            quantity--;
            updateQuantityTextView();
        }
    }

    // Function to update the quantity_text_view
    private void updateQuantityTextView() {
        quantityTextView.setText(String.valueOf(quantity));
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

    public void toCart(View view){
        Intent intent = new Intent(this, Cart.class);
        TextView toCart = findViewById(R.id.cart);
        startActivity(intent);
    }

}
