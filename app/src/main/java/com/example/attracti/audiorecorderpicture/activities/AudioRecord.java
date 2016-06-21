package com.example.attracti.audiorecorderpicture.activities;

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
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.utils.Statics;
import com.example.attracti.audiorecorderpicture.fragments.CameraFragment;
import com.example.attracti.audiorecorderpicture.fragments.ViewFragment;
import com.example.attracti.audiorecorderpicture.interfaces.OnHeadlineSelectedListener;
import com.example.attracti.audiorecorderpicture.interfaces.OnSwipePictureListener;
import com.example.attracti.audiorecorderpicture.widgets.CustomViewPagerH;
import com.example.attracti.audiorecorderpicture.utils.RealPathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This is the class where going the process of audio recording. This activity has two fragments:
 * CameraFragment and ViewFragment (ViewPager)
 */

public class AudioRecord extends AppCompatActivity implements OnHeadlineSelectedListener {

    private final  String LOG_TAG = AudioRecord.class.getSimpleName();

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
    private Button chooseButton;
    private Toolbar myToolbar;
    private Button leftButton;
    private Button rightButton;

    private CameraFragment fragment;
    private ViewFragment viewFragment;

    private long timePictureChange;

    private File labelFile;
    private String mCurrentProject;
    private File mPreviewDirectory;
    private File mPictureDirectory;
    private File mLabelsDirectory;
    private File mAudioDirectory;

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
            mFileName = Statics.mDiretoryName + "/" + mCurrentProject +"/"+mCurrentProject+".3gp";
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

//    public AudioRecord() {
//        mFileName = Statics.mAudioFolder + "/" + mCurrentProject + ".3gp";
//    }

    private static int firstClick = 0;

    private OnClickListener mCaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (firstSlide == 0) {
                firstSlide++;

                fragment.takePicture();

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
    private OnClickListener recordButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onRecord(mStartRecording);
            startTimeAudio = System.currentTimeMillis();

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

    private OnClickListener leftButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.wtf("Left button", "works!");
            onSwipePictureListener.previous();

        }
    };

    private OnClickListener rightButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onSwipePictureListener.next();
            Log.wtf("Rigth button", "works!");

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
        LinearLayout ll = (LinearLayout) findViewById(R.id.lin_three);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

        //-----Camera features
        mCaptureImageButton = (Button) findViewById(R.id.capture_image);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);


        recordButtonpause = (Button) findViewById(R.id.record_button);
        recordButtonpause.setOnClickListener(recordButtonListener);
        //  findViewById(R.id.record_button).setOnClickListener(recordButtonListener);

        chooseButton = (Button) findViewById(R.id.choose_button);
        chooseButton.setOnClickListener(chooseButtonListener);
        //  findViewById(R.id.choose_button).setOnClickListener(chooseButtonListener);

        leftButton = (Button) findViewById(R.id.left_button);
        leftButton.setOnClickListener(leftButtonListener);

        rightButton = (Button) findViewById(R.id.right_button);
        rightButton.setOnClickListener(rightButtonListener);

        mPager = (CustomViewPagerH) findViewById(R.id.pager);
        mPager.setPagingEnabled(false);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        int rotation = this.getWindowManager().getDefaultDisplay()
                .getRotation();

        mCurrentProject = getIntent().getStringExtra("currentProject");

        mPreviewDirectory = new File(Statics.mDiretoryName + "/" + mCurrentProject+"/Previews");
        mPictureDirectory = new File(Statics.mDiretoryName + "/" + mCurrentProject+"/Pictures");
        mLabelsDirectory =  new File (Statics.mDiretoryName + "/"+ mCurrentProject);
        mAudioDirectory = new File(Statics.mDiretoryName + "/"+ mCurrentProject);

    }

    //play first label
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

    public interface ReceivePictureListener {
        public void recievePicture(Bitmap bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    public static int done = 0;

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
                    fragment.setCallback(AudioRecord.this);
                    fragments.add(fragment);
                    return fragment;

                case 1:
                    viewFragment = new ViewFragment();
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
