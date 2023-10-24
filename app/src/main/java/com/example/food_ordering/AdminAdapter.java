package com.example.food_ordering;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.CardViewHolder> {
    private List<User> dataList;
    private Context context;

    public AdminAdapter(Context context, List<User> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setFilteredList(List<User> filteredList){
        this.dataList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        User data = dataList.get(position);
        holder.name.setText(data.getName());
        holder.id.setText(data.getId());
        holder.email.setText(data.getEmail());
        holder.contact.setText(data.getContact());
        holder.password.setText(data.getPassword());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        
        TextView name;
        TextView id;
        TextView email;
        TextView contact;
        TextView password;
        

        public CardViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            id = itemView.findViewById(R.id.id);
            email = itemView.findViewById(R.id.email);
            contact = itemView.findViewById(R.id.contact);
            password = itemView.findViewById(R.id.password);
        }
    }
}

