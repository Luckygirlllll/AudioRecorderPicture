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
import com.example.attracti.audiorecorderpicture.activities.ReadyRecordActivity;
import com.example.attracti.audiorecorderpicture.async.ReadyDownloadTask;
import com.example.attracti.audiorecorderpicture.interfaces.OnCreateCanvasListener;

/**
 * Created by Iryna on 6/2/16.
 * <p>
 * this Fragment responsible for the showing of the picture of the certain project
 * this class is responsible for the playing of the audio, when label is pressed
 */


public class ReadyViewFragment extends Fragment {

    private final  String LOG_TAG = ReadyViewFragment.class.getSimpleName();
    OnCreateCanvasListener canvasListener = null;
    private static String BITMAP_TAG = "BITMAP_TAG";

    private String mFile;
    private ImageView imageView;
    private int positionCurrent;

    private GestureDetectorCompat DoubleTap;
    private ReadyRecordActivity readyRecordActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        canvasListener = (OnCreateCanvasListener) context;
//        this.viewActivity = (ViewActivity) context;

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        int x=0;
        int y=0;
        int update =0;
        loadBitmap(readyRecordActivity, mFile, imageView, positionCurrent, x, y, update);

        DoubleTap = new GestureDetectorCompat(getActivity(), new MyGestureListener());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                DoubleTap.onTouchEvent(event);
                return false;
            }
        });

        return rootView;
    }

    public void loadBitmap(ReadyRecordActivity readyRecordActivity, String path, ImageView imageView, int position, int x, int y, int update) {
        final String imageKey = String.valueOf(path);
        final Bitmap bitmap = null;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            ReadyRecordActivity activity = (ReadyRecordActivity) getActivity();
            ReadyDownloadTask task = new ReadyDownloadTask(readyRecordActivity, canvasListener, imageView, position, x, y, update);
            task.execute(imageKey);
        }
    }

    public static ReadyViewFragment create(String file, int position) {
        ReadyViewFragment fragment = new ReadyViewFragment();
        Bundle args = new Bundle();
        args.putString(BITMAP_TAG, file);
        args.putInt("INT", position);
        fragment.setArguments(args);
        return fragment;
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

            Log.wtf("Touch listener ", "works!");

            return true;
        }
    }
}
