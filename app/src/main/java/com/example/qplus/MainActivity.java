package com.example.qplus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.qplus.ui.comunity.ComunityFragment;
import com.example.qplus.ui.home.HomeFragment;
import com.example.qplus.ui.market.MarketFragment;
import com.example.qplus.ui.ranking.RankingFragment;
import com.example.qplus.ui.user.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    Fragment fragment_home, fragment_ranking, fragment_market, fragment_comunity, fragment_user;
    BottomNavigationView navView;
    public static Context mainContext;
    private static final int GALLERY_IMG = 10;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContext = this;

        navView = findViewById(R.id.nav_view);
        fragment_home = new HomeFragment();
        fragment_ranking = new RankingFragment();
        fragment_market = new MarketFragment();
        fragment_comunity = new ComunityFragment();
        fragment_user = new UserFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment_home).commitAllowingStateLoss();

        //bottom
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment_home, getString(R.string.fragment_home)).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_ranking:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment_ranking, getString(R.string.fragment_ranking)).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_market:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment_market, getString(R.string.fragment_market)).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_comunity:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment_comunity, getString(R.string.fragment_comunity)).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_user:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment_user, getString(R.string.fragment_user)).commitAllowingStateLoss();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("fragment_home");
        ComunityFragment comunityFragment = (ComunityFragment) getSupportFragmentManager().findFragmentByTag("fragment_comunity");
        if(homeFragment!=null) {
            homeFragment.homerefresh();
        }else{
            Log.e("homeFragment","null");
        }

        if(homeFragment!=null) {
            //comunityFragment.communityrefresh();
        }else{
            Log.e("comunityFragment","null");
        }
    }

}