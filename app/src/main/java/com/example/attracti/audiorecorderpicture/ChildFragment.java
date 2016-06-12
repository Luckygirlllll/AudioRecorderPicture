package com.example.attracti.audiorecorderpicture;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Iryna on 6/2/16.
 *
 * this Fragment responsible for the showing of the picture which just have been captured (ViewFrgament)
 */


public class ChildFragment extends Fragment {

    public static String BITMAP_TAG = "BITMAP_TAG";
    private int positionCurrent;

    String file = null;
    ImageView imageView;
    private static int xCoord;
    private static int yCoord;

    private GestureDetectorCompat DoubleTap;

    public Context context;

    public static File labelFile;
    public static ArrayList<Integer> xcoordList = new ArrayList();
    public static ArrayList<Integer> ycoordList = new ArrayList();
    public static ArrayList<Integer> positionList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file = getArguments().getString(BITMAP_TAG);
        positionCurrent = getArguments().getInt("INT");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        loadBitmap(file, imageView, positionCurrent, xCoord, yCoord);

        DoubleTap = new GestureDetectorCompat(getActivity(), new MyGestureListener());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                DoubleTap.onTouchEvent(event);
                return false;
            }
        });

        return rootView;
    }

    public static void loadBitmap(String path, ImageView imageView, int position, int x, int y) {
        final String imageKey = String.valueOf(path);
        Log.wtf("Image Key: ", String.valueOf(imageKey));

        final Bitmap bitmap = null;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            ChildDownloadTask task = new ChildDownloadTask(imageView, position, x, y);
            task.execute(imageKey);
        }
    }


    public static ChildFragment createfragment(Context context, String file, int position, int x, int y) {
        xCoord = x;
        yCoord = y;
        ChildFragment fragment = new ChildFragment();
        Bundle args = new Bundle();
        args.putString(BITMAP_TAG, file);
        args.putInt("INT", position);
        fragment.setArguments(args);
        return fragment;
    }





    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {





        @Override
        public void onLongPress(MotionEvent e) {
           Log.wtf("Pre Long press", " works!!!");
            //    Log.i("Position: ", String.valueOf(mPager.getCurrentItem()));
//            int x = (int) e.getX();
//            int y = (int) e.getY();

           // Log.wtf("imageView==null ", String.valueOf(imageView == null));
            //    imageView.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
            //    ChildFragment.loadBitmap(ArrayFilepaths.get(pos).getPath(), imageView, mPager.getCurrentItem(), x, y);


            int downX=0;
            int downY=0;
            long downTime = 0;


            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = (int) e.getX();
                    downY=(int) e.getY();
                    Log.wtf("DownX", String.valueOf(downX));
                    Log.wtf("DownY", String.valueOf(downY));

                    downTime = Calendar.getInstance().getTimeInMillis();

                case MotionEvent.ACTION_UP:
                    int upX = (int) e.getX();
                    int upY =(int) e.getY();
                    long upTime = Calendar.getInstance().getTimeInMillis();
                    Log.wtf("UpX", String.valueOf(upX));
                    Log.wtf("UpY", String.valueOf(upY));

                    float deltaX = downX - upX;
                    float deltaTime = upTime - downTime;
                 //   Log.wtf("Delta Time", String.valueOf(deltaTime));

                  //  if ((deltaX == 0) && (deltaTime < 10) && ViewFragment.pageChanged==false) {
                    Log.wtf("pageChanged==false 1", String.valueOf(ViewFragment.pageChanged ==false));
                    if (ViewFragment.longpress==1) {
                        Log.wtf("Events ", "onLongPress works");
                      int x = (int) e.getX();
                      int y = (int) e.getY();

//                        switch (e.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    Log.wtf("X ", "in Long Press" + x);
//                    Log.wtf("Y ", "in Long Press" + y);

                        //   currentPosition = mPager.getCurrentItem();

                        xcoordList.add(x);
                        ycoordList.add(y);
                        positionList.add(positionCurrent);

                        loadBitmap(file, imageView, positionCurrent, x, y);

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
                                    writer.append(positionCurrent + "\n" + sBody + "\n" + x + "\n" + y + "\n");
                                    writer.flush();
                                    writer.close();
                                } catch (IOException f) {
                                    f.printStackTrace();
                                }
                            }
                        }
                    }
                    else {
                        ViewFragment.longpress=0;
                    }
            }
        }


    }
}
