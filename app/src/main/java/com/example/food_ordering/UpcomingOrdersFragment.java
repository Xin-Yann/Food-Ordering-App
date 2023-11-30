package com.example.food_ordering;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpcomingOrdersFragment extends Fragment {

    private FirebaseFirestore db;
    private Query orderQuery;
    private String currentUserEmail;

    private RecyclerView recyclerView;
    private UpcomingOrdersAdapter adapter;
    private List<UpcomingOrder> upcomingOrderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_upcoming, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingOrderList = new ArrayList<>();
        adapter = new UpcomingOrdersAdapter(upcomingOrderList, currentUserEmail);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Fetch and populate the "Upcoming Orders" layout
        orderQuery = db.collection("orders").whereEqualTo("user_email", currentUserEmail);

        // Add a real-time listener for order changes
        orderQuery.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                // Handle the error
                return;
            }

            upcomingOrderList.clear(); // Clear the previous data

            for (QueryDocumentSnapshot document : querySnapshot) {
                // Get data for the order
                String documentId = document.getId();
                Map<String, Object> orderItemsMap = (Map<String, Object>) document.get("order_items");

                if (orderItemsMap != null) {
                    // Access nested data within order_items
                    String foodImgUrl = (String) orderItemsMap.get("cart_image");
                    String foodDetailsText = (String) orderItemsMap.get("cart_name");
                    String remarkText = (String) orderItemsMap.get("cart_remark");


                    // Check if cart_quantity is null before using it
                    Long cartQuantityLong = (Long) orderItemsMap.get("cart_quantity");
                    int foodQuantity = (cartQuantityLong != null) ? cartQuantityLong.intValue() : 0;

                    // Check if cart_price is null before using it
                    Double cartPriceDouble = (Double) orderItemsMap.get("cart_price");
                    double priceValue = (cartPriceDouble != null) ? cartPriceDouble : 0.0;

                    // Extract fields outside order_items
                    String pickupTimeText = document.getString("pickup_time");
                    String orderStatusText = document.getString("order_status");
                    String paymentMethodText = document.getString("payment_method");
                    String totalamountText = document.getString("total_amount");

                    // Extract order_number from order_items or from outside if it's a number
                    Object orderNumberField = orderItemsMap.get("order_number");
                    String orderNumberText = "";

                    if (orderNumberField != null) {
                        if (orderNumberField instanceof Number) {
                            // If it's a number, convert it to String
                            orderNumberText = String.valueOf(((Number) orderNumberField).longValue());
                        } else if (orderNumberField instanceof String) {
                            // If it's already a string, use it
                            orderNumberText = (String) orderNumberField;
                        }
                    } else {
                        // If order_number is not found in order_items, check outside the map
                        Object outsideOrderNumberField = document.get("order_number");
                        if (outsideOrderNumberField instanceof Number) {
                            orderNumberText = String.valueOf(((Number) outsideOrderNumberField).longValue());
                        } else if (outsideOrderNumberField instanceof String) {
                            orderNumberText = (String) outsideOrderNumberField;
                        }
                    }

                    // Create an UpcomingOrder object
                    UpcomingOrder upcomingOrder = new UpcomingOrder(
                            documentId,
                            foodImgUrl,
                            foodDetailsText,
                            foodQuantity,
                            priceValue,
                            orderNumberText,
                            pickupTimeText,
                            orderStatusText,
                            paymentMethodText,
                            totalamountText,
                            remarkText

                    );

                    // Only add orders with status other than "Pickup Completed"
                    if (!"Pickup Completed".equals(orderStatusText)) {
                        upcomingOrderList.add(upcomingOrder);
                    }
                }
            }

            // Notify the adapter that data has changed
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}
