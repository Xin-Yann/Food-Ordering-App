package com.example.food_ordering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ConfirmationAdapter extends RecyclerView.Adapter<ConfirmationAdapter.MyViewHolder> {

    Context context;
    ArrayList<Confirmation> confirmationArrayList;

    public ConfirmationAdapter(Context context, ArrayList<Confirmation> confirmationArrayList) {
        this.context = context;
        this.confirmationArrayList = confirmationArrayList;
    }

    @NonNull
    @Override
    public ConfirmationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.confirmation,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmationAdapter.MyViewHolder holder, int position) {

        Confirmation confirmation = confirmationArrayList.get(position);

        holder.cartName.setText(confirmation.cart_name);
        holder.cartPrice.setText(confirmation.cart_price);
        holder.cartRemarks.setText(confirmation.cart_remarks);
        holder.cartQuantity.setText(String.valueOf(confirmation.cart_quantity));

    }

    @Override
    public int getItemCount() {
        return confirmationArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView cartName, cartRemarks, cartPrice, cartQuantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cartName = itemView.findViewById(R.id.cartName);
            cartRemarks = itemView.findViewById(R.id.cartRemarks);
            cartPrice = itemView.findViewById(R.id.cartPrice);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
        }
    }
}
