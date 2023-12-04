package com.example.food_ordering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {

    Context context;
    ArrayList<CartReport> cartReportArrayList;

    public ReportAdapter(Context context, ArrayList<CartReport> cartReportArrayList) {
        this.context = context;
        this.cartReportArrayList = cartReportArrayList;
    }

    @NonNull
    @Override
    public ReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.cart_report,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.MyViewHolder holder, int position) {

        CartReport cartReport = cartReportArrayList.get(position);

        holder.cart_name.setText(cartReport.cart_name);
        holder.cart_price.setText(cartReport.cart_price);
        holder.payment_status.setText(cartReport.payment_status);
        holder.cart_quantity.setText(String.valueOf(cartReport.cart_quantity));

    }

    @Override
    public int getItemCount() {
        return cartReportArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView cart_name, cart_price, cart_quantity, payment_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_name = itemView.findViewById(R.id.cart_name);
            cart_price = itemView.findViewById(R.id.cart_price);
            cart_quantity = itemView.findViewById(R.id.cart_quantity);
            payment_status = itemView.findViewById(R.id.payment_status);

        }
    }
}
