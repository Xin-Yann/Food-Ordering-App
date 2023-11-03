package com.example.food_ordering;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.List;

import java.util.List;

public class AdminOrderHistoryAdapter extends RecyclerView.Adapter<AdminOrderHistoryAdapter.OrderHistoryViewHolder> {
    private List<AdminOrderHistory> orderHistoryList;

    public AdminOrderHistoryAdapter(List<AdminOrderHistory> orderHistoryList) {
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_history_item, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        AdminOrderHistory orderHistory = orderHistoryList.get(position);

        // Populate UI elements with data from AdminOrderHistory
        String foodImgUrl = orderHistory.getFoodImgUrl();
        String foodDetailsText = orderHistory.getFoodDetailsText();
        int foodQuantity = orderHistory.getFoodQuantity();
        double priceValue = orderHistory.getPriceValue();
        String orderNumberText = orderHistory.getOrderNumber();
        String pickupTimeText = orderHistory.getPickupTime();
        String orderStatusText = orderHistory.getOrderStatus();
        String paymentMethodText = orderHistory.getPaymentMethod();
        String pickupedOnText = orderHistory.getPickupedOn();

        // Set the data to your UI elements in the OrderHistoryViewHolder
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
        ImageView foodImg;
        TextView foodDetails, price, orderNumber, pickupTime, orderStatus, paymentMethod, pickupedOn;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
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
