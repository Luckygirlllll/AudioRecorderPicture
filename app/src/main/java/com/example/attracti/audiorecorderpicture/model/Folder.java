package com.example.attracti.audiorecorderpicture.model;

import java.util.ArrayList;

/**
 * Created by attracti on 5/23/16.
 *
 * This class represents folder structure of the project
 */

public class Folder {

    public String mName;
    public ArrayList<String> pictureList;


    public Folder() {
        pictureList = new ArrayList<>();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public ArrayList<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(ArrayList<String> pictureList) {
        this.pictureList = pictureList;
    }

    public void addFile(String path) {
        pictureList.add(path);
    }

    @Override
    public String toString() {
        return "Folder{" +
                "mName='" + mName + '\'' +
                //      ", pictureList=" + pictureList +
                '}';
    }
}
