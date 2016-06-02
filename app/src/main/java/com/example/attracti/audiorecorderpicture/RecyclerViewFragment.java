package com.example.attracti.audiorecorderpicture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Iryna on 5/25/16.
 * <p/>
 * This is RecyclerView where each item is a picture from the Camera
 */


public class RecyclerViewFragment extends Fragment implements AudioRecord.UpdateRecyckerView, RecyclerItemClickListener.OnItemClickListener {

    public MyAdapter2 mAdapter;
    private LinearLayoutManager mLayoutManager;

    RecyclerView list;

    File[] listFile;
    File[] listFolders;

    ArrayList<Folder> FOLDERS = new ArrayList<>();
    public static LruCache<String, Bitmap> mMemoryCache;

    MyAdapter2 myAdapter2 = new MyAdapter2(getActivity(), FOLDERS);

    //---- Canvas
    Canvas tempCanvas;
    Bitmap tempBitmap;
    Paint myPaint;
    Paint textPaint;
    static int clicked = 1;
    static File gpxfile;
    private GestureDetectorCompat DoubleTap;


    public void getFromSdcardFolders() {
        File file = new File(Environment.getExternalStorageDirectory() +
                "/Audio_Recorder_Picture", "Picture");
        if (file.isDirectory()) {
            listFolders = file.listFiles();
            for (int i = 0; i < listFolders.length; i++) {

                Folder folderobject = new Folder();
                folderobject.setName(listFolders[i].getName());

                File picturelist = new File(Environment.getExternalStorageDirectory() +
                        "/Audio_Recorder_Picture/Picture", listFolders[i].getName());
                if (picturelist.isDirectory()) {
                    listFile = picturelist.listFiles();
                    for (int j = 0; j < listFile.length; j++) {
                        folderobject.addFile(listFile[j].getAbsolutePath());
                    }
                }
                FOLDERS.add(folderobject);
                //   Log.wtf("TAG", "Folders size inside the getFRom:" + FOLDERS.size());
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.recyclerview, container, false);

        list = (RecyclerView) rootView.findViewById(R.id.list);
        //  list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        DoubleTap = new GestureDetectorCompat(getActivity(), new MyGestureListener());
        // list.onInterceptTouchEvent()

        //    list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));

        /*
        * Check when view has been recycled
        */

        list.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                Log.i("TAG", "View has been recycled");
                int adaptposition = MyAdapter2.vh.getAdapterPosition();
                Log.wtf("TAG", "adapter position of the recycled item: " + adaptposition);
                // TODO: 5/24/16  cancel AsyncTask of the exactly position isCancelled(), cancel()
                // BitmapWorkerTask.cancel(true)

                Iterator iterator = MyAdapter2.TASKS_MAP.keySet().iterator();

                if (MyAdapter2.TASKS_MAP.get(adaptposition) != null) {
                    //  MyAdapter2.TASKS_MAP.get(adaptposition).cancel(true);
                    Log.wtf("TAG", "AsyncTask was cancelled! " + adaptposition);
                }
            }

            //           }
        });

        list.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int position1 = mLayoutManager.findFirstVisibleItemPosition();
                int position2 = mLayoutManager.findLastVisibleItemPosition();
                Log.wtf("TAG", "First visible position " + position1);
                Log.wtf("TAG", "Last visible position " + position2);
            }
        });


        list.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.findLastCompletelyVisibleItemPosition();

        getFromSdcardFolders();

        list.setLayoutManager(mLayoutManager);

        // Log.wtf("TAG", "Folders size!!:" + FOLDERS.size());
        mAdapter = new MyAdapter2(getActivity(), FOLDERS);
        list.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache
                = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        tempBitmap = Bitmap.createBitmap(4000, 3000, Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmap);


        return rootView;
    }

    @Override
    public void update(final int position) {
        Log.i("Position: ", String.valueOf(position - 1));
        Log.i("GetCount: ", String.valueOf(myAdapter2.getItemCount()));
        // myAdapter2.notifyDataSetChanged();
//        list.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.wtf("Runnable.run()","->>>>>>>>>>>>");
        list.scrollToPosition(myAdapter2.getItemCount() - 1);

        //       }
        //    });
    }

    @Override
    public void onItemClick(View childView, int position) {
        Log.i("onItemClick", String.valueOf(position));
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        Log.i("onItemLongPress", String.valueOf(position));
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("...", "onDown works");
            int xlong = (int) e.getX();
            int ylong = (int) e.getY();
            Log.wtf("Coordinates of xlong: ", String.valueOf(xlong));
            Log.wtf("Coordinates of ylong: ", String.valueOf(ylong));

            ArrayList xcoordin = CameraFragment.getXcoordin();
            ArrayList ycoordin = CameraFragment.getYcoordin();

            Log.wtf("Xcoordin cameraFragm", String.valueOf(CameraFragment.getXcoordin()));
            Log.wtf("Ycoordin cameraFragm", String.valueOf(CameraFragment.getYcoordin()));

            Log.wtf("Xcoordin size", String.valueOf(xcoordin.size()));
            Log.wtf("Ycoordin size", String.valueOf(ycoordin.size()));

            for (int i = 0; i < xcoordin.size(); i++) {
                Log.i("Xcoordin", (String) xcoordin.get(i));
                Log.i("Ycoordin", (String) ycoordin.get(i));
                int xfile = Integer.parseInt((String) xcoordin.get(i));
                int yfile = Integer.parseInt((String) ycoordin.get(i));

                if ((xlong < xfile + 50 && xlong > xfile - 50) && (ylong < yfile + 50 && ylong > yfile - 50)) {
                    //   audioRecord.startPlayingPictureLabel(i);
                    Log.i("Index i of the label", String.valueOf(i));

                    myPaint.setColor(Color.BLUE);
                    tempCanvas.save();
                    tempCanvas.rotate(-90, xfile * 6, yfile * 6);
                    textPaint.setTextSize(140);

                    textPaint.setColor(Color.WHITE);
                    textPaint.setAntiAlias(true);
                    textPaint.setTextAlign(Paint.Align.CENTER);

                    myPaint.setAntiAlias(true);
                    Rect bounds = new Rect();
                    textPaint.getTextBounds(String.valueOf(i + 1), 0, String.valueOf(i + 1).length(), bounds);
                    if (i + 1 < 10 && i + 1 > 1) {
                        tempCanvas.drawCircle(xfile * 6, yfile * 6 - (bounds.height() / 2), bounds.width() + 70, myPaint);
                    } else if (i + 1 == 1) {
                        tempCanvas.drawCircle(xfile * 6, yfile * 6 - (bounds.height() / 2), bounds.width() + 95, myPaint);
                    } else {
                        tempCanvas.drawCircle(xfile * 6, yfile * 6 - (bounds.height() / 2), bounds.width() + 10, myPaint);
                    }
                    ;

                    tempCanvas.drawText(String.valueOf(i + 1), xfile * 6, yfile * 6, textPaint);
                    tempCanvas.restore();
                    list.invalidate();
                }
            }
            return true;
        }
    }

    // @Override
    public void onLongPress(MotionEvent e) {
        Log.d("...", "onLongPress сработал");
        int x = (int) e.getX();
        int y = (int) e.getY();

        Log.i("X", "case 0 " + x);
        Log.i("Y", "case 0 " + y);
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("X", "case 3 " + x);
                Log.i("Y", "case 3 " + y);

                //  MediaPlayer mPlayer2 = audioRecord.getmPlayer();
                //  Log.i("mPlayer2!!!", String.valueOf(mPlayer2));

                //                   if (mPlayer2 == null) {
                long after = System.currentTimeMillis();
                android.util.Log.i("Time after click", " Time value in milliseconds " + after);
                //  int difference = (int) (after - audioRecord.getStart());
//                Log.i("difference", String.valueOf(difference));
//
//                int sBody = difference;

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_mm_dd_hh",
                        Locale.getDefault());
                ;
                Date now = new Date();
                String fileName = formatter.format(now) + ".txt";//like 2016_01_12.txt

//                try {
//                    File root = new File(Environment.getExternalStorageDirectory(), "Audio_Recorder_Picture");
//                    if (!root.exists()) {
//                        root.mkdirs();
//                    }
//                    gpxfile = new File(root, fileName);

//                    FileWriter writer = new FileWriter(gpxfile, true);
//                    Log.i("Time, X, Y", "Time:" + sBody + " X:" + x + "\n" + "Y" + y + "\n");
//                    writer.append(sBody + "\n" + x + "\n" + y + "\n");
//                    writer.flush();
//                    writer.close();

//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
                // readFromFile();
                textPaint = new Paint();
                tempCanvas.save();
                tempCanvas.rotate(-90, x * 6, y * 6);
                textPaint.setTextSize(140);
                textPaint.setColor(Color.WHITE);
                textPaint.setAntiAlias(true);
                textPaint.setTextAlign(Paint.Align.CENTER);

                myPaint.setAntiAlias(true);
                Rect bounds = new Rect();
                textPaint.getTextBounds(String.valueOf(clicked), 0, String.valueOf(clicked).length(), bounds);

                if (clicked < 10 && clicked > 1) {
                    tempCanvas.drawCircle(x * 6, y * 6 - (bounds.height() / 2), bounds.width() + 70, myPaint);
                } else if (clicked == 1) {
                    tempCanvas.drawCircle(x * 6, y * 6 - (bounds.height() / 2), bounds.width() + 95, myPaint);
                } else {
                    tempCanvas.drawCircle(x * 6, y * 6 - (bounds.height() / 2), bounds.width() + 10, myPaint);
                }
                ;

                tempCanvas.drawText(String.valueOf(clicked), x * 6, y * 6, textPaint);
                clicked++;
                tempCanvas.restore();
                //    view.invalidate();
        }
    }
}





