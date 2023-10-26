package com.example.food_ordering;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

public class OrderHistoryFragment extends Fragment {
    private FirebaseFirestore db;
    private Query orderQuery;
    private String currentUserEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        db = FirebaseFirestore.getInstance();
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        LinearLayout orderHistoryLayout = view.findViewById(R.id.orderHistoryLayout);

        orderQuery = db.collection("orders").whereEqualTo("email", currentUserEmail);

        orderQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isFirstOrder = true;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (isFirstOrder) {
                        // For the first order, populate the existing views in fragment_order_history.xml
                        isFirstOrder = false;
                        ImageView foodImg = view.findViewById(R.id.foodimg);
                        TextView foodDetails = view.findViewById(R.id.foodDetails1);
                        TextView foodQuantityTextView = view.findViewById(R.id.foodQuantity);
                        TextView price = view.findViewById(R.id.price1);
                        TextView orderNumber = view.findViewById(R.id.orderNumber);
                        TextView pickupTime = view.findViewById(R.id.pickupTime);
                        TextView orderStatus = view.findViewById(R.id.orderStatus);
                        TextView paymentMethod = view.findViewById(R.id.paymentMethod);

                        // Get data for the first order
                        String foodImgUrl = document.getString("food_img");
                        String foodDetailsText = document.getString("food_name");
                        int foodQuantity = document.getLong("food_quantity").intValue();
                        double priceValue = document.getDouble("food_price");
                        String orderNumberText = document.getString("order_number");
                        String pickupTimeText = document.getString("pickup_time");
                        String orderStatusText = document.getString("order_status");
                        String paymentMethodText = document.getString("payment_method");

                        // Populate the views with data for the first order
                        Picasso.get().load(foodImgUrl).into(foodImg);
                        foodDetails.setText(foodQuantity + "x " + foodDetailsText);
                        price.setText(String.format("RM"+"%.2f", priceValue));
                        orderNumber.setText(orderNumberText);
                        pickupTime.setText(pickupTimeText);
                        orderStatus.setText(orderStatusText);
                        paymentMethod.setText(paymentMethodText);
                    } else {
                        // For subsequent orders, create a new order history item and add it to the layout
                        View orderItemView = inflater.inflate(R.layout.fragment_order_history, null);
                        ImageView foodImg = orderItemView.findViewById(R.id.foodimg);
                        Picasso.get().load(document.getString("food_img")).into(foodImg);
                        TextView foodDetails = orderItemView.findViewById(R.id.foodDetails1);
                        TextView foodQuantityTextView = orderItemView.findViewById(R.id.foodQuantity);
                        TextView price = orderItemView.findViewById(R.id.price1);
                        TextView orderNumber = orderItemView.findViewById(R.id.orderNumber);
                        TextView pickupTime = orderItemView.findViewById(R.id.pickupTime);
                        TextView orderStatus = orderItemView.findViewById(R.id.orderStatus);
                        TextView paymentMethod = orderItemView.findViewById(R.id.paymentMethod);

                        // Get data for subsequent orders
                        String foodImgUrl = document.getString("food_img");
                        String foodDetailsText = document.getString("food_name");
                        int foodQuantity = document.getLong("food_quantity").intValue();
                        double priceValue = document.getDouble("food_price");
                        String orderNumberText = document.getString("order_number");
                        String pickupTimeText = document.getString("pickup_time");
                        String orderStatusText = document.getString("order_status");
                        String paymentMethodText = document.getString("payment_method");

                        // Populate the views in the new order history item
                        Picasso.get().load(foodImgUrl).into(foodImg);
                        foodDetails.setText(foodQuantity + "x " + foodDetailsText);
                        price.setText(String.format("RM"+"%.2f", priceValue));
                        orderNumber.setText(orderNumberText);
                        pickupTime.setText(pickupTimeText);
                        orderStatus.setText(orderStatusText);
                        paymentMethod.setText(paymentMethodText);

                        orderHistoryLayout.addView(orderItemView);
                    }
                }
            }
        });

        return view;
    }
}
