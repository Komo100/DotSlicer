package com.example.dotslicer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;

public class Dot extends LinearLayout {

    private float dX, dY;
    private float centerX, centerY;
    Integer number;
    private int base_radius = 10;
    private int radius;

    public Dot(Context context, float X, float Y, int number) {
        super(context);
        this.setX(X);
        this.setY(Y);
        this.number = number;
        radius = (int) (base_radius*number + 50);
        inflate(getContext(), R.layout.dot_layout, this);
        TextView numberTextView = (TextView) findViewById(R.id.numberTextView);
        numberTextView.setText(Integer.toString(this.number));
        LinearLayout layout = (LinearLayout) findViewById(R.id.dot_layout);
        layout.getLayoutParams().height = 2*radius;
        layout.getLayoutParams().width = 2*radius;
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {

            case MotionEvent.ACTION_DOWN:

                dX = getX() - e.getRawX();
                dY = getY() - e.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:

                this.animate()
                        .x(e.getRawX() + dX)
                        .y(e.getRawY() + dY)
                        .setDuration(0)
                        .start();
                this.setX(e.getRawX() + dX);
                this.setY(e.getRawY() + dY);
                break;

            default:
                return false;
        }
        return true;
    }
    public int getRadius() {
        return radius;
    }
}
