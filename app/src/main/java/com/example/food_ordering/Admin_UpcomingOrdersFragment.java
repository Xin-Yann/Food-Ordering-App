package com.example.food_ordering;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.os.Handler;

public class Admin_UpcomingOrdersFragment extends Fragment {

    private FirebaseFirestore db;
    private Query orderQuery;
    private ImageView foodImg;
    private TextView foodDetails;
    private TextView price;
    private TextView orderNumber;
    private TextView pickupTime;
    private TextView orderStatus;
    private TextView paymentMethod;
    private String currentUserEmail;
    private int foodQuantity; // Declare it once

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_order_upcoming, container, false);

        // Initialize views
        foodImg = view.findViewById(R.id.food_img);
        foodDetails = view.findViewById(R.id.foodDetails);
        price = view.findViewById(R.id.price);
        orderNumber = view.findViewById(R.id.orderNumber);
        pickupTime = view.findViewById(R.id.pickupTime);
        orderStatus = view.findViewById(R.id.orderStatus);
        paymentMethod = view.findViewById(R.id.paymentMethod);

        db = FirebaseFirestore.getInstance();

        // Get the current user's email after they have signed in
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Modify the query to fetch orders by the user's email
        orderQuery = db.collection("orders")
                .whereEqualTo("email", currentUserEmail);

        // Add a snapshot listener to the query
        orderQuery.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                // Handle the error
                return;
            }

            if (querySnapshot != null) {
                for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        // This will be called when an upcoming order for the user is found

                        // Get data from the document
                        String foodImgUrl = dc.getDocument().getString("food_img");
                        String foodDetailsText = dc.getDocument().getString("food_name");
                        foodQuantity = dc.getDocument().getLong("food_quantity").intValue(); // Update the existing variable
                        double priceValue = dc.getDocument().getDouble("food_price");
                        String orderNumberText = dc.getDocument().getString("order_number");
                        String pickupTimeText = dc.getDocument().getString("pickup_time");
                        String orderStatusText = dc.getDocument().getString("order_status");
                        String paymentMethodText = dc.getDocument().getString("payment_method");

                        // Load and display the image using Picasso
                        Picasso.get().load(foodImgUrl).into(foodImg);

                        // Set other data to your views directly
                        foodDetails.setText(foodQuantity + "x " + foodDetailsText); // Display food_quantity beside food_name
                        price.setText(String.format("RM"+"%.2f", priceValue));
                        orderNumber.setText(orderNumberText);
                        pickupTime.setText(pickupTimeText);
                        orderStatus.setText(orderStatusText);
                        paymentMethod.setText(paymentMethodText);



                        Button pickupButton = view.findViewById(R.id.preparingButton);
                        pickupButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showPreparingDialog();
                            }
                        });

                        Button readyToPickupButton = view.findViewById(R.id.readyToPickupButton);
                        readyToPickupButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showReadyToPickupDialog();
                            }
                        });

                    }
                }
            }
        });
        fetchDataAndUpdateUI();
        return view;
    }

    private void showPreparingDialog() {
        // Show a confirmation dialog
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Preparing")
                .setMessage("Are you sure you want to mark as preparing orders?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateOrderStatus("Preparing");
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showReadyToPickupDialog() {
        // Show a confirmation dialog
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Ready to Pickup")
                .setMessage("Are you sure you want to mark as ready to pickup orders?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateOrderStatus("Ready to Pickup");
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void updateOrderStatus(String newStatus) {
        // Query the database to find the orders associated with the current user's email
        db.collection("orders")
                .whereEqualTo("email", currentUserEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Update the order_status in each document
                        document.getReference().update("order_status", newStatus)
                                .addOnSuccessListener(aVoid -> {
                                    // Update successful
                                })
                                .addOnFailureListener(e -> {
                                    // Handle the error
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    private void fetchDataAndUpdateUI() {
        // Your existing code to query the database and populate the UI
        // ...

        // After updating the UI, use a Handler to schedule a delayed refresh
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Fetch data and update UI again
                fetchDataAndUpdateUI();
            }
        }, 3000); // 3000 milliseconds (3 seconds)
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop listening for changes when the fragment is destroyed
        orderQuery = null;
    }
}

