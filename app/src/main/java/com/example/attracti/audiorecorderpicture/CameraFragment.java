package com.example.attracti.audiorecorderpicture;

/**
 * Created by Iryna on 4/28/16.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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

    public void setCallback(OnHeadlineSelectedListener callback){
        mCallback = callback;
    }


    private String TAG = CameraFragment.class.getSimpleName();
    private Context context;
    private AppCompatActivity activity;

    public static final String EXTRA_CAMERA_DATA = "camera_data";

    private static final String KEY_IS_CAPTURING = "is_capturing";
    private static final int TAKE_PICTURE_REQUEST_B = 100;

    private static Camera mCamera = null;
    private ImageView mCameraImage;
    private SurfaceView mCameraPreview;
    private Button mCaptureImageButton;
    private static byte[] mCameraData;
    private boolean mIsCapturing;

    public static Bitmap mCameraBitmap;
    private ImageView mCameraImageView;

    //Canvas tempCanvas;
    //Bitmap tempBitmap;
    //Paint myPaint;
    public static Bitmap bitmap;

    private View view;

    private GestureDetectorCompat DoubleTap;

    static File gpxfile;

    static String[] filetime2 = new String[100];
    static ArrayList filetime3 = new ArrayList();

    static ArrayList xcoordin = new ArrayList();
    static ArrayList ycoordin = new ArrayList();

    public static ArrayList getXcoordin() {
        return xcoordin;
    }
    public static ArrayList getYcoordin() {
        return ycoordin;
    }
    public static ArrayList getFiletime3() {
        return filetime3;
    }

    AudioRecord audioRecord;

    Paint textPaint;
    static int clicked = 1;

    //structure of the project's folders
    public static String mDiretoryName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Audio_Recorder_Picture";
    public static String mAudioFolder = mDiretoryName + "/Audios";
    public static String mPictureFolder = mDiretoryName + "/Pictures";
    public static String mLabelsFolder = mDiretoryName + "/Labels";
    public static String mPreviewsFolder = mDiretoryName + "/Previews";
    public static File mPreviewDirectory = new File(mPreviewsFolder + "/" + FirstscreenActivity.mCurrentProject);
    public static File mPictureDirectory = new File(mPictureFolder + "/" + FirstscreenActivity.mCurrentProject);
    public static File mAudioDirectory = new File(mAudioFolder);
    public static File mLabelsDirectory = new File(mLabelsFolder);

    // array of the files of the pictures which have been taken in the current project
    public static ArrayList <File> arrayFilepaths=new ArrayList<>();



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
            // mCameraImage.setImageBitmap(bitmap);
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

        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public void setActivityContext(AudioRecord activity) {
        audioRecord = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.camera_fragment,
                container, false);

        mCameraImage = (ImageView) view.findViewById(R.id.camera_image_view);
        mCameraPreview = (SurfaceView) view.findViewById(R.id.preview_view);

       // DoubleTap = new GestureDetectorCompat(getActivity(), new MyGestureListener());

        final SurfaceHolder surfaceHolder = mCameraPreview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        mIsCapturing = true;

        if (mCamera == null) {
            Log.i("Camera open", "Camera open");
            try {
                mCamera = Camera.open();
                Log.i("Camera created: ", String.valueOf(mCamera != null));
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
        }

        return view;
    }




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

                if (mCameraBitmap != null) {
                    mCameraBitmap.recycle();
                    mCameraBitmap = null;
                }
                Bundle extras = data.getExtras();
                byte[] cameraData = extras.getByteArray(CameraFragment.EXTRA_CAMERA_DATA);
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
        if (!mPictureDirectory.exists() && !mPictureDirectory.mkdirs()) {
            mPictureDirectory = null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss",
                    Locale.getDefault());
            return new File(mPictureDirectory.getPath() +
                    File.separator + "image_" +
                    dateFormat.format(new Date()) + ".png");
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
        Log.i("On pause method works!", "works!");
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

                    if (!mPictureDirectory.exists() && !mPictureDirectory.mkdirs()) {
                        mPictureDirectory = null;
                    } else {

                        File file = new File(
                                mPictureDirectory.getPath() +
                                        File.separator + "image_" +
                                        dateFormat.format(new Date()) + ".png");


                        arrayFilepaths.add(file);
                        mCallback.onArticleSelected(arrayFilepaths);
                        Log.i("Array", "size 3: " + String.valueOf(arrayFilepaths.size()));
                        for (int i=0; i<arrayFilepaths.size(); i++){
                            Log.i("Array filepaths", String.valueOf(arrayFilepaths.get(i)));
                        }
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            fos.write(data);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //Закомментировать и удалить
//                    bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
//                    Log.i("Bitmap height: ", String.valueOf(bitmap.getHeight()));
//                    Log.i("Bitmap width: ", String.valueOf(bitmap.getWidth()));
                    //mCameraImage.setImageBitmap(bitmap);

                    //mCamera.stopPreview();

//                    mCameraPreview.setVisibility(View.INVISIBLE);
                    // mCameraImage.setVisibility(View.VISIBLE);
                    //                   mCameraImage.setRotation(90);


//                    MyAdapter2.myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//                    MyAdapter2.tempCanvas.drawBitmap(bitmap, 0, 0, null);
//                    MyAdapter2.myPaint.setColor(Color.RED);
                    //                   mCameraImage.setImageDrawable(new BitmapDrawable(getResources(), MyAdapter2.tempBitmap));
                }
            });
        }
    }

    private void setupImageCapture() {
        if (mCameraPreview != null) {
            mCameraPreview.setVisibility(View.VISIBLE);
            if (mCamera != null) {
                mCamera.startPreview();
            }
        }
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
        captureImage();
    }

    @Override
    public void recievePicture(Bitmap bitmap) {
        mCameraImage.setImageBitmap(bitmap);
        mCameraImage.setRotation(0);
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
 //                   audioRecord.startPlayingPictureLabel(i);
                    Log.i("Index i of the label", String.valueOf(i));

//                    myPaint.setColor(Color.BLUE);
//                    tempCanvas.save();
//                    tempCanvas.rotate(-90, xfile * 6, yfile * 6);
//                    textPaint.setTextSize(140);t
//
//                    textPaint.setColor(Color.WHITE);
//                    textPaint.setAntiAlias(true);
//                    textPaint.setTextAlign(Paint.Align.CENTER);
//
//                    myPaint.setAntiAlias(true);
//                    Rect bounds = new Rect();
//                    textPaint.getTextBounds(String.valueOf(i+1), 0, String.valueOf(i+1).length(), bounds);
//                    if (i+1 < 10 && i+1>1) {
//                        tempCanvas.drawCircle(xfile * 6, yfile * 6 - (bounds.height() / 2), bounds.width() + 70, myPaint);
//                    } else if (i+1==1) {
//                        tempCanvas.drawCircle(xfile * 6, yfile * 6 - (bounds.height() / 2), bounds.width() + 95, myPaint);
//                    }
//                    else {
//                        tempCanvas.drawCircle(xfile * 6, yfile * 6 - (bounds.height() / 2), bounds.width() + 10, myPaint);
//                    };
//
//                    tempCanvas.drawText(String.valueOf(i+1), xfile * 6, yfile * 6, textPaint);
//                    tempCanvas.restore();
                    view.invalidate();
//                    for (int j = 0; j < xcoordin.size(); j++){
//                        Log.i("J", "J in for cycle");
//                        if (j!=i) {
//                            Log.i("J", "J after the for cycle");
//                            int xfilej = Integer.parseInt((String) xcoordin.get(j));
//                            int yfilej = Integer.parseInt((String) ycoordin.get(j));
//                            tempCanvas.drawCircle(xfilej * 6, yfilej * 6, 150, myPaint);
//                            myPaint.setColor(Color.WHITE);
//                            view.invalidate();
//                        }
//                    }
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
                    textPaint = new Paint();
//                    tempCanvas.save();
//                    tempCanvas.rotate(-90, x* 6, y* 6);
                    textPaint.setTextSize(140);
                    textPaint.setColor(Color.WHITE);
                    textPaint.setAntiAlias(true);
                    textPaint.setTextAlign(Paint.Align.CENTER);

//                    myPaint.setAntiAlias(true);
                    Rect bounds = new Rect();
                    textPaint.getTextBounds(String.valueOf(clicked), 0, String.valueOf(clicked).length(), bounds);
//
//                    if (clicked < 10 && clicked>1) {
//                        tempCanvas.drawCircle(x * 6, y * 6 - (bounds.height() / 2), bounds.width() + 70, myPaint);
//                    } else if (clicked==1) {
//                        tempCanvas.drawCircle(x * 6, y * 6 - (bounds.height() / 2), bounds.width() + 95, myPaint);
//                    }
//                    else {
//                        tempCanvas.drawCircle(x * 6, y * 6 - (bounds.height() / 2), bounds.width() + 10, myPaint);
//                    };
//
//                    tempCanvas.drawText(String.valueOf(clicked), x * 6, y * 6, textPaint);
//                    clicked++;
//                    tempCanvas.restore();
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
                    //       onLongPress(e)
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


