package com.example.attracti.audiorecorderpicture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * You can delete this class when all code for View Activity will be written
 */


public class AdapterViewProject extends RecyclerView.Adapter<AdapterViewProject.ViewHolder> {

    private Activity context = null;
    public static File[] pictureList;
    String parentName;

    public static HashMap<Integer, BitmapWorkerTaskView> TASKS_MAP = new HashMap<>();

    static ArrayList fileTime = new ArrayList();
    static ArrayList xfile = new ArrayList();
    static ArrayList yfile = new ArrayList();
    static ArrayList filePosition = new ArrayList();

    public void loadBitmap(int position, File path, ImageView imageView) {
        final String imageKey = String.valueOf(path);
        Log.wtf("Image Key: ", String.valueOf(imageKey));
        Log.wtf("Image position: ", String.valueOf(position));

        final Bitmap bitmap = null;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {

            BitmapWorkerTaskView task = new BitmapWorkerTaskView(imageView, position);
            task.execute(imageKey);

            TASKS_MAP.put(position, task);
        }
    }


    public AdapterViewProject(ViewActivity viewActivity, File[] list) {
        this.context = viewActivity;
        this.pictureList = list;
        getItemCount();
    }


    @Override
    public AdapterViewProject.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_screen_slide, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterViewProject.ViewHolder holder, int position) {

        loadBitmap(position, pictureList[position], holder.image1);
        Log.wtf("Parent:", String.valueOf(pictureList[0].getParentFile().getName()));
        parentName = pictureList[0].getParentFile().getName();
        readFromFile();
    }

    @Override
    public int getItemCount() {
        return pictureList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image1;

        public ViewHolder(View v) {
            super(v);
        }
    }

    public void readFromFile() {
        Log.i("reading from File", "in View Activity");
        StringBuilder text = new StringBuilder();


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
            Log.i("FILE", "LabelPosition: " + filetime2[i]);
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
}
