package com.example.attracti.audiorecorderpicture.activities;

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



    public CircleDrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CircleDrawView(Context context)
    {
        super(context);
        paint = new Paint();
    }

    public CircleDrawView(Context context, int x, int y)
    {
        super(context);
        paint = new Paint();
        this.x=x;
        this.y=y;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(x, y, 50, paint);
//        Paint paintExtra = new Paint();
//        paintExtra.setTextSize(40);
//        paintExtra.setColor(Color.WHITE);
//        canvas.drawText("A", x, y, paintExtra);
    }
}