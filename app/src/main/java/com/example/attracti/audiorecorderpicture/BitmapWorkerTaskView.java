package com.example.attracti.audiorecorderpicture;

/**
 * Created by attracti on 5/23/16.
 * <p/>
 * You can delete this class after all code for the ViewActivity will be written
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import static com.example.attracti.audiorecorderpicture.MyAdapter.decodeSampledBitmapFromResource;

class BitmapWorkerTaskView extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> viewHolderWeakReference;
    private String data;
    private static int position;

    static Bitmap tempBitmapTest;
    Canvas tempCanvas;

    public BitmapWorkerTaskView(ImageView imageView, int position) {
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
        //-----Test

        tempBitmapTest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmapTest);
        tempCanvas.drawBitmap(bitmap, 0, 0, null);
        Paint myPaint3 = new Paint();
        myPaint3.setColor(Color.RED);

        Set<Integer> uniquePositions = new HashSet<>(MyAdapter2.positionList);

        for (int i = 0; i < AdapterViewProject.filePosition.size(); i++)
            if (position == Integer.parseInt((String) AdapterViewProject.filePosition.get(i))) {
                Log.i("FilePosition: ", String.valueOf(Integer.parseInt((String) AdapterViewProject.filePosition.get(i))));
                tempCanvas.drawCircle(Float.parseFloat((String) AdapterViewProject.xfile.get(i)) / 2, Float.parseFloat((String) AdapterViewProject.yfile.get(i)) / 2, 20, myPaint3);
                Log.i("Events X in Async: ", AdapterViewProject.xfile.get(i) + " Y in Async: " + MyAdapter2.y);
            }
        tempCanvas.save();
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.i("onPostExecute", "works!");
        if (viewHolderWeakReference != null && bitmap != null) {
            final ImageView imageView = viewHolderWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setImageDrawable(new BitmapDrawable(BitmapWorkerTaskView.tempBitmapTest));
                Paint myPaint4 = new Paint();
                Log.i("Events X in Async: ", MyAdapter2.x + " Y in Async: " + MyAdapter2.y);
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


