package com.example.attracti.audiorecorderpicture;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AudioRecord extends AppCompatActivity {

  //  private int i = 22;

    static long start;
    public static long getStart() {
        return start;
    }
    private long after;

    int current = 1;
    int currlabel = 1;

    static String[] filetime = new String[100];

    public String[] getFiletime() {
        return filetime;
    }

    public void setFiletime(String[] filetime) {
        this.filetime = filetime;
    }

    CanvasView mCanvasView;

    private static int labeltime;

    TakePictureListener takePictureListener;
    SavePictureListener savePictureListener;

    BufferedReader br = null;
    File xfile;

    static String info2;

    public String getInfo2() {
        return info2;
    }

    public static void setInfo2(String info2) {
        AudioRecord.info2 = info2;
    }

    private static final String LOG_TAG = "AudioRecord";
    private static String mFileName = null;


    private RecordButton recordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton playButton = null;

    private static MediaPlayer mPlayer = null;

    public MediaPlayer getmPlayer() {
        return mPlayer;
    }

    private LabelButton labelButton = null;
    private Button mLabelPlayButton = null;

    // go to the next label
    private Button mNextButton = null;
    private Button mPreviousButton = null;

    public static int timefile = 1;

    private static final int TAKE_PICTURE_REQUEST_B = 100;

    //------Camera features

    private Button mCaptureImageButton;
    private Button mSaveImageButton;

    CameraFragment fragment = new CameraFragment();
    static String[] filetime3;

    static ArrayList time = new ArrayList();


    private void initHeaderFragmet() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CameraFragment cameraActivity = new CameraFragment();
        fragmentTransaction.add(R.id.camera_frame2, cameraActivity);
        fragmentTransaction.commit();
        takePictureListener = cameraActivity;
        savePictureListener = cameraActivity;
    }

//------Camera features

    public int getTimefile() {
        return timefile;
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }

    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void onPlayLabel(boolean start) {
        if (start) {
            startPlayingmodified();
        } else {
            stopPlaying();
        }
    }

    private void onPlayNext(boolean start) {
        if (start) {
            startNextPlaying();
        } else {
            stopPlaying();
        }
    }

    private void onPlayPrevious(boolean start) {
        if (start) {
            startPreviousPlaying();
        } else {
            stopPlaying();
        }
    }

    public void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void startPlayingmodified() {

        time= fragment.getFiletime3();
        Log.i("Time 0", (String) time.get(0));
        Log.i("Time 1", (String) time.get(1));

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.seekTo(Integer.parseInt((String) time.get(0)));
            mPlayer.start();


            //playing the next label
            new CountDownTimer(Integer.parseInt( (String) time.get(1)) - Integer.parseInt((String) time.get(0)), 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    stopPlaying();
                }
            }.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPlayingPictureLabel(int i) {

        time= fragment.getFiletime3();
        Log.i("Time i", (String) time.get(i));
      //  Log.i("Time i+1", (String) time.get(i+1));

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.seekTo(Integer.parseInt((String) time.get(i)));
            mPlayer.start();

            if (i < time.size()-1) {

                new CountDownTimer(Integer.parseInt((String) time.get(i + 1)) - Integer.parseInt((String) time.get(i)), 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        stopPlaying();
                    }
                }.start();
            }
        else{
                new CountDownTimer(10000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        stopPlaying();
                    }
                }.start();
            }

            }catch(IOException e) {
            e.printStackTrace();

        }
    }








    private void startNextPlaying() {
        mPlayer = new MediaPlayer();
        time= fragment.getFiletime3();
        try {
            if (currlabel < time.size() - 1) {
                currlabel++;
            }
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.seekTo(Integer.parseInt((String) time.get(currlabel-1)));
            mPlayer.start();

            Log.i("CurrentLabel", String.valueOf(currlabel));

            new CountDownTimer(Integer.parseInt((String) time.get(currlabel)) - Integer.parseInt((String) time.get(currlabel - 1)), 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    stopPlaying();
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startPreviousPlaying() {
        mPlayer = new MediaPlayer();
        time= fragment.getFiletime3();
        try {

            if (currlabel > 1) {
                currlabel--;
            }
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.seekTo(Integer.parseInt((String) time.get(currlabel - 1)));
            mPlayer.start();
            Log.i("CurrentLabelPrevious", String.valueOf(currlabel));
            new CountDownTimer(Integer.parseInt((String) time.get(currlabel)) - Integer.parseInt((String) time.get(currlabel - 1)), 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    stopPlaying();
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }


//    public void readFromFile() {
//        StringBuilder text = new StringBuilder();
//
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(gpxfile));
//            String line;
//
//            while ((line = br.readLine()) != null) {
//
//                text.append(line);
//                text.append('\n');
//            }
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.i("TextInfo", String.valueOf(text));
//
//        filetime = text.toString().split("\n");
//        Arrays.sort(filetime);
//        for (int i = 0; i < filetime.length - 1; i++) {
//            Log.i("Sorted array 2", filetime[i]);
//        }
//    };

    public class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);

                start = System.currentTimeMillis();
                android.util.Log.i("Time Current ", " Time value in millisecinds " + start);

                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    public void test(){
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.lin_one);
        recordButton = new RecordButton(this);
        playButton = new PlayButton(this);
        labelButton = new LabelButton(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        linearLayout.addView(recordButton,params);
        linearLayout.addView(playButton,params);
        linearLayout.addView(labelButton,params);
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    public static class LabelButton extends Button {
        public LabelButton(Context ctx) {
            super(ctx);
            setText("Label");
        }
    }

    interface TakePictureListener {
        public void takePicture();
    }


    interface SavePictureListener {
        public void savePicture();
    }


    public AudioRecord() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

    private OnClickListener mCaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            initHeaderFragmet();
            takePictureListener.takePicture();
        }
    };

    private OnClickListener mSaveImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            savePictureListener.savePicture();
        }
    };


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);

        LinearLayout ll= (LinearLayout) findViewById(R.id.lin_three);
        mNextButton  =(Button) findViewById(R.id.test4);
        mPreviousButton=(Button) findViewById(R.id.test5);
        mLabelPlayButton=(Button) findViewById(R.id.test6);


        //-----Camera features
        mCaptureImageButton = (Button) findViewById(R.id.capture_image_button);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);

        findViewById(R.id.capture_image_button).setOnClickListener(mCaptureImageButtonClickListener);

        mSaveImageButton = (Button) findViewById(R.id.save_image_button);
        mSaveImageButton.setOnClickListener(mSaveImageButtonClickListener);
        mSaveImageButton.setEnabled(true);
        //----Camera features

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        test();
        labelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPlayer == null) {
                    after = System.currentTimeMillis();
                    android.util.Log.i("Time after click", " Time value in millisecinds " + after);
                    int difference = (int) (after - start);
                    Log.i("difference", String.valueOf(difference));


                    int sBody = difference;

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss",
                            Locale.getDefault());;
                    Date now = new Date();
                    String fileName = formatter.format(now) + ".txt";//like 2016_01_12.txt
                    Log.i("Current", String.valueOf(labeltime));

                    try {
                        File root = new File(Environment.getExternalStorageDirectory(), "Audio_Recorder");
                        if (!root.exists()) {
                            root.mkdirs();
                        }
                        xfile = new File(root, fileName);

                        FileWriter writer = new FileWriter(xfile, true);
                        writer.append(sBody + "\n");
                        writer.flush();
                        writer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //readFromFile();
                    mCanvasView.drawLine();

                } else {

                    info2 = String.valueOf(mPlayer.getCurrentPosition());
                    labeltime = mPlayer.getCurrentPosition();
                    String sBody = info2;

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_mm_dd_hh_mm",
                            Locale.getDefault());;
                    Date now = new Date();
                    String fileName = formatter.format(now) + ".txt";//like 2016_01_12.txt
                    Log.i("Current", String.valueOf(labeltime));

                    try {
                        File root = new File(Environment.getExternalStorageDirectory(), "Audio_Recorder");
                        if (!root.exists()) {
                            root.mkdirs();
                        }
                        xfile = new File(root, fileName);

                        FileWriter writer = new FileWriter(xfile, true);
                        writer.append(sBody + "\n");
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   // readFromFile();
                    mCanvasView.drawLine();
                }
            }
        });

        //play first label
        mLabelPlayButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                fragment.readFromFile();
                time= fragment.getFiletime3();
                boolean mStartPlaying = true;
                mCanvasView = new CanvasView(AudioRecord.this);
                onPlayLabel(mStartPlaying);
            }
        });


        mNextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean mStartPlaying = true;
//                StringBuilder text = new StringBuilder();
//
//                try {
//                    BufferedReader br = new BufferedReader(new FileReader(fragment.getGpxfile()));
//                    String line;
//
//                    while ((line = br.readLine()) != null) {
//
//                        text.append(line);
//                        text.append('\n');
//                    }
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Log.i("TextInfo", String.valueOf(text));

               // String[] filetime = text.toString().split("\n");

                time= fragment.getFiletime3();
                Log.i("Time size", String.valueOf(time.size()));
                if (current - 1 < time.size() - 1) {
                    current++;
                }
                Log.i("Time size", String.valueOf(time.size()));
                Log.i("Iinfo!!!!", String.valueOf(current));

//                mCanvasView = new CanvasView(AudioRecord.this);
//                mCanvasView.invalidate();
                onPlayNext(mStartPlaying);
            }
        });


        mPreviousButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean mStartPlaying = true;
                onPlayPrevious(mStartPlaying);
            }
        });

        mCanvasView = new CanvasView(AudioRecord.this);

        ll.addView(mCanvasView,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
