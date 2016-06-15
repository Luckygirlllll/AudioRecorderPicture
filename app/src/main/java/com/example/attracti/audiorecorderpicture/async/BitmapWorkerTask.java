package com.example.attracti.audiorecorderpicture.async;

/**
 * Created by Iryna on 5/23/16.
 * <p>
 * In this class going the process of downloading bitmaps in AsyncTask
 */

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class responsible for the dowloading pictures for the first screen
 */


import static com.example.attracti.audiorecorderpicture.adapters.RecyclerViewAdapter.decodeSampledBitmapFromResource;

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
        //-----Test

        tempBitmapTest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmapTest);
        tempCanvas.drawBitmap(bitmap, 0, 0, null);
        Paint myPaint3 = new Paint();

        myPaint3.setColor(Color.RED);
        ArrayList<Integer> xcoord = new ArrayList<>();
        xcoord.add(50);
        xcoord.add(90);
        xcoord.add(120);
        xcoord.add(140);

        ArrayList<Integer> ycoord = new ArrayList<>();
        ycoord.add(60);
        ycoord.add(100);
        ycoord.add(130);
        ycoord.add(150);

        // OldRecyclerViewAdapter.xcoordList;
        // OldRecyclerViewAdapter.ycoordList;
        // OldRecyclerViewAdapter.positionList;

//        AdapterViewProject.xfile;
//        AdapterViewProject.yfile;
//        AdapterViewProject.filePosition;


        Set<Integer> uniquePositions = new HashSet<>(OldRecyclerViewAdapter.positionList);

//        for(int i=0; i<uniquePositions.size();i++ )
//        if (position == OldRecyclerViewAdapter.positionList.get(i) && OldRecyclerViewAdapter.xcoordin!=null && OldRecyclerViewAdapter.ycoordin!=null ) {
//                tempCanvas.drawCircle( (Float)OldRecyclerViewAdapter.xcoordin.get(i)/2,(Float) OldRecyclerViewAdapter.ycoordin.get(i)/2 , 20, myPaint3);
//                Log.i("Events X in Async: ", xcoord.get(i) + " Y in Async: " + ycoord.get(i));
//            }
//                tempCanvas.restore();
        //  tempCanvas.drawCircle(50, 50, 20, myPaint3);

//            }
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
                imageView.setImageDrawable(new BitmapDrawable(BitmapWorkerTask.tempBitmapTest));
                Paint myPaint4 = new Paint();
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


