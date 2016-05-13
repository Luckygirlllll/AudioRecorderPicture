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
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    MediaPlayer mPlayer2;

    private GestureDetectorCompat DoubleTap;

    static File gpxfile;

    public static File getGpxfile() {
        return gpxfile;
    }

    static String[] filetime2 = new String[100];

    static ArrayList filetime3 = new ArrayList();

    static ArrayList xcoordin = new ArrayList();
    static ArrayList ycoordin = new ArrayList();

    AudioRecord audioRecord;

    public static ArrayList getXcoordin() {
        return xcoordin;
    }

    public static ArrayList getYcoordin() {
        return ycoordin;
    }

    public static ArrayList getFiletime3() {
        return filetime3;
    }


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
        audioRecord = (AudioRecord) context;
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
        DoubleTap = new GestureDetectorCompat(getActivity(), new MyGestureListener());
        mCameraImage.setOnTouchListener(onTouchListener);
        mCameraImage.setOnLongClickListener(onLongClickListener);

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

        tempBitmap = Bitmap.createBitmap(4000, 3000, Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmap);

        return view;
    }


    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Log.i("Long click", "Long click occured");
            return true;
        }
    };


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            return DoubleTap.onTouchEvent(event);
        }
    };

    public void readFromFile() {
        Log.i("read ", "in Fragment");
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(gpxfile));
            String line;

            while ((line = br.readLine()) != null) {

                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("TextInfo", String.valueOf(text));

        filetime2 = text.toString().split("\n");
        //  Arrays.sort(filetime2);
        for (int i = 0; i < filetime2.length; i = i + 3) {
            Log.i("Array 2", filetime2[i]);
            String n = filetime2[i];
            filetime3.add(filetime2[i]);
            Log.i("FIletime 3 size", String.valueOf(filetime3.size()));
        }
        for (int i = 1; i < filetime2.length; i = i + 3) {
            Log.i("Coordinates of X: ", filetime2[i]);
            String n = filetime2[i];
            xcoordin.add(filetime2[i]);
        }

        for (int i = 2; i < filetime2.length; i = i + 3) {
            Log.i("Coordinates of Y: ", filetime2[i]);
            String n = filetime2[i];
            ycoordin.add(filetime2[i]);
        }
    }


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
                Log.i("Data", "Data: " + data + "Length" + data.length);
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

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("...", "onDown сработал");
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


                if ((xlong < xfile + 100 && xlong > xfile - 100) && (ylong < yfile + 100 && ylong > yfile - 100)) {
                    audioRecord.startPlayingPictureLabel(i);
                    Log.i("Index i of the label", String.valueOf(i));
                    tempCanvas.drawCircle(xfile * 6, yfile * 6, 150, myPaint);
                    myPaint.setColor(Color.BLUE);
                    view.invalidate();
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("...", "onLongPress сработал");
            int x = (int) e.getX();
            int y = (int) e.getY();

            Log.i("X", "case 0 " + x);
            Log.i("Y", "case 0 " + y);
            switch (e.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    Log.i("X", "case 1 " + x);
//                    Log.i("Y", "case 1 " + y);
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    Log.i("X", "case 2 " + x);
//                    Log.i("Y", "case 2 " + y);
//                    break;
                case MotionEvent.ACTION_DOWN:
                    Log.i("X", "case 3 " + x);
                    Log.i("Y", "case 3 " + y);

                    MediaPlayer mPlayer2 = audioRecord.getmPlayer();
                    Log.i("mPlayer2!!!", String.valueOf(mPlayer2));

                    //                   if (mPlayer2 == null) {
                    long after = System.currentTimeMillis();
                    android.util.Log.i("Time after click", " Time value in milliseconds " + after);
                    int difference = (int) (after - audioRecord.getStart());
                    Log.i("difference", String.valueOf(difference));

                    int sBody = difference;

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
                        gpxfile = new File(root, fileName);

                        FileWriter writer = new FileWriter(gpxfile, true);
                        Log.i("Time, X, Y", "Time:" + sBody + " X:" + x + "\n" + "Y" + y + "\n");
                        writer.append(sBody + "\n" + x + "\n" + y + "\n");
                        writer.flush();
                        writer.close();

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    // readFromFile();

                    tempCanvas.drawCircle(x * 6, y * 6, 150, myPaint);
                    view.invalidate();

//                    } else {
//
//                        String info2 = String.valueOf(mPlayer2.getCurrentPosition());
//                        long labeltime = mPlayer2.getCurrentPosition();
//                        String sBody = info2;
//
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_mm_dd_hh",
//                                Locale.getDefault());;
//                        Date now = new Date();
//                        String fileName = formatter.format(now) + ".txt";//like 2016_01_12.txt
//                        Log.i("Current", String.valueOf(labeltime));
//
//                        try {
//                            File root = new File(Environment.getExternalStorageDirectory(), "Audio_Recorder_Picture");
//                            if (!root.exists()) {
//                                root.mkdirs();
//                            }
//                             File gpxfile = new File(root, fileName);
//
//                            FileWriter writer = new FileWriter(gpxfile, true);
//                            writer.append(sBody + "\n");
//                            writer.flush();
//                            writer.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                  //    readFromFile();
//                        tempCanvas.drawCircle(x*6, y*6, 200,myPaint);
//                        view.invalidate();
//                    }


                    //----Saving process
//                    int sBody = x;
//
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_mm_dd_hh_mm",
//                            Locale.getDefault());;
//                    Date now = new Date();
//                    String fileName = formatter.format(now) + ".txt";

//                    try {
//                        File root = new File(Environment.getExternalStorageDirectory(), "Audio_Recorder_Picture");
//                        if (!root.exists()) {
//                            root.mkdirs();
//                        }
//                        File file = new File(root, fileName);
//
//                        FileWriter writer = new FileWriter(file, true);
//                        writer.append(x+"\n"+y+"\n");
//                        writer.flush();
//                        writer.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
                    //                   }

//                                       break;
                    //       onLongPress(e);

            }
            //   return true;
//            return DoubleTap.onTouchEvent(e);
            //     return true;
        }
    }

    public boolean onDoubleTap(MotionEvent e) {
        Log.d("...", "DoubleTap сработал");
        return false;
    }
}


