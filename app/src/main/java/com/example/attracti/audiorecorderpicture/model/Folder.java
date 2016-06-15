package com.example.attracti.audiorecorderpicture.model;

import java.util.ArrayList;

/**
 * Created by attracti on 5/23/16.
 *
 * This class represents folder structure of the project
 */

public class Folder {

    public String name;
    public ArrayList<String> picturelist;

    public Folder() {
        picturelist = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPicturelist() {
        return picturelist;
    }

    public void setPicturelist(ArrayList<String> picturelist) {
        this.picturelist = picturelist;
    }

    public void addFile(String path) {
        picturelist.add(path);
    }
}
