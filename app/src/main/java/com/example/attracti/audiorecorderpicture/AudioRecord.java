package com.example.attracti.audiorecorderpicture;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * This is the class where going the process of audio recording. This activity has two fragments:
 * CameraFragment and RecyclerViewFragment
 */

public class AudioRecord extends AppCompatActivity {

    static ArrayList<String> bitmapppaths = new ArrayList();

    int firstslide =0;

    private static final int NUM_PAGES = 2;

    private CustomViewPagerH mPager;
    private PagerAdapter mPagerAdapter;


    static long startTimeAudio;

    private ArrayList<String> imagesPathList;

    public static long getStart() {
        return startTimeAudio;
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
    ReceivePictureListener receivePictureListener;

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

    Button recordButtonpause;
    Button chooseButton;
    Toolbar myToolbar;

    static long timePictureChange;

    private void initHeaderFragmet() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CameraFragment cameraActivity = new CameraFragment();
        fragmentTransaction.add(R.id.camera_frame2, cameraActivity);
        fragmentTransaction.commit();
        //takePictureListener = cameraActivity;
       // savePictureListener = cameraActivity;
       // receivePictureListener = cameraActivity;
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

        time = fragment.getFiletime3();
        Log.i("Time 0", (String) time.get(0));
        Log.i("Time 1", (String) time.get(1));

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.seekTo(Integer.parseInt((String) time.get(0)));
            mPlayer.start();

            //playing the next label
            new CountDownTimer(Integer.parseInt((String) time.get(1)) - Integer.parseInt((String) time.get(0)), 1000) {
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

        time = fragment.getFiletime3();
        Log.i("Time i", (String) time.get(i));
        //  Log.i("Time i+1", (String) time.get(i+1));

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.seekTo(Integer.parseInt((String) time.get(i)));
            mPlayer.start();

            if (i < time.size() - 1) {

                new CountDownTimer(Integer.parseInt((String) time.get(i + 1)) - Integer.parseInt((String) time.get(i)), 1000) {
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


    private void startNextPlaying() {
        mPlayer = new MediaPlayer();
        time = fragment.getFiletime3();
        try {
            if (currlabel < time.size() - 1) {
                currlabel++;
            }
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.seekTo(Integer.parseInt((String) time.get(currlabel - 1)));
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
        time = fragment.getFiletime3();
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
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        } else {
            Log.i("mPlayer is null", "Nothing to stop");
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        if (!CameraFragment.mAudioDirectory.exists() && !CameraFragment.mAudioDirectory.mkdirs()) {
            CameraFragment.mAudioDirectory = null;
        }
        else {
            mRecorder.setOutputFile(mFileName);
        }

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

    public static void setArrayBitmap(ArrayList<String> list) {
        bitmapppaths=list;
    }

    public static ArrayList<String> getArratBitmap(){
        return bitmapppaths;
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
//boolean mStartRecording = true;
//
//    OnClickListener clicker2 = new OnClickListener() {
//        public void onClick(View v) {
//            onRecord(mStartRecording);
//            start = System.currentTimeMillis();
//
//            android.util.Log.i("Time Current ", " Time value in millisecinds " + start);
//
//            if (mStartRecording) {
////                setText("Stop recording");
//            } else {
////                setText("Start recording");
//            }
//            mStartRecording = !mStartRecording;
//        }
//


    public class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);

                startTimeAudio = System.currentTimeMillis();
                android.util.Log.i("Time Current ", " Time value in millisecinds " + startTimeAudio);

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

    public void test() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lin_one);
        recordButton = new RecordButton(this);
        playButton = new PlayButton(this);
        labelButton = new LabelButton(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        if (linearLayout!=null) {
            linearLayout.addView(recordButton, params);
            linearLayout.addView(playButton, params);
            linearLayout.addView(labelButton, params);
        }
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
        mFileName =CameraFragment.mAudioFolder+"/"+FirstscreenActivity.mCurrentProject+".3gp";
    }


    static int clicked =0;


    private OnClickListener mCaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.wtf("bitmap", "First listener");

                mCaptureImageButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (firstslide==0) {
                            firstslide++;
                            if (fragment!=null) {
                                fragment.takePicture();
                                if (clicked==0){
                                    clicked++;
                                }
                                else {

                                   timePictureChange = System.currentTimeMillis();
                                    long sBody;
                                    sBody = timePictureChange-startTimeAudio;
                                    FileWriter writer = null;
                                    if (!CameraFragment.mLabelsDirectory.exists() && !CameraFragment.mLabelsDirectory.mkdirs()) {
                                        CameraFragment.mLabelsDirectory = null;
                                    } else {
                                        FileWriter writer2 = null;
                                        try {
                                            if(MyAdapter2.labelFile==null){
                                                String labelFileName = FirstscreenActivity.mCurrentProject + ".txt";
                                                MyAdapter2.labelFile = new File(CameraFragment.mLabelsDirectory, labelFileName);
                                                writer2 = new FileWriter(MyAdapter2.labelFile, true);
                                            }
                                            else {
                                                writer2 = new FileWriter(MyAdapter2.labelFile, true);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            writer2.append(MyAdapter2.position + 1 + "\n" + sBody + "\n" + 0 + "\n" + 0 + "\n");
                                            writer2.flush();
                                            writer2.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            }
                            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                            if (CameraFragment.bitmappaths!=null) {
                                updateCallback.update(CameraFragment.bitmappaths.size());
                                for (int i = 0; i < CameraFragment.bitmappaths.size(); i++) {
                                    Log.i("BITMAPPATHS", (String) CameraFragment.bitmappaths.get(i));
                                }
                            }
                        }
                        else{
                            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                            firstslide--;
                        }

                    }
                });
            }
    };

    boolean mStartRecording = true;
    private OnClickListener recordButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onRecord(mStartRecording);
            startTimeAudio = System.currentTimeMillis();

            android.util.Log.i("Time Current ", " Time value in milliseconds " + startTimeAudio);
            if (mStartRecording) {
                recordButtonpause.setBackgroundResource(R.drawable.pause_black);
                myToolbar.setBackgroundColor(Color.RED);


            } else {
                recordButtonpause.setBackgroundResource(R.drawable.mic_black);
                myToolbar.setBackgroundColor(Color.parseColor("#118b0a"));
            }
            mStartRecording = !mStartRecording;

        }
    };

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

    private OnClickListener chooseButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                extractPickedImages(requestCode, resultCode, data);
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            }
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


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

        LinearLayout ll = (LinearLayout) findViewById(R.id.lin_three);
        mNextButton = (Button) findViewById(R.id.test4);
        mPreviousButton = (Button) findViewById(R.id.test5);
        mLabelPlayButton = (Button) findViewById(R.id.test6);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        Log.wtf("TAG", String.valueOf("TOOLBAR: " + myToolbar == null));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

        //-----Camera features
        mCaptureImageButton = (Button) findViewById(R.id.capture_image);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);

        findViewById(R.id.capture_image).setOnClickListener(mCaptureImageButtonClickListener);

        recordButtonpause = (Button) findViewById(R.id.record_button);
        recordButtonpause.setOnClickListener(recordButtonListener);
        findViewById(R.id.record_button).setOnClickListener(recordButtonListener);

        chooseButton = (Button) findViewById(R.id.choose_button);
        chooseButton.setOnClickListener(chooseButtonListener);
        findViewById(R.id.choose_button).setOnClickListener(chooseButtonListener);

        mPager = (CustomViewPagerH) findViewById(R.id.pager);
        mPager.setPagingEnabled(false);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

//        chooseButton.setOnClickListener(chooseButtonListener);
//        findViewById(R.id.choose_button).setOnClickListener(chooseButtonListener);


//        mSaveImageButton = (Button) findViewById(R.id.save_image_button);
//        mSaveImageButton.setOnClickListener(mSaveImageButtonClickListener);
//        mSaveImageButton.setEnabled(true);
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
                    int difference = (int) (after - startTimeAudio);
                    Log.i("difference", String.valueOf(difference));

                    int sBody = difference;

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss",
                            Locale.getDefault());
                    ;
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
                            Locale.getDefault());
                    ;
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
                time = fragment.getFiletime3();
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

                time = fragment.getFiletime3();
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

    private void extractPickedImages(int requestCode, int resultCode, Intent data) {
        String realPath;

        try {
            // When an Image is picked
            if (requestCode == 1 && resultCode == Activity.RESULT_OK && null != data) {

                imagesPathList = new ArrayList<String>();

                if (data.getData() != null) {
                    if (Build.VERSION.SDK_INT < 11)
                        realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                        // SDK >= 11 && SDK < 19
                    else if (Build.VERSION.SDK_INT < 19)
                        realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                        // SDK > 19 (Android 4.4)
                    else
                        realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());


                    saveImageOnDevice(Build.VERSION.SDK_INT, data.getData().getPath(), realPath);

                    Log.e("TAG", "imageEncoded:" + realPath);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();

                            if (Build.VERSION.SDK_INT < 11)
                                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, uri);

                                // SDK >= 11 && SDK < 19
                            else if (Build.VERSION.SDK_INT < 19)
                                realPath = RealPathUtil.getRealPathFromURI_API11to18(this, uri);

                                // SDK > 19 (Android 4.4)
                            else
                                realPath = RealPathUtil.getRealPathFromURI_API19(this, uri);


                            saveImageOnDevice(Build.VERSION.SDK_INT, uri.getPath(), realPath);

                            Log.e("TAG", "imageEncoded:" + realPath);

                            imagesPathList.add(realPath);
                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }
    }

    private void saveImageOnDevice(int sdk, String uriPath, String realPath) {

        Uri uriFromPath = Uri.fromFile(new File(realPath));
        Uri photoUri = uriFromPath;

        Log.e("TAG", "photoUri: " + uriFromPath);

        // you have two ways to display selected image

        // ( 1 ) imageView.setImageURI(uriFromPath);

        // ( 2 ) imageView.setImageBitmap(bitmap);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
            //  cameraImage.setImageBitmap(bitmap);

            if (receivePictureListener != null) {
                receivePictureListener.recievePicture(bitmap);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        //save path photo fro current message
//        photoPath = uriFromPath.toString().substring(uriFromPath.toString().lastIndexOf("/") + 1, uriFromPath.toString().length());
//
//        //save photo drawable for current message to device storage
//        new FileStorage(getApplicationContext()).saveChatImageToFile(photoPath, bitmap);
//
//        //get photo drawable to current message
//        photo = new FileStorage(getApplicationContext()).getChatImageFromFile(photoPath);
//
//        Log.e("TAG", "Saving file: " + photoPath);
//
////        imageView.setImageBitmap(bitmap);
//
//        Log.d("HMKCODE", "Build.VERSION.SDK_INT:" + sdk);
//        Log.d("HMKCODE", "URI Path:" + uriPath);
//        Log.d("HMKCODE", "Real Path: " + realPath);
    }

    interface ReceivePictureListener {
        public void recievePicture(Bitmap bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.doneItem:
                Intent firstScreen = new Intent(getApplicationContext(), FirstscreenActivity.class);
                startActivity(firstScreen);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    CameraFragment fragment = new CameraFragment();
                    fragment.setActivityContext(AudioRecord.this);
                    return fragment;
//
                case 1:
                    RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
                    updateCallback = recyclerViewFragment;
                    return recyclerViewFragment;
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private UpdateRecyckerView updateCallback;
    public interface UpdateRecyckerView{
        void update(int position);
    }
}
