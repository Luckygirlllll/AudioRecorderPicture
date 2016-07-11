package com.example.attracti.audiorecorderpicture.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.fragments.ReadyViewFragment;

import java.util.ArrayList;

/**
 * Created by Iryna on 7/8/16.
 * <p/>
 * This is class, where displays choosed images from the Gallery
 */


public class ReadyRecordActivity extends FragmentActivity {

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;

    private ArrayList pictureList;

    private Window window;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ready_view);
        Intent intent = getIntent();
        pictureList = (ArrayList) intent.getSerializableExtra("sortedItems");

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.statusBarRecordingColor));
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ReadyViewFragment fragment = ReadyViewFragment.create((String) pictureList.get(position), position);
            fragments.add(fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return pictureList.size();
        }

        public Fragment getFragment(int position) {
            return fragments.get(position);
        }
    }

}