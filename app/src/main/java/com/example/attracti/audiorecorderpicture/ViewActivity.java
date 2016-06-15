package com.example.attracti.audiorecorderpicture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Iryna on 6/1/16.
 * <p>
 * in this class pictures are displayed from a certain project with labels and here user can listen the audio record
 * both from labels and in the order in which it was recorded.
 */


public class ViewActivity extends FragmentActivity {


    public static ArrayList<Folder> FOLDERS = new ArrayList<>();

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    ImageView imageView;

    File[] array;
    public static HashMap<Integer, BitmapWorkerTaskView> TASKS_MAP = new HashMap<>();

    static ArrayList fileTime = new ArrayList();
    static ArrayList xfile = new ArrayList();
    static ArrayList yfile = new ArrayList();
    static ArrayList filePosition = new ArrayList();

    static ArrayList zeroLabelPosition = new ArrayList();
    static ArrayList xzero = new ArrayList();
    static ArrayList yzero = new ArrayList();
    static ArrayList zeroTime = new ArrayList();


    public static String parentName;

    MediaPlayer mPlayer;

    Button playButton;


    public void getFromSdcardFolders() {
        File file = new File(Environment.getExternalStorageDirectory() +
                "/Audio_Recorder_Picture", "Pictures");
        if (file.isDirectory()) {
            File[] listFolders = file.listFiles();
            for (int i = 0; i < listFolders.length; i++) {

                Folder folderobject = new Folder();
                folderobject.setName(listFolders[i].getName());
                Log.i("List of folders: ", String.valueOf(listFolders[i].getName()));

                File picturelist = new File(Environment.getExternalStorageDirectory() +
                        "/Audio_Recorder_Picture/Pictures", listFolders[i].getName());
                if (picturelist.isDirectory()) {
                    File[] listFile
                            = picturelist.listFiles();
                    for (int j = 0; j < listFile.length; j++) {
                        folderobject.addFile(listFile[j].getAbsolutePath());
                    }
                }
                FOLDERS.add(folderobject);
                Log.wtf("TAG", "Folders size inside the getFRom:" + FOLDERS.size());
            }
        }
    }

    public void loadBitmap(int position, File path, ImageView imageView) {
        final String imageKey = String.valueOf(path);
        Log.wtf("Image Key: ", String.valueOf(imageKey));
        Log.wtf("Image position: ", String.valueOf(position));

        // final Bitmap bitmap = OldRecyclerViewAdapter.getBitmapFromMemCache(imageKey);
        final Bitmap bitmap = null;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {

            BitmapWorkerTaskView task = new BitmapWorkerTaskView(imageView, position);
            task.execute(imageKey);

            TASKS_MAP.put(position, task);
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mPager = (ViewPager) findViewById(R.id.pager);
        imageView = (ImageView) findViewById(R.id.imageView);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(playButtonListener);

        FirstscreenActivity.listFile[0].getAbsolutePath();

        getFromSdcardFolders();

        Intent intent = getIntent();
        array = (File[]) intent.getSerializableExtra("FILE_TAG");

        for (int i = 0; i < array.length; i++) {
            Log.wtf("Array elements ", String.valueOf(array[i]));
        }
        if (savedInstanceState == null) {
            readFromFile();
            for (int i = 0; i < zeroLabelPosition.size(); i++) {
                Log.wtf("ZERO", "label position: " + zeroLabelPosition.get(i));
                Log.wtf("ZERO", "zero Time: " + zeroTime.get(i));
                Log.wtf("ZERO", "xzero: " + xzero.get(i));
                Log.wtf("ZERO", "yzero: " + yzero.get(i));

            }
        }
    }


    boolean mStartPlaying = true;
    private View.OnClickListener playButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mStartPlaying) {
                playButton.setBackgroundResource(R.drawable.pause_black);
                Log.wtf("Play button ", "works!");
                startPlayingLabels();

            } else {
                playButton.setBackgroundResource(R.drawable.play_circle);
                pause();
            }
            mStartPlaying = !mStartPlaying;
        }

    };


    CountDownTimer timer;
     static int timeStampIterator=1;
    int[] timeStamp = new int[5]; // your own time stamps when view must be switched
    static int length;

    public void startPlayingLabels() {

        mPlayer = new MediaPlayer();
            String mFileName = CameraFragment.mAudioFolder + "/" + ViewActivity.parentName + ".3gp";
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();

            if(length==0){
            mPlayer.start();
            }
            else {
                mPlayer.seekTo(length);
                mPlayer.start();
            }

            timer = new CountDownTimer(mPlayer.getDuration() - mPlayer.getCurrentPosition(), 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                 //   int timeSpends = mPlayer.getDuration() - mPlayer.getCurrentPosition();
                    int timeSpends = mPlayer.getCurrentPosition();
                    Log.wtf("timeSpends: ", String.valueOf(timeSpends));
                    if (timeSpends>=Integer.parseInt(String.valueOf(zeroTime.get(timeStampIterator)))-100 &&timeSpends<=Integer.parseInt(String.valueOf(zeroTime.get(timeStampIterator)))+100) {
                       Log.wtf("if works", "!!!");
                        mPager.setCurrentItem(Integer.parseInt(String.valueOf(zeroLabelPosition.get(timeStampIterator))));
                        if(timeStampIterator<zeroTime.size()-1) {
                            timeStampIterator++;
                        }
                    }
                }
                @Override
                public void onFinish() {

                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void pause() {
        if (mPlayer.isPlaying()){
            Log.wtf("Pause of the MediaPlayer", "pause");
            mPlayer.pause();
            length=mPlayer.getCurrentPosition();
            timer.cancel();
            timer = null;
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


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void readFromFile() {
        Log.i("reading from File", "in View Activity");
        StringBuilder text = new StringBuilder();
        parentName = array[0].getParentFile().getName();

        try {
            File labelsFile = new File(CameraFragment.mLabelsFolder, parentName + ".txt");
            BufferedReader br = new BufferedReader(new FileReader(labelsFile));
            String line;

            while ((line = br.readLine()) != null) {

                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("TextInfo", String.valueOf(text));

        String[] filetime2 = text.toString().split("\n");

        for (int i = 0; i < filetime2.length; i = i + 4) {
            Log.i("FILE", "Position: " + filetime2[i]);

            if (Integer.parseInt(String.valueOf(filetime2[i + 2])) != 0 && Integer.parseInt(String.valueOf(filetime2[i + 3])) != 0) {
                filePosition.add(filetime2[i]);
            } else {
                zeroLabelPosition.add(filetime2[i]);
            }
            Log.i("FILE", "filePosition size: " + String.valueOf(fileTime.size()));
        }

        for (int i = 1; i < filetime2.length; i = i + 4) {
            Log.i("FileTime elements: ", filetime2[i]);
            String n = filetime2[i];
            if (Integer.parseInt(String.valueOf(filetime2[i + 1])) != 0 && Integer.parseInt(String.valueOf(filetime2[i + 2])) != 0) {
                fileTime.add(filetime2[i]);
            } else {
                zeroTime.add(filetime2[i]);
            }
            Log.i("FILE", "FileTime size: " + String.valueOf(fileTime.size()));
        }
        for (int i = 2; i < filetime2.length; i = i + 4) {
            Log.i("FILE", "Coordinates of X: " + filetime2[i]);
            String n = filetime2[i];
            if (Integer.parseInt(String.valueOf(filetime2[i])) != 0 && Integer.parseInt(String.valueOf(filetime2[i + 1])) != 0) {
                xfile.add(filetime2[i]);
            } else {
                xzero.add(filetime2[i]);
            }
        }

        for (int i = 3; i < filetime2.length; i = i + 4) {
            Log.i("FILE", "Coordinates of Y: " + filetime2[i]);
            String n = filetime2[i];
            if (Integer.parseInt(String.valueOf(filetime2[i - 1])) != 0 && Integer.parseInt(String.valueOf(filetime2[i])) != 0) {
                yfile.add(filetime2[i]);
            } else {
                yzero.add(filetime2[i]);
            }
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();
        File[] array;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Intent intent = getIntent();
            array = (File[]) intent.getSerializableExtra("FILE_TAG");

            return BitmapFragment.create(array[position].getPath(), position);
        }

        @Override
        public int getCount() {
            Intent intent = getIntent();
            array = (File[]) intent.getSerializableExtra("FILE_TAG");
            return array.length;
        }
    }
}


