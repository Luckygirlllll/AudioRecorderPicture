package com.example.attracti.audiorecorderpicture;

/**
 * Created by Iryna on 5/25/16.
 * <p>
 * This is a CustomViewPager, it uses in AudioRecord activity and in ViewFragment (it disables swipes in ViewPager)
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPagerH extends ViewPager {

    private boolean isPagingEnabled = true;

    public CustomViewPagerH(Context context) {
        super(context);
    }

    public CustomViewPagerH(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
