package com.example.food_ordering;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Admin_OrderHistoryFragment extends Fragment {

    private FirebaseFirestore db;
    private Query orderQuery;
    private List<AdminOrderHistory> orderItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_order_history, container, false);

        // Initialize your data list
        orderItemList = new ArrayList<>();

        // Initialize the RecyclerView and set the adapter
        RecyclerView recyclerView = view.findViewById(R.id.AdminOrderHistoryRecyclerView);
        AdminOrderHistoryAdapter orderItemAdapter = new AdminOrderHistoryAdapter(orderItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(orderItemAdapter);

        db = FirebaseFirestore.getInstance();

        // Update the query to filter by order_status
        orderQuery = db.collection("orders")
                .whereEqualTo("order_status", "Pickup Completed");

        orderQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                orderItemList.clear(); // Clear the previous data
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get data for the current order
                    String foodImgUrl = document.getString("food_img");
                    String foodDetailsText = document.getString("food_name");
                    int foodQuantity = document.getLong("food_quantity") != null ? document.getLong("food_quantity").intValue() : 0;
                    double priceValue = document.getDouble("food_price") != null ? document.getDouble("food_price") : 0.0;
                    String orderNumberText = document.getString("order_number");
                    String pickupTimeText = document.getString("pickup_time");
                    String orderStatusText = document.getString("order_status");
                    String paymentMethodText = document.getString("payment_method");
                    String pickupedOnText = document.getString("pickupedOn");
                    String email = document.getString("email");

                    // Create an AdminOrderHistory object and add it to the list
                    orderItemList.add(new AdminOrderHistory(foodImgUrl, foodDetailsText, foodQuantity, priceValue, orderNumberText, pickupTimeText, orderStatusText, paymentMethodText, pickupedOnText, email));
                }
                // Notify the adapter that data has changed
                orderItemAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
