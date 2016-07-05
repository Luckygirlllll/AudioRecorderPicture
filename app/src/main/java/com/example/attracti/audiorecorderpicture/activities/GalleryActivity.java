package com.example.attracti.audiorecorderpicture.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.fragments.GalleryFragment;
import com.example.attracti.audiorecorderpicture.fragments.GoogleDriveFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iryna on 6/30/16.
 * <p>
 * This is the class which displays pictures from the folders of the user phone
 */

public class GalleryActivity extends AppCompatActivity  {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_view);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.statusBarColor));

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.wtf("Position tab", String.valueOf(position));
                Log.wtf("viewPager position 1: ", String.valueOf(viewPager.getCurrentItem()));
                viewPager.setCurrentItem(position);
                Log.wtf("viewPager position 2: ", String.valueOf(viewPager.getCurrentItem()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment(), "Gallery");
        adapter.addFragment(new GoogleDriveFragment(), "Google Drive");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                onBackPressed();
                return super.onOptionsItemSelected(item);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
