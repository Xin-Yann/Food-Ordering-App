package com.example.food_ordering;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Report extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<CartReport> cartReportArrayList;
    ReportAdapter reportAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    TextView totalProfit;
    TextView cartCount;
    private Map<String, Integer> cartNameCountMap = new HashMap<>();
    private Map<String, Long> cartNameQuantityMap = new HashMap<>();
    private Map<String, Double> cartNameTotalPriceMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        cartReportArrayList = new ArrayList<CartReport>();
        reportAdapter = new ReportAdapter(Report.this,cartReportArrayList);

        recyclerView.setAdapter(reportAdapter);

        totalProfit = findViewById(R.id.totalProfit);
        cartCount = findViewById(R.id.cart_count);

        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("cart")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        cartReportArrayList.clear();
                        cartNameCountMap.clear();
                        cartNameQuantityMap.clear();
                        cartNameTotalPriceMap.clear();

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                CartReport cartReport = dc.getDocument().toObject(CartReport.class);
                                cartReportArrayList.add(cartReport);
                                updateCartNameCount(cartReport.cart_name);
                                updateCartNameQuantity(cartReport.cart_name, cartReport.cart_quantity);
                                updateCartNameTotalPrice(cartReport.cart_name, cartReport.cart_price);
                            }
                        }

                        reportAdapter.notifyDataSetChanged();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                        updateTotalProfit();
                    }
                });
    }

    private void updateCartNameTotalPrice(String cartName, String price) {
        double totalPrice = cartNameTotalPriceMap.getOrDefault(cartName, 0.0);
        totalPrice += Double.parseDouble(price);
        cartNameTotalPriceMap.put(cartName, totalPrice);
    }

    private void updateCartNameCount(String cartName) {
        cartNameCountMap.put(cartName, cartNameCountMap.getOrDefault(cartName, 0) + 1);
    }

    private void updateCartNameQuantity(String cartName, long quantity) {
        cartNameQuantityMap.put(cartName, cartNameQuantityMap.getOrDefault(cartName, 0L) + quantity);
    }

    private void updateTotalProfit() {
        double totalProfitAmount = 0;
        StringBuilder cartNamesStringBuilder = new StringBuilder();

        for (Map.Entry<String, Double> entry : cartNameTotalPriceMap.entrySet()) {
            String cartName = entry.getKey();
            double totalPrice = entry.getValue();
            long totalQuantity = cartNameQuantityMap.getOrDefault(cartName, 0L);

            CartReport cartReport = findCartReportByCartName(cartName);

            if (cartReport != null && "paid".equals(cartReport.getPayment_status())) {
                totalProfitAmount += totalPrice;

                cartNamesStringBuilder.append(cartName)
                        .append(" (Price: RM ").append(String.format("%.2f", totalPrice))
                        .append(", Quantity: ").append(totalQuantity).append(")\n\n");
            }
        }

        totalProfit.setText("Total Profit: RM " + String.format("%.2f", totalProfitAmount));
        cartCount.setText("Names:\n\n" + cartNamesStringBuilder.toString());
    }

    private CartReport findCartReportByCartName(String cartName) {
        for (CartReport cartReport : cartReportArrayList) {
            if (cartName.equals(cartReport.cart_name)) {
                return cartReport;
            }
        }
        return null;
    }


}
