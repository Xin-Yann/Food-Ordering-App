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

public class DessertAdapter extends RecyclerView.Adapter<DessertAdapter.CardViewHolder> {
    private List<Dessert_Data>  dessertList; // Change to the appropriate data class (BeverageData)
    private Context context;

    public DessertAdapter(Context context, List<Dessert_Data>  dessertList) { // Change the parameter type to the appropriate data class (BeverageData)
        this.context = context;
        this. dessertList =  dessertList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dessert_menu, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Dessert_Data dessert =  dessertList.get(position); // Change the variable name to beverage
        Picasso.get().load(dessert.getImage()).into(holder.imageView);
        holder.nameTextView.setText(dessert.getName());
        holder.priceTextView.setText(dessert.getPrice());
    }

    @Override
    public int getItemCount() {
        return dessertList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;

        public CardViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.dessert_Image);
            nameTextView = itemView.findViewById(R.id.dessert_name);
            priceTextView = itemView.findViewById(R.id.dessert_price);
        }
    }
}
