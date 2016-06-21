package com.example.attracti.audiorecorderpicture.utils;

import android.os.Environment;

/**
 * Created by Iryna on 6/17/16.
 */
public class Statics {

    public final static String mDiretoryName;
//    public final static String mAudioFolder;
//    public final static String mPictureFolder;
//    public final static String mLabelsFolder;
//    public final static String mPreviewsFolder;

    static {
        mDiretoryName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Audio_Recorder_Picture";
//        mAudioFolder = mDiretoryName + "/Audios";
//        mPictureFolder = mDiretoryName + "/Pictures";
//        mLabelsFolder = mDiretoryName + "/Labels";
//        mPreviewsFolder = mDiretoryName + "/Previews";
    }

}
