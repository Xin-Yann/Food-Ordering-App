package com.example.food_ordering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class BeverageAdapter extends RecyclerView.Adapter<BeverageAdapter.CardViewHolder> {
    private List<BeverageData> berverageList; // Change to the appropriate data class (BeverageData)
    private Context context;

    public BeverageAdapter(Context context, List<BeverageData> berverageList) { // Change the parameter type to the appropriate data class (BeverageData)
        this.context = context;
        this.berverageList = berverageList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.beverage_menu, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        BeverageData beverage = berverageList.get(position); // Change the variable name to beverage
        Picasso.get().load(beverage.getImage()).into(holder.imageView);
        holder.nameTextView.setText(beverage.getName());
        holder.priceTextView.setText(beverage.getPrice());
    }

    @Override
    public int getItemCount() {
        return berverageList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;

        public CardViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.beverage_Image);
            nameTextView = itemView.findViewById(R.id.beverage_name);
            priceTextView = itemView.findViewById(R.id.beverage_price);
        }
    }
}
