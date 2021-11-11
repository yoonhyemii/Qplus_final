package com.example.qplus.ui.comunity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CommnunityTabAdapter extends FragmentStatePagerAdapter {

    public CommnunityTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                NewsFragment newsFragment = new NewsFragment();
                return newsFragment;
            case 1:
                VolunteerFragment volunteerFragment = new VolunteerFragment();
                return volunteerFragment;
            case 2:
                SortingFragment sortingFragment = new SortingFragment();
                return sortingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
