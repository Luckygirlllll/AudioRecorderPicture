package com.example.attracti.audiorecorderpicture;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Iryna on 6/6/16
 * <p>
 * updating of the arrayList by sending data from CameraFragment to AudioRecordActivity
 */

public interface OnHeadlineSelectedListener {
    public void onArticleSelected(ArrayList<File> arrayFilepaths);
}
