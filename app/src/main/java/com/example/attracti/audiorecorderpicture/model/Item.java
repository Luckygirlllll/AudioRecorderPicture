package com.example.attracti.audiorecorderpicture.model;

/**
 * Created by Iryna on 6/24/16.
 */
public class Item {
    private int mDrawableRes;
    private String mTitle;

    public Item(int drawable, String title) {
        mDrawableRes = drawable;
        mTitle = title;
    }

    public Item( String title) {
        mTitle = title;
    }

    public int getDrawableResource() {
        return mDrawableRes;
    }

    public String getTitle() {
        return mTitle;
    }
}
