package com.example.attracti.audiorecorderpicture.utils;

import android.os.Environment;

import com.example.attracti.audiorecorderpicture.model.Folder;
import com.example.attracti.audiorecorderpicture.model.Label;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

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

    /*
     * this method read labels from the file
     */

    public static LinkedList<Label> readFromFile(File[] mArray) {

        ArrayList fileTime = new ArrayList();
        ArrayList xFile = new ArrayList();
        ArrayList yFile = new ArrayList();
        ArrayList filePosition = new ArrayList();
        LinkedList<Label> labelList = new LinkedList<Label>();
        String parentName;

        parentName = mArray[0].getParentFile().getParentFile().getName();

        try {
            File labelsFile = new File(Statics.mDiretoryName + "/" + parentName + "/" + parentName + ".txt");
            BufferedReader br = new BufferedReader(new FileReader(labelsFile));
            String line;

            while ((line = br.readLine()) != null) {
                String[] oneItem = line.split("\t");
                Label label = new Label(oneItem[0], Integer.parseInt(oneItem[1]), Integer.parseInt(oneItem[2]), Integer.parseInt(oneItem[3]));
                labelList.add(label);

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //sorted labellist
        Collections.sort(labelList);
        for (Object str : labelList) {
        }
        return labelList;
    }
}
