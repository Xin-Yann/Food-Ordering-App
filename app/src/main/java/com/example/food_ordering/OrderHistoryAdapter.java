package com.example.food_ordering;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
    private List<OrderHistory> orderHistoryList;

    public OrderHistoryAdapter(List<OrderHistory> orderHistoryList) {
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
        OrderHistory orderHistory = orderHistoryList.get(position);

        // Populate UI elements with data from OrderHistory
        holder.foodDetails.setText(orderHistory.getFoodQuantity() + "x " + orderHistory.getFoodDetailsText());
        holder.price.setText(String.format("RM%.2f", orderHistory.getPriceValue()));
        holder.orderNumber.setText(orderHistory.getOrderNumberText());
        holder.pickupTime.setText(orderHistory.getPickupTimeText());
        holder.orderStatus.setText(orderHistory.getOrderStatusText());
        holder.paymentMethod.setText(orderHistory.getPaymentMethodText());
        holder.totalamount.setText(orderHistory.getTotalAmountText());
        holder.pickupedOn.setText(orderHistory.getPickupedOnText());
        holder.remarks.setText(orderHistory.getRemarkText());

        // Load foodImgUrl using Picasso or your preferred image loading library
        Picasso.get().load(orderHistory.getFoodImgUrl()).into(holder.foodImg);
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImg;
        TextView foodDetails, price, orderNumber, pickupTime, orderStatus, paymentMethod, pickupedOn, totalamount, remarks;

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
            totalamount = itemView.findViewById(R.id.totalPrice);
            pickupedOn = itemView.findViewById(R.id.pickupedOn);
            remarks = itemView.findViewById(R.id.remarks);
        }
    }
}
