package com.example.attracti.audiorecorderpicture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {


    private final Activity context;
    public static HashMap<Integer,BitmapWorkerTask> TASKS_MAP = new HashMap<>();
    private final ArrayList<Folder> FOLDERS;
    private ArrayList<File> FILES;
    // TODO: 5/25/16 Create List of all files in the exact folder

    public static ViewHolder vh;

    View view;


    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        Log.wtf("TAG", "Folders size: " + FOLDERS.size());
        return FOLDERS.size();
    }

    public int getPosition(int position) {
        return position;
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

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {

            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            task.execute(path);

            TASKS_MAP.put(position, task);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image1;
        public ViewHolder(View v) {
            super(v);
            image1 = (ImageView) v.findViewById(R.id.icon1);
        }
    }

    public MyAdapter2(Activity context, ArrayList<Folder> FOLDERS) {
        this.context = context;
        this.FOLDERS = FOLDERS;
        getItemCount();
    }

    @Override
    public MyAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        Log.wtf("TAG", "OnCreateViewHolder works!!!");
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mylist2, parent, false);
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Folder folder = FOLDERS.get(position);

        holder.image1.setImageResource(R.drawable.placeholder);
        ArrayList<String> imgs = folder.getPicturelist();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;

      // loadBitmap(position, imgs.get(0), holder.image1);

        if (position<CameraFragment.bitmappaths.size()) {
                    Log.i("CF Bitmp size: ", String.valueOf(CameraFragment.bitmappaths.size()));
                    loadBitmap(position, CameraFragment.bitmappaths.get(position), holder.image1);
                    Log.i("Bitmappaths in MA2:", CameraFragment.bitmappaths.get(position));
        }

        view.setTag(holder);
    }


    public  Bitmap getBitmapFromMemCache(String key) {
        return com.example.attracti.audiorecorderpicture.RecyclerViewFragment.mMemoryCache.get(key);
    }
}

