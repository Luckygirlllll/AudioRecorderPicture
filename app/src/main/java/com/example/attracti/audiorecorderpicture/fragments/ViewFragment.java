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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Iryna on 5/25/16.
 * <p>
 * This is ViewPager where  each item is a picture from the Camera
 */


public class ViewFragment extends Fragment implements OnSwipePictureListener {


    public static LruCache<String, Bitmap> mMemoryCache;

    public static CustomViewPagerH mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    public static ImageView imageView;

    private String TAG = CameraFragment.class.getSimpleName();

    public static ArrayList<File> ArrayFilepaths;

    public Context context;

    public static File labelFile;

    // -- you can delete it soon
    static int x = 0;
    static int y = 0;
    static ArrayList fileTime = new ArrayList();
    static ArrayList xfile = new ArrayList();
    static ArrayList yfile = new ArrayList();
    static ArrayList filePosition = new ArrayList();
    // -------

    public static boolean pageChanged = false;

    ArrayList items = new ArrayList();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("arraylist", ArrayFilepaths);
        Log.d("works", "onSaveInstanceState");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            ArrayFilepaths = (ArrayList<File>) savedInstanceState.getSerializable("arraylist");
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
        this.ArrayFilepaths = ArrayFilepaths;
        for (int i = 0; i < this.ArrayFilepaths.size(); i++) {
            Log.i("Array new ", String.valueOf(ArrayFilepaths.size()));
            Log.wtf("Array item: ", String.valueOf(ArrayFilepaths.get(i)));
        }
        if (mPagerAdapter != null) {
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    public void readFromFile() {
        Log.i("reading from File", "in View Activity");
        StringBuilder text = new StringBuilder();
        String parentName = ArrayFilepaths.get(0).getParentFile().getName();

        try {
            File labelsFile = new File(CameraFragment.mLabelsFolder, parentName + ".txt");
            BufferedReader br = new BufferedReader(new FileReader(labelsFile));
            String line;

            while ((line = br.readLine()) != null) {

                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("TextInfo", String.valueOf(text));

        String[] filetime2 = text.toString().split("\n");

        for (int i = 0; i < filetime2.length; i = i + 4) {
            Log.i("FILE", "Position: " + filetime2[i]);
            String n = filetime2[i];
            filePosition.add(filetime2[i]);
            Log.i("FILE", "filePosition size: " + String.valueOf(fileTime.size()));
        }

        for (int i = 1; i < filetime2.length; i = i + 4) {
            Log.i("FileTime elements: ", filetime2[i]);
            String n = filetime2[i];
            fileTime.add(filetime2[i]);
            Log.i("FILE", "FileTime size: " + String.valueOf(fileTime.size()));
        }
        for (int i = 2; i < filetime2.length; i = i + 4) {
            Log.i("FILE", "Coordinates of X: " + filetime2[i]);
            String n = filetime2[i];
            xfile.add(filetime2[i]);
        }

        for (int i = 3; i < filetime2.length; i = i + 4) {
            Log.i("FILE", "Coordinates of Y: " + filetime2[i]);
            String n = filetime2[i];
            yfile.add(filetime2[i]);
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
            return ChildFragment.createfragment(context, ArrayFilepaths.get(position).getPath(), position, x, y);
        }

        @Override
        public int getCount() {
            return ArrayFilepaths.size();
        }
    }
}


