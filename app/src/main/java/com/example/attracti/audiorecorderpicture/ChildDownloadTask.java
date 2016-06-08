package com.example.attracti.audiorecorderpicture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

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
    
    File f;

    Paint textPaint;
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

        if (bitmap == null) {
            tempBitmapTest = Bitmap.createBitmap(300, 400, Bitmap.Config.RGB_565);
        } else {
            tempBitmapTest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);}
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

            if (ChildFragment.positionList != null) {
                for (int i = 0; i < ChildFragment.positionList.size(); i++) {
                    Log.i("filePosition size: ", String.valueOf(ChildFragment.positionList.size()));
                    if (position == (ChildFragment.positionList.get(i))) {
                        tempCanvas.drawCircle(ChildFragment.xcoordList.get(i) / 4, ChildFragment.ycoordList.get(i) / 4, 10, myPaint3);

                       textPaint.getTextBounds(String.valueOf(i), 0, String.valueOf(i).length(), bounds);
//                    if (i < 10 && i>1) {
//                        tempCanvas.drawCircle(ChildFragment.xcoordList.get(i)/4, ChildFragment.ycoordList.get(i)/4 - (bounds.height() / 2), bounds.width() + 70, myPaint3);
//                    } else if (i==1) {
//                        tempCanvas.drawCircle(ChildFragment.xcoordList.get(i) * 6, ChildFragment.ycoordList.get(i) * 6 - (bounds.height() / 2), bounds.width() + 95, myPaint3);
//                    }
//                    else {
//                        tempCanvas.drawCircle(ChildFragment.xcoordList.get(i) * 6, ChildFragment.ycoordList.get(i) * 6 - (bounds.height() / 2), bounds.width() + 10, myPaint3);
//                    };
                    tempCanvas.drawText(String.valueOf(i+1), ChildFragment.xcoordList.get(i)/4, ChildFragment.ycoordList.get(i)/4, textPaint);
//                        tempCanvas.save();
                    }
                }
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

}