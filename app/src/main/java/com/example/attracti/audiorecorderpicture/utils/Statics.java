package com.example.attracti.audiorecorderpicture.utils;

import android.os.Environment;

/**
 * Created by Iryna on 6/17/16.
 */
public class Statics {

    public final static String mDiretoryName;

    static {
        mDiretoryName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Audio_Recorder_Picture";
    }

}
