package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.tabs.TabLayout;

public class Order_history_n_upcoming extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history_n_upcoming);

        // Set UpcomingOrdersFragment as the default fragment
        Fragment defaultFragment = new UpcomingOrdersFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.upcomingFragmentContainer, defaultFragment);
        transaction.commit();

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        // Show Upcoming Orders
                        selectedFragment = new UpcomingOrdersFragment();
                        break;
                    case 1:
                        // Show Order History
                        selectedFragment = new OrderHistoryFragment();
                        break;
                }

                if (selectedFragment != null) {
                    // Replace the fragment using the correct container IDs
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.upcomingFragmentContainer, selectedFragment);
                    transaction.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle unselected tabs if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle reselected tabs if needed
            }
        });
        // Get the ImageButton
        View backBtn = findViewById(R.id.backBtn);

        // Set an OnClickListener to navigate to Admin_home when the ImageButton is clicked
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Admin_home activity
                startActivity(new Intent(Order_history_n_upcoming.this, MainActivity.class));
            }
        });
    }
}
