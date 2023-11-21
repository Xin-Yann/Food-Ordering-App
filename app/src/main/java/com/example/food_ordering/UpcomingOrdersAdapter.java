package com.example.food_ordering;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UpcomingOrdersAdapter extends RecyclerView.Adapter<UpcomingOrdersAdapter.ViewHolder> {
    private List<UpcomingOrder> upcomingOrdersList;
    private String currentUserEmail;

    public UpcomingOrdersAdapter(List<UpcomingOrder> upcomingOrdersList, String currentUserEmail) {
        this.upcomingOrdersList = upcomingOrdersList;
        this.currentUserEmail = currentUserEmail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UpcomingOrder order = upcomingOrdersList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return upcomingOrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView foodImg;
        private TextView foodDetails;
        private TextView price;
        private TextView foodQuantity;
        private TextView orderNumber;
        private TextView pickupTime;
        private TextView orderStatus;
        private TextView paymentMethod;
        private Button pickupButton;

        public ViewHolder(View itemView) {
            super(itemView);
            foodImg = itemView.findViewById(R.id.food_img);
            foodDetails = itemView.findViewById(R.id.foodDetails);
            price = itemView.findViewById(R.id.price);
            foodQuantity = itemView.findViewById(R.id.foodQuantity);
            orderNumber = itemView.findViewById(R.id.orderNumber);
            pickupTime = itemView.findViewById(R.id.pickupTime);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            pickupButton = itemView.findViewById(R.id.pickupButton);
        }

        public void bind(UpcomingOrder order) {
            // Bind data to the views
            Picasso.get().load(order.getFoodImgUrl()).into(foodImg);
            foodDetails.setText(order.getFoodQuantity() + "x " + order.getFoodDetailsText());
            price.setText(String.format("RM%.2f", order.getPriceValue()));
            orderNumber.setText(order.getOrderNumberText());
            pickupTime.setText(order.getPickupTimeText());
            orderStatus.setText(order.getOrderStatusText());
            paymentMethod.setText(order.getPaymentMethodText());

            // Set the click listener for the "Pickup Completed" button
            pickupButton.setOnClickListener(v -> {
                showPickupConfirmationDialog(order);
            });

            if ("Ready to Pickup".equals(order.getOrderStatusText())) {
                // Show an in-app notification
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Order Ready for Pickup")
                        .setMessage("Your order is ready for pickup.")
                        .setPositiveButton("OK", null)
                        .show();
            }

        }

        private void showPickupConfirmationDialog(UpcomingOrder order) {
            // Show a confirmation dialog
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle("Confirm Pickup Completion")
                    .setMessage("Are you sure you want to mark this order as Pickup Completed?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Handle the pickup completion for the order here
                        updateOrderStatusForCurrentUser(order);
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        private void updateOrderStatusForCurrentUser(UpcomingOrder order) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a map to update both the "order_status" and "pickupedOn" fields
            Map<String, Object> updates = new HashMap<>();
            updates.put("order_status", "Pickup Completed");

            // Get the current date and time
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String pickupedOn = dateFormat.format(currentDate);

            updates.put("pickupedOn", pickupedOn);

            // Update the Firestore document
            db.collection("orders")
                    .document(order.getDocumentId())
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Successfully updated the order status and pickup date and time
                        // You can also handle any UI updates or other actions here
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                    });
        }
    }
}
