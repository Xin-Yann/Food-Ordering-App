package com.example.food_ordering;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.google.firebase.firestore.DocumentSnapshot; // Use DocumentSnapshot
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
    private List<DocumentSnapshot> orderHistoryList; // Change to DocumentSnapshot

    public OrderHistoryAdapter(List<DocumentSnapshot> orderHistoryList) {
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        DocumentSnapshot document = orderHistoryList.get(position); // Use DocumentSnapshot

        // Populate UI elements with data from DocumentSnapshot
        String foodImgUrl = document.getString("food_img");
        String foodDetailsText = document.getString("food_name");
        int foodQuantity = document.getLong("food_quantity").intValue();
        double priceValue = document.getDouble("food_price");
        String orderNumberText = document.getString("order_number");
        String pickupTimeText = document.getString("pickup_time");
        String orderStatusText = document.getString("order_status");
        String paymentMethodText = document.getString("payment_method");
        String pickupedOnText = document.getString("pickupedOn");


        // Set the data to your UI elements in the OrderHistoryViewHolder
        // For example:
        holder.foodDetails.setText(foodQuantity + "x " + foodDetailsText);
        holder.price.setText(String.format("RM%.2f", priceValue));
        holder.orderNumber.setText(orderNumberText);
        holder.pickupTime.setText(pickupTimeText);
        holder.orderStatus.setText(orderStatusText);
        holder.paymentMethod.setText(paymentMethodText);
        holder.pickupedOn.setText(pickupedOnText);

        // Load foodImgUrl using Picasso or your preferred image loading library
        Picasso.get().load(foodImgUrl).into(holder.foodImg);
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        // Define your UI elements here, e.g., foodImg, foodDetails, price, etc.
        ImageView foodImg;
        TextView foodDetails, price, orderNumber, pickupTime, orderStatus, paymentMethod, pickupedOn;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your UI elements here
            foodImg = itemView.findViewById(R.id.foodimg);
            foodDetails = itemView.findViewById(R.id.foodDetails1);
            price = itemView.findViewById(R.id.price1);
            orderNumber = itemView.findViewById(R.id.orderNumber);
            pickupTime = itemView.findViewById(R.id.pickupTime);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            pickupedOn = itemView.findViewById(R.id.pickupedOn);
        }
    }
}
