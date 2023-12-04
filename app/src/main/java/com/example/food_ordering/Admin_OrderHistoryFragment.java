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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.Map;

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

                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    try {
                        // Fetch and populate the "order_items" map
                        Map<String, Object> orderItemsMap = (Map<String, Object>) document.get("order_items");

                        // Your existing code to fetch other fields
                        String foodImgUrl = getStringFromMap(orderItemsMap, "cart_image");
                        String foodDetailsText = getStringFromMap(orderItemsMap, "cart_name");
                        int foodQuantity = getIntegerFromMap(orderItemsMap, "cart_quantity");
                        double priceValue = getDoubleFromMap(orderItemsMap, "cart_price");
                        String remarkText = getStringFromMap(orderItemsMap, "cart_remark");
                        String orderNumberText = getOrderNumberFromDocument(document);
                        String pickupTimeText = document.getString("pickup_time");
                        String orderStatusText = document.getString("order_status");
                        String paymentMethodText = document.getString("payment_method");
                        String totalamountText = document.getString("total_amount");
                        String pickupedOnText = document.getString("pickupedOn");
                        String email = document.getString( "user_email");

                        // Create an AdminOrderHistory object and add it to the list
                        orderItemList.add(new AdminOrderHistory(foodImgUrl, foodDetailsText, foodQuantity, priceValue, orderNumberText, pickupTimeText, orderStatusText, paymentMethodText, pickupedOnText, email, totalamountText, remarkText));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Notify the adapter that data has changed
                orderItemAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    // Helper methods to handle null values
    private String getStringFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return (value != null) ? value.toString() : "";
    }

    private int getIntegerFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return (value instanceof Number) ? ((Number) value).intValue() : 0;
    }

    private double getDoubleFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return (value instanceof Number) ? ((Number) value).doubleValue() : 0.0;
    }

    private String getOrderNumberFromDocument(DocumentSnapshot document) {
        Object orderNumberField = document.get("order_number");
        return (orderNumberField != null) ? orderNumberField.toString() : "";
    }
}

