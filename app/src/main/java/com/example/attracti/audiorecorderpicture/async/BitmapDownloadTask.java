package com.example.attracti.audiorecorderpicture.async;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.ViewGroup;
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

    private int x;
    private int y;
    private int update;
    private ImageView imageView;
    private ViewGroup rootView;

    // Matrix
    private int framesPerSecond = 5;
    private long animationDuration = 1000; // 10 seconds
    private Matrix matrix = new Matrix(); // transformation matrix
    private Path path = new Path();
    private long startTime;


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
        final Bitmap bitmap = decodeSampledBitmapFromResource(mData, 100, 100);
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
       //myPaint3.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setTextSize(15);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Rect bounds = new Rect();
      //  CircleDrawView circle=null;
            for (int i = 0; i < activity.getLabelList().size(); i++) {
                if (position == (Integer.parseInt((String) activity.getLabelList().get(i).getPictureName()))) {
                    if(activity.getLabelList().get(i).getxLabel()!=0 &&activity.getLabelList().get(i).getyLabel()!=0) {
//----------animation test
//                        int xLabel = activity.getLabelList().get(i).getxLabel();
//                        int yLabel = activity.getLabelList().get(i).getyLabel();
//                        CircleDrawView circle = new CircleDrawView(activity, xLabel , yLabel, String.valueOf(i));

                        tempCanvas.drawCircle(activity.getLabelList().get(i).getxLabel() / 4, activity.getLabelList().get(i).getyLabel() / 4, 10, myPaint3);
                        textPaint.getTextBounds(String.valueOf(i), 0, String.valueOf(i).length(), bounds);
                        tempCanvas.drawText(String.valueOf(i), activity.getLabelList().get(i).getxLabel() / 4, activity.getLabelList().get(i).getyLabel() / 4, textPaint);

//                        rootView.addView(circle);
//                        rootView.invalidate();
//
//                        if(x!=0 && y!=0 ){
//                            Log.wtf("Animation", "block'");
//                            circle.animate().scaleX(1.2f).scaleY(1.2f).setDuration(2000);
//                        }
                    }
                        if (x != 0 && y != 0) {

                            Paint myPaint4 = new Paint();
                            myPaint4.setAntiAlias(true);
                            myPaint4.setColor(Color.RED);
                            long elapsedTime = System.currentTimeMillis() - startTime;

                            path.addCircle(x/4, y/4, 10, Path.Direction.CW);
                            matrix.setScale(1.1f, 1.1f, x/4-5, y/4-5);
                            path.transform(matrix);
                            tempCanvas.drawPath(path, myPaint4); // draw on canvas

//                        textPaint.getTextBounds(String.valueOf(i), 0, String.valueOf(i).length(), bounds);
//                        tempCanvas.drawText(String.valueOf(i), x / 4, y / 4, textPaint);
                        }
                        //   tempCanvas.save();
                }
            }

        if (viewHolderWeakReference != null && bitmap != null) {
            final ImageView imageView = viewHolderWeakReference.get();

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setImageDrawable(new BitmapDrawable(tempBitmapTest));
                imageView.invalidate();
            }
        }
        // todo: do I need this line of code?
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