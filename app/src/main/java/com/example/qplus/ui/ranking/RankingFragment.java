package com.example.qplus.ui.ranking;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.qplus.R;
import com.example.qplus.ui.ranking.Adapter.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RankingFragment extends Fragment {

    TextView today_date;
    TextView today_day;

    ViewPager vp;
    long now;
    Date date;
    SimpleDateFormat Format = new SimpleDateFormat("yyyy/M/dd (EE)");
    String time;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ranking, container, false);

        now = System.currentTimeMillis();
        date = Calendar.getInstance().getTime();
        time = Format.format(date);

        today_date = root.findViewById(R.id.today_date);

        today_date.setText(time);

        vp = root.findViewById(R.id.view_pager);
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager());
        vp.setAdapter(tabPagerAdapter);
        vp.setOffscreenPageLimit(2);
        vp.setSaveEnabled(false);

        TabLayout tabLayout = root.findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#24B651"));
        tabLayout.addTab(tabLayout.newTab().setText("유저"),0,true);
        tabLayout.addTab(tabLayout.newTab().setText("커뮤니티"),1);

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return root;
    }
}