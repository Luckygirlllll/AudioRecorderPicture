package com.example.attracti.audiorecorderpicture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.attracti.audiorecorderpicture.MyAdapter.decodeSampledBitmapFromResource;

/**
 * Created by Iryna on 6/2/16.
 * <p/>
 * In this class is going the process of the downloading bitmaps for the ViewFragment
 */

class ChildDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> viewHolderWeakReference;
    private String data = null;

    static Bitmap tempBitmapTest;
    Canvas tempCanvas;
    int position;
    int x;
    int y;

    static ArrayList fileTime = new ArrayList();
    static ArrayList xfile = new ArrayList();
    static ArrayList yfile = new ArrayList();
    static ArrayList filePosition = new ArrayList();

    File f;


    ImageView imageView;

    public ChildDownloadTask(ImageView imageView, int position, int x, int y) {
        viewHolderWeakReference = new WeakReference<ImageView>(imageView);
        this.position = position;
        this.x = x;
        this.y = y;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        Log.i("TAG", "Async task works in background");
        data = String.valueOf(params[0]);
        f = new File(data);
        Log.wtf("Params: ", params[0]);
        final Bitmap bitmap = decodeSampledBitmapFromResource(data, 100, 100);

        //addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
        //-----Test

        tempBitmapTest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmapTest);
        tempCanvas.drawBitmap(bitmap, 0, 0, null);
        Paint myPaint3 = new Paint();
        myPaint3.setAntiAlias(true);
        myPaint3.setColor(Color.RED);
//      Log.wtf("X in Child: ", x + "Y in Child: " + y);
        readFromFile();

        if(filePosition!=null) {
            for (int i = 0; i < filePosition.size(); i++) {
                Log.i("filePosition size: ", String.valueOf(filePosition.size()));
                if (position == (Integer.parseInt((String) filePosition.get(i)))) {
                    tempCanvas.drawCircle(Integer.parseInt((String) xfile.get(i)) / 4, Integer.parseInt((String) yfile.get(i)) / 4, 20, myPaint3);
                    Log.wtf("Position DownloadTask: ", (String) filePosition.get(i));
                    Log.wtf("Xfile DownloadTask: ", (String) xfile.get(i));
                    Log.wtf("Yfile DownloadTask: ", (String) yfile.get(i));
                    tempCanvas.save();
                }
            }
        }

        if(position==position) {
            tempCanvas.drawCircle(x / 4, y / 4, 20, myPaint3);
            tempCanvas.save();
        }


        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.i("onPostExecute", "works!");
        if (viewHolderWeakReference != null && bitmap != null) {
            imageView = viewHolderWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);

                imageView.setImageDrawable(new BitmapDrawable(ChildDownloadTask.tempBitmapTest));
                imageView.invalidate();
            }
        }
    }
//    public  void addBitmapToMemoryCache(String key, Bitmap bitmap) {
//        if (getBitmapFromMemCache(key) == null) {
//            FirstscreenActivity.mMemoryCache.put(key, bitmap);
//        }
//    }
//    public  Bitmap getBitmapFromMemCache(String key) {
//        return FirstscreenActivity.mMemoryCache.get(key);
//    }
public void readFromFile() {
    Log.i("reading from File", "in View Activity");
    StringBuilder text = new StringBuilder();
    String parentName = f.getParentFile().getName();

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
}