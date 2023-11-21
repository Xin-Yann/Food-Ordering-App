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

        // Fetch and populate the "Upcoming Orders" layout
        Query orderQuery = db.collection("orders");

        // Add a real-time listener for order changes
        orderQuery.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                // Handle the error
                return;
            }

            orderItemList.clear(); // Clear the previous data

            for (QueryDocumentSnapshot document : querySnapshot) {
                // Get data for the order
                String documentId = document.getId();
                String foodImgUrl = document.getString("food_img");
                String foodDetailsText = document.getString("food_name");
                int foodQuantity = document.getLong("food_quantity").intValue();
                double priceValue = document.getDouble("food_price");
                String orderNumberText = document.getString("order_number");
                String pickupTimeText = document.getString("pickup_time");
                String orderStatusText = document.getString("order_status");
                String paymentMethodText = document.getString("payment_method");
                String email = document.getString("email");

                // Create an AdminUpcomingOrder object
                AdminUpcomingOrder orderItem = new AdminUpcomingOrder(
                        documentId, foodImgUrl, foodDetailsText, foodQuantity,
                        priceValue, orderNumberText, pickupTimeText, orderStatusText,
                        paymentMethodText, email
                );

                // Only add orders with status other than "Pickup Completed"
                if (!"Pickup Completed".equals(orderStatusText)) {
                    orderItemList.add(orderItem);
                }
            }

            // Notify the adapter that data has changed
            orderItemAdapter.notifyDataSetChanged();
        });

        return view;
    }
}
