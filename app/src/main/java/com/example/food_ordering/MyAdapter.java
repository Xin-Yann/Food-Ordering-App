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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CardViewHolder> {
    private List<Dish> dataList;
    private Context context;

    public MyAdapter(Context context, List<Dish> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setFilteredList(List<Dish> filteredList){
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
        Dish data = dataList.get(position);
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

