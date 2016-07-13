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
import com.example.attracti.audiorecorderpicture.activities.ViewActivity;
import com.example.attracti.audiorecorderpicture.async.BitmapDownloadTask;
import com.example.attracti.audiorecorderpicture.interfaces.OnCreateCanvasListener;
import com.example.attracti.audiorecorderpicture.utils.Statics;

import java.io.IOException;

/**
 * Created by Iryna on 6/2/16.
 * <p>
 * this Fragment responsible for the showing of the picture of the certain project
 * this class is responsible for the playing of the audio, when label is pressed
 */


public class BitmapFragment extends Fragment {

    private final  String LOG_TAG = BitmapFragment.class.getSimpleName();
    OnCreateCanvasListener canvasListener = null;
    private static String BITMAP_TAG = "BITMAP_TAG";

    private String mFile;
    private ImageView imageView;
    private int positionCurrent;

    private GestureDetectorCompat DoubleTap;
    private MediaPlayer mPlayer;
    private ViewActivity viewActivity;

    private ViewGroup rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        canvasListener = (OnCreateCanvasListener) context;
        this.viewActivity = (ViewActivity) context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFile = getArguments().getString(BITMAP_TAG);
        Log.wtf("mFile in onCreate: ", mFile);
        positionCurrent = getArguments().getInt("INT");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        int x=0;
        int y=0;
        int update =0;
        loadBitmap(rootView,viewActivity, mFile, imageView, positionCurrent, x, y, update);

        DoubleTap = new GestureDetectorCompat(getActivity(), new MyGestureListener());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                DoubleTap.onTouchEvent(event);
                return false;
            }
        });

        return rootView;
    }

    public void loadBitmap(ViewGroup view, ViewActivity viewActivity, String path, ImageView imageView, int position, int x, int y, int update) {
        final String imageKey = String.valueOf(path);
        final Bitmap bitmap = null;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            ViewActivity activity = (ViewActivity)getActivity();
            BitmapDownloadTask task = new BitmapDownloadTask(view, viewActivity, canvasListener, imageView, position, x, y, update);
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
            String mFileName = Statics.mDiretoryName + "/" + viewActivity.getParentName()+"/"+viewActivity.getParentName() + ".3gp";
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.seekTo(viewActivity.getLabelList().get(i).getLabelTime());
            mPlayer.start();


            if (i < viewActivity.getLabelList().size() - 1) {
                new CountDownTimer(viewActivity.getLabelList().get(i + 1).getLabelTime() -  viewActivity.getLabelList().get(i).getLabelTime(), 1000) {
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

    public void updateBitmap(int x, int y, int update){
        mFile=getArguments().getString(BITMAP_TAG);
        update =1;
        loadBitmap(rootView, viewActivity, mFile,  imageView ,positionCurrent, x, y, update);
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        } else {

        }
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            int xlong = (int) e.getX();
            int ylong = (int) e.getY();

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xlong = (int) e.getX();
                    ylong = (int) e.getY();
            }
            for (int i = 0; i < viewActivity.getLabelList().size(); i++) {
                if (positionCurrent == (Integer.parseInt((String) viewActivity.getLabelList().get(i).getPictureName()))) {

                    int xfile = viewActivity.getLabelList().get(i).getxLabel();
                    int yfile = viewActivity.getLabelList().get(i).getyLabel();

                    if ((xlong < xfile * 0.68 + 30 && xlong > xfile * 0.68 - 30) && (ylong < yfile + 200 + 30 && ylong > yfile + 200 - 30)) {
                        startPlayingPictureLabel(i);
                    }
                }
            }

            return true;
        }
    }
}
