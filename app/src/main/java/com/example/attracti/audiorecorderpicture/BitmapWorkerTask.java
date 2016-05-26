package com.example.attracti.audiorecorderpicture;

/**
 * Created by attracti on 5/23/16.
 */
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import static com.example.attracti.audiorecorderpicture.MyAdapter.decodeSampledBitmapFromResource;

 /*
  * Download bitmaps in AsyncTask
  */


class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> viewHolderWeakReference;
    private String data;

    public BitmapWorkerTask(ImageView imageView) {
        viewHolderWeakReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Log.i("TAG", "Async task works in background");
        data = String.valueOf(params[0]);
        Log.wtf("Params: ", params[0]);
        final Bitmap bitmap =decodeSampledBitmapFromResource(data, 100, 100);
        addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.i("onPostExecute", "works!");
        if (viewHolderWeakReference != null && bitmap != null) {
            final ImageView imageView= viewHolderWeakReference.get();
            if (imageView != null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }
    public  void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            FirstscreenActivity.mMemoryCache.put(key, bitmap);
        }
    }
    public  Bitmap getBitmapFromMemCache(String key) {
        return FirstscreenActivity.mMemoryCache.get(key);
    }
}


