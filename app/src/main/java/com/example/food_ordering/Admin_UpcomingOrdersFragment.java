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
import java.util.Map;

public class Admin_UpcomingOrdersFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private List<AdminUpcomingOrder> orderItemList;
    private AdminUpcomingOrdersAdapter orderItemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_order_upcoming, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        orderItemList = new ArrayList<>();
        orderItemAdapter = new AdminUpcomingOrdersAdapter(requireContext(), orderItemList);

        recyclerView.setAdapter(orderItemAdapter);

        db = FirebaseFirestore.getInstance();

        Query orderQuery = db.collection("orders");

        // Add a real-time listener for order changes
        orderQuery.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                return;
            }

            orderItemList.clear();

            for (QueryDocumentSnapshot document : querySnapshot) {

                String documentId = document.getId();

                // Fetch and populate the "order_items" map
                Map<String, Object> orderItemsMap = (Map<String, Object>) document.get("order_items");

                if (orderItemsMap != null) {
                    String foodImgUrl = getStringFromMap(orderItemsMap, "cart_image");
                    String foodDetailsText = getStringFromMap(orderItemsMap, "cart_name");
                    int foodQuantity = getIntegerFromMap(orderItemsMap, "cart_quantity");
                    double priceValue = getDoubleFromMap(orderItemsMap, "cart_price");
                    String remarkText = getStringFromMap(orderItemsMap,"cart_remark");
                    Object orderNumberObject = document.get("order_number");
                    String orderNumberText = (orderNumberObject != null) ? orderNumberObject.toString() : "";
                    String pickupTimeText = document.getString("pickup_time");
                    String orderStatusText = document.getString("order_status");
                    String paymentMethodText = document.getString("payment_method");
                    String totalPrice = document.getString("total_amount");
                    String email = document.getString("user_email");

                    AdminUpcomingOrder orderItem = new AdminUpcomingOrder(
                            documentId, foodImgUrl, foodDetailsText, foodQuantity,
                            priceValue, orderNumberText, pickupTimeText, orderStatusText,
                            paymentMethodText, email, totalPrice, remarkText
                    );

                    // Only add orders with status other than "Pickup Completed"
                    if (!"Pickup Completed".equals(orderStatusText)) {
                        orderItemList.add(orderItem);
                    }
                }
            }

            orderItemAdapter.notifyDataSetChanged();
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
}
