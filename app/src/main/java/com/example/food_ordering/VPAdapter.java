package com.example.food_ordering;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import java.util.List;

public class VPAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitles = new ArrayList<>();

    public VPAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public VPAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments) {
        super(fragmentActivity);
        fragmentList.addAll(fragments);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitles.add(title);
    }

    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    public String getFragmentTitle(int position) {
        return fragmentTitles.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
