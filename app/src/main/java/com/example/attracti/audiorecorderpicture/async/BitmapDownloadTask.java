package com.example.attracti.audiorecorderpicture.async;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.activities.ViewActivity;
import com.example.attracti.audiorecorderpicture.interfaces.OnCreateCanvasListener;
import com.example.attracti.audiorecorderpicture.utils.Statics;
import com.example.attracti.audiorecorderpicture.views.CircleDrawView;

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

    private int position;

    OnCreateCanvasListener canvasListener;
    ViewActivity activity;

    private int x;
    private int y;
    private int update;
    private ImageView imageView;
    private ViewGroup rootView;
    private long startTime;

    private CircleDrawView circle;


    public BitmapDownloadTask(ViewGroup rootView, ViewActivity activity, OnCreateCanvasListener canvasListener, ImageView imageView, int position, int x, int y , int update) {
        this.rootView=rootView;
        this.activity = activity;
        this.canvasListener = canvasListener;
        viewHolderWeakReference = new WeakReference<ImageView>(imageView);
        this.imageView=imageView;
        this.position = position;
        this.x=x;
        this.y=y;
        this.update=update;
        // start the animation:
        this.startTime = System.currentTimeMillis();
        imageView.postInvalidate();
    }


    @Override
    protected Bitmap doInBackground(String... params) {

        mData = String.valueOf(params[0]);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap bitmap = decodeSampledBitmapFromResource(mData, 100, 100);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);


        //addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {

        tempBitmapTest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmapTest);
        tempCanvas.drawBitmap(bitmap, 0, 0, null);

        CircleDrawView circle = null;
        int firstLabelName = 0;

            for (int i = 0; i < activity.getLabelList().size(); i++) {
                if (position == (Integer.parseInt((String) activity.getLabelList().get(i).getPictureName()))) {

                    if (activity.getLabelList().get(i).getxLabel() != 0 && activity.getLabelList().get(i).getyLabel() != 0) {
                        circle = new CircleDrawView(activity, activity.getLabelList().get(i).getxLabel(), activity.getLabelList().get(i).getyLabel(), String.valueOf(Statics.alphabetEnglish.get(firstLabelName)));
                        rootView.addView(circle);
                        rootView.invalidate();
                        firstLabelName++;

                    }
                        if (x != 0 && y != 0) {
                            int letterName = 0;
                            circle = new CircleDrawView(activity, x, y, String.valueOf(Statics.alphabetEnglish.get(letterName)));
                            rootView.addView(circle);
                            rootView.invalidate();
                            final CircleDrawView finalCircle = circle;
                            circle.animate().scaleX(1.5f).scaleY(1.5f).setDuration(2000).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    finalCircle.animate().scaleX(0.8f).scaleY(0.8f).setDuration(2000);

                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            letterName++;
                        }
                }
            }

        if (viewHolderWeakReference != null && bitmap != null) {
            final ImageView imageView = viewHolderWeakReference.get();

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                imageView.invalidate();
            }
        }

        // canvasListener.saveCanvas(tempCanvas, position);
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