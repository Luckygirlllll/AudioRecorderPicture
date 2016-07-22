package com.example.attracti.audiorecorderpicture.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.fragments.ReadyViewFragment;
import com.example.attracti.audiorecorderpicture.widgets.CustomViewPagerH;

import java.util.ArrayList;

/**
 * Created by Iryna on 7/8/16.
 * <p/>
 * This is class, where displays choosed images from the Gallery
 */


public class ReadyRecordActivity extends FragmentActivity {

    private CustomViewPagerH  mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;

    private ArrayList pictureList;
    private Window window;

    private TextView pictureCounter;
    private Button leftButton;
    private Button rightButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ready_view);
        Intent intent = getIntent();
        pictureList = (ArrayList) intent.getSerializableExtra("sortedItems");

        leftButton = (Button) findViewById(R.id.left_button);
        leftButton.setOnClickListener(leftButtonListener);

        rightButton = (Button) findViewById(R.id.right_button);
        rightButton.setOnClickListener(rightButtonListener);

        mPager = (CustomViewPagerH) findViewById(R.id.pager);
        mPager.setPagingEnabled(false);
        pictureCounter = (TextView) findViewById(R.id.picture_counter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int realPosition = position+1;
                    pictureCounter.setText(realPosition + " из "+pictureList.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        window = getWindow();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.statusBarRecordingColor));
        }
    }

    private View.OnClickListener leftButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);
        }
    };

    private View.OnClickListener rightButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
        }
    };



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
