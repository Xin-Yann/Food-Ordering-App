package com.example.food_ordering;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private List<OrderItem> orderItemList;

    public OrderItemAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);

        // Populate the views with data from the OrderItem
        Picasso.get().load(orderItem.getFoodImgUrl()).into(holder.foodImg);
        holder.foodDetails.setText(orderItem.getFoodDetailsText());
        holder.price.setText(String.format("RM%.2f", orderItem.getPriceValue()));
        holder.orderNumber.setText(orderItem.getOrderNumberText());
        holder.pickupTime.setText(orderItem.getPickupTimeText());
        holder.orderStatus.setText(orderItem.getOrderStatusText());
        holder.paymentMethod.setText(orderItem.getPaymentMethodText());
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

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg = itemView.findViewById(R.id.food_img);
            foodDetails = itemView.findViewById(R.id.foodDetails);
            price = itemView.findViewById(R.id.price);
            orderNumber = itemView.findViewById(R.id.orderNumber);
            pickupTime = itemView.findViewById(R.id.pickupTime);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
        }
    }
}
