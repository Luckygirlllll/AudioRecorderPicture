package com.example.attracti.audiorecorderpicture.fragments;

/**
 * Created by Iryna on 4/28/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.attracti.audiorecorderpicture.R;
import com.example.attracti.audiorecorderpicture.Statics;
import com.example.attracti.audiorecorderpicture.activities.AudioRecord;
import com.example.attracti.audiorecorderpicture.interfaces.OnHeadlineSelectedListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * In this Fragment is going the process of making pictures
 * AudioRecord is the activity of this fragment
 */


public class CameraFragment extends Fragment
        implements SurfaceHolder.Callback, AudioRecord.TakePictureListener, AudioRecord.SavePictureListener,
        AudioRecord.ReceivePictureListener {

    OnHeadlineSelectedListener mCallback;

    private static final String LOG_TAG = CameraFragment.class.getSimpleName();

    public void setCallback(OnHeadlineSelectedListener callback) {
        mCallback = callback;
    }

    public final String EXTRA_CAMERA_DATA = "camera_data";

    private String TAG = CameraFragment.class.getSimpleName();
    private Context context;
    private AudioRecord activity;

    private final String KEY_IS_CAPTURING = "is_capturing";
    private final int TAKE_PICTURE_REQUEST_B = 100;

    private Camera mCamera = null;
    private ImageView mCameraImage;
    private SurfaceView mCameraPreview;
    private Button mCaptureImageButton;
    private byte[] mCameraData;
    private boolean mIsCapturing;

    private Bitmap mCameraBitmap;
    private ImageView mCameraImageView;

    private View view;

    //structure of the project's folders
    private File mAudioDirectory = new File(Statics.mAudioFolder);
    private File mLabelsDirectory = new File(Statics.mLabelsFolder);

    // array of the files of the pictures which have been taken in the current project
    private ArrayList<File> arrayFilepaths = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (AudioRecord) context;

        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.camera_fragment,
                container, false);

        mCameraImage = (ImageView) view.findViewById(R.id.camera_image_view);
        mCameraPreview = (SurfaceView) view.findViewById(R.id.preview_view);


        final SurfaceHolder surfaceHolder = mCameraPreview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mIsCapturing = true;

        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.set("orientation", "portrait");
                mCamera.setParameters(parameters);

                mCamera.setPreviewDisplay(mCameraPreview.getHolder());
                mCamera.setDisplayOrientation(90);

                int rotation = activity.getWindowManager().getDefaultDisplay()
                        .getRotation();

                //setCameraDisplayOrientation(getActivity(), mCamera);
                if (mIsCapturing) {
                    mCamera.startPreview();
                }
            } catch (Exception e) {
                Toast.makeText(context, "Unable to open camera.", Toast.LENGTH_LONG)
                        .show();
            }
        }

        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();

        int x = getActivity().getResources().getConfiguration().orientation;

        return view;
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   android.hardware.Camera camera) {

        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        //android.hardware.Camera.getCameraInfo( info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;

        result = (info.orientation - degrees + 360) % 360;
        camera.setDisplayOrientation(result);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_IS_CAPTURING, mIsCapturing);
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//      //  super.onRestoreInstanceState(savedInstanceState);
//
//        mIsCapturing = savedInstanceState.getBoolean(KEY_IS_CAPTURING, mCameraData == null);
//     //   if (mCameraData != null) {
//     //       setupImageDisplay();
//     //   } else {
//     //       setupImageCapture();
//     //   }
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE_REQUEST_B) {
            if (resultCode == AppCompatActivity.RESULT_OK) {

                if (mCameraBitmap != null) {
                    mCameraBitmap.recycle();
                    mCameraBitmap = null;
                }
                Bundle extras = data.getExtras();
                byte[] cameraData = extras.getByteArray(EXTRA_CAMERA_DATA);
                if (cameraData != null) {
                    mCameraBitmap = BitmapFactory.decodeByteArray(cameraData, 0, cameraData.length);
                    mCameraImageView.setImageBitmap(mCameraBitmap);
                }
            } else {
                mCameraBitmap = null;
            }
        }
    }

    private File openFileForImage() {
        if (!activity.getmPictureDirectory().exists() && !activity.getmPictureDirectory().mkdirs()) {
            //  activity.getmPictureDirectory() = null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss",
                    Locale.getDefault());
            return new File(activity.getmPictureDirectory().getPath() +
                    File.separator + "image_" +
                    dateFormat.format(new Date()) + ".png");
        }
        return null;
    }

    private void saveImageToFile(File file) {

        if (mCameraBitmap != null) {
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(file);
                if (!mCameraBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)) {
                    Toast.makeText(context, "Unable to save image to file.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Saved image to: " + file.getPath(),
                            Toast.LENGTH_LONG).show();
                }
                outStream.close();
            } catch (Exception e) {
                Toast.makeText(context, "Unable to save image to file.",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
                if (mIsCapturing) {
                    mCamera.startPreview();
                }
            } catch (IOException e) {
                Toast.makeText(context, "Unable to start camera preview.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void captureImage() {

        if (mCamera != null) {
            mCamera.takePicture(null, null, new PictureCallback() {

                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                    mCameraData = data;

                    Bitmap picture = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
                    Bitmap resized = ThumbnailUtils.extractThumbnail(picture, 60, 60);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    resized.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss",
                            Locale.getDefault());

                    File mPreviewDirectory = new File(Statics.mPreviewsFolder + "/" + activity.getmCurrentProject());

                    if (!mPreviewDirectory.exists() && !mPreviewDirectory.mkdirs()) {
                        mPreviewDirectory = null;
                    } else {

                    }
                    File previewFile = new File(
                            mPreviewDirectory.getPath() +
                                    File.separator + "preview_" +
                                    dateFormat.format(new Date()) + ".jpg");

                    try {
                        previewFile.createNewFile();
                        FileOutputStream fo = new FileOutputStream(previewFile);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    File mPictureDirectory = new File(Statics.mPictureFolder + "/" + activity.getmCurrentProject());

                    if (!mPictureDirectory.exists() && !mPictureDirectory.mkdirs()) {
                        mPictureDirectory = null;
                    } else {

                        File file = new File(
                                mPictureDirectory.getPath() +
                                        File.separator + "image_" +
                                        dateFormat.format(new Date()) + ".png");

                        // todo: save pictures in the right rotation (orientation)
                        //----- Test---------

//                        Bitmap bMap = BitmapFactory.decodeByteArray(data, 100, 100);
//                        int orientation;
//
//                        if(bMap.getHeight() < bMap.getWidth()){
//                            orientation = 90;
//                        } else {
//                            orientation = 0;
//                        }
//
//                        Bitmap bMapRotate;
//                        if (orientation != 0) {
//                            Matrix matrix = new Matrix();
//                            matrix.postRotate(orientation);
//                            bMapRotate = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(),
//                                    bMap.getHeight(), matrix, true);
//                        } else
//                            bMapRotate = Bitmap.createScaledBitmap(bMap, bMap.getWidth(),
//                                    bMap.getHeight(), true);


                        //-------------------

                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            fos.write(data);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        arrayFilepaths.add(file);
                        mCallback.onArticleSelected(arrayFilepaths);
                        for (int i = 0; i < arrayFilepaths.size(); i++) {
                        }
                    }
                }
            });
        }
    }


    public void savePicture() {
        File saveFile = openFileForImage();
        if (saveFile != null) {
            saveImageToFile(saveFile);
        } else {
            Toast.makeText(context, "Unable to open file for saving image.",
                    Toast.LENGTH_LONG).show();
        }
    }


    public void takePicture() {
        captureImage();
    }

    @Override
    public void recievePicture(Bitmap bitmap) {
        mCameraImage.setImageBitmap(bitmap);
        mCameraImage.setRotation(0);
    }
}


