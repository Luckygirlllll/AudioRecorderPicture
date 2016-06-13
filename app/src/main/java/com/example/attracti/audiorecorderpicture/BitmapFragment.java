package com.example.attracti.audiorecorderpicture;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by Iryna on 6/2/16.
 * <p/>
 * this Fragment responsible for the showing of the picture of the certain project
 * this class is responsible for the playing of the audio, when label is pressed
 */


public class BitmapFragment extends Fragment {

    public static String BITMAP_TAG = "BITMAP_TAG";

    String file = null;
    ImageView imageView;
    private  int positionCurrent;

    private GestureDetectorCompat DoubleTap;

    MediaPlayer mPlayer;

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
        loadBitmap(file, imageView, positionCurrent);

        DoubleTap = new GestureDetectorCompat(getActivity(), new MyGestureListener());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                DoubleTap.onTouchEvent(event);
                return false;
            }
        });

        return rootView;
    }

    public void loadBitmap(String path, ImageView imageView, int position) {
        final String imageKey = String.valueOf(path);
        Log.wtf("Image Key: ", String.valueOf(imageKey));

        final Bitmap bitmap = null;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {

            BitmapDownloadTask task = new BitmapDownloadTask(imageView, position);
            task.execute(imageKey);

        }
    }

    public static BitmapFragment create(String file, int position) {
        BitmapFragment fragment = new BitmapFragment();
        Bundle args = new Bundle();
        args.putString(BITMAP_TAG, file);
        args.putInt("INT" , position);
        fragment.setArguments(args);
        return fragment;
    }

    public void startPlayingPictureLabel(int i) {
         Log.wtf("StartPlaying", "works!");

        MediaPlayer mPlayer = new MediaPlayer();
        try {
            String mFileName = CameraFragment.mAudioFolder + "/" + ViewActivity.parentName + ".3gp";
            Log.wtf("mFile: ", mFileName);
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.seekTo(Integer.parseInt((String) ViewActivity.fileTime.get(i)));
            mPlayer.start();

            Log.wtf("FileTime: ", String.valueOf(ViewActivity.fileTime.size()));
            Log.wtf("value of i: ", String.valueOf(i));

            if (i < ViewActivity.fileTime.size() - 1) {

                new CountDownTimer(Integer.parseInt((String) ViewActivity.fileTime.get(i + 1)) - Integer.parseInt((String) ViewActivity.fileTime.get(i)), 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        stopPlaying();
                    }
                }.start();
            } else {
                new CountDownTimer(10000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        stopPlaying();
                    }
                }.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        } else {
            Log.i("mPlayer is null", "Nothing to stop");
        }
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.wtf("onDown works", "in Bitmap Fragment");
            int xlong = (int) e.getX();
            int ylong = (int) e.getY();

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xlong = (int) e.getX();
                    ylong = (int) e.getY();

                    Log.wtf("xlong: ", String.valueOf(xlong));
                    Log.wtf("ylong: ", String.valueOf(ylong));

            }
 for (int i = 0; i < ViewActivity.filePosition.size(); i++) {
     if (positionCurrent == (Integer.parseInt((String) ViewActivity.filePosition.get(i)))) {

         int xfile = Integer.parseInt((String) ViewActivity.xfile.get(i));
         int yfile = Integer.parseInt((String) ViewActivity.yfile.get(i));

         if ((xlong < xfile*0.68 + 30 && xlong > xfile*0.68 - 30) && (ylong < yfile+200 + 30 && ylong > yfile+200 - 30)) {
             Log.wtf("Xlong: ", String.valueOf(xlong));
             Log.wtf("Ylong: ", String.valueOf(ylong));

             Log.wtf("Xlong 3/4: ", String.valueOf(xlong*(3/4)));
             Log.wtf("Ylong 3/4: ", String.valueOf(ylong*(3/4)));

             Log.wtf("xfile: ", String.valueOf(xfile));
             Log.wtf("yfile: ", String.valueOf(yfile));
             startPlayingPictureLabel(i);
             Log.i("Index i of the label", String.valueOf(i));
             Log.wtf("The label is identified", "!!!");
         }
     }
 }

            return true;
        }
    }
}
