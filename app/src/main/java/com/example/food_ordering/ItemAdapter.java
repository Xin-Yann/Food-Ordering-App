package com.example.food_ordering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import com.squareup.picasso.Picasso;
import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<Item> itemArrayList;

    public ItemAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {

        Item item = itemArrayList.get(position);

        holder.cart_name.setText(item.cart_name);
        holder.cart_remarks.setText(item.cart_remarks);
        holder.cart_price.setText(item.cart_price);
        holder.cart_quantity.setText(String.valueOf(item.cart_quantity));
        Picasso.get().load(item.getCart_image()).into(holder.cart_image);

        // Initialize deleteBtn and cart_name for each item
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete action for this item
                String itemName = item.cart_name;
                // Call a method to delete the item from Firestore using the itemName
                // Implement this delete method in your Cart.java
                ((Cart) context).deleteItemFromFirestore(itemName);

                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView cart_name, cart_price, cart_remarks, cart_quantity;
        ImageView cart_image;
        ImageButton deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_name = itemView.findViewById(R.id.cart_name);
            cart_price = itemView.findViewById(R.id.cart_price);
            cart_remarks = itemView.findViewById(R.id.cart_remarks);
            cart_quantity = itemView.findViewById(R.id.cart_quantity);
            cart_image = itemView.findViewById(R.id.cart_image);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
