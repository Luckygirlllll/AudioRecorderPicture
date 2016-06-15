package com.example.attracti.audiorecorderpicture.async;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.activities.FirstscreenActivity;
import com.example.attracti.audiorecorderpicture.adapters.OldRecyclerViewAdapter;

import java.lang.ref.WeakReference;

import static com.example.attracti.audiorecorderpicture.adapters.RecyclerViewAdapter.decodeSampledBitmapFromResource;

/**
 * Created by Iryna on 5/23/16.
 * <p>
 * In this class going the process of downloading bitmaps in AsyncTask
 * This class responsible for the dowloading pictures for the first screen
 * doesn't use it any more, this class can be deleted soon
 */

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> viewHolderWeakReference;
    private String data;
    private static int position;

    static Bitmap tempBitmapTest;
    Canvas tempCanvas;

    public BitmapWorkerTask(ImageView imageView, int position) {
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
        tempCanvas.save();
        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.i("onPostExecute", "works!");
        if (viewHolderWeakReference != null && bitmap != null) {
            final ImageView imageView = viewHolderWeakReference.get();

            tempBitmapTest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
            tempCanvas = new Canvas(tempBitmapTest);
            tempCanvas.drawBitmap(bitmap, 0, 0, null);
            Paint myPaint3 = new Paint();
            myPaint3.setAntiAlias(true);
            myPaint3.setColor(Color.RED);
            tempCanvas.drawCircle(200, 200, 200, myPaint3);

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setImageDrawable(new BitmapDrawable(BitmapWorkerTask.tempBitmapTest));

                Log.i("Events X in Async: ", OldRecyclerViewAdapter.x + " Y in Async: " + OldRecyclerViewAdapter.y);
//                if (OldRecyclerViewAdapter.x>0 && OldRecyclerViewAdapter.y<0 ){
//                    tempCanvas.drawCircle(OldRecyclerViewAdapter.x/2, OldRecyclerViewAdapter.y/2, 20, myPaint4);
//                }
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


