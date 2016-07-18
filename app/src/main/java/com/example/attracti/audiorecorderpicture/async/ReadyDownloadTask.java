package com.example.attracti.audiorecorderpicture.async;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.activities.ReadyRecordActivity;
import com.example.attracti.audiorecorderpicture.interfaces.OnCreateCanvasListener;

import java.lang.ref.WeakReference;

import static com.example.attracti.audiorecorderpicture.adapters.RecyclerViewAdapter.decodeSampledBitmapFromResource;

/**
 * Created by Iryna on 7/11/16.
 */
public class ReadyDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private final  String LOG_TAG = BitmapDownloadTask.class.getSimpleName();

    private final WeakReference<ImageView> viewHolderWeakReference;
    private String mData = null;

    private int position;

    OnCreateCanvasListener canvasListener;
    ReadyRecordActivity activity;

    private int x;
    private int y;
    private int update;
    private ImageView imageView;

    // Matrix
    private int framesPerSecond = 5;
    private long animationDuration = 1000; // 10 seconds
    private Matrix matrix = new Matrix(); // transformation matrix
    private Path path = new Path();
    private long startTime;


    public ReadyDownloadTask(ReadyRecordActivity activity, OnCreateCanvasListener canvasListener, ImageView imageView, int position, int x, int y , int update) {
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

        if (viewHolderWeakReference != null && bitmap != null) {
            final ImageView imageView = viewHolderWeakReference.get();

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);

               // imageView.setImageDrawable(new BitmapDrawable(tempBitmapTest));
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
