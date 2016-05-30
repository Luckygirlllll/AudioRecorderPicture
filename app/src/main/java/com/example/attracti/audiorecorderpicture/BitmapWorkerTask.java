package com.example.attracti.audiorecorderpicture;

/**
 * Created by attracti on 5/23/16.
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
import java.util.ArrayList;

import static com.example.attracti.audiorecorderpicture.MyAdapter.decodeSampledBitmapFromResource;

 /*
  * Download bitmaps in AsyncTask
  */


class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> viewHolderWeakReference;
    private String data;

     static Bitmap tempBitmapTest;
     Canvas tempCanvas;

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
        //-----Test

        tempBitmapTest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmapTest);
        tempCanvas.drawBitmap(bitmap, 0, 0, null);
        Paint myPaint3 = new Paint();

        myPaint3.setColor(Color.RED);
        ArrayList <Integer> xcoord = new ArrayList<>();
        xcoord.add(50);
        xcoord.add(90);
        xcoord.add(120);

        ArrayList <Integer> ycoord =new ArrayList<>();
        ycoord.add(60);
        ycoord.add(100);
        ycoord.add(130);

        for (int j=0; j<xcoord.size(); j++){
            tempCanvas.drawCircle(xcoord.get(j), ycoord.get(j), 20, myPaint3);
        }

        //tempCanvas.drawCircle(50, 50, 20, myPaint3);
        if(MyAdapter2.x!=0 && MyAdapter2.y!=0) {
            tempCanvas.drawCircle(MyAdapter2.x / 2, MyAdapter2.y / 2, 20, myPaint3);
        }
        tempCanvas.save();
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.i("onPostExecute", "works!");
        if (viewHolderWeakReference != null && bitmap != null) {
            final ImageView imageView= viewHolderWeakReference.get();
            if (imageView != null){
                imageView.setImageBitmap(bitmap);
                imageView.setImageDrawable(new BitmapDrawable(BitmapWorkerTask.tempBitmapTest));
                Paint myPaint4 = new Paint();
                Log.i("Events X in Async: ", MyAdapter2.x + " Y in Async: " + MyAdapter2.y);
                if (MyAdapter2.x>0 && MyAdapter2.y<0 ){
                    tempCanvas.drawCircle(MyAdapter2.x/2, MyAdapter2.y/2, 20, myPaint4);
                }
                imageView.invalidate();
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


