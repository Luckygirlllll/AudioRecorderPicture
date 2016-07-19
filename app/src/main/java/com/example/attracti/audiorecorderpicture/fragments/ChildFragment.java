package com.example.attracti.audiorecorderpicture.fragments;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
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
import android.widget.RelativeLayout;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.activities.AudioRecordActivity;
import com.example.attracti.audiorecorderpicture.async.ChildDownloadTask;
import com.example.attracti.audiorecorderpicture.views.DrawView;

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
    private RelativeLayout layout;
    private ViewGroup rootView;

    private GestureDetectorCompat DoubleTap;
    private Context context;

    private File labelFile;

    private ArrayList<Integer> xcoordList = new ArrayList();
    private ArrayList<Integer> ycoordList = new ArrayList();
    private ArrayList<Integer> positionList = new ArrayList<>();
    private ArrayList<Integer> timeList = new ArrayList<>();
    private ArrayList<DrawView> drawViewList = new ArrayList<>();
    private ArrayList<DrawView> drawViewExtra = new ArrayList<>();


    private AudioRecordActivity activity;
    private RelativeLayout progressBar;

    private DrawView draw;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        this.activity = (AudioRecordActivity) context;
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
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide, container, false);

        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        loadBitmap(getActivity(), rootView, mFile, imageView, positionCurrent, xcoordList, ycoordList, positionList);

        DoubleTap = new GestureDetectorCompat(getActivity(), new MyGestureListener(context));

        imageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                DoubleTap.onTouchEvent(event);
                return false;
            }
        });

        return rootView;
    }

    public static void loadBitmap(Context context, ViewGroup view, String path, ImageView imageView, int position, ArrayList xcoordList, ArrayList ycoordList, ArrayList positionList) {
        final String imageKey = String.valueOf(path);

        final Bitmap bitmap = null;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            ChildDownloadTask task = new ChildDownloadTask(context, view, imageView, position, xcoordList, ycoordList, positionList);
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

    public void sendLayout(RelativeLayout progressBar) {
        this.progressBar = progressBar;
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        Context context;
        private CountDownTimer timer;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.wtf("Receive: ", action);
                if (action.equals("Finish")) {
                    timer.cancel();
                }
            }
        };

        public MyGestureListener(Context context) {
            this.context = context;
            IntentFilter intentFilter = new IntentFilter("Finish");
            context.registerReceiver(broadcastReceiver, intentFilter);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int x = (int) e.getX();
                    int y = (int) e.getY();

                    xcoordList.add(x);
                    ycoordList.add(y);
                    positionList.add(positionCurrent);

                    long after = System.currentTimeMillis();
                    int difference = (int) (after - activity.getStartTimeAudio());
                    int sBody = difference;
                    timeList.add(sBody);

                    draw = new DrawView(getActivity(), 1, String.valueOf(xcoordList.size()));
                    drawViewList.add(draw);
                    progressBar.addView(draw);
                    progressBar.invalidate();

                    drawViewList.get(drawViewList.size() - 1).animate().translationXBy(-360f).setDuration(1000);

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            timer = new CountDownTimer(System.currentTimeMillis() + 2000 - timeList.get(0), 1000) {
                                int j = 3;

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    // ------animation test
                                    final DrawView drawView = drawViewList.get(drawViewList.size() - 1);
                                    int currentLabel = timeList.get(timeList.size() - 1);
                                    // drawView.animate().translationXBy((-720f) / j).setDuration(1000);

                                    drawView.animate().translationXBy((-720f) / j).setDuration(1000).setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            j++;
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            drawView.animate().translationXBy((-720f) / j).setDuration(1000);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });


//                            drawViewList.get(drawViewList.size()-1).animate().translationXBy((-720f)/j).setDuration(1000);
                                    //       j++;
                                    //-------
//                            draw = new DrawView(getActivity(), j++, String.valueOf(xcoordList.size()));
//                            drawViewExtra.add(draw);
//                            if (drawViewExtra.size() > 1) {
//                                ((ViewGroup) drawViewExtra.get(drawViewExtra.size() - 2).getParent()).removeView(drawViewExtra.get(drawViewExtra.size() - 2));
//                            }
//                            progressBar.addView(drawViewExtra.get(drawViewExtra.size() - 1));
//                            progressBar.invalidate();
                                }

                                @Override
                                public void onFinish() {

                                }
                            }.start();
                        }
                    };

                    getActivity().runOnUiThread(runnable);

                    // new Thread(runnable).start();


                    loadBitmap(getActivity(), rootView, mFile, imageView, positionCurrent, xcoordList, ycoordList, positionList);

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


