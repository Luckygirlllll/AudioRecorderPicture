package com.example.attracti.audiorecorderpicture;

import android.content.Context;
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
 * this Fragment responsible for the showing of the picture which just have been captured (ViewFrgament)
 */


public class ChildFragment extends Fragment {

    public Context context;

    public static String BITMAP_TAG = "BITMAP_TAG";
    private static int positionCurrent;

    String file = null;
    ImageView imageView;
    private static int xCoord;
    private static int yCoord;

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
        loadBitmap(file, imageView, positionCurrent, xCoord, yCoord);
        return rootView;
    }

    public static void loadBitmap(String path, ImageView imageView, int position, int x, int y) {
        final String imageKey = String.valueOf(path);
        Log.wtf("Image Key: ", String.valueOf(imageKey));

        final Bitmap bitmap = null;
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {

            ChildDownloadTask task = new ChildDownloadTask(imageView, position, x, y);
            task.execute(imageKey);

        }
    }


    public static ChildFragment createfragment(Context context, String file, int position, int x, int y) {
        xCoord=x;
        yCoord=y;
        positionCurrent=position;
        ChildFragment fragment = new ChildFragment();
        Bundle args = new Bundle();
        args.putString(BITMAP_TAG, file);
        fragment.setArguments(args);
        return fragment;
    }
}
