package com.example.food_ordering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminMenuAdapter extends RecyclerView.Adapter<AdminMenuAdapter.CardViewHolder> {
    private List<AdminMenu> dataList;
    private Context context;
    private OnItemClickListener listener; // Declare a listener

    // Define an interface for the click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        Button editButton;
        Button deleteButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize edit and delete buttons
            editButton = itemView.findViewById(R.id.editButton); // Make sure you have the correct ID in your XML
            deleteButton = itemView.findViewById(R.id.deleteButton); // Make sure you have the correct ID in your XML

            // Set click listeners for edit and delete buttons
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public AdminMenuAdapter(Context context, List<AdminMenu> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setFilteredList(List<AdminMenu> filteredList){
        this.dataList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manage_food_menu, parent, false);
        return new CardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        AdminMenu data = dataList.get(holder.getAdapterPosition());
        Picasso.get().load(data.getImage()).into(holder.imageView);
        holder.nameTextView.setText(data.getName());
        holder.detailTextView.setText(data.getDetail());
        holder.priceTextView.setText(data.getPrice());
        holder.idTextView.setText(data.getId());

        // Set an OnClickListener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int itemPosition = holder.getAdapterPosition(); // Use getAdapterPosition()
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemPosition);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView detailTextView;
        TextView priceTextView;
        TextView idTextView;

        public CardViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.main_Image);
            nameTextView = itemView.findViewById(R.id.main_name);
            detailTextView = itemView.findViewById(R.id.main_detail);
            priceTextView = itemView.findViewById(R.id.main_price);
            idTextView = itemView.findViewById(R.id.main_id);
        }
    }
}
