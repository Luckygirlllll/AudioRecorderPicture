package com.example.attracti.audiorecorderpicture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.LruCache;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Iryna on 5/25/16.
 * <p/>
 * This is ViewPager where  each item is a picture from the Camera
 */


public class ViewFragment extends Fragment {


    File[] listFile;
    File[] listFolders;

    ArrayList<Folder> FOLDERS = new ArrayList<>();
    public static LruCache<String, Bitmap> mMemoryCache;

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    public static ImageView imageView;

    private String TAG = CameraFragment.class.getSimpleName();

    static ArrayList<File> ArrayFilepaths;

    public static File labelFile;
    public static ArrayList<Integer> xcoordList = new ArrayList();
    public static ArrayList<Integer> ycoordList = new ArrayList();
    public static ArrayList<Integer> positionList = new ArrayList<>();

    static int x=0;
    static int y=0;
    int currentPosition;

    //---- Canvas
    Canvas tempCanvas;
    Bitmap tempBitmap;
    Paint myPaint;
    Paint textPaint;
    static int clicked = 1;
    static File gpxfile;

    public Context context;

    static ArrayList fileTime = new ArrayList();
    static ArrayList xfile = new ArrayList();
    static ArrayList yfile = new ArrayList();
    static ArrayList filePosition = new ArrayList();




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


    private GestureDetectorCompat DoubleTap;


    public void getFromSdcardFolders() {
        File file = new File(Environment.getExternalStorageDirectory() +
                "/Audio_Recorder_Picture", "Picture");
        if (file.isDirectory()) {
            listFolders = file.listFiles();
            for (int i = 0; i < listFolders.length; i++) {

                Folder folderobject = new Folder();
                folderobject.setName(listFolders[i].getName());

                File picturelist = new File(Environment.getExternalStorageDirectory() +
                        "/Audio_Recorder_Picture/Picture", listFolders[i].getName());
                if (picturelist.isDirectory()) {
                    listFile = picturelist.listFiles();
                    for (int j = 0; j < listFile.length; j++) {
                        folderobject.addFile(listFile[j].getAbsolutePath());
                    }
                }
                FOLDERS.add(folderobject);
                //   Log.wtf("TAG", "Folders size inside the getFRom:" + FOLDERS.size());
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.view_pager, container, false);
        mPager = (ViewPager) rootView.findViewById(R.id.pager_fragment);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        DoubleTap = new GestureDetectorCompat(getActivity(), new MyGestureListener());

        mPager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                DoubleTap.onTouchEvent(event);
                return false;
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

        tempBitmap = Bitmap.createBitmap(4000, 3000, Bitmap.Config.RGB_565);
        tempCanvas = new
                Canvas(tempBitmap);
        return rootView;
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("...", "onLongPress works");
            Log.i("Position: ", String.valueOf(mPager.getCurrentItem()));
             x = (int) e.getX();
             y = (int) e.getY();

            int pos = mPager.getCurrentItem();
                Log.wtf("imageView==null ", String.valueOf(imageView == null));
        //    imageView.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
        //    ChildFragment.loadBitmap(ArrayFilepaths.get(pos).getPath(), imageView, mPager.getCurrentItem(), x, y);



            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.wtf("X ", "in Long Press" + x);
                    Log.wtf("Y ", "in Long Press" + y);

                    currentPosition = mPager.getCurrentItem();

                    xcoordList.add(x);
                    ycoordList.add(y);
                    positionList.add(mPager.getCurrentItem());

                    Log.i("Events X: ", +x + " Events Y: " + y);
                    long after = System.currentTimeMillis();
                    int difference = (int) (after - AudioRecord.startTimeAudio);
                    int sBody = difference;

                    if (!CameraFragment.mLabelsDirectory.exists() && !CameraFragment.mLabelsDirectory.mkdirs()) {
                        CameraFragment.mLabelsDirectory = null;
                    } else {

                        String labelFileName = FirstscreenActivity.mCurrentProject + ".txt";
                        if (labelFile == null) {
                            labelFile = new File(CameraFragment.mLabelsDirectory, labelFileName);
                        } else {

                            FileWriter writer = null;
                            try {
                                writer = new FileWriter(labelFile, true);
                                Log.i("Time, X, Y", "Time:" + sBody + " X:" + x + "\n" + "Y" + y + "\n");
                                writer.append(mPager.getCurrentItem() + "\n" + sBody + "\n" + x + "\n" + y + "\n");
                                writer.flush();
                                writer.close();
                            } catch (IOException f) {
                                f.printStackTrace();
                            }
                            readFromFile();
                        }
                    }
            }
        }


        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("...", "onDown works");
            int xlong = (int) e.getX();
            int ylong = (int) e.getY();
//            Log.wtf("Coordinates of xlong: ", String.valueOf(xlong));
//            Log.wtf("Coordinates of ylong: ", String.valueOf(ylong));

            ArrayList xcoordin = CameraFragment.getXcoordin();
            ArrayList ycoordin = CameraFragment.getYcoordin();


            for (int i = 0; i < xcoordin.size(); i++) {
//
                int xfile = Integer.parseInt((String) xcoordin.get(i));
                int yfile = Integer.parseInt((String) ycoordin.get(i));

                if ((xlong < xfile + 50 && xlong > xfile - 50) && (ylong < yfile + 50 && ylong > yfile - 50)) {
                    //   audioRecord.startPlayingPictureLabel(i);
                    Log.i("Index i of the label", String.valueOf(i));

                    myPaint.setColor(Color.BLUE);
                    tempCanvas.save();
                    tempCanvas.rotate(-90, xfile * 6, yfile * 6);
                    textPaint.setTextSize(140);

                    textPaint.setColor(Color.WHITE);
                    textPaint.setAntiAlias(true);
                    textPaint.setTextAlign(Paint.Align.CENTER);

                    myPaint.setAntiAlias(true);
                    Rect bounds = new Rect();
                    textPaint.getTextBounds(String.valueOf(i + 1), 0, String.valueOf(i + 1).length(), bounds);
                    if (i + 1 < 10 && i + 1 > 1) {
                        tempCanvas.drawCircle(xfile * 6, yfile * 6 - (bounds.height() / 2), bounds.width() + 70, myPaint);
                    } else if (i + 1 == 1) {
                        tempCanvas.drawCircle(xfile * 6, yfile * 6 - (bounds.height() / 2), bounds.width() + 95, myPaint);
                    } else {
                        tempCanvas.drawCircle(xfile * 6, yfile * 6 - (bounds.height() / 2), bounds.width() + 10, myPaint);
                    }
                    ;

                    tempCanvas.drawText(String.valueOf(i + 1), xfile * 6, yfile * 6, textPaint);
                    tempCanvas.restore();
                    //list.invalidate();
                }
            }
            return true;
        }
    }


    public void updateArray(ArrayList<File> ArrayFilepaths) {
        this.ArrayFilepaths = ArrayFilepaths;
        for (int i = 0; i < this.ArrayFilepaths.size(); i++) {
            Log.i("Array new ", String.valueOf(ArrayFilepaths.size()));
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

        @Override
        public Fragment getItem(int position) {
            return ChildFragment.createfragment(context ,ArrayFilepaths.get(position).getPath(), position, x, y);
        }

        @Override
        public int getCount() {
            return ArrayFilepaths.size();
        }
    }
}


