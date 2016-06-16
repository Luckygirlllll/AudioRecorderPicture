package com.example.attracti.audiorecorderpicture.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.interfaces.OnSwipePictureListener;
import com.example.attracti.audiorecorderpicture.model.CustomViewPagerH;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Iryna on 5/25/16.
 * <p>
 * This is ViewPager where  each item is a picture from the Camera
 */


public class ViewFragment extends Fragment implements OnSwipePictureListener {


    private static LruCache<String, Bitmap> mMemoryCache;

    private static CustomViewPagerH mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private static ImageView imageView;

    private String TAG = CameraFragment.class.getSimpleName();

    public static ArrayList<File> arrayFilepaths;

    private Context context;

    public static File labelFile;

    // -- you can delete it soon
    private static int x = 0;
    private static int y = 0;
    // -------

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("arraylist", arrayFilepaths);
        Log.d("works", "onSaveInstanceState");
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
            Log.i("Array new ", String.valueOf(ArrayFilepaths.size()));
            Log.wtf("Array item: ", String.valueOf(ArrayFilepaths.get(i)));
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
            return ChildFragment.createfragment(context, arrayFilepaths.get(position).getPath(), position, x, y);
        }

        @Override
        public int getCount() {
            return arrayFilepaths.size();
        }
    }
}


