package com.example.attracti.audiorecorderpicture.utils;

import android.os.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Iryna on 6/17/16.
 */
public class Statics {

    public final static String mDiretoryName;
    public final static List<String> alphabetEnglish;


    static {
        mDiretoryName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Audio_Recorder_Picture";
        alphabetEnglish = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
    }

}
