package com.example.dotslicer;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

/*
 * Class representing dots on the screen and handling movement
 */
public class Dot extends AppCompatTextView {

    /*
     * dX, dY are distances between touched place on the screen and the dots X and Y
     * CenterX, CenterY are X Y coordinates of the circle center
     */
    private float dX, dY, CenterX, CenterY;
    /*
     * Is used to differentiate between tap and swipe
     */
    private boolean isClick;
    /*
     * Number of the dot and screen size
     */
    private Integer number, screenWidth, screenHeight;
    /*
     * Base radius for radius calculation
     * Radius of the circle that makes up the dot
     */
    private int base_radius = 5, radius;
    /*
     * Is true if the dot was tapped instead of moved
     */
    private boolean Clicked;

    /*
     * Constructor sets the values, position and size of the dot
     */
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
        this.setText(Integer.toString(this.number));
        this.setTextSize(20);
        this.setGravity(Gravity.CENTER);
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        resetPosition();
    }
    /*
     * Detects touches and handles dot movement
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        setCenter();
            switch (e.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    dX = getX() - e.getRawX();
                    dY = getY() - e.getRawY();
                    isClick = true;
                    break;
                case MotionEvent.ACTION_UP:
                    if (isClick) {
                        Clicked = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    float newX = e.getRawX() + dX;
                    float newY = e.getRawY() + dY;
                    if(newX < 0) newX = 0;
                    if(newY < screenHeight/10) newY = screenHeight/10;
                    if(newX + this.getWidth() > screenWidth) newX = screenWidth - this.getWidth();
                    if(newY + this.getHeight() > screenHeight) newY = screenHeight - this.getHeight();
                        this.animate()
                                .x(newX)
                                .y(newY)
                                .setDuration(0)
                                .start();
                        this.setX(newX);
                        this.setY(newY);
                    isClick = false;
                    break;
                default:
                    return false;
            }
        return true;
    }

    /*
     * Sets the dot inside of the screen if it is outside
     */

    private void resetPosition() {
        if(getX() < 0) setX(0);
        if(getY() < screenHeight/10) setY(screenHeight/10);
        if(getCenterX() + radius > screenWidth) setX(screenWidth - 2*radius);
        if(getCenterY() + radius > screenHeight) setY(screenHeight - 2*radius);
    }
    /*
     * Sets CenterX and CenterY using X and Y provided by the superclass
     */
    private void setCenter() {
        CenterX = this.getX() + radius;
        CenterY = this.getY() + radius;
    }
    /*
     * Gets CenterX
     */
    public float getCenterX() {
        return this.getX() + radius;
    }
    /*
     * Gets CenterY
     */
    public float getCenterY() {
        return this.getY() + radius;
    }
    /*
     * Gets Number
     */
    public Integer getNumber() { return number; }
    /*
     * Gets Clicked
     */
    public boolean isClicked() {
        return Clicked;
    }
    /*
     * Gets Radius
     */
    public int getRadius() {
        return radius;
    }
}
