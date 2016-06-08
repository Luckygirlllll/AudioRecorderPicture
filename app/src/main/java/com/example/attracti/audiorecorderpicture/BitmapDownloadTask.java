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

import java.lang.ref.WeakReference;

import static com.example.attracti.audiorecorderpicture.MyAdapter.decodeSampledBitmapFromResource;

/**
 * Created by Iryna on 6/2/16.
 * <p/>
 * In this class is going the process of the downloading bitmaps for the BitmapFragment
 */

class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap> {
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
            Log.i("filePosition size: ", String.valueOf(ViewActivity.filePosition.size()));
            if (position == (Integer.parseInt((String) ViewActivity.filePosition.get(i)))) {
                tempCanvas.drawCircle(Integer.parseInt((String) ViewActivity.xfile.get(i))/4, Integer.parseInt((String) ViewActivity.yfile.get(i))/4, 10, myPaint3);

                textPaint.getTextBounds(String.valueOf(i), 0, String.valueOf(i).length(), bounds);
                tempCanvas.drawText(String.valueOf(i), Integer.parseInt((String)ViewActivity.xfile.get(i)) / 4, Integer.parseInt((String)ViewActivity.yfile.get(i)) / 4, textPaint);
                tempCanvas.save();
            }
        }

//
//        Set<Integer> uniquePositions = new HashSet<>(MyAdapter2.positionList);
//
//        for(int i=0; i<AdapterViewProject.filePosition.size();i++ )
        //     if (position == Integer.parseInt((String) ViewFragment.filePosition.get(i))) {
        //               Log.i("FilePosition: ", String.valueOf(Integer.parseInt((String) AdapterViewProject.filePosition.get(i))));
//                if(ViewFragment.xcoordList!=null && ViewFragment.ycoordList!=null) {
//                    tempCanvas.drawCircle(ViewFragment.xcoordList.get(0), ViewFragment.xcoordList.get(0), 20, myPaint3);
//                }
//                Log.i("Events X in Async: ", AdapterViewProject.xfile.get(i) + " Y in Async: " + MyAdapter2.y);
//            }
//        tempCanvas.save();
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
}