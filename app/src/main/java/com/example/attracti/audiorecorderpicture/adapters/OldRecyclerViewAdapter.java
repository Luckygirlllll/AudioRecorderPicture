package com.example.attracti.audiorecorderpicture.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.activities.AudioRecord;
import com.example.attracti.audiorecorderpicture.activities.FirstscreenActivity;
import com.example.attracti.audiorecorderpicture.async.BitmapWorkerTask;
import com.example.attracti.audiorecorderpicture.fragments.CameraFragment;
import com.example.attracti.audiorecorderpicture.model.Folder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * This is Adapter of the RecyclerViewFragment, you can delete soon this Adapter
 */

public class OldRecyclerViewAdapter extends RecyclerView.Adapter<OldRecyclerViewAdapter.ViewHolder> {

    private final Activity context;
    public static HashMap<Integer, BitmapWorkerTask> TASKS_MAP = new HashMap<>();
    private final ArrayList<Folder> FOLDERS;
    private ArrayList<File> FILES;

    // TODO: 5/25/16 Create List of all files in the exact folder

    //---------------Canvas
    private GestureDetector mDetector;
    static ArrayList xcoordin = new ArrayList();
    static ArrayList ycoordin = new ArrayList();
    static Canvas tempCanvas;
    static Bitmap tempBitmap;
    static Paint myPaint;
    public static Paint textPaint;


    public static ViewHolder vh;
    View view;

    // coordinates of LongPress
    public static int x;
    public static int y;

    // file with the labels coordinates
    public static File labelFile;
    static int position;

    public static ArrayList<Integer> xcoordList = new ArrayList();
    public static ArrayList<Integer> ycoordList = new ArrayList();
    public static ArrayList<Integer> positionList = new ArrayList<>();


    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        Log.wtf("TAG", "Folders size: " + FOLDERS.size());
        return AudioRecord.getArratBitmap().size();
    }


    // optimisation of bitmap

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String path,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public void loadBitmap(int position, String path, ImageView imageView) {
        final String imageKey = String.valueOf(path);

        // final Bitmap bitmap = getBitmapFromMemCache(imageKey);
//        if (bitmap != null) {
//            imageView.setImageBitmap(bitmap);
//        } else {

        BitmapWorkerTask task = new BitmapWorkerTask(imageView, position);
        task.execute(path);

        TASKS_MAP.put(position, task);
        //      }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image1;

        public ViewHolder(View v) {
            super(v);
            image1 = (ImageView) v.findViewById(R.id.icon1);
        }
    }

    OldRecyclerViewAdapter adapter = null;

    public OldRecyclerViewAdapter(Activity context, ArrayList<Folder> FOLDERS) {
        this.context = context;
        this.FOLDERS = FOLDERS;
        getItemCount();
        adapter = this;
    }

    @Override
    public OldRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {

        Log.wtf("TAG", "OnCreateViewHolder works!!!");
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mylist2, parent, false);
        vh = new ViewHolder(view);
        return vh;
    }

//    public void setArrayBitmap(ArrayList list){
//        Log.wtf("bitmap", "updateBitmap");
//        this.bitmappaths2=list;
//        Log.wtf("Bitmap ppaths size", String.valueOf(bitmappaths2.size()));
//    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.image1.setImageResource(R.drawable.placeholder);
        this.position = position;

        // mDetector = new GestureDetector(new MyGestureDetector());
        holder.image1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int downX = 0;
                long downTime = 0;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = (int) event.getX();
                        downTime = Calendar.getInstance().getTimeInMillis();
                        return true;

                    case MotionEvent.ACTION_UP:
                        int upX = (int) event.getX();
                        long upTime = Calendar.getInstance().getTimeInMillis();

                        float deltaX = downX - upX;
                        float deltaTime = upTime - downTime;

                        if ((deltaX < 10) && (deltaTime > 1)) {
                            Log.i("Events ", "onLongPress works");
                            x = (int) event.getX();
                            y = (int) event.getY();

                            xcoordList.add(x);
                            ycoordList.add(y);
                            positionList.add(position);

                            Log.i("Events X: ", +x + " Events Y: " + y);
                            adapter.notifyItemChanged(position);
                            long after = System.currentTimeMillis();
                            int difference = (int) (after - AudioRecord.startTimeAudio);
                            int sBody = difference;

                            if (!CameraFragment.mLabelsDirectory.exists() && !CameraFragment.mLabelsDirectory.mkdirs()) {
                                CameraFragment.mLabelsDirectory = null;
                            } else {

                                String labelFileName = FirstscreenActivity.mCurrentProject + ".txt";
                                if (labelFile == null) {
                                    labelFile = new File(CameraFragment.mLabelsDirectory, labelFileName);
                                } else {

                                    FileWriter writer = null;
                                    try {
                                        writer = new FileWriter(labelFile, true);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("Time, X, Y", "Time:" + sBody + " X:" + x + "\n" + "Y" + y + "\n");
                                    try {
                                        writer.append(position + "\n" + sBody + "\n" + x + "\n" + y + "\n");
                                        writer.flush();
                                        writer.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else
                            //REPORT SOMETHING ELSE
                            return true;
                    default:
                        return true;
                }
            }
        });

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;

        if (position < AudioRecord.getArratBitmap().size()) {
            for (int i = 0; i < AudioRecord.getArratBitmap().size(); i++) {
            }
            loadBitmap(position, AudioRecord.getArratBitmap().get(position), holder.image1);
        }

        view.setTag(holder);
    }


//    public static Bitmap getBitmapFromMemCache(String key) {
//       // return com.example.attracti.audiorecorderpicture.RecyclerViewFragment.mMemoryCache.get(key);
//
//    }


    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("Events", "onDown works");
            int xlong = (int) e.getX();
            int ylong = (int) e.getY();
            Log.i("xlong onDown !!!: ", String.valueOf(xlong));
            Log.i("ylong onDown !!!: ", String.valueOf(ylong));


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
                    view.invalidate();
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("Events onLong", ".......");

            x = (int) e.getX();
            y = (int) e.getY();

            //   adapter.notifyItemChanged(position);
            //   adapter.notifyDataSetChanged();

            Log.i("Events X", "case 0 " + x);
            Log.i("Events Y", "case 0 " + y);
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i("X", "case 3 " + x);
                    Log.i("Y", "case 3 " + y);
                    Log.i("Events time onLong down", String.valueOf(e.getDownTime()));
                    Log.i("Events time onLong even", String.valueOf(e.getEventTime()));


//                      MediaPlayer mPlayer2 = audioRecord.getmPlayer();
//                      Log.i("mPlayer2!!!", String.valueOf(mPlayer2));
//                    android.util.Log.i("Time after click", " Time value in milliseconds " + after);
//                      int difference = (int) (after - audioRecord.getStart());
//                Log.i("difference", String.valueOf(difference));

                    int sBody = 60;

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_mm_dd_hh",
                            Locale.getDefault());
                    ;
                    Date now = new Date();
                    String fileName = formatter.format(now) + ".txt";//like 2016_01_12.txt

                    try {
                        File root = new File(Environment.getExternalStorageDirectory(), "Audio_Recorder_Picture");
                        if (!root.exists()) {
                            root.mkdirs();
                        }

                        File gpxfile = new File(root, fileName);

                        FileWriter writer = new FileWriter(gpxfile, true);
                        Log.i("Time, X, Y", "Time:" + sBody + " X:" + x + "\n" + "Y" + y + "\n");
                        writer.append(sBody + "\n" + x + "\n" + y + "\n");
                        writer.flush();
                        writer.close();

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    // readFromFile();
//                    textPaint = new Paint();
//                    tempCanvas.save();
//                    tempCanvas.rotate(-90, x * 6, y * 6);
//                    textPaint.setTextSize(140);
//                    textPaint.setColor(Color.WHITE);
//                    textPaint.setAntiAlias(true);
//                    textPaint.setTextAlign(Paint.Align.CENTER);
//
//                    myPaint.setAntiAlias(true);
//                    Rect bounds = new Rect();
//                    textPaint.getTextBounds(String.valueOf(clicked), 0, String.valueOf(clicked).length(), bounds);
//
//                    if (clicked < 10 && clicked > 1) {
//                        tempCanvas.drawCircle(x * 6, y * 6 - (bounds.height() / 2), bounds.width() + 70, myPaint);
//                    } else if (clicked == 1) {
//                        tempCanvas.drawCircle(x * 6, y * 6 - (bounds.height() / 2), bounds.width() + 95, myPaint);
//                    } else {
//                        tempCanvas.drawCircle(x * 6, y * 6 - (bounds.height() / 2), bounds.width() + 10, myPaint);
//                    }
//                    ;
//
//                    tempCanvas.drawText(String.valueOf(clicked), x * 6, y * 6, textPaint);
//                    clicked++;
//                    tempCanvas.restore();
                    view.invalidate();
            }
        }


        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i("Taghere..", "onDoubleTapEvent");
            return true;
        }
    }
}

