package com.example.attracti.audiorecorderpicture.async;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.activities.FirstscreenActivity;
import com.example.attracti.audiorecorderpicture.adapters.OldRecyclerViewAdapter;
import com.example.attracti.audiorecorderpicture.activities.ViewActivity;
import com.example.attracti.audiorecorderpicture.adapters.AdapterViewProject;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import static com.example.attracti.audiorecorderpicture.adapters.RecyclerViewAdapter.decodeSampledBitmapFromResource;

/**
 * Created by Iryna on 6/2/16.
 * <p>
 * In this class is going the process of the downloading bitmaps for the BitmapFragment
 */

public class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> viewHolderWeakReference;
    private String data = null;

    static Bitmap tempBitmapTest;
    Canvas tempCanvas;
    Paint textPaint;

    int position;

    public BitmapDownloadTask(ImageView imageView, int position) {
        viewHolderWeakReference = new WeakReference<ImageView>(imageView);
        this.position = position;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        Log.i("TAG", "Async task works in background");
        data = String.valueOf(params[0]);
        Log.wtf("Params: ", params[0]);
        final Bitmap bitmap = decodeSampledBitmapFromResource(data, 100, 100);

        //addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);

        tempBitmapTest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmapTest);
        tempCanvas.drawBitmap(bitmap, 0, 0, null);
        Paint myPaint3 = new Paint();
        myPaint3.setAntiAlias(true);
        myPaint3.setColor(Color.RED);

        textPaint = new Paint();
        textPaint.setTextSize(15);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Rect bounds = new Rect();

        for (int i = 0; i < ViewActivity.filePosition.size(); i++) {
            Log.wtf("filePosition size: ", String.valueOf(ViewActivity.filePosition.size()));
            Log.wtf("file Position items: ", String.valueOf(ViewActivity.filePosition.get(i)));
            if (position == (Integer.parseInt((String) ViewActivity.filePosition.get(i)))) {
                tempCanvas.drawCircle(Integer.parseInt((String) ViewActivity.xfile.get(i)) / 4, Integer.parseInt((String) ViewActivity.yfile.get(i)) / 4, 10, myPaint3);

                textPaint.getTextBounds(String.valueOf(i), 0, String.valueOf(i).length(), bounds);
                tempCanvas.drawText(String.valueOf(i + 1), Integer.parseInt((String) ViewActivity.xfile.get(i)) / 4, Integer.parseInt((String) ViewActivity.yfile.get(i)) / 4, textPaint);
                tempCanvas.save();
            }
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.i("onPostExecute", "works!");
        if (viewHolderWeakReference != null && bitmap != null) {
            final ImageView imageView = viewHolderWeakReference.get();

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);

                imageView.setImageDrawable(new BitmapDrawable(BitmapDownloadTask.tempBitmapTest));
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

    public static class BitmapWorkerTaskView extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> viewHolderWeakReference;
        private String data;
        private static int position;

        static Bitmap tempBitmapTest;
        Canvas tempCanvas;

        public BitmapWorkerTaskView(ImageView imageView, int position) {
            viewHolderWeakReference = new WeakReference<ImageView>(imageView);
            this.position = position;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.i("TAG", "Async task works in background");
            data = String.valueOf(params[0]);
            Log.wtf("Params: ", params[0]);
            final Bitmap bitmap = decodeSampledBitmapFromResource(data, 100, 100);
            addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            //-----Test

            tempBitmapTest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
            tempCanvas = new Canvas(tempBitmapTest);
            tempCanvas.drawBitmap(bitmap, 0, 0, null);
            Paint myPaint3 = new Paint();
            myPaint3.setColor(Color.RED);

            Set<Integer> uniquePositions = new HashSet<>(OldRecyclerViewAdapter.positionList);

            for (int i = 0; i < AdapterViewProject.filePosition.size(); i++)
                if (position == Integer.parseInt((String) AdapterViewProject.filePosition.get(i))) {
                    Log.i("FilePosition: ", String.valueOf(Integer.parseInt((String) AdapterViewProject.filePosition.get(i))));
                    tempCanvas.drawCircle(Float.parseFloat((String) AdapterViewProject.xfile.get(i)) / 2, Float.parseFloat((String) AdapterViewProject.yfile.get(i)) / 2, 20, myPaint3);
                    Log.i("Events X in Async: ", AdapterViewProject.xfile.get(i) + " Y in Async: " + OldRecyclerViewAdapter.y);
                }
            tempCanvas.save();
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.i("onPostExecute", "works!");
            if (viewHolderWeakReference != null && bitmap != null) {
                final ImageView imageView = viewHolderWeakReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setImageDrawable(new BitmapDrawable(BitmapWorkerTaskView.tempBitmapTest));
                    Paint myPaint4 = new Paint();
                    Log.i("Events X in Async: ", OldRecyclerViewAdapter.x + " Y in Async: " + OldRecyclerViewAdapter.y);
                    imageView.invalidate();
                }
            }
        }

        public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
            if (getBitmapFromMemCache(key) == null) {
                FirstscreenActivity.mMemoryCache.put(key, bitmap);
            }
        }

        public Bitmap getBitmapFromMemCache(String key) {
            return FirstscreenActivity.mMemoryCache.get(key);
        }
    }
}