package com.example.food_ordering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
public class Admin_Order_history_n_upcoming extends AppCompatActivity{

    public static final String name="";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_order_history_n_upcoming);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);

        // Create a new instance of the VPAdapter class
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // Add the fragments to the VPAdapter object
        vpAdapter.addFragment(new Admin_UpcomingOrdersFragment(), "Upcoming Order");
        vpAdapter.addFragment(new Admin_OrderHistoryFragment(), "Order History" );

        // Set the adapter of the ViewPager to the VPAdapter object
        viewPager.setAdapter(vpAdapter);

        // Set the current item of the ViewPager to 0
        viewPager.setCurrentItem(0);

    }
}
