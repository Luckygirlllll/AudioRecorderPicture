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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by attracti on 5/23/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final Activity context;

    private final ArrayList<Folder> FOLDERS;
   // Activity MainActivity = new MainActivity();

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

    public void loadBitmap(String path, ImageView imageView) {
        final String imageKey = String.valueOf(path);

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            // imageView.setImageResource(R.drawable.image_placeholder);
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            task.execute(path);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView title;
        public ImageView image1;
        ImageView image2;
        ImageView image3;
        ImageView image4;
        ImageView image5;
        TextView slides;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.item);
            image1 = (ImageView) v.findViewById(R.id.icon1);
            image2 = (ImageView) v.findViewById(R.id.icon2);
            image3 = (ImageView) v.findViewById(R.id.icon3);
            image4 = (ImageView) v.findViewById(R.id.icon4);
            image5 = (ImageView) v.findViewById(R.id.icon5);
            slides = (TextView) v.findViewById(R.id.textView1);
        }
    }

    public MyAdapter(Activity context, ArrayList<Folder> FOLDERS) {
        this.context = context;
        this.FOLDERS = FOLDERS;
        getItemCount();
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        Log.wtf("TAG", "OnCreateViewHolder works!!!");
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mylist, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Folder folder = FOLDERS.get(position);


        holder.image1.setImageResource(R.drawable.placeholder);
        holder.image2.setImageResource(R.drawable.placeholder);
        holder.image3.setImageResource(R.drawable.placeholder);
        holder.image4.setImageResource(R.drawable.placeholder);
        holder.image5.setImageResource(R.drawable.placeholder);


        ArrayList<String> imgs = folder.getPicturelist();

        holder.title.setText(folder.getName());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 10;


        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    if (imgs.size()>i && imgs.size()!=0) {
                        loadBitmap(imgs.get(i), holder.image1);
                    }
                    else{
                        holder.image1.setImageResource(R.drawable.placeholder);
                    }
                    break;
                case 1:
                    if (imgs.size()>i && imgs.size()!=0 ) {
                        loadBitmap(imgs.get(i), holder.image2);
                    }
                    else{
                        holder.image2.setImageResource(R.drawable.placeholder);
                    }
                    break;
                case 2:
                    if (imgs.size()>i && imgs.size()!=0) {
                        loadBitmap(imgs.get(i), holder.image3);
                    }
                    else{
                        holder.image3.setImageResource(R.drawable.placeholder);
                    }
                    break;
                case 3:
                    if (imgs.size()>i && imgs.size()!=0) {
                        loadBitmap(imgs.get(i), holder.image4);
                    }
                    else{
                        holder.image4.setImageResource(R.drawable.placeholder);
                    }
                    break;
                case 4:
                    if (imgs.size()>i && imgs.size()!=0) {
                        loadBitmap(imgs.get(i), holder.image5);
                    }
                    else{
                        holder.image5.setImageResource(R.drawable.placeholder);
                    }
                    break;
            }
        }

        holder.slides.setText("Количество слайдов: " + imgs.size());
        view.setTag(holder);
    }


    public  Bitmap getBitmapFromMemCache(String key) {
        return com.example.attracti.audiorecorderpicture.FirstscreenActivity.mMemoryCache.get(key);
    }
}

