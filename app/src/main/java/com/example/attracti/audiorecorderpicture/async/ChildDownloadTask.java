package com.example.attracti.audiorecorderpicture.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.views.CircleDrawView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.attracti.audiorecorderpicture.adapters.RecyclerViewAdapter.decodeSampledBitmapFromResource;

/**
 * Created by Iryna on 6/2/16.
 * <p>
 * In this class is going the process of the downloading bitmaps for the ViewFragment
 * download pictures which just have been captured!!!
 */

public class ChildDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private final String LOG_TAG = ChildDownloadTask.class.getSimpleName();

    private final WeakReference<ImageView> viewHolderWeakReference;
    private String data = null;

    private Context context;
    private Bitmap tempBitmapTest;
    private Canvas tempCanvas;
    private int position;
    private int x;
    private int y;

    private File f;

    private Paint textPaint;
    private ImageView imageView;
    private ViewGroup rootView;

    private ArrayList<Integer> xCoordList;
    private ArrayList<Integer> yCoordList;
    private ArrayList<Integer> positionList;

    public ChildDownloadTask(Context context, ViewGroup view, ImageView imageView, int position, ArrayList xCoordList, ArrayList yCoordList, ArrayList positionList) {
        viewHolderWeakReference = new WeakReference<ImageView>(imageView);
        this.rootView = view;
        this.context = context;
        this.position = position;
        this.xCoordList = xCoordList;
        this.yCoordList = yCoordList;
        this.positionList = positionList;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        data = String.valueOf(params[0]);
        f = new File(data);
        final Bitmap bitmap = decodeSampledBitmapFromResource(data, 100, 100);
        //addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);

        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        tempBitmapTest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmapTest);
        tempCanvas.drawBitmap(bitmap, 0, 0, null);
        Paint myPaint3 = new Paint();
        myPaint3.setAntiAlias(true);
        myPaint3.setColor(Color.RED);

        textPaint = new Paint();
        textPaint.setTextSize(10);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Rect bounds = new Rect();

        if (positionList != null) {
            for (int i = 0; i < positionList.size(); i++) {

                if (position == (positionList.get(i))) {
                    // TODO: 7/13/16 change canvas labels to the viewLabels 
                    // awesome labels! :)     
                    CircleDrawView circle = new CircleDrawView(context, xCoordList.get(i), yCoordList.get(i), String.valueOf(i + 1));

                    //   tempCanvas.drawCircle(xCoordList.get(i) / 4, yCoordList.get(i) / 4, 10, myPaint3);
                    //  tempCanvas.drawCircle(ChildFragment.xcoordList.get(i)/4, ChildFragment.ycoordList.get(i)/4 - (bounds.height() / 2), bounds.width() + 10, myPaint3);

                    textPaint.getTextBounds(String.valueOf(i), 0, String.valueOf(i).length(), bounds);

//                    if (i < 10 && i>1) {
//                        tempCanvas.drawCircle(ChildFragment.xcoordList.get(i)/4, ChildFragment.ycoordList.get(i)/4 - (bounds.height() / 2), bounds.width() + 6, myPaint3);
//                    } else if (i==1) {
//                        tempCanvas.drawCircle(ChildFragment.xcoordList.get(i)/4, ChildFragment.ycoordList.get(i)/4 - (bounds.height() / 2), bounds.width() + 15, myPaint3);
//                    }
//                    else {
//                        tempCanvas.drawCircle(ChildFragment.xcoordList.get(i)/4, ChildFragment.ycoordList.get(i)/4 - (bounds.height() / 2), bounds.width() + 3, myPaint3);
//                    };

                    //      tempCanvas.drawText(String.valueOf(i + 1), xCoordList.get(i) / 4, yCoordList.get(i) / 4, textPaint);
                    tempCanvas.save();
                    rootView.addView(circle);
                    rootView.invalidate();

                    //animation
//                    ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    fade_in.setDuration(2000);     // animation duration in milliseconds
//                    fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
//                    circle.startAnimation(fade_in);
                    // circle.animate().scaleX(1.2f).scaleY(1.2f).setDuration(2000);
//                    Animation a = AnimationUtils.loadAnimation(context, R.anim.animation_scale_circle);
//                    circle.startAnimation(a);

                }
            }
        }
        if (viewHolderWeakReference != null && bitmap != null) {
            imageView = viewHolderWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setImageDrawable(new BitmapDrawable(tempBitmapTest));
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