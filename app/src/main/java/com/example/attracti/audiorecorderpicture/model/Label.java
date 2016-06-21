package com.example.attracti.audiorecorderpicture.model;

/**
 * Created by Iryna on 6/21/16.
 */
public class Label implements Comparable {

    public String pictureName;
    public int labelTime;
    public int xLabel;
    public int yLabel;

    public Label(String pictureName, int labelTime, int xLabel, int yLabel) {
        this.pictureName = pictureName;
        this.labelTime = labelTime;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public int getLabelTime() {
        return labelTime;
    }

    public void setLabelTime(int labelTime) {
        this.labelTime = labelTime;
    }

    public int getxLabel() {
        return xLabel;
    }

    public void setxLabel(int xLabel) {
        this.xLabel = xLabel;
    }

    public int getyLabel() {
        return yLabel;
    }

    public void setyLabel(int yLabel) {
        this.yLabel = yLabel;
    }

    @Override
    public int compareTo(Object comparestu) {
        int comparetime = ((Label) comparestu).getLabelTime();
        return this.labelTime - comparetime;
    }

    @Override
    public String toString() {
        return "Label{" +
                "pictureName='" + pictureName + '\'' +
                ", labelTime=" + labelTime +
                ", xLabel=" + xLabel +
                ", yLabel=" + yLabel +
                '}';
    }
}
