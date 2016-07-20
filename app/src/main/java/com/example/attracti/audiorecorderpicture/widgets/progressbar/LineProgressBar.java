package com.example.attracti.audiorecorderpicture.widgets.progressbar;

/**
 * Created by Iryna on 7/20/16.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;


public class LineProgressBar extends ProgressView {

    protected int lineOrientation = ProgressLineOrientation.HORIZONTAL
            .getValue();
    private boolean isGradientColor;
    // private boolean isEdgeRounded;

    public LineProgressBar(Context context) {
        super(context);
    }

    public LineProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public LineProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    void init() {

        initForegroundColor();
        initBackgroundColor();

    }

    // public void setRoundedEdgeProgress(boolean isEdgeRounded) {
    // this.isEdgeRounded = isEdgeRounded;
    // init();
    // }

    @Override
    public void onDraw(Canvas canvas) {

        if (getLineOrientation() == ProgressLineOrientation.HORIZONTAL
                .getValue()) {
            drawLineProgress(canvas);
            if (isGradientColor)
                setGradientColorHorizontal(gradColors);

        } else {
            drawLineProgressVertical(canvas);
            if (isGradientColor)
                setGradientColorVertical(gradColors);

        }
        drawText(canvas);

    }

    private void drawLineProgressVertical(Canvas canvas) {
        int nMiddle = width / 2;
        float progressY = (height / maximum_progress) * maximum_progress;

        canvas.drawLine(nMiddle, height - progressY, nMiddle, height, backgroundPaint);

        float progressX = (height / maximum_progress) * progress;
        canvas.drawLine(nMiddle, height, nMiddle, height - progressX,
                foregroundPaint);
        foregroundPaint.setTextSize(40);
        Paint textPaint = new Paint();

        canvas.drawText("Б", height - progressX, nMiddle, foregroundPaint);

    }


    private void drawLineProgress(Canvas canvas) {

        int nMiddle = height / 2;
        //canvas.drawLine(0, nMiddle, width, nMiddle, backgroundPaint);
        canvas.drawRect(0, nMiddle - 20, width, nMiddle, backgroundPaint);

        int progressX = (int) (width * progress / maximum_progress);

        // long progressX = (long) (width * labelTime/(progress/1000-pastTime));

        //  canvas.drawLine(0, nMiddle, progressX, nMiddle, foregroundPaint);
        canvas.drawRect(0, nMiddle - 20, progressX, nMiddle, foregroundPaint);

        Point a = new Point(0 + progressX - 15, nMiddle + 30);
        Point b = new Point(15 + progressX - 15, nMiddle);
        Point c = new Point(30 + progressX - 15, nMiddle + 30);

        Paint triangle = new Paint();
        triangle.setColor(Color.RED);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
        canvas.drawPath(path, triangle);

        foregroundPaint.setTextSize(30);

        Paint rectanglePaint = new Paint();
        Paint circle = new Paint();

        //go from the left to the right
        Paint textPaint = new Paint();
        textPaint.setTextSize(20);
        textPaint.setColor(Color.WHITE);

//        canvas.drawText("A",height/2-progressX, nMiddle, textPaint);
//        canvas.drawText("B",height/2-progressX+50, nMiddle, textPaint);
//        canvas.drawText("C",height/2-progressX+80, nMiddle, textPaint);
//        canvas.drawText("D",height/2-progressX+120, nMiddle, textPaint);


        canvas.drawText("A", height / 2 + 20, nMiddle, textPaint);
        canvas.drawText("B", height / 2 + 50, nMiddle, textPaint);
        canvas.drawText("C", height / 2 + 80, nMiddle, textPaint);
        //   canvas.drawText("D",height/2+120, nMiddle, textPaint);

        //nMiddle, progressX,
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        setMeasuredDimension(width, height);

    }

    /****
     * @return int value for orientation. <li>
     * <i>HORIZONTAL=0   <li> VERTICAL=1</i></li>
     ***/
    public int getLineOrientation() {
        return lineOrientation;
    }

    /***
     * @param position ProgressLineOrientation value
     **/
    public void setLineOrientation(ProgressLineOrientation position) {
        this.lineOrientation = position.getValue();
    }

    @Override
    public ShapeType setType(ShapeType type) {
        return ShapeType.LINE;
    }


    public void setLinearGradientProgress(boolean isGradientColor) {
        this.isGradientColor = isGradientColor;
    }

    public void setLinearGradientProgress(boolean isGradientColor, int[] colors) {
        this.isGradientColor = isGradientColor;
        gradColors = colors;
    }

    private void setGradientColorVertical(int[] gradColors) {
        if (gradColors != null)
            colorHelper.setGradientPaint(foregroundPaint, 0, height, 0, 0, gradColors);
        else
            colorHelper.setGradientPaint(foregroundPaint, 0, height, 0, 0);

    }

    private void setGradientColorHorizontal(int[] gradColors) {
        if (gradColors != null)
            colorHelper.setGradientPaint(foregroundPaint, 0, 0, width, 0, gradColors);
        else
            colorHelper.setGradientPaint(foregroundPaint, 0, 0, width, 0);
    }
}
