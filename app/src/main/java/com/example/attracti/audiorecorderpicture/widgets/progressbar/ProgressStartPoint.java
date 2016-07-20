package com.example.attracti.audiorecorderpicture.widgets.progressbar;

/**
 * Created by Iryna on 7/20/16.
 */
public class ProgressStartPoint {

    //      DEFAULT(-90), LEFT(180), RIGHT(0), BOTTOM(90);
    int value;

    private ProgressStartPoint(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

