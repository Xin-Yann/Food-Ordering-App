package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {
    FirebaseFirestore firestore;
    RadioGroup radioGroup;
    TimePicker timePicker;
    FrameLayout cardFragmentContainer;
    Fragment currentFragment;
    Button orderButton;
    TextView textView;
    FirebaseAuth auth;
    FirebaseUser user , staffs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        firestore = FirebaseFirestore.getInstance();

        // Initialize FirebaseAuth and get the current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        staffs= auth.getCurrentUser();

        if (user == null) {
            // If the user is not logged in, redirect them to the login screen
            Intent intent = new Intent(Payment.this, login.class);
            startActivity(intent);
            finish();
        } else {
            // Query Firestore to retrieve additional user data
            firestore.collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String userName = document.getString("name");
                                    String userId = document.getString("id");
                                    String userContact = document.getString("contact");
                                    String userEmail = document.getString("email");
                                    // You can retrieve other user data here
                                    textView = findViewById(R.id.showUsername);
                                    textView.setText(userName);
                                    textView = findViewById(R.id.showDarpaId);
                                    textView.setText(userId);
                                    textView = findViewById(R.id.showContactNo);
                                    textView.setText(userContact);
                                    textView = findViewById(R.id.showEmail);
                                    textView.setText(userEmail);

                                } else if (staffs == null) {
                                    // If the user is not logged in, redirect them to the login screen
                                    Intent intent = new Intent(Payment.this, login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Query Firestore to retrieve additional user data
                                    firestore.collection("staffs")
                                            .document(staffs.getUid())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            String staffName = document.getString("name");
                                                            String staffId = document.getString("id");
                                                            String staffContact = document.getString("contact");
                                                            String staffEmail = document.getString("email");
                                                            // You can retrieve other user data here
                                                            textView = findViewById(R.id.showUsername);
                                                            textView.setText(staffName);
                                                            textView = findViewById(R.id.showDarpaId);
                                                            textView.setText(staffId);
                                                            textView = findViewById(R.id.showContactNo);
                                                            textView.setText(staffContact);
                                                            textView = findViewById(R.id.showEmail);
                                                            textView.setText(staffEmail);
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
        }

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
                }

                transaction.commit();
            }
        });

        orderButton = findViewById(R.id.orderBtn);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });
    }

    private void placeOrder() {
        Map<String, Object> order = new HashMap<>();
        // Get data and add it to the 'menu' map
        String name = (findViewById(R.id.showUsername)).toString();
        String id = (findViewById(R.id.showDarpaId)).toString();
        String email = (findViewById(R.id.showEmail)).toString();
        String contact = (findViewById(R.id.showContactNo)).toString();

        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        String paymentMethod = getPaymentMethod(selectedRadioButtonId);

        timePicker = findViewById(R.id.selectPickupTime);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String pickupTime = String.format("%02d:%02d", hour, minute);

        // Create a menu object with the added data
        order.put("name", name);
        order.put("id", id);
        order.put("email", email);
        order.put("contact", contact);
        order.put("paymentMethod", paymentMethod);
        order.put("pickupTime", pickupTime);

        firestore.collection("orders").add(order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Order placed successfully", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to place order", Toast.LENGTH_LONG).show();
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
}