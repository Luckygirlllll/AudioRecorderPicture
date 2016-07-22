package com.example.attracti.audiorecorderpicture.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.fragments.BitmapFragment;
import com.example.attracti.audiorecorderpicture.interfaces.OnCreateCanvasListener;
import com.example.attracti.audiorecorderpicture.model.Label;
import com.example.attracti.audiorecorderpicture.utils.SdCardDataRetriwHеlper;
import com.example.attracti.audiorecorderpicture.utils.Statics;
import com.example.attracti.audiorecorderpicture.widgets.progressbar.LineProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

/**
 * Created by Iryna on 6/1/16.
 * <p>
 * in this class pictures are displayed from a certain project with labels and here user can listen the audio record
 * both from labels and in the order in which it was recorded.
 */


public class ViewActivity extends FragmentActivity implements OnCreateCanvasListener {

    private static final String TAG = ViewActivity.class.getSimpleName();

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private File[] mArray;
    private String parentName;

    private MediaPlayer mPlayer;
    private Button playButton;
    private SeekBar seekbar;
    private Handler durationHandler = new Handler();

    private double timeElapsed = 0;
    private double finalTime = 0;

    private TextView duration;

    private LinkedList<Label> labelList = new LinkedList<Label>();

    private ArrayList canvasList = new ArrayList();
    private ArrayList positionList = new ArrayList();

    private TextView pictureCounter;

    private long timeStop = 0;
    private long startTime;
    private boolean mStartPlaying = true;
    private LineProgressBar lineProgressBar;
    private int realPosition;
    private float maxProgress;
    private TextView songDuration;
    private ArrayList nextTimeSlide = new ArrayList();
    private ArrayList<Float> usualLabelTime = new ArrayList();
    private ArrayList positionLabelList = new ArrayList();
    private ArrayList<Float> currenSlideTimeList = new ArrayList();


    public String getParentName() {
        return parentName;
    }

    public LinkedList<Label> getLabelList() {
        return labelList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        playButton = (Button) findViewById(R.id.play_button);
        songDuration = (TextView) findViewById(R.id.track_lenght_seek);
        seekbar = new SeekBar(getApplicationContext());

        Intent intent = getIntent();
        mArray = (File[]) intent.getSerializableExtra("FILE_TAG");
        parentName = mArray[0].getParentFile().getParentFile().getName();
        labelList = SdCardDataRetriwHеlper.readFromFile(mArray);

        if (labelList != null) {
            for (int i = 0; i < labelList.size(); i++) {
                if (labelList.get(i).getxLabel() == 0 && labelList.get(i).getyLabel() == 0) {
                    nextTimeSlide.add(labelList.get(i).getLabelTime());
                } else {
                    positionLabelList.add(labelList.get(i).getPictureName());
                    usualLabelTime.add((float) labelList.get(i).getLabelTime());
                }
            }
        }


        lineProgressBar = (LineProgressBar) findViewById(R.id.progress);
        // use it for the vertical orientation of the screen
        //lineProgressBar.setLineOrientation(ProgressLineOrientation.VERTICAL);
        lineProgressBar.setRoundEdgeProgress(true);

        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstScreen = new Intent(getApplicationContext(), FirstscreenActivity.class);
                startActivity(firstScreen);
            }
        });


        pictureCounter = (TextView) findViewById(R.id.picture_counter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                realPosition = position + 1;
                //  lineProgressBar.cleanCanvas();
                if (currenSlideTimeList != null) {
                    currenSlideTimeList.clear();
                }

                pictureCounter.setText(realPosition + " из " + mArray.length);
                lineProgressBar.resetProgressBar();
                if (realPosition < nextTimeSlide.size()) {
                    lineProgressBar.setMaximum_progress(Float.parseFloat((Integer) nextTimeSlide.get(realPosition) - (Integer) nextTimeSlide.get(position) + ""));
                    if (!mStartPlaying) {
                        setTimer((int) nextTimeSlide.get(realPosition) - (int) nextTimeSlide.get(position));
                        lineProgressBar.setMaximumAbsoluteTime(Float.parseFloat(nextTimeSlide.get(realPosition) + ""));
                        //// TODO: 7/21/16 Be careful here! 
                        // usualLabelTime.add(mPlayer.getDuration()-Float.parseFloat((Integer)nextTimeSlide.get(position)+""));
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (savedInstanceState != null) {
            labelList = (LinkedList<Label>) savedInstanceState.getSerializable("labellist");
        }
        View.OnClickListener playButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mStartPlaying) {
                    playButton.setBackgroundResource(R.drawable.pause_violet);
                    startPlayingLabels();
                    Float.parseFloat(nextTimeSlide.get(mPager.getCurrentItem() + 1) + "");

                    lineProgressBar.setMaximum_progress(Float.parseFloat((Integer) nextTimeSlide.get(mPager.getCurrentItem() + 1) - (Integer) nextTimeSlide.get(mPager.getCurrentItem()) + ""));
                    lineProgressBar.setMaximumAbsoluteTime(Float.parseFloat((Integer) nextTimeSlide.get(mPager.getCurrentItem() + 1) + ""));
                    setTimer((Integer) nextTimeSlide.get(mPager.getCurrentItem() + 1) - (Integer) nextTimeSlide.get(mPager.getCurrentItem()));


                } else {
                    playButton.setBackgroundResource(R.drawable.play_violet);
                    pause();
                }
                mStartPlaying = !mStartPlaying;
            }

        };
        playButton.setOnClickListener(playButtonListener);


    }


    private void setTimer(int maxProgress) {
        if (usualLabelTime != null) {
            for (int i = 0; i < usualLabelTime.size(); i++) {
                if ((parseInt((String) positionLabelList.get(i))) == (mPager.getCurrentItem())) {
                    currenSlideTimeList.add(usualLabelTime.get(i) - Float.parseFloat(nextTimeSlide.get(mPager.getCurrentItem()) + ""));
                    lineProgressBar.setUsualLabelTime(currenSlideTimeList);
                }
            }
        }
        new ObjectAnimator().ofInt(lineProgressBar, "progress", 0, maxProgress).setDuration(maxProgress)
                .start();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("labellist", labelList);
        super.onSaveInstanceState(outState);
    }

    public void updateCurrent(int x, int y) {
        int position = mPager.getCurrentItem();
        BitmapFragment fragment = (BitmapFragment) mPagerAdapter.getItem(position);
        int update = 1;
        fragment.updateBitmap(x, y, update);
    }


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
            if (length == 0) {
                finalTime = mPlayer.getDuration();
                seekbar.setMax((int) finalTime);
//                  mPlayer.start();
                timeElapsed = mPlayer.getCurrentPosition();
                seekbar.setProgress((int) timeElapsed);
                durationHandler.postDelayed(updateSeekBarTime, 100);
//
//
            } else {
                mPlayer.seekTo(length);
                finalTime = mPlayer.getDuration();
                seekbar.setMax((int) finalTime);
//                //  mPlayer.start();
                timeElapsed = mPlayer.getCurrentPosition();
                seekbar.setProgress((int) timeElapsed);
                durationHandler.postDelayed(updateSeekBarTime, 100);
//
            }
            timer = new CountDownTimer(mPlayer.getDuration() - mPlayer.getCurrentPosition(), 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int timeSpends = mPlayer.getCurrentPosition();
                    if (timeSpends >= labelList.get(timeStampIterator).getLabelTime() - 100 && timeSpends <= labelList.get(timeStampIterator).getLabelTime() + 100) {

                        BitmapFragment fragment = (BitmapFragment) mPagerAdapter.getFragment(parseInt(labelList.get(timeStampIterator).getPictureName()));
                        int update = 1;
                        fragment.updateBitmap(labelList.get(timeStampIterator).getxLabel(), labelList.get(timeStampIterator).getyLabel(), update);

                        if (labelList.get(timeStampIterator).getxLabel() == 0 && labelList.get(timeStampIterator).getyLabel() == 0) {
                            mPager.setCurrentItem(parseInt(labelList.get(timeStampIterator).getPictureName()));

                            finalTime = labelList.get(timeStampIterator).getLabelTime();
//                            seekbar.setMax((int) finalTime);
//                            timeElapsed = mPlayer.getCurrentPosition();
//                            seekbar.setProgress((int) timeElapsed);
//                            durationHandler.postDelayed(updateSeekBarTime, 100);
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
            songDuration.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));

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


