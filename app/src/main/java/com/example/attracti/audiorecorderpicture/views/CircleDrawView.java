package com.example.attracti.audiorecorderpicture.views;

/**
 * Created by Iryna on 7/13/16.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Iryna on 7/12/16.
 *
 * Class for the drawing labels
 *
 */

public class CircleDrawView extends View {
    private Paint paint;
    private int x;
    private int y;
    private String labelName;



    public CircleDrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CircleDrawView(Context context)
    {
        super(context);
        paint = new Paint();
    }

    public CircleDrawView(Context context, int x, int y, String labelName)
    {
        super(context);
        paint = new Paint();
        this.x=x;
        this.y=y;
        this.labelName=labelName;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        canvas.drawCircle(x, y, 40, paint);
        Paint textPaint = new Paint();
        textPaint.setTextSize(25);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Rect bounds = new Rect();
        textPaint.getTextBounds(labelName, 0, labelName.length(), bounds);
        canvas.drawText(labelName, x, y, textPaint);

    }
}