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

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.CardViewHolder> {
    private List<Menu> dataList;
    private Context context;

    public MenuAdapter(Context context, List<Menu> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setFilteredList(List<Menu> filteredList){
        this.dataList = filteredList;
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
        Menu data = dataList.get(position);
        Picasso.get().load(data.getImage()).into(holder.imageView);
        holder.nameTextView.setText(data.getName());
        holder.priceTextView.setText(data.getPrice());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;

        public CardViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.main_Image);
            nameTextView = itemView.findViewById(R.id.main_name);
            priceTextView = itemView.findViewById(R.id.main_price);
        }
    }
}

