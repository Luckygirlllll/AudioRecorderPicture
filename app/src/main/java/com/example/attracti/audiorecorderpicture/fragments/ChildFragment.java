package com.example.attracti.audiorecorderpicture.fragments;

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

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.activities.AudioRecord;
import com.example.attracti.audiorecorderpicture.async.ChildDownloadTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Iryna on 6/2/16.
 * <p/>
 * this Fragment responsible for the showing of the picture which just have been captured
 */


public class ChildFragment extends Fragment {

    private static String TAG = ChildFragment.class.getSimpleName();
    private static String BITMAP_TAG = "BITMAP_TAG";
    private int positionCurrent;

    private String mFile = null;
    private ImageView imageView;

    private GestureDetectorCompat DoubleTap;
    private Context context;

    private File labelFile;

    private ArrayList<Integer> xcoordList = new ArrayList();
    private ArrayList<Integer> ycoordList = new ArrayList();
    private ArrayList<Integer> positionList = new ArrayList<>();

    private AudioRecord activity;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        this.activity = (AudioRecord) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFile = getArguments().getString(BITMAP_TAG);
        positionCurrent = getArguments().getInt("INT");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        loadBitmap(mFile, imageView, positionCurrent, xcoordList, ycoordList, positionList);

        DoubleTap = new GestureDetectorCompat(getActivity(), new MyGestureListener());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                DoubleTap.onTouchEvent(event);
                return false;
            }
        });

        return rootView;
    }

    public static void loadBitmap(String path, ImageView imageView, int position, ArrayList xcoordList, ArrayList ycoordList, ArrayList positionList) {
        final String imageKey = String.valueOf(path);

        final Bitmap bitmap = null;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            ChildDownloadTask task = new ChildDownloadTask(imageView, position, xcoordList, ycoordList, positionList);
            task.execute(imageKey);
        }
    }


    public static ChildFragment createfragment(Context context, String file, int position) {
        ChildFragment fragment = new ChildFragment();
        Bundle args = new Bundle();
        args.putString(BITMAP_TAG, file);
        args.putInt("INT", position);
        fragment.setArguments(args);
        return fragment;
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int x = (int) e.getX();
                    int y = (int) e.getY();

                    xcoordList.add(x);
                    ycoordList.add(y);
                    positionList.add(positionCurrent);

                    loadBitmap(mFile, imageView, positionCurrent, xcoordList, ycoordList, positionList);

                    long after = System.currentTimeMillis();
                    int difference = (int) (after - activity.getStartTimeAudio());
                    int sBody = difference;
                    String labelFileName = activity.getmCurrentProject() + ".txt";
                    if (!activity.getmLabelsDirectory().exists() && !activity.getmLabelsDirectory().mkdirs()) {
                        //   activity.getmLabelsDirectory() = null;

                    } else {
                        labelFile = new File(activity.getmLabelsDirectory(), labelFileName);
                        FileWriter writer = null;
                        try {
                            writer = new FileWriter(labelFile, true);
                            writer.append(positionCurrent + "\t" + sBody + "\t" + x + "\t" + y + "\n");
                            writer.flush();
                            writer.close();

                        } catch (IOException f) {
                            f.printStackTrace();
                        }
                    }
            }

            return true;
        }

        public void onLongPress(MotionEvent e) {
            Log.wtf("onLongPress ", "works!");
            //todo: delete labels while made a onLongPress
        }
    }
}


