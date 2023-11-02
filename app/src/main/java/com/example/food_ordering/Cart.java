package com.example.food_ordering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Item> itemArrayList;
    ItemAdapter itemAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    TextView confirmTotal, confirmTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        itemArrayList = new ArrayList<Item>();
        itemAdapter = new ItemAdapter(Cart.this,itemArrayList);

        recyclerView.setAdapter(itemAdapter);

        // Initialize both TextViews here
        confirmTotal = findViewById(R.id.confirmTotal);
        confirmTotalAmount = findViewById(R.id.confirmTotalAmount);

        EventChangeListener();

    }

    private void EventChangeListener() {

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        db.collection("cart")
                .whereEqualTo("user_email", userEmail)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Log.e("Firestore error",error.getMessage());
                            return;

                        }

                        double totalAmount = 0;

                        for (DocumentChange dc : value.getDocumentChanges()){

                            if (dc.getType() == DocumentChange.Type.ADDED){
                                Item item = dc.getDocument().toObject(Item.class);
                                // Fetch the cart_image from the document
                                item.setCart_image(dc.getDocument().getString("cart_image"));
                                itemArrayList.add(dc.getDocument().toObject(Item.class));

                                // Calculate and update the total amount
                                double itemPrice = Double.parseDouble(item.getCart_price());
                                long itemQuantity = item.getCart_quantity();
                                totalAmount += itemPrice * itemQuantity;

                            }

                            itemAdapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            // Set the calculated total amount in the TextView
                            confirmTotalAmount.setText("RM " + String.format("%.2f", totalAmount));

                        }

                    }
                });

    }

     void deleteItemFromFirestore(String itemName){
        db.collection("cart")
                .whereEqualTo("cart_name", itemName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            db.collection("cart")
                                    .document(documentID)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(Cart.this,"Item Deleted", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(Cart.this, Cart.class);
                                            startActivity(intent);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(Cart.this,"Deleted Failed", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } else {

                            Toast.makeText(Cart.this,"Item Not Found", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void toOrderConfirmation(View view){
        Intent intent = new Intent(this, Order_Confirmation.class);
        TextView toOrderConfirmation = findViewById(R.id.payBtn);
        startActivity(intent);
    }
}
