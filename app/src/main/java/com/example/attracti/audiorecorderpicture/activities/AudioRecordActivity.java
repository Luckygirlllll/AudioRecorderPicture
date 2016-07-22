package com.example.attracti.audiorecorderpicture.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.fragments.CameraFragment;
import com.example.attracti.audiorecorderpicture.fragments.ViewFragment;
import com.example.attracti.audiorecorderpicture.interfaces.OnHeadlineSelectedListener;
import com.example.attracti.audiorecorderpicture.interfaces.OnSwipePictureListener;
import com.example.attracti.audiorecorderpicture.utils.RealPathUtil;
import com.example.attracti.audiorecorderpicture.utils.Statics;
import com.example.attracti.audiorecorderpicture.widgets.CustomViewPagerH;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This is the class where going the process of audio recording. This activity has two fragments:
 * CameraFragment and ViewFragment (ViewPager)
 */

public class AudioRecordActivity extends AppCompatActivity implements OnHeadlineSelectedListener {

    private final String LOG_TAG = AudioRecordActivity.class.getSimpleName();

    private OnSwipePictureListener onSwipePictureListener;
    private SavePictureListener savePictureListener;
    private ReceivePictureListener receivePictureListener;

    private int firstSlide = 0;
    private final int NUM_PAGES = 2;
    private CustomViewPagerH mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;

    private long startTimeAudio;
    private ArrayList<String> imagesPathList;

    private String mFileName = null;

    private MediaRecorder mRecorder = null;

    private MediaPlayer mPlayer = null;

    private PlayButton playButton = null;
    private LabelButton labelButton = null;
    private Button mLabelPlayButton = null;
    private Button mNextButton = null;
    private Button mPreviousButton = null;
    private Button mCaptureImageButton;
    private Button mSaveImageButton;
    private Button recordButtonpause;
    private Button leftButton;
    private Button rightButton;

    private View myToolbar;
    private Button backButton;
    private TextView doneView;
    private Chronometer chronometer;
    public static TextView pictureCounter;
    private RelativeLayout progressBar;

    private CameraFragment fragment;
    private ViewFragment viewFragment;

    private long timePictureChange;

    public int done = 0;

    private File labelFile;
    private String mCurrentProject;
    private File mPreviewDirectory;
    private File mPictureDirectory;
    private File mLabelsDirectory;
    private File mAudioDirectory;

    private Window window;

    private ArrayList<File> arrayFilepaths2 = new ArrayList<>();

    public File getmPictureDirectory() {
        return mPictureDirectory;
    }

    public long getStartTimeAudio() {
        return startTimeAudio;
    }

    public String getmCurrentProject() {
        return mCurrentProject;
    }

    public File getmLabelsDirectory() {
        return mLabelsDirectory;
    }

    public MediaPlayer getmPlayer() {
        return mPlayer;
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

//        mTime = fragment.getFileTime3();
//
//        mPlayer = new MediaPlayer();
//        try {
//            mPlayer.setDataSource(mFileName);
//            mPlayer.prepare();
//            mPlayer.seekTo(Integer.parseInt((String) mTime.get(0)));
//            mPlayer.start();
//
//            //playing the next label
//            new CountDownTimer(Integer.parseInt((String) mTime.get(1)) - Integer.parseInt((String) mTime.get(0)), 1000) {
//                public void onTick(long millisUntilFinished) {
//                }
//
//                public void onFinish() {
//                    stopPlaying();
//                }
//            }.start();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void startNextPlaying() {
//        mPlayer = new MediaPlayer();
//        mTime = fragment.getFileTime3();
//        try {
//            if (currlabel < mTime.size() - 1) {
//                currlabel++;
//            }
//            mPlayer.setDataSource(mFileName);
//            mPlayer.prepare();
//            mPlayer.seekTo(Integer.parseInt((String) mTime.get(currlabel - 1)));
//            mPlayer.start();
//
//            Log.i("CurrentLabel", String.valueOf(currlabel));
//
//            new CountDownTimer(Integer.parseInt((String) mTime.get(currlabel)) - Integer.parseInt((String) mTime.get(currlabel - 1)), 1000) {
//                public void onTick(long millisUntilFinished) {
//                }
//
//                public void onFinish() {
//                    stopPlaying();
//                }
//            }.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void startPreviousPlaying() {
//        mPlayer = new MediaPlayer();
//        mTime = fragment.getFileTime3();
//        try {
//
//            if (currlabel > 1) {
//                currlabel--;
//            }
//            mPlayer.setDataSource(mFileName);
//            mPlayer.prepare();
//            mPlayer.seekTo(Integer.parseInt((String) mTime.get(currlabel - 1)));
//            mPlayer.start();
//            Log.i("CurrentLabelPrevious", String.valueOf(currlabel));
//            new CountDownTimer(Integer.parseInt((String) mTime.get(currlabel)) - Integer.parseInt((String) mTime.get(currlabel - 1)), 1000) {
//                public void onTick(long millisUntilFinished) {
//                }
//
//                public void onFinish() {
//                    stopPlaying();
//                }
//            }.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        } else {
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        if (!mAudioDirectory.exists() && !mAudioDirectory.mkdirs()) {
            mAudioDirectory = null;
        } else {
            mFileName = Statics.mDiretoryName + "/" + mCurrentProject + "/" + mCurrentProject + ".3gp";
            mRecorder.setOutputFile(mFileName);
        }

        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
        }
        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }



    @Override
    public void onArticleSelected(ArrayList<File> arrayFilepaths) {
        this.arrayFilepaths2 = arrayFilepaths;
//        ViewFragment viewFragment = (ViewFragment)
//                getSupportFragmentManager().findFragmentById(R.id.pager_fragment);
        if (this.arrayFilepaths2 != null && viewFragment != null) {
            viewFragment.updateArray(arrayFilepaths2);

        }
        if ((ViewFragment) mPagerAdapter.getFragment(1) != null) {
            ((ViewFragment) mPagerAdapter.getFragment(1)).updateArray(arrayFilepaths);
            mPagerAdapter.notifyDataSetChanged();
        }
    }


    public class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                startTimeAudio = System.currentTimeMillis();

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

    public interface TakePictureListener {
        public void takePicture();
    }


    public interface SavePictureListener {
        public void savePicture();
    }

    private static int firstClick = 0;
    private int firstPressed = 0;


    private OnClickListener mCaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (firstSlide == 0) {
                firstSlide++;

                fragment.takePicture();

                if (firstPressed == 0) {
                    recordButtonpause.setBackgroundResource(R.drawable.record_red);
                    recordButtonpause.setEnabled(true);
                }

                firstPressed++;

                timePictureChange = System.currentTimeMillis();
                long sBody;
                sBody = timePictureChange - startTimeAudio;
                FileWriter writer = null;
                if (!mLabelsDirectory.exists() && !mLabelsDirectory.mkdirs()) {
                    mLabelsDirectory = null;
                } else {
                    FileWriter writer2 = null;
                    try {
                        if (labelFile == null) {
                            String labelFileName = mCurrentProject + ".txt";
                            Log.wtf("LabelFileName: ", labelFileName);
                            labelFile = new File(mLabelsDirectory, labelFileName);
                            writer2 = new FileWriter(labelFile, true);
                        } else {
                            writer2 = new FileWriter(labelFile, true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (firstClick == 0) {
                            writer2.append(viewFragment.getArrayFilepaths().size() + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\n");
                            firstClick++;
                        } else {
                            writer2.append(viewFragment.getArrayFilepaths().size() + "\t" + sBody + "\t" + 0 + "\t" + 0 + "\n");
                        }

                        writer2.flush();
                        writer2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                viewFragment.scrolToLast();

            } else {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                firstSlide--;
            }
        }
    };


    private boolean mStartRecording = true;
    private long timeStop = 0;
    private long startTime;

    private OnClickListener recordButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onRecord(mStartRecording);
            startTimeAudio = System.currentTimeMillis();

            if (mStartRecording) {
                if (timeStop == 0) {
                    startTime = SystemClock.elapsedRealtime();
                    chronometer.setBase(startTime);
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime() - timeStop);
                }
                chronometer.start();
                recordButtonpause.setBackgroundResource(R.drawable.pause_red);
                doneView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.toolbarRecordingActiveColor));
                doneView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                myToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.toolbarRecordingActiveColor));
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    window.setStatusBarColor(getResources().getColor(R.color.statusBarRecordingActiveColor));
                }
                backButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_back_white));
                pictureCounter.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                chronometer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

            } else {
                timeStop = SystemClock.elapsedRealtime() - chronometer.getBase();

                Intent intent = new Intent("Finish");
                sendBroadcast(intent);

                chronometer.stop();
                recordButtonpause.setBackgroundResource(R.drawable.record_red);
                doneView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.toolbarRecordingColor));
                doneView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.toolbarTextAccentColor));
                pictureCounter.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                chronometer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.toolbarGrayColor));
                myToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.toolbarRecordingColor));
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    window.setStatusBarColor(getResources().getColor(R.color.statusBarRecordingColor));
                }
                backButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_back_violet));
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

    private OnClickListener leftButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onSwipePictureListener.previous();
        }
    };

    private OnClickListener rightButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onSwipePictureListener.next();

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

        IntentFilter intentFilter = new IntentFilter("Finish");
        registerReceiver(broadcastReceiver, intentFilter);

        //test of the animation----------------------------------
        progressBar = (RelativeLayout) findViewById(R.id.progress_bar);

//        DrawView draw = new DrawView(this, 4, "A");
//        DrawView draw2 = new DrawView(this, 3, "B");
//        progressBar.addView(draw);
//        progressBar.addView(draw2);
//        progressBar.invalidate();
//
//        draw.animate().translationXBy(-100f).setDuration(2000);
//        draw2.animate().translationXBy(-100f).setDuration(2000);

        FrameLayout cameraLayout = (FrameLayout) findViewById(R.id.camera_frame2);

//        CircleDrawView circle1 = new CircleDrawView(this, 300, 300);
//        CircleDrawView circle2 = new CircleDrawView(this, 500, 500);
//        CircleDrawView circle3 = new CircleDrawView(this, 150, 700);
//
//        cameraLayout.addView(circle1);
//        cameraLayout.addView(circle2);
//        cameraLayout.addView(circle3);
//
//        cameraLayout.invalidate();
//        circle1.animate().scaleX(1.2f).scaleY(1.2f).setDuration(2000);
//        circle2.animate().scaleX(0.5f).scaleY(0.5f).setDuration(2000);
//        circle3.animate().scaleX(0.5f).scaleY(0.5f).setDuration(2000);
        //------------------------

        myToolbar = (View) findViewById(R.id.custom_toolbar);
        backButton = (Button) findViewById(R.id.back_button);

        mCaptureImageButton = (Button) findViewById(R.id.capture_image);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);

        recordButtonpause = (Button) findViewById(R.id.record_button);
        recordButtonpause.setOnClickListener(recordButtonListener);

        leftButton = (Button) findViewById(R.id.left_button);
        leftButton.setOnClickListener(leftButtonListener);

        rightButton = (Button) findViewById(R.id.right_button);
        rightButton.setOnClickListener(rightButtonListener);

        mPager = (CustomViewPagerH) findViewById(R.id.pager);
        mPager.setPagingEnabled(false);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        pictureCounter = (TextView) findViewById(R.id.picture_counter);
        chronometer = (Chronometer) findViewById(R.id.track_lenght);

        doneView = (TextView) findViewById(R.id.done);
        doneView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                done++;
                arrayFilepaths2.clear();
                Intent firstScreen = new Intent(getApplicationContext(), FirstscreenActivity.class);
                startActivity(firstScreen);
            }
        });

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                done++;
                arrayFilepaths2.clear();
                Intent firstScreen = new Intent(getApplicationContext(), FirstscreenActivity.class);
                startActivity(firstScreen);
            }
        });


        window = getWindow();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.statusBarRecordingColor));
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        int rotation = this.getWindowManager().getDefaultDisplay()
                .getRotation();

        recordButtonpause.setEnabled(false);

        mCurrentProject = getIntent().getStringExtra("currentProject");

        mPreviewDirectory = new File(Statics.mDiretoryName + "/" + mCurrentProject + "/Previews");
        mPictureDirectory = new File(Statics.mDiretoryName + "/" + mCurrentProject + "/Pictures");
        mLabelsDirectory = new File(Statics.mDiretoryName + "/" + mCurrentProject);
        mAudioDirectory = new File(Statics.mDiretoryName + "/" + mCurrentProject);

    }

// play first label
//        mLabelPlayButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                fragment.readFromFile();
//                mTime = fragment.getFileTime3();
//                boolean mStartPlaying = true;
//                onPlayLabel(mStartPlaying);
//            }
//        });


//        mNextButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                boolean mStartPlaying = true;
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

//                time = fragment.getFileTime3();
//                Log.i("Time size", String.valueOf(time.size()));
//                if (current - 1 < time.size() - 1) {
//                    current++;
//                }
//                Log.i("Time size", String.valueOf(time.size()));
//                Log.i("Iinfo!!!!", String.valueOf(current));
//
//
//                onPlayNext(mStartPlaying);
//            }
//        });


//        mPreviousButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                boolean mStartPlaying = true;
//                onPlayPrevious(mStartPlaying);
//            }
//        });
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mRecorder != null) {
//            mRecorder.release();
//            mRecorder = null;
//        }
//
//        if (mPlayer != null) {
//            mPlayer.release();
//            mPlayer = null;
//        }
//    }

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


                            imagesPathList.add(realPath);
                        }
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

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("Finish")) {

            }
        }
    };

    public interface ReceivePictureListener {
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
                done++;
                arrayFilepaths2.clear();
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
        public ArrayList<Fragment> fragments = new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fragment = new CameraFragment();
                    fragment.setCallback(AudioRecordActivity.this);
                    fragments.add(fragment);
                    return fragment;

                case 1:
                    viewFragment = new ViewFragment();
                    viewFragment.sendView(progressBar);
                    onArticleSelected(arrayFilepaths2);
                    fragments.add(viewFragment);
                    onSwipePictureListener = viewFragment;
                    return viewFragment;
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        public Fragment getFragment(int position) {
            if (position < fragments.size()) {
                return fragments.get(position);
            }
            return null;
        }
    }
}
