package com.example.attracti.audiorecorderpicture;

/**
 * Created by Iryna on 4/28/16.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraFragment extends Fragment
        implements SurfaceHolder.Callback, AudioRecord.TakePictureListener, AudioRecord.SavePictureListener {

    private Context context;
    private AppCompatActivity activity;

    public static final String EXTRA_CAMERA_DATA = "camera_data";

    private static final String KEY_IS_CAPTURING = "is_capturing";
    private static final int TAKE_PICTURE_REQUEST_B = 100;

    private Camera mCamera;
    private ImageView mCameraImage;
    private SurfaceView mCameraPreview;
    private Button mCaptureImageButton;
    private static byte[] mCameraData;
    private boolean mIsCapturing;

    static int currentZoomLevel;
    ZoomControls zoomControls;


    private Bitmap mCameraBitmap;
    private ImageView mCameraImageView;
    Bitmap bitmap;
    Canvas tempCanvas;
    Bitmap tempBitmap;
    Paint myPaint;

        private View view;


    private OnClickListener mSaveImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            File saveFile = openFileForImage();
            if (saveFile != null) {
                saveImageToFile(saveFile);
            } else {
                Toast.makeText(context, "Unable to open file for saving image.",
                        Toast.LENGTH_LONG).show();
            }
        }
    };


    private OnClickListener mRecaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            setupImageCapture();
            mCameraImage.setImageBitmap(bitmap);
        }
    };

    private OnClickListener mDoneButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCameraData != null) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CAMERA_DATA, mCameraData);
                activity.setResult(activity.RESULT_OK, intent);
            } else {
                activity.setResult(activity.RESULT_CANCELED);
            }
            activity.finish();
        }
    };

    OnClickListener mCaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            captureImage();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_camera,
                container, false);


        mCameraImage = (ImageView) view.findViewById(R.id.camera_image_view);
        // mCameraImage.setVisibility(View.INVISIBLE);

        mCameraPreview = (SurfaceView) view.findViewById(R.id.preview_view);

        RelativeLayout fragment = (RelativeLayout) view.findViewById(R.id.fragment);
//        fragment.setOnTouchListener(onTouchListener);
        mCameraImage.setOnTouchListener(onTouchListener);

        final SurfaceHolder surfaceHolder = mCameraPreview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCaptureImageButton = (Button) view.findViewById(R.id.capture_image_button);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);

        final Button doneButton = (Button) view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(mDoneButtonClickListener);

        zoomControls = (ZoomControls) view.findViewById(R.id.cameraZoom);

        mIsCapturing = true;

//        if (mCamera == null) {
//            Log.i("Camera open", "Camera open");
        try {
            mCamera = Camera.open();
            Log.i("Camera open2", "Camera open2");
            mCamera.setPreviewDisplay(mCameraPreview.getHolder());
            mCamera.setDisplayOrientation(90);
            if (mIsCapturing) {
                mCamera.startPreview();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Unable to open camera.", Toast.LENGTH_LONG)
                    .show();
            Log.i("Unable to open camera", "Unable to open camera");
        }
        zoom();

         tempBitmap = Bitmap.createBitmap(4000, 4000, Bitmap.Config.RGB_565);
         tempCanvas = new Canvas(tempBitmap);


        return view;
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
           int  x = (int) event.getX();
           int  y = (int) event.getY();

            Log.i("X", "case 0 " + x);
            Log.i("Y", "case 0 " + y);
            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    Log.i("X", "case 1 " + x);
//                    Log.i("Y", "case 1 " + y);
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    Log.i("X", "case 2 " + x);
//                    Log.i("Y", "case 2 " + y);
//                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("X", "case 3 " + x);
                    Log.i("Y", "case 3 " + y);
                    tempCanvas.drawCircle(x*8, y*8, 200,myPaint);
                    view.invalidate();
                    break;
            }
            return true;
        }
    };


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_IS_CAPTURING, mIsCapturing);
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        mIsCapturing = savedInstanceState.getBoolean(KEY_IS_CAPTURING, mCameraData == null);
//        if (mCameraData != null) {
//            setupImageDisplay();
//        } else {
//            setupImageCapture();
//        }
//    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult", "image");
        if (requestCode == TAKE_PICTURE_REQUEST_B) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                Log.i("Bitmap", "bitmap recycled");
                // Recycle the previous bitmap.
                if (mCameraBitmap != null) {
                    mCameraBitmap.recycle();
                    mCameraBitmap = null;
                }
                Bundle extras = data.getExtras();
                byte[] cameraData = extras.getByteArray(CameraFragment.EXTRA_CAMERA_DATA);
                if (cameraData != null) {
                    mCameraBitmap = BitmapFactory.decodeByteArray(cameraData, 0, cameraData.length);
                    mCameraImageView.setImageBitmap(mCameraBitmap);
                    //   mSaveImageButton.setEnabled(true);
                }
            } else {
                mCameraBitmap = null;
            }
        }
    }

    private File openFileForImage() {
        File imageDirectory = null;
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            imageDirectory = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "CameraApp");
            if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
                imageDirectory = null;
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd_hh_mm",
                        Locale.getDefault());

                return new File(imageDirectory.getPath() +
                        File.separator + "image_" +
                        dateFormat.format(new Date()) + ".png");
            }
        }
        return null;
    }

    private void saveImageToFile(File file) {
        Log.i("Save", "Save image");
        if (mCameraBitmap != null) {
            Log.i("Save", "Save image2");
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(file);
                if (!mCameraBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)) {
                    Toast.makeText(context, "Unable to save image to file.",
                            Toast.LENGTH_LONG).show();
                    Log.i("Image", "Unable to save image to file.");
                } else {
                    Toast.makeText(context, "Saved image to: " + file.getPath(),
                            Toast.LENGTH_LONG).show();
                    Log.i("Image", "Saved image to:" + file.getPath());
                }
                outStream.close();
            } catch (Exception e) {
                Toast.makeText(context, "Unable to save image to file.",
                        Toast.LENGTH_LONG).show();
                Log.i("Image", "Unable to save image to file.");
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

    private void captureImage() {
        mCamera.takePicture(null, null, new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.i("Data", "Data: "+data+"Length"+data.length);
                mCameraData = data;
                Log.i("Real saving", "Real saving");
                File imageDirectory = null;
                imageDirectory = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "CameraApp");
                if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
                    imageDirectory = null;
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss",
                            Locale.getDefault());
                    File file = new File(
                            imageDirectory.getPath() +
                                    File.separator + "image_" +
                                    dateFormat.format(new Date()) + ".png");

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
                Log.i("Bitmap height: ", String.valueOf(bitmap.getHeight()));
                Log.i("Bitmap width: ", String.valueOf(bitmap.getWidth()));
                mCameraImage.setImageBitmap(bitmap);
                mCamera.stopPreview();
                mCameraPreview.setVisibility(View.INVISIBLE);
                mCameraImage.setVisibility(View.VISIBLE);
                mCameraImage.setRotation(90);
                mCaptureImageButton.setText(R.string.recapture_image);
                mCaptureImageButton.setOnClickListener(mRecaptureImageButtonClickListener);


                myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                tempCanvas.drawBitmap(bitmap, 0, 0, null);
                myPaint.setColor(Color.RED);
                mCameraImage.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
            }
        });
    }


    private void setupImageCapture() {
        mCameraImage.setVisibility(View.INVISIBLE);
        mCameraPreview.setVisibility(View.VISIBLE);
        mCamera.startPreview();
        mCaptureImageButton.setText(R.string.capture_image);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);
    }

    private void setupImageDisplay() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
        mCameraImage.setImageBitmap(bitmap);
        mCamera.stopPreview();
        mCameraPreview.setVisibility(View.INVISIBLE);
        mCameraImage.setVisibility(View.VISIBLE);
        mCameraImage.setRotation(90);
        mCaptureImageButton.setText(R.string.recapture_image);
        mCaptureImageButton.setOnClickListener(mRecaptureImageButtonClickListener);
    }

    public void zoom() {

        final Camera.Parameters params = mCamera.getParameters();
        if (params.isZoomSupported()) {
            final int maxZoomLevel = params.getMaxZoom();
            Log.i("max ZOOM ", "is " + maxZoomLevel);
            zoomControls.setIsZoomInEnabled(true);
            zoomControls.setIsZoomOutEnabled(true);


            zoomControls.setIsZoomInEnabled(true);
            zoomControls.setIsZoomOutEnabled(true);

            zoomControls.setOnZoomInClickListener(new OnClickListener() {
                public void onClick(View v) {
                    currentZoomLevel = params.getZoom();
                    if (currentZoomLevel < maxZoomLevel) {
                        currentZoomLevel++;
                        mCamera.startSmoothZoom(currentZoomLevel);
                        params.setZoom(currentZoomLevel);
                        mCamera.setParameters(params);
                    }
                }
            });

            zoomControls.setOnZoomOutClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (currentZoomLevel > 0) {
                        currentZoomLevel--;
                        params.setZoom(currentZoomLevel);
                        mCamera.setParameters(params);
                    }
                }
            });
        } else
            zoomControls.setVisibility(View.GONE);
    }

    public void savePicture() {
        File saveFile = openFileForImage();
        if (saveFile != null) {
            saveImageToFile(saveFile);
            Log.i("Trying to save file", "Trying to save file");
        } else {
            Toast.makeText(context, "Unable to open file for saving image.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void takePicture() {

    }
}


