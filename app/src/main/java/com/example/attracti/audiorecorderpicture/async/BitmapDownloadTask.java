package com.example.attracti.audiorecorderpicture.async;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.activities.ViewActivity;
import com.example.attracti.audiorecorderpicture.interfaces.OnCreateCanvasListener;

import java.lang.ref.WeakReference;

import static com.example.attracti.audiorecorderpicture.adapters.RecyclerViewAdapter.decodeSampledBitmapFromResource;

/**
 * Created by Iryna on 6/2/16.
 * <p>
 * In this class is going the process of the downloading bitmaps for the BitmapFragment,
 * (which is responsible for the showing of the picture of the certain project)
 */

public class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private final  String LOG_TAG = BitmapDownloadTask.class.getSimpleName();

    private final WeakReference<ImageView> viewHolderWeakReference;
    private String mData = null;

    private Bitmap tempBitmapTest;
    private Canvas tempCanvas;
    private Paint textPaint;

    private int position;

    OnCreateCanvasListener canvasListener;
    ViewActivity activity;

    public BitmapDownloadTask(ViewActivity activity, OnCreateCanvasListener canvasListener, ImageView imageView, int position) {
        this.activity = activity;
        this.canvasListener = canvasListener;
        viewHolderWeakReference = new WeakReference<ImageView>(imageView);
        this.position = position;
    }


    @Override
    protected Bitmap doInBackground(String... params) {

        mData = String.valueOf(params[0]);
        final Bitmap bitmap = decodeSampledBitmapFromResource(mData, 100, 100);

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

        for (int i = 0; i < activity.getLabelList().size(); i++) {
            if (position == (Integer.parseInt((String) activity.getLabelList().get(i).getPictureName()))) {
                tempCanvas.drawCircle( activity.getLabelList().get(i).getxLabel() / 4, activity.getLabelList().get(i).getyLabel() / 4, 10, myPaint3);

                textPaint.getTextBounds(String.valueOf(i), 0, String.valueOf(i).length(), bounds);
                tempCanvas.drawText(String.valueOf(i + 1),  activity.getLabelList().get(i).getxLabel() / 4, activity.getLabelList().get(i).getyLabel() / 4, textPaint);
                tempCanvas.save();
            }
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (viewHolderWeakReference != null && bitmap != null) {
            final ImageView imageView = viewHolderWeakReference.get();

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);

                imageView.setImageDrawable(new BitmapDrawable(tempBitmapTest));
                imageView.invalidate();
            }
        }
        canvasListener.saveCanvas(tempCanvas, position);

    }
//    public  void addBitmapToMemoryCache(String key, Bitmap bitmap) {
//        if (getBitmapFromMemCache(key) == null) {
//            FirstscreenActivity.mMemoryCache.put(key, bitmap);
//        }
//    }
//    public  Bitmap getBitmapFromMemCache(String key) {
//        return FirstscreenActivity.mMemoryCache.get(key);
//    }

//
}