package com.example.attracti.audiorecorderpicture.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Iryna on 7/12/16.
 *
 * Class for the drawing labels
 *
 */

public class DrawView extends View {
    private Paint paint;
    private int xRatio;
    private String labelName;


    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DrawView(Context context)
    {
        super(context);
        paint = new Paint();
    }

    public DrawView(Context context, int xRatio, String labelName)
    {
        super(context);
        paint = new Paint();
        this.xRatio=xRatio;
        this.labelName=labelName;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        paint.setColor(Color.WHITE);
        paint.setTextSize(getHeight());
        canvas.drawText(labelName, (getWidth() - 20) / xRatio, (float) (getHeight() * 0.8), paint);

        // canvas.drawText(labelName, 720, (float) (getHeight()*0.8), paint);

        //Log.wtf("Width", String.valueOf(getWidth()));
        // 720
        //canvas.drawText("Б", getWidth()/3, (float) (getHeight()*0.8), paint);

    }
}