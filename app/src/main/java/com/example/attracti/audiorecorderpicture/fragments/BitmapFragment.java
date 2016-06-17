package com.example.attracti.audiorecorderpicture.fragments;

import android.content.Context;
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

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.Statics;
import com.example.attracti.audiorecorderpicture.activities.ViewActivity;
import com.example.attracti.audiorecorderpicture.async.BitmapDownloadTask;
import com.example.attracti.audiorecorderpicture.interfaces.OnCreateCanvasListener;

import java.io.IOException;

/**
 * Created by Iryna on 6/2/16.
 * <p>
 * this Fragment responsible for the showing of the picture of the certain project
 * this class is responsible for the playing of the audio, when label is pressed
 */


public class BitmapFragment extends Fragment {

    OnCreateCanvasListener canvasListener = null;

    private static String BITMAP_TAG = "BITMAP_TAG";

    private String mFile = null;
    private ImageView imageView;
    private int positionCurrent;

    private GestureDetectorCompat DoubleTap;
    private MediaPlayer mPlayer;
    private  ViewActivity viewActivity;


//    private static OnCreateCanvasListener canvasListener = new OnCreateCanvasListener() {
//        @Override
//        public void saveCanvas(Canvas canvas) {
//            Log.wtf("Lalala" , " SaveCanvas");
//        }
//    };



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        canvasListener = (OnCreateCanvasListener) context;
        this.viewActivity=(ViewActivity) context;

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
        loadBitmap(mFile, imageView, positionCurrent);

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

            BitmapDownloadTask task = new BitmapDownloadTask(viewActivity, canvasListener,imageView, position);
            task.execute(imageKey);

        }
    }

    public static BitmapFragment create(String file, int position) {
        BitmapFragment fragment = new BitmapFragment();
        Bundle args = new Bundle();
        args.putString(BITMAP_TAG, file);
        args.putInt("INT", position);
        fragment.setArguments(args);
        return fragment;
    }

    public void startPlayingPictureLabel(int i) {

        mPlayer = new MediaPlayer();
        try {
            String mFileName = Statics.mAudioFolder + "/" + viewActivity.getParentName() + ".3gp";
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.seekTo(Integer.parseInt((String) viewActivity.getFileTime().get(i)));
            mPlayer.start();

            Log.wtf("FileTime: ", String.valueOf(viewActivity.getFileTime().size()));
            Log.wtf("value of i: ", String.valueOf(i));
            Log.wtf("Start playing time: ", (String) viewActivity.getFileTime().get(i));


            if (i < viewActivity.getFileTime().size() - 1) {
                Log.wtf("Stop playing time: ", (String) viewActivity.getFileTime().get(i + 1));
                new CountDownTimer(Integer.parseInt((String) viewActivity.getFileTime().get(i + 1)) - Integer.parseInt((String) viewActivity.getFileTime().get(i)), 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        stopPlaying();
                    }
                }.start();
            } else {
                Log.wtf("Stop playing time in else: ", (String)  viewActivity.getFileTime().get(i) + 10000);
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
            for (int i = 0; i < viewActivity.getFilePosition().size(); i++) {
                if (positionCurrent == (Integer.parseInt((String) viewActivity.getFilePosition().get(i)))) {

                    int xfile = Integer.parseInt((String) viewActivity.getxFile().get(i));
                    int yfile = Integer.parseInt((String) viewActivity.getyFile().get(i));

                    if ((xlong < xfile * 0.68 + 30 && xlong > xfile * 0.68 - 30) && (ylong < yfile + 200 + 30 && ylong > yfile + 200 - 30)) {

                        Log.wtf("xFile: ", String.valueOf(xfile));
                        Log.wtf("yFile: ", String.valueOf(yfile));
                        startPlayingPictureLabel(i);
                        Log.i("Index i of the label", String.valueOf(i));

                    }
                }
            }

            return true;
        }
    }
}
