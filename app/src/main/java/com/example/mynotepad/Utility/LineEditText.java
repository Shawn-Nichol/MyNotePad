package com.example.mynotepad.Utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * LineEditText adds lines to the background of the the note.
 */
public class LineEditText extends AppCompatEditText {

    private Rect mRect;
    private Paint mPaint;

    // Constructor requires an AttributeSet.
    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE); // Specifies how the line is drawn
        mPaint.setStrokeWidth(2); // The Width of the Line
        mPaint.setColor(0xFFFFD966); // The color of the lines.

    }

    /**
     * onDraw is called when the view should be rendered, this onDraw will draw the lines in the background
     * of the noteActivity.
     *
     * @param canvas The canvas on which the background will be drawn.
     */
    @Override
    protected void onDraw(Canvas canvas) {

        // Height of the screen.
        int height = ((View)this.getParent()).getHeight();

        // Distance between each line
        int lineHeight = getLineHeight();
        // Total number of lines that fight on the screen.
        int numberOfLines = height/lineHeight;

        // Rect Holds four integer coordinates for a rectangle. The rectangle is represented by the
        // coordinates of its 4 edges (left, top, right, bottom).
        Rect r = mRect;

        // The paint class holds the style and color information about how to draw geometries, text and bitmaps
        Paint paint = mPaint;

        // The line to draw on.
        int baseline = getLineBounds(0, r);

        for(int i = 0; i < numberOfLines; i++) {

            // Draw a line segment with the specified start and stop x,y coordinates, using the specified paint.
            canvas.drawLine(
                    r.left, // X-coordinate of the start point.
                    baseline + 1, // Y-coordinate of the start point.
                    r.right, // X-coordinate of the stop point.
                    baseline + 1, // y-coordinate of the stop point.
                    paint); // The paint used to draw the line.
            baseline += lineHeight;

        }
        super.onDraw(canvas);
    }
}