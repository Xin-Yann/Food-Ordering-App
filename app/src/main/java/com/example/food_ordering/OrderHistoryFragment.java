package com.example.food_ordering;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderHistoryFragment extends Fragment {
    private FirebaseFirestore db;
    private Query orderQuery;
    private String currentUserEmail;
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<OrderHistory> orderHistoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        db = FirebaseFirestore.getInstance();
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        recyclerView = view.findViewById(R.id.orderHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderHistoryList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(orderHistoryList);
        recyclerView.setAdapter(adapter);

        // Set up your Firestore query to filter by email and order status
        orderQuery = db.collection("orders")
                .whereEqualTo("user_email", currentUserEmail)
                .whereEqualTo("order_status", "Pickup Completed");
        orderQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                orderHistoryList.clear();

                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    try {
                        // Fetch and populate the "order_items" map
                        Map<String, Object> orderItemsMap = (Map<String, Object>) document.get("order_items");

                        // Your existing code to fetch other fields
                        String foodImgUrl = getStringFromMap(orderItemsMap, "cart_image");
                        String foodDetailsText = getStringFromMap(orderItemsMap, "cart_name");
                        int foodQuantity = getIntegerFromMap(orderItemsMap, "cart_quantity");
                        double priceValue = getDoubleFromMap(orderItemsMap, "cart_price");
                        String remarkText = (String) orderItemsMap.get("cart_remark");
                        String orderNumberText = getOrderNumberFromDocument(document);
                        String pickupTimeText = document.getString("pickup_time");
                        String orderStatusText = document.getString("order_status");
                        String paymentMethodText = document.getString("payment_method");
                        String totalamountText = document.getString("total_amount");
                        String pickupedOnText = document.getString("pickupedOn");

                        // Use the helper method to handle null values for remarks
                        String remarksText = getRemarksFromMap(orderItemsMap, "cart_remark");

                        // Create an OrderHistory object and add it to the list
                        OrderHistory orderHistory = new OrderHistory(
                                document.getId(),
                                foodImgUrl,
                                foodDetailsText,
                                foodQuantity,
                                priceValue,
                                orderNumberText,
                                pickupTimeText,
                                orderStatusText,
                                paymentMethodText,
                                pickupedOnText,
                                totalamountText,
                                remarksText
                        );
                        orderHistoryList.add(orderHistory);
                    } catch (Exception e) {
                        // Log the exception and continue to the next iteration
                        Log.e("OrderHistoryFragment", "Exception while processing order document", e);
                    }
                }

                adapter.notifyDataSetChanged();
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

    private String getRemarksFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return (value != null) ? value.toString() : null;
    }
    private String getOrderNumberFromDocument(DocumentSnapshot document) {
        Object orderNumberField = document.get("order_number");
        return (orderNumberField != null) ? orderNumberField.toString() : "";
    }
}
