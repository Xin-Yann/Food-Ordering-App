package com.example.food_ordering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Order_Confirmation extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Confirmation> confirmationArrayList;
    ConfirmationAdapter confirmationAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    TextView confirmTotalAmount;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_confirmation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        confirmationArrayList = new ArrayList<Confirmation>();
        confirmationAdapter = new ConfirmationAdapter(Order_Confirmation.this,confirmationArrayList);

        recyclerView.setAdapter(confirmationAdapter);

        confirmTotalAmount = findViewById(R.id.totalAmount);

        String inputDarpaId = getIntent().getStringExtra("inputDarpaId");

        EventChangeListener();
    }

    private void EventChangeListener() {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Query query = db.collection("cart")
                .whereEqualTo("user_email", userEmail);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Firestore Error", error.getMessage());
                    return;
                }

                double totalAmount = 0;

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        DocumentSnapshot document = dc.getDocument();
                        // Check if the document has a "payment_status" field
                        if (document.contains("payment_status")) {
                            String status = document.getString("payment_status");

                            // Skip documents with status "paid"
                            if ("paid".equals(status)) {
                                continue;
                            }
                        }

                        Confirmation confirmation = dc.getDocument().toObject(Confirmation.class);

                        confirmationArrayList.add(confirmation);

                        // Calculate and update the total amount
                        double itemPrice = Double.parseDouble(confirmation.getCart_price());
                        long itemQuantity = confirmation.getCart_quantity();
                        totalAmount += itemPrice * itemQuantity;
                    }

                    confirmationAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    confirmTotalAmount.setText("RM " + String.format("%.2f", totalAmount));
                }
            }
        });
    }

    public void toCart(View view){
        Intent intent = new Intent(this, Cart.class);
        TextView toCart = findViewById(R.id.cart);
        startActivity(intent);
    }

    public void toPayment(View view){
        Intent intent = new Intent(this, Payment.class);
        intent.putExtra("totalAmount", confirmTotalAmount.getText().toString());
        TextView toOrderConfirmation = findViewById(R.id.payBtn);
        startActivity(intent);
    }
}
