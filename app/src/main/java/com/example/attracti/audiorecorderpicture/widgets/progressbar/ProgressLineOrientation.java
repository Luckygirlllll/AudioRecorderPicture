package com.example.attracti.audiorecorderpicture.widgets.progressbar;

/**
 * Created by Iryna on 7/20/16.
 */
public enum ProgressLineOrientation {
    HORIZONTAL(0), VERTICAL(1);
    int value;

    private ProgressLineOrientation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
