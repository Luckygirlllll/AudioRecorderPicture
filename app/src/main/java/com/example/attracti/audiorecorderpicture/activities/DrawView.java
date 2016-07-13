package com.example.attracti.audiorecorderpicture.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Iryna on 7/12/16.
 */
public class DrawView extends View {
    Paint paint;

    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DrawView(Context context)
    {
        super(context);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        paint.setColor(Color.WHITE);
        paint.setTextSize(getHeight());
      //  paint.setTextSize(100);
      //  canvas.drawText("A", 80, 77, paint);
        canvas.drawText("A", getWidth()/4, (float) (getHeight()*0.8), paint);
        canvas.drawText("Б", getWidth()/3, (float) (getHeight()*0.8), paint);

    }
}