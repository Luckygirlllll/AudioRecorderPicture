package com.example.attracti.audiorecorderpicture.views;

/**
 * Created by Iryna on 7/13/16.
 */

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

public class CircleDrawView extends View {
    private Paint paint;
    private int x;
    private int y;

    public String getLabelName() {
        return labelName;
    }

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
        setPivotX(x);
        setPivotY(y);
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
        canvas.drawText(labelName, x, y + 8, textPaint);

    }

    //  tempCanvas.drawCircle(xCoordList.get(i) / 4, yCoordList.get(i) / 4, 10, myPaint3);
    //  tempCanvas.drawCircle(ChildFragment.xcoordList.get(i)/4, ChildFragment.ycoordList.get(i)/4 - (bounds.height() / 2), bounds.width() + 10, myPaint3);
    //  textPaint.getTextBounds(String.valueOf(i), 0, String.valueOf(i).length(), bounds);
    //                    if (i < 10 && i>1) {
    //                        tempCanvas.drawCircle(ChildFragment.xcoordList.get(i)/4, ChildFragment.ycoordList.get(i)/4 - (bounds.height() / 2), bounds.width() + 6, myPaint3);
    //                    } else if (i==1) {
    //                        tempCanvas.drawCircle(ChildFragment.xcoordList.get(i)/4, ChildFragment.ycoordList.get(i)/4 - (bounds.height() / 2), bounds.width() + 15, myPaint3);
    //                    }
    //                    else {
    //                        tempCanvas.drawCircle(ChildFragment.xcoordList.get(i)/4, ChildFragment.ycoordList.get(i)/4 - (bounds.height() / 2), bounds.width() + 3, myPaint3);
    //                    };
    //      tempCanvas.drawText(String.valueOf(i + 1), xCoordList.get(i) / 4, yCoordList.get(i) / 4, textPaint);

}