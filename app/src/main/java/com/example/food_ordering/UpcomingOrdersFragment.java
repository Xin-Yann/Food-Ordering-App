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
        orderQuery = db.collection("orders").whereEqualTo("email", currentUserEmail);

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
                String foodImgUrl = document.getString("food_img");
                String foodDetailsText = document.getString("food_name");
                int foodQuantity = document.getLong("food_quantity").intValue();
                double priceValue = document.getDouble("food_price");
                String orderNumberText = document.getString("order_number");
                String pickupTimeText = document.getString("pickup_time");
                String orderStatusText = document.getString("order_status");
                String paymentMethodText = document.getString("payment_method");

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
                        paymentMethodText
                );

                // Only add orders with status other than "Pickup Completed"
                if (!"Pickup Completed".equals(orderStatusText)) {
                    upcomingOrderList.add(upcomingOrder);
                }
            }

            // Notify the adapter that data has changed
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}
