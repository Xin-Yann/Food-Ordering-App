package com.example.food_ordering;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class Admin_Order_history_n_upcoming extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_order_history_n_upcoming);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager2 viewPager = findViewById(R.id.viewpager);

        // Create a list of fragments
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Admin_UpcomingOrdersFragment());
        fragments.add(new Admin_OrderHistoryFragment());

        // Create a new instance of the VPAdapter class
        VPAdapter vpAdapter = new VPAdapter(this, fragments);

        viewPager.setAdapter(vpAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Upcoming Order");
            } else if (position == 1) {
                tab.setText("Order History");
            }
        }).attach();
    }
}
