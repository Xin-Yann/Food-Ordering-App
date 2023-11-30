package com.example.food_ordering;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {
    FirebaseFirestore firestore;
    RadioGroup radioGroup;
    TimePicker timePicker;
    FrameLayout cardFragmentContainer;
    Fragment currentFragment;
    TextView confirmTotalAmount;
    Button orderButton;
    TextView textView;
    FirebaseAuth auth;
    FirebaseUser user, staffs;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        firestore = FirebaseFirestore.getInstance();

        confirmTotalAmount = findViewById(R.id.confirmTotalAmount);
        String totalAmount = getIntent().getStringExtra("totalAmount");
        confirmTotalAmount.setText(totalAmount);

        // Initialize FirebaseAuth and get the current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        staffs = auth.getCurrentUser();

        // Query Firestore to retrieve additional user data
        // Query Firestore to retrieve user data
        firestore.collection("users")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // User data found
                                String userName = document.getString("name");
                                String userId = document.getString("id");
                                String userContact = document.getString("contact");
                                String userEmail = document.getString("email");

                                // Update TextViews with user data
                                textView = findViewById(R.id.showUsername);
                                textView.setText(userName);
                                textView = findViewById(R.id.showDarpaId);
                                textView.setText(userId);
                                textView = findViewById(R.id.showContactNo);
                                textView.setText(userContact);
                                textView = findViewById(R.id.showEmail);
                                textView.setText(userEmail);

                            } else {
                                // If user data not found, try retrieving staff data
                                firestore.collection("staffs")
                                        .document(staffs.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        // Staff data found
                                                        String staffName = document.getString("name");
                                                        String staffId = document.getString("id");
                                                        String staffContact = document.getString("contact");
                                                        String staffEmail = document.getString("email");

                                                        // Update TextViews with staff data
                                                        textView = findViewById(R.id.showUsername);
                                                        textView.setText(staffName);
                                                        textView = findViewById(R.id.showDarpaId);
                                                        textView.setText(staffId);
                                                        textView = findViewById(R.id.showContactNo);
                                                        textView.setText(staffContact);
                                                        textView = findViewById(R.id.showEmail);
                                                        textView.setText(staffEmail);

                                                    } else {
                                                        // Handle the case when neither user nor staff data is found
                                                    }
                                                } else {
                                                    // Handle the error during staff data retrieval
                                                }
                                            }
                                        });

                            }
                        } else {
                            // Handle the error during user data retrieval
                        }
                    }
                });

        radioGroup = findViewById(R.id.radioGroup);
        cardFragmentContainer = findViewById(R.id.cardFragmentContainer);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                currentFragment = fragmentManager.findFragmentById(R.id.cardFragmentContainer);

                // Remove current fragment
                if (currentFragment != null) {
                    transaction.remove(currentFragment);
                }

                // Replace fragment container with the CardFragment
                if (checkedId == R.id.card) {
                    transaction.replace(R.id.cardFragmentContainer, new FragmentCard());
                } else {
                    currentFragment = null;
                }

                transaction.commit();

                if (checkedId == R.id.card) {
                    adjustLayoutForFragment(true,
                            1790,
                            1790,
                            R.id.pickupTime, R.id.selectPickupTime);
                } else {
                    adjustLayoutForFragment(false,
                            945,
                            945,
                            R.id.pickupTime, R.id.selectPickupTime);
                }
            }
        });

        timePicker = findViewById(R.id.selectPickupTime);

        timePicker.setHour(8);
        timePicker.setMinute(0);

        int maxHour = 16;
        int maxMinute = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            timePicker.setHour(maxHour);
            timePicker.setMinute(maxMinute);
        }

        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if ((hourOfDay < 8 || (hourOfDay == 8 && minute < 0)) || (hourOfDay > 16 || (hourOfDay == 16 && minute > 0))) {
                    // If outside the range, set the time back to the minimum (8:00 AM)
                    timePicker.setHour(8);
                    timePicker.setMinute(0);
                }
            }
        });


        orderButton = findViewById(R.id.orderBtn);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long orderId = generateOrderId();
                Map<String, Object> orderDetails = new HashMap<>();
                placeOrder(orderDetails, orderId);

                // Deduct the order total from the wallet amount
                double walletAmount = getWalletAmount();
                double orderTotal = parseOrderTotal(confirmTotalAmount.getText().toString());

                if (walletAmount >= orderTotal) {
                    double newWalletAmount = walletAmount - orderTotal;
                    saveWalletAmount(newWalletAmount);
                    updateDisplayAmount(newWalletAmount);
                    placeOrder(orderDetails, orderId);
                } else {
                    Toast.makeText(getApplicationContext(), "Insufficient funds in the wallet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void adjustLayoutForFragment(boolean fragmentVisible, int marginTopPickupTime, int marginTopSelectPickupTime,
                                         int... viewIds) {
        for (int viewId : viewIds) {
            View view = findViewById(viewId);
            if (view != null) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

                if (viewId == R.id.pickupTime) {
                    layoutParams.topMargin = marginTopPickupTime;
                } else if (viewId == R.id.selectPickupTime) {
                    layoutParams.topMargin = marginTopSelectPickupTime;
                }

                view.setLayoutParams(layoutParams);
            }
        }
    }

    private void updateStatusForCart(String userEmail) {
        firestore.collection("cart")
                .whereEqualTo("user_email", userEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            // Update the status to "paid" for each cart item
                            firestore.collection("cart")
                                    .document(document.getId())
                                    .update("payment_status", "paid")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Cart item status updated successfully for user: " + userEmail);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Error updating cart item status for user: " + userEmail, e);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error querying cart items for user: " + userEmail, e);
                    }
                });
    }


    private long generateOrderId() {
        return System.currentTimeMillis() + (long) (Math.random() * 1000);
    }

    private void placeOrder(Map<String, Object> order, long orderId) {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Fetch cart data
        firestore.collection("cart")
                .whereEqualTo("user_email", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot cartDocument : queryDocumentSnapshots.getDocuments()) {
                        // Get cart data
                        String cartImage = cartDocument.getString("cart_image");
                        String cartName = cartDocument.getString("cart_name");
                        long cartQuantity = cartDocument.getLong("cart_quantity");
                        String cartPriceString = cartDocument.getString("cart_price");
                        String cartRemarkString = cartDocument.getString("cart_remarks");
                        String paymentStatus = cartDocument.getString("payment_status");

                        // Check if payment_status is null
                        if (paymentStatus == null) {
                            try {
                                double cartPrice = Double.parseDouble(cartPriceString);

                                // Add cart data to the order
                                Map<String, Object> cartItem = new HashMap<>();
                                cartItem.put("cart_image", cartImage);
                                cartItem.put("cart_name", cartName);
                                cartItem.put("cart_quantity", cartQuantity);
                                cartItem.put("cart_price", cartPrice);
                                cartItem.put("cart_remark", cartRemarkString);

                                // Add the cart item to the "order_items" field in the order
                                order.put("order_items", cartItem);

                                // Add other order details here
                                String name = ((TextView) findViewById(R.id.showUsername)).getText().toString();
                                String id = ((TextView) findViewById(R.id.showDarpaId)).getText().toString();
                                String email = ((TextView) findViewById(R.id.showEmail)).getText().toString();
                                String contact = ((TextView) findViewById(R.id.showContactNo)).getText().toString();

                                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                                String paymentMethod = getPaymentMethod(selectedRadioButtonId);

                                TimePicker timePicker = findViewById(R.id.selectPickupTime);
                                int hour = timePicker.getHour();
                                int minute = timePicker.getMinute();
                                String pickupTime = String.format("%02d:%02d", hour, minute);

                                // Add other order details to the "orders" collection
                                order.put("user_email", userEmail);
                                order.put("name", name);
                                order.put("id", id);
                                order.put("contact", contact);
                                order.put("payment_method", paymentMethod);
                                order.put("pickup_time", pickupTime);
                                order.put("order_number", orderId);
                                order.put("total_amount", confirmTotalAmount.getText().toString());
                                order.put("order_status", "Pending");

                                // Save the order to Firestore
                                firestore.collection("orders").add(order)
                                        .addOnSuccessListener(documentReference -> {
                                            // Update the payment_status to "paid" for each cart item
                                            updateStatusForCart(userEmail);

                                            // Display a success message
                                            Toast.makeText(getApplicationContext(), "Order placed successfully!", Toast.LENGTH_LONG).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getApplicationContext(), "Failed to place order", Toast.LENGTH_LONG).show();
                                        });
                            } catch (NumberFormatException e) {
                                // Handle the case where 'cart_price' is not a valid number
                            }
                        } else {
                            // Handle the case where payment_status is not null
                            // You can choose to skip or handle accordingly
                        }
                    }
                });
    }



    private String getPaymentMethod(int selectedRadioButtonId) {
        if (selectedRadioButtonId == R.id.cash) {
            return "Cash";
        } else if (selectedRadioButtonId == R.id.wallet) {
            return "Wallet";
        } else if (selectedRadioButtonId == R.id.card) {
            return "Debit / Credit Card";
        } else {
            return "Unknown";
        }
    }

    public void toOrderConfirmation(View view) {
        Intent intent = new Intent(this, Order_Confirmation.class);
        ImageButton toOrderConfirmation = findViewById(R.id.backBtn);
        startActivity(intent);
    }

    public void toOrderHistory(View view) {
        Intent intent = new Intent(this, Order_history_n_upcoming.class);
        Button toOrderHistory = findViewById(R.id.orderBtn);
        startActivity(intent);
    }
    private double getWalletAmount() {
        // Retrieve the wallet amount from SharedPreferences
        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String walletAmountStr = preferences.getString("walletAmount", "0");
        return Double.parseDouble(walletAmountStr);
    }

    private double parseOrderTotal(String orderTotalStr) {
        // Parse the order total from String to double
        return Double.parseDouble(orderTotalStr.replace("RM ", ""));
    }

    private void saveWalletAmount(double newWalletAmount) {
        // Save the updated wallet amount to SharedPreferences
        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("walletAmount", String.valueOf(newWalletAmount));
        editor.apply();
    }

    private void updateDisplayAmount(double newWalletAmount) {
        // Send the updated wallet amount back to the calling activity
        Intent intent = new Intent();
        intent.putExtra("updatedAmount", "RM " + newWalletAmount);
        setResult(RESULT_OK, intent);
    }
}