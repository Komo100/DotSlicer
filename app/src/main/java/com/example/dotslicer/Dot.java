package com.example.dotslicer;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import java.util.Observable;

public class Dot extends AppCompatTextView {

    private float dX, dY, ClickX, ClickY, CenterX, CenterY;
    private boolean isClick;
    Integer number;
    private int base_radius = 10;
    private int radius;
    private boolean Clicked;

    public Dot(Context context, float X, float Y, int number) {
        super(context);
        radius = (int) (base_radius*number + 50);
        this.CenterX = X;
        this.CenterY = Y;
        this.setX(X - radius);
        this.setY(Y - radius);
        this.number = number;
        this.Clicked = false;

        radius = (int) (base_radius*number + 50);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(2*radius, 2*radius);
        this.setLayoutParams(layoutParams);
        this.setBackground(ContextCompat.getDrawable(context, R.drawable.dot));
        //inflate(getContext(), R.layout.dot_layout, this);
        this.setText(Integer.toString(this.number));
        this.setTextSize(20);
        this.setGravity(Gravity.CENTER);
        //LinearLayout layout = (LinearLayout) findViewById(R.id.dot_layout);
        //layout.getLayoutParams().height = 2*radius;
        //layout.getLayoutParams().width = 2*radius;
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        setCenter();

            switch (e.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    dX = getX() - e.getRawX();
                    dY = getY() - e.getRawY();
                    ClickX = e.getX();
                    ClickY = e.getY();
                    isClick = true;
                    break;
                case MotionEvent.ACTION_UP:
                    if (isClick) {
                        Clicked = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    this.animate()
                            .x(e.getRawX() + dX)
                            .y(e.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    this.setX(e.getRawX() + dX);
                    this.setY(e.getRawY() + dY);
                    isClick = false;

                    break;
                default:
                    return false;
            }
        return true;
    }

    private void setCenter() {
        CenterX = this.getX() + radius;
        CenterY = this.getY() + radius;
    }
    public float getCenterX() {
        return this.getX() + radius;
    }
    public float getCenterY() {
        return this.getY() + radius;
    }
    public Integer getNumber() { return number; }
    public boolean isClicked() {
        return Clicked;
    }
    public int getRadius() {
        return radius;
    }
}
