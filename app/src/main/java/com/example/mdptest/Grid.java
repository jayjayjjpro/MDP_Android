package com.example.mdptest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class Grid extends View {
    private static final int numberOfColumns = 20;
    private static final int numberOfRows = 20;
    private static final int cellWidth = 40;
    private static final int cellHeight = 40;

    private static final Paint blackPaint = new Paint();
    private static final Paint greyPaint = new Paint();
    private static final Paint whitePaint = new Paint();

    private GifDrawable gifDrawable;

    public Grid(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        blackPaint.setColor(Color.BLACK);
        greyPaint.setColor(Color.TRANSPARENT);
        whitePaint.setColor(Color.WHITE);
    }

    @Override
    public void onDraw(Canvas canvas) {

        Bitmap backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.ruin_back);

//        int centerX = (getWidth() - backgroundImage.getWidth()) / 2;
//        int centerY = (getHeight() - backgroundImage.getHeight()) / 2;
//
//        // Draw the background image
//        canvas.drawBitmap(backgroundImage, centerX, centerY, null);
        canvas.drawBitmap(backgroundImage, 0, 0, null);

//        // Draw the GIF if it's loaded
//        if (gifDrawable != null) {
//            gifDrawable.setBounds(0, 0, getWidth(), getHeight());
//            gifDrawable.draw(canvas);
//        }

        // Draw the background boxes for the grid
        for (int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                canvas.drawRect(i * cellWidth, j * cellHeight,
                        (i + 1) * cellWidth, (j + 1) * cellHeight,
                        greyPaint);
            }
        }

        // Draw vertical lines
        for (int i = 1; i < numberOfColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, numberOfRows * cellHeight, whitePaint);
        }

        // Draw horizontal lines
        for (int i = 1; i < numberOfRows; i++) {
            canvas.drawLine(0, i * cellHeight, numberOfColumns * cellWidth, i * cellHeight, whitePaint);
        }

        // Draw vertical grid axis
        for (int i = 0; i < numberOfRows; i++) {
            //canvas.drawText(String.valueOf(i), cellWidth * 10 + 5, cellHeight * (numberOfRows - i - 1) + 15, whitePaint);
            canvas.drawText(String.valueOf(i), cellWidth * 10 + 5, cellHeight * (numberOfRows - i - 1) + 15, whitePaint);
        }

        for (int i = 0; i < numberOfColumns; i++) {
            //canvas.drawText(String.valueOf(i), cellWidth * (i) + 5, cellHeight * 9 + 15, whitePaint);
            canvas.drawText(String.valueOf(i), cellWidth * (i) + 5, cellHeight * 9 + 15, whitePaint);
        }
    }
}
