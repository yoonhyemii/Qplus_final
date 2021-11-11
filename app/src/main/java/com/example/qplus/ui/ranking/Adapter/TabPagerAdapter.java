package com.example.qplus.ui.ranking.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.qplus.ui.ranking.PostRankingFragment;
import com.example.qplus.ui.ranking.UserRankingFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                return new UserRankingFragment();
            case 1:
                return new PostRankingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


}

