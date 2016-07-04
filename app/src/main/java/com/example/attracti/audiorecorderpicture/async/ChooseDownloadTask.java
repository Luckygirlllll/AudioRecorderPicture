package com.example.attracti.audiorecorderpicture.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import static com.example.attracti.audiorecorderpicture.adapters.RecyclerViewAdapter.decodeSampledBitmapFromResource;

/**
 * Created by Iryna on 7/1/16.
 *
 * class which is responsible for the downloading pictures for the ChooseActivity (Choosing
 * pictures of the certain folder and displaying them on the screen).
 */

public class ChooseDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> viewHolderWeakReference;
    private String data = null;

    private ImageView imageView;

    public ChooseDownloadTask(ImageView imageView) {
        viewHolderWeakReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        data = String.valueOf(params[0]);
        final Bitmap bitmap = decodeSampledBitmapFromResource(data, 100, 100);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        imageView = viewHolderWeakReference.get();

        if (imageView != null) {
            imageView.setImageBitmap(bitmap);

        }
    }
}
