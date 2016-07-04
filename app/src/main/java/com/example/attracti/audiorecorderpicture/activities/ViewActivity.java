package com.example.attracti.audiorecorderpicture.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.fragments.BitmapFragment;
import com.example.attracti.audiorecorderpicture.interfaces.OnCreateCanvasListener;
import com.example.attracti.audiorecorderpicture.model.Folder;
import com.example.attracti.audiorecorderpicture.model.Label;
import com.example.attracti.audiorecorderpicture.utils.Statics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Iryna on 6/1/16.
 * <p/>
 * in this class pictures are displayed from a certain project with labels and here user can listen the audio record
 * both from labels and in the order in which it was recorded.
 */


public class ViewActivity extends FragmentActivity implements OnCreateCanvasListener {

    private static final String TAG = ViewActivity.class.getSimpleName();

    private ArrayList<Folder> FOLDERS = new ArrayList<>();
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private File[] mArray;

    private ArrayList fileTime = null;
    private ArrayList xFile = null;
    private ArrayList yFile = null;

    public ArrayList filePosition = null;
    private String parentName;

    private MediaPlayer mPlayer;
    private Button playButton;
    private SeekBar seekbar;
    private Handler durationHandler = new Handler();

    private double timeElapsed = 0;
    private double finalTime = 0;

    TextView duration;

    private LinkedList<Label> labelList = new LinkedList<Label>();

    public LinkedList<Label> getLabelList() {
        return labelList;
    }

    private ArrayList canvasList = new ArrayList();
    private ArrayList positionList = new ArrayList();

    private File[] listFile;

    public ArrayList getFileTime() {
        return fileTime;
    }

    public ArrayList getxFile() {
        return xFile;
    }

    public ArrayList getyFile() {
        return yFile;
    }

    public ArrayList getFilePosition() {
        return filePosition;
    }

    public String getParentName() {
        return parentName;
    }


    public void getFromSdcardFolders() {
        File file = new File(Environment.getExternalStorageDirectory() +
                "/Audio_Recorder_Picture", "Pictures");
        if (file.isDirectory()) {
            File[] listFolders = file.listFiles();
            for (int i = 0; i < listFolders.length; i++) {

                Folder folderobject = new Folder();
                folderobject.setName(listFolders[i].getName());

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
            }
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(playButtonListener);

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        duration = (TextView) findViewById(R.id.songDuration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_view);
        getFromSdcardFolders();
        Intent intent = getIntent();
        mArray = (File[]) intent.getSerializableExtra("FILE_TAG");
        listFile = (File[]) intent.getSerializableExtra("listFile");

        listFile[0].getAbsolutePath();
        if (savedInstanceState == null) {
            readFromFile();
        }
    }


    public void updateCurrent(int x, int y) {
        int position = mPager.getCurrentItem();
        BitmapFragment fragment = (BitmapFragment) mPagerAdapter.getItem(position);
        int update = 1;
        fragment.updateBitmap(x, y, update);
    }

    private boolean mStartPlaying = true;
    private View.OnClickListener playButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mStartPlaying) {
                playButton.setBackgroundResource(R.drawable.pause_black);
                startPlayingLabels();
            } else {
                playButton.setBackgroundResource(R.drawable.play_circle);
                pause();
            }
            mStartPlaying = !mStartPlaying;
        }

    };


    private CountDownTimer timer;
    private static int timeStampIterator = 1;
    private static int length;

    public void startPlayingLabels() {

        mPlayer = new MediaPlayer();
        String mFileName = Statics.mDiretoryName + "/" + parentName + "/" + parentName + ".3gp";

        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
//            if (length == 0) {
//                finalTime = mPlayer.getDuration();
//                seekbar.setMax((int) finalTime);
//                //  mPlayer.start();
//                timeElapsed = mPlayer.getCurrentPosition();
//                seekbar.setProgress((int) timeElapsed);
//                durationHandler.postDelayed(updateSeekBarTime, 100);
//
//
//            } else {
//                mPlayer.seekTo(length);
//                finalTime = mPlayer.getDuration();
//                seekbar.setMax((int) finalTime);
//                //  mPlayer.start();
//                timeElapsed = mPlayer.getCurrentPosition();
//                seekbar.setProgress((int) timeElapsed);
//                durationHandler.postDelayed(updateSeekBarTime, 100);
//
//            }
            timer = new CountDownTimer(mPlayer.getDuration() - mPlayer.getCurrentPosition(), 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int timeSpends = mPlayer.getCurrentPosition();
                    if (timeSpends >= labelList.get(timeStampIterator).getLabelTime() - 100 && timeSpends <= labelList.get(timeStampIterator).getLabelTime() + 100) {


                        BitmapFragment fragment = (BitmapFragment) mPagerAdapter.getFragment(Integer.parseInt(labelList.get(timeStampIterator).getPictureName()));
                        int update = 1;
                        fragment.updateBitmap(labelList.get(timeStampIterator).getxLabel(), labelList.get(timeStampIterator).getyLabel(), update);





                        if (labelList.get(timeStampIterator).getxLabel() == 0 && labelList.get(timeStampIterator).getyLabel() == 0) {
                            mPager.setCurrentItem(Integer.parseInt(labelList.get(timeStampIterator).getPictureName()));

                            finalTime = labelList.get(timeStampIterator).getLabelTime();
                            seekbar.setMax((int) finalTime);
                            timeElapsed = mPlayer.getCurrentPosition();
                            seekbar.setProgress((int) timeElapsed);
                            durationHandler.postDelayed(updateSeekBarTime, 100);
                        }
                        if (timeStampIterator < labelList.size() - 1) {
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

    // recursive CountDownTimer -
//            timer = new CountDownTimer(labelList.get(timeStampIterator).getLabelTime() - labelList.get(timeStampIterator - 1).getLabelTime(), labelList.get(timeStampIterator).getLabelTime() - labelList.get(timeStampIterator - 1).getLabelTime()) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//
//                    mPlayer.start();
//                    mPlayer.seekTo(labelList.get(timeStampIterator).getLabelTime());
//                    if (labelList.get(timeStampIterator).getxLabel() == 0 && labelList.get(timeStampIterator).getyLabel() == 0) {
//                        mPager.setCurrentItem(Integer.parseInt(labelList.get(timeStampIterator).getPictureName()));
//                    }
//                    if (timeStampIterator < labelList.size() - 1) {
//                        timeStampIterator++;
//                    }
//                }
//
//                @Override
//                public void onFinish() {
//                    Log.wtf("timeStampIterator: ", String.valueOf(timeStampIterator));
//
//                    if (timeStampIterator + 1 < labelList.size()) {
//                        timer.start();
//                    } else {
//                        stopPlaying();
//                    }
//
//                }
//            }.start();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            length = mPlayer.getCurrentPosition();
            timer.cancel();
            timer = null;
        }
    }


    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        } else {
        }
    }

    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get current position
            timeElapsed = mPlayer.getCurrentPosition();
            //set seekbar progress
            seekbar.setProgress((int) timeElapsed);
            //set time remaing
            double timeRemaining = finalTime - timeElapsed;
            //duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
            duration.setText(String.format("%d:%d ", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));

            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);
        }
    };


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void readFromFile() {

        fileTime = new ArrayList();
        xFile = new ArrayList();
        yFile = new ArrayList();
        filePosition = new ArrayList();

        parentName = mArray[0].getParentFile().getParentFile().getName();

        try {
            File labelsFile = new File(Statics.mDiretoryName + "/" + parentName + "/" + parentName + ".txt");
            BufferedReader br = new BufferedReader(new FileReader(labelsFile));
            String line;

            while ((line = br.readLine()) != null) {
                String[] oneItem = line.split("\t");
                Label label = new Label(oneItem[0], Integer.parseInt(oneItem[1]), Integer.parseInt(oneItem[2]), Integer.parseInt(oneItem[3]));
                labelList.add(label);

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //sorted labellist
        Collections.sort(labelList);
        for (Object str : labelList) {
        }
    }

    @Override
    public void saveCanvas(Canvas canvas, int position) {
        canvasList.add(canvas);
        positionList.add(position);

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
            BitmapFragment fragment = BitmapFragment.create(array[position].getPath(), position);
            fragments.add(fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            Intent intent = getIntent();
            array = (File[]) intent.getSerializableExtra("FILE_TAG");
            return array.length;
        }

        public Fragment getFragment(int position) {
            return fragments.get(position);
        }


    }
}


