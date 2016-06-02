package com.example.attracti.audiorecorderpicture;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Iryna on 6/2/16.
 *
 * this Fragment responsible for the showing of the picture of the certain project
 *
 */
public class BitmapFragment extends Fragment {

    public static String BITMAP_TAG = "BITMAP_TAG";

    String file = null;
    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file = getArguments().getString(BITMAP_TAG);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        loadBitmap(file, imageView);
        return rootView;
    }

    public void loadBitmap(String path, ImageView imageView) {
        final String imageKey = String.valueOf(path);
        Log.wtf("Image Key: ", String.valueOf(imageKey));

        final Bitmap bitmap = null;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {

            BitmapDownloadTask task = new BitmapDownloadTask(imageView);
            task.execute(imageKey);

        }
    }


    public static BitmapFragment create(String file) {
        BitmapFragment fragment = new BitmapFragment();
        Bundle args = new Bundle();
        args.putString(BITMAP_TAG, file);
        fragment.setArguments(args);
        return fragment;
    }
}
