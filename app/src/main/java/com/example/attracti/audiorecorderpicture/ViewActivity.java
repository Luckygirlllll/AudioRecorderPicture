package com.example.attracti.audiorecorderpicture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Iryna on 6/1/16.
 * <p/>
 * in this class pictures are displayed from a certain project with labels and here user can listen the audio record
 * both from labels and in the order in which it was displayed.
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

    public static String parentName;


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

        // final Bitmap bitmap = MyAdapter2.getBitmapFromMemCache(imageKey);
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


        FirstscreenActivity.listFile[0].getAbsolutePath();

        getFromSdcardFolders();

        Intent intent = getIntent();
        array = (File[]) intent.getSerializableExtra("FILE_TAG");

        for (int i = 0; i < array.length; i++) {
            Log.wtf("Array elements ", String.valueOf(array[i]));
        }
        readFromFile();
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
            String n = filetime2[i];
            filePosition.add(filetime2[i]);
            Log.i("FILE", "filePosition size: " + String.valueOf(fileTime.size()));
        }

        for (int i = 1; i < filetime2.length; i = i + 4) {
            Log.i("FileTime elements: ", filetime2[i]);
            String n = filetime2[i];
            fileTime.add(filetime2[i]);
            Log.i("FILE", "FileTime size: " + String.valueOf(fileTime.size()));
        }
        for (int i = 2; i < filetime2.length; i = i + 4) {
            Log.i("FILE", "Coordinates of X: " + filetime2[i]);
            String n = filetime2[i];
            xfile.add(filetime2[i]);
        }

        for (int i = 3; i < filetime2.length; i = i + 4) {
            Log.i("FILE", "Coordinates of Y: " + filetime2[i]);
            String n = filetime2[i];
            yfile.add(filetime2[i]);
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


