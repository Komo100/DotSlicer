package com.example.dotslicer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Dot extends View{
    private float X;
    private float Y;
    private Paint dot_paint;
    Integer number;
    private int radius = 100;
    public Dot(Context context, float X, float Y, int color, int number) {
        super(context);
        this.X = X;
        this.Y = Y;
        this.number = number;
        dot_paint = new Paint();
        dot_paint.setColor(color);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(X, Y, 100, dot_paint);
        dot_paint.setColor(Color.WHITE);
        dot_paint.setTextSize(100);
        canvas.drawText(number.toString(), X - 25, Y + 30, dot_paint);
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public int getRadius() {
        return radius;
    }
}
