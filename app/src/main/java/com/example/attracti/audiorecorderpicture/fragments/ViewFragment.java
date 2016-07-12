package com.example.attracti.audiorecorderpicture.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.activities.AudioRecord;
import com.example.attracti.audiorecorderpicture.interfaces.OnSwipePictureListener;
import com.example.attracti.audiorecorderpicture.widgets.CustomViewPagerH;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Iryna on 5/25/16.
 * <p>
 * This is ViewPager where  each item is a picture from the Camera
 */


public class ViewFragment extends Fragment implements OnSwipePictureListener {

    private static final String TAG = CameraFragment.class.getSimpleName();

    private LruCache<String, Bitmap> mMemoryCache;

    private CustomViewPagerH mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private ImageView imageView;

    private ArrayList<File> arrayFilepaths;

    private Context context;

    public ArrayList<File> getArrayFilepaths() {
        return arrayFilepaths;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("arraylist", arrayFilepaths);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            arrayFilepaths = (ArrayList<File>) savedInstanceState.getSerializable("arraylist");
        }
    }

    //// TODO: 6/15/16 add programmatically swipe to the last picture

    public void scrolToLast() {
        //   mPager.setCurrentItem(mPagerAdapter.getCount()+1);
//        mPagerAdapter.notifyDataSetChanged();
//        mPager.post(new Runnable() {
//            @Override
//            public void run() {
//                mPager.setCurrentItem(mPagerAdapter.getCount()-1);
//
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.view_pager, container, false);


        mPager = (CustomViewPagerH) rootView.findViewById(R.id.pager_fragment);
        mPager.setPagingEnabled(false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(arrayFilepaths.size()==0){
                    AudioRecord.pictureCounter.setText(position + " из " + arrayFilepaths.size());
                }
                else {
                    int realPosition = position + 1;
                    AudioRecord.pictureCounter.setText(realPosition + " из " + arrayFilepaths.size());
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        mMemoryCache
                = new LruCache<String, Bitmap>(cacheSize)

        {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };


        return rootView;
    }

    @Override
    public void next() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
    }

    @Override
    public void previous() {
        mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);
    }


    public void updateArray(ArrayList<File> ArrayFilepaths) {
        this.arrayFilepaths = ArrayFilepaths;
        for (int i = 0; i < this.arrayFilepaths.size(); i++) {
        }
        if (mPagerAdapter != null) {
            mPagerAdapter.notifyDataSetChanged();
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public ArrayList positionArr = new ArrayList();


        @Override
        public Fragment getItem(int position) {
            positionArr.add(position);
            return ChildFragment.createfragment(context, arrayFilepaths.get(position).getPath(), position);
        }

        @Override
        public int getCount() {
             if(arrayFilepaths!=null) {
                 return arrayFilepaths.size();
             }
            else return 0;
        }
    }
}


