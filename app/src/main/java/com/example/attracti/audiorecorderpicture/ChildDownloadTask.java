package com.example.attracti.audiorecorderpicture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

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


    ImageView imageView;

    public ChildDownloadTask(ImageView imageView, int position, int x, int y) {
        viewHolderWeakReference = new WeakReference<ImageView>(imageView);
        this.position=position;
        this.x=x;
        this.y=y;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        Log.i("TAG", "Async task works in background");
        data = String.valueOf(params[0]);
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
        Log.wtf("X in Child: ",x+"Y in Child: "+y);

            if(position==0) {
                    tempCanvas.drawCircle(x, y, 100, myPaint3);
                    tempCanvas.save();
                }


            if(position==1) {
                myPaint3.setColor(Color.GREEN);
                tempCanvas.drawCircle(50, 50, 20, myPaint3);
                tempCanvas.save();
            }
            if(position==2) {
                tempCanvas.drawCircle(20, 20, 20, myPaint3);
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
}