package com.example.attracti.audiorecorderpicture.async;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    ArrayList<CircleDrawView> circleList = new ArrayList<>();
    private CircleDrawView circle;

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
        
        if (positionList != null) {
            for (int i = 0; i < positionList.size(); i++) {
                if (position == (positionList.get(positionList.size() - 1))) {
                    circle = new CircleDrawView(context, xCoordList.get(xCoordList.size() - 1), yCoordList.get(yCoordList.size() - 1), String.valueOf(i + 1));
                    tempCanvas.save();
                    rootView.addView(circle);
                    rootView.invalidate();
                }
            }
        }
        if (circle != null) {
            circleList.add(circle);
//        Animation a = AnimationUtils.loadAnimation(context, R.anim.animation_scale_circle);
//        circleList.get(circleList.size()-1).startAnimation(a);

            circleList.get(circleList.size() - 1).animate().scaleX(1.5f).scaleY(1.5f).setDuration(2000).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    circleList.get(circleList.size() - 1).animate().scaleX(0.8f).scaleY(0.8f).setDuration(2000);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });



//            CircleDrawView circle = circleList.get(circleList.size()-1);
//            ObjectAnimator animator = ObjectAnimator.ofFloat(circle, "scaleX", 2.5f);
//            ObjectAnimator animator2 = ObjectAnimator.ofFloat(circle, "scaleY", 2.5f);
//            AnimatorSet set = new AnimatorSet();
//            set.playTogether(animator,animator2);
//            set.setDuration(3000);
//            set.start();

//            circleList.get(circleList.size()-1).animate().scaleX(1.2f).scaleY(1.2f).setDuration(2000);
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