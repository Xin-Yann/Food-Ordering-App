package com.example.food_ordering;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryFragment extends Fragment {
    private FirebaseFirestore db;
    private Query orderQuery;
    private String currentUserEmail;
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<DocumentSnapshot> orderHistoryList; // Change this to DocumentSnapshot

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        db = FirebaseFirestore.getInstance();
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        recyclerView = view.findViewById(R.id.orderHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderHistoryList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(orderHistoryList);
        recyclerView.setAdapter(adapter);

        // Set up your Firestore query to filter by email and order status
        orderQuery = db.collection("orders")
                .whereEqualTo("email", currentUserEmail)
                .whereEqualTo("order_status", "Pickup Completed");
        orderQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                orderHistoryList.clear();
                orderHistoryList.addAll(task.getResult().getDocuments());
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
