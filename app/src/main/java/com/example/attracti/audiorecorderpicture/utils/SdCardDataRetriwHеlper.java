package com.example.attracti.audiorecorderpicture.utils;

import android.os.Environment;

import com.example.attracti.audiorecorderpicture.model.Folder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Iryna on 7/13/16.
 */
public class SdCardDataRetriwHеlper {

    // private File[] listFile;
    // private File[] listFolders;

    public static ArrayList<Folder> getFromSdcardFolders() {
        ArrayList<Folder> FOLDERS = null;
        File[] listFile;
        File[] listFolders;

        FOLDERS = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory() +
                "/Audio_Recorder_Picture");
        if (file.isDirectory()) {
            listFolders = file.listFiles();
            for (int i = 0; i < listFolders.length; i++) {

                Folder folderobject = new Folder();
                folderobject.setName(listFolders[i].getName());
                File picturelist = new File(Environment.getExternalStorageDirectory() +
                        "/Audio_Recorder_Picture/", listFolders[i].getName() + "/Previews");
                if (picturelist.isDirectory()) {
                    listFile = picturelist.listFiles();
                    for (int j = 0; j < listFile.length; j++) {
                        folderobject.addFile(listFile[j].getAbsolutePath());
                    }
                }
                FOLDERS.add(folderobject);
            }
        }
        return FOLDERS;
    }
}
