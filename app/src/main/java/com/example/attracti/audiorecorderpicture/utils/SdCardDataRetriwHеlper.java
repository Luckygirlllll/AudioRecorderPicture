package com.example.attracti.audiorecorderpicture.utils;

import android.os.Environment;

import com.example.attracti.audiorecorderpicture.model.Folder;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Created by Iryna on 7/13/16.
 *
 * class which helps find folders which needed for the project (First screen projects)
 * and Pictures folders for the Gallery.
 *
 */
public class SdCardDataRetriwHеlper {


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

    public static ArrayList<Folder> getFromSdcardPicturesFolders(String path) {
        File[] listFile;
        File[] listFolders;
        ArrayList<Folder> FOLDERS = null;

        FOLDERS = new ArrayList<>();
        File file = new File(path);
        if (file.isDirectory()) {
            listFolders = file.listFiles(new ImageFileFilter());

            for (int i = 0; i < listFolders.length; i++) {
                String name = listFolders[i].getName();
                if (name.toCharArray()[0] != '.') {
                    Folder folderobject = new Folder();
                    folderobject.setName(listFolders[i].getName());

                    File picturelist = new File(path, listFolders[i].getName());
                    if (picturelist.isDirectory()) {
                        listFile = picturelist.listFiles();
                        for (int j = 0; j < listFile.length; j++) {
                            folderobject.addFile(listFile[j].getAbsolutePath());
                        }
                    }
                    FOLDERS.add(folderobject);
                }
            }
        }
        return FOLDERS;
    }

    private static class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            } else if (isImageFile(file.getAbsolutePath())) {
                return true;
            }
            return false;
        }
    }

    /**
     * Checks the file to see if it has a compatible extension.
     */
    private static boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png"))
        // Add other formats as desired
        {
            return true;
        }
        return false;
    }


}
