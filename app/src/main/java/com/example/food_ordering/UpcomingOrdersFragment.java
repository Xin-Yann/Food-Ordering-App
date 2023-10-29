package com.example.food_ordering;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import androidx.appcompat.app.AlertDialog;

public class UpcomingOrdersFragment extends Fragment {

    private FirebaseFirestore db;
    private Query orderQuery;
    private TabLayout tabLayout;
    private String currentUserEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_upcoming, container, false);

        db = FirebaseFirestore.getInstance();
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        tabLayout = requireActivity().findViewById(R.id.tabLayout);

        // Get references to the views within the layout
        ImageView foodImg = view.findViewById(R.id.food_img);
        TextView foodDetails = view.findViewById(R.id.foodDetails);
        TextView price = view.findViewById(R.id.price);
        TextView orderNumber = view.findViewById(R.id.orderNumber);
        TextView pickupTime = view.findViewById(R.id.pickupTime);
        TextView orderStatus = view.findViewById(R.id.orderStatus);
        TextView paymentMethod = view.findViewById(R.id.paymentMethod);

        // Fetch and populate the "Upcoming Orders" layout
        orderQuery = db.collection("orders").whereEqualTo("email", currentUserEmail);

        orderQuery.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                // Handle the error
                return;
            }

            if (querySnapshot != null) {
                for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        // Get data for the order
                        String foodImgUrl = dc.getDocument().getString("food_img");
                        String foodDetailsText = dc.getDocument().getString("food_name");
                        int foodQuantity = dc.getDocument().getLong("food_quantity").intValue();
                        double priceValue = dc.getDocument().getDouble("food_price");
                        String orderNumberText = dc.getDocument().getString("order_number");
                        String pickupTimeText = dc.getDocument().getString("pickup_time");
                        String orderStatusText = dc.getDocument().getString("order_status");
                        String paymentMethodText = dc.getDocument().getString("payment_method");

                        // Populate the views with order data
                        Picasso.get().load(foodImgUrl).into(foodImg);
                        foodDetails.setText(foodQuantity + "x " + foodDetailsText);
                        price.setText(String.format("RM%.2f", priceValue));
                        orderNumber.setText(orderNumberText);
                        pickupTime.setText(pickupTimeText);
                        orderStatus.setText(orderStatusText);
                        paymentMethod.setText(paymentMethodText);
                    }
                }
            }
        });

        // Set up the "Pickup Completed" button click listener
        Button pickupButton = view.findViewById(R.id.pickupButton);
        pickupButton.setOnClickListener(v -> showPickupConfirmationDialog());

        return view;
    }

    private void showPickupConfirmationDialog() {
        // Show a confirmation dialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Pickup Completion")
                .setMessage("Are you sure you want to mark this order as Pickup Completed?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Handle the pickup completion
                })
                .setNegativeButton("No", null)
                .show();
    }
}