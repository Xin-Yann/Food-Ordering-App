package com.example.food_ordering;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.Toast;
import android.util.Log;

import java.util.List;

public class AdminUpcomingOrdersAdapter extends RecyclerView.Adapter<AdminUpcomingOrdersAdapter.OrderItemViewHolder> {
    private List<AdminUpcomingOrder> orderItemList;
    private Context context;



    public AdminUpcomingOrdersAdapter(Context context, List<AdminUpcomingOrder> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_upcoming_order_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        AdminUpcomingOrder orderItem = orderItemList.get(position);

        // Populate the views with data from the AdminUpcomingOrder
        Picasso.get().load(orderItem.getFoodImgUrl()).into(holder.foodImg);

        // Display food details and quantity in the desired format
        String foodDetailsAndQuantity = orderItem.getFoodQuantity() + "x " + orderItem.getFoodDetailsText();
        holder.foodDetails.setText(foodDetailsAndQuantity);

        holder.price.setText(String.format("RM%.2f", orderItem.getPriceValue()));
        holder.orderNumber.setText(orderItem.getOrderNumberText());
        holder.pickupTime.setText(orderItem.getPickupTimeText());
        holder.orderStatus.setText(orderItem.getOrderStatusText());
        holder.paymentMethod.setText(orderItem.getPaymentMethodText());
        String email = orderItem.getemail();
        Log.d("EmailDebug", "Email: " + email); // Add this line for debugging
        holder.email.setText(orderItem.getemail());


        // Handle button clicks
        holder.preparingButton.setOnClickListener(v -> {
            showConfirmationDialog(
                    "Are you sure you want to set the order as 'Preparing'?",
                    "Preparing Confirmation",
                    (dialog, which) -> {
                        // Handle the 'Preparing' button click
                        String documentId = orderItem.getDocumentId();
                        updateOrderStatus(documentId, "Preparing");
                    }
            );
        });

        holder.readyToPickupButton.setOnClickListener(v -> {
            showConfirmationDialog(
                    "Is the order ready for pickup?",
                    "Ready to Pickup Confirmation",
                    (dialog, which) -> {
                        // Handle the 'Ready to Pickup' button click
                        String documentId = orderItem.getDocumentId();
                        updateOrderStatus(documentId, "Ready to Pickup");
                    }
            );
        });
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImg;
        TextView foodDetails;
        TextView price;
        TextView orderNumber;
        TextView pickupTime;
        TextView orderStatus;
        TextView paymentMethod;
        Button preparingButton;
        Button readyToPickupButton;

        TextView email;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg = itemView.findViewById(R.id.food_img);
            foodDetails = itemView.findViewById(R.id.foodDetails);
            price = itemView.findViewById(R.id.price);
            orderNumber = itemView.findViewById(R.id.orderNumber);
            pickupTime = itemView.findViewById(R.id.pickupTime);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            preparingButton = itemView.findViewById(R.id.preparingButton);
            readyToPickupButton = itemView.findViewById(R.id.readyToPickupButton);
            email = itemView.findViewById(R.id.email);
        }
    }

    // Helper method to show a confirmation dialog
    private void showConfirmationDialog(String message, String title, DialogInterface.OnClickListener onConfirm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton("Yes", onConfirm)
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateOrderStatus(String documentId, String newStatus) {
        // Update the 'order_status' field in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference orderRef = db.collection("orders").document(documentId);
        orderRef.update("order_status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    // Handle a successful update
                    // For example, you can show a success message to the user
                    showToast("Order status updated successfully");
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occurred during the update
                    // For example, you can show an error message to the user
                    showToast("Failed to update order status: " + e.getMessage());
                });
    }

    // Helper method to show a toast message
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
