package com.example.food_ordering;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import android.content.Context;


import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.CardViewHolder> {
    private List<Menu> datalist;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MenuAdapter(Context context, List<Menu> dataList) {
        this.context = context;
        this.datalist = dataList;
    }

    public void setFilteredList(List<Menu> filteredList) {
        this.datalist = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_menu, parent, false);
        return new CardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Menu data = datalist.get(holder.getAdapterPosition());
        Picasso.get().load(data.getImage()).into(holder.imageView);
        holder.nameTextView.setText(data.getName());
        holder.detailTextView.setText(data.getDetail());
        holder.priceTextView.setText(data.getPrice());


        // Set an OnClickListener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int itemPosition = holder.getAdapterPosition();
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemPosition);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView detailTextView;
        TextView priceTextView;


        public CardViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.main_Image);
            nameTextView = itemView.findViewById(R.id.main_name);
            detailTextView = itemView.findViewById(R.id.main_detail);
            priceTextView = itemView.findViewById(R.id.main_price);

        }
    }
}
