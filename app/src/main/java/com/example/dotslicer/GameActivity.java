package com.example.dotslicer;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.color.MaterialColors;

import java.util.Observer;
import java.util.Random;
import java.util.Vector;


public class GameActivity extends AppCompatActivity {

    private Vector<Dot> VectorOfDots;
    private Integer number_to_get = 20;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        VectorOfDots = GenerateDots();
        findViewById(R.id.button_exit_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainActivityIntent = new Intent(GameActivity.this, MainActivity.class);
                GameActivity.this.startActivity(MainActivityIntent, ActivityOptions.makeSceneTransitionAnimation(GameActivity.this).toBundle());
                finish();
            }
        });

        final TextView number_to_get_tv = (TextView) findViewById(R.id.number_to_get);
        number_to_get_tv.setText("Number to get: " + number_to_get.toString());
    }

    private Vector<Dot> GenerateDots() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        Vector<Dot> vector_of_dots = new Vector();
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.game_layout);
        for(int i = 0; i < 5; i++) {
            float pos_x;
            float pos_y;
            int number;
            Dot dot;
            do {
                Random random = new Random();
                number = random.nextInt(9) + 1;
                pos_y = (float)random.nextInt(height);
                pos_x = (float)random.nextInt(width);
                dot = new Dot(getApplicationContext(), pos_x, pos_y, number);
            } while(DotTooClose(vector_of_dots, dot));

            layout.addView(dot);
            CreateDotListener(dot, layout);
            vector_of_dots.add(dot);
        }
        return vector_of_dots;
    }

    private float distance(float x0, float y0, float x1, float y1){
        return (float)Math.sqrt((x0 - x1)*(x0 - x1) + (y0 - y1)*(y0 - y1));
    };

    private boolean DotTooClose(Vector<Dot> vector, Dot dot) {
        for(int i = 0; i < vector.size(); i++) {
            if(CollidedWith(dot, vector) != null || TooCloseToEdge(dot)) return true;
        }
        return false;
    }
    private Dot CollidedWith(Dot dot, Vector<Dot> vector_of_dots) {
        for (Integer i = 0; i < vector_of_dots.size(); i++) {
            Dot dot1 = vector_of_dots.get(i);
            float dotCenterX = dot.getX() + dot.getWidth()/2;
            float dotCenterY = dot.getY() + dot.getHeight()/2;
            float dot1CenterX = dot1.getX() + dot1.getWidth()/2;
            float dot1CenterY = dot1.getY() + dot1.getHeight()/2;
            boolean isIntersecting = (distance(dotCenterX, dotCenterY, dot1CenterX, dot1CenterY) < dot.getRadius() + dot1.getRadius());
            if((vector_of_dots.get(i) != dot && isIntersecting) ) {
                return vector_of_dots.get(i);
            };

        }
        return null;
    }

    private boolean TooCloseToEdge(Dot dot) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        return (dot.getX() < width*0.2 ||
                dot.getY() < height*0.2 ||
                dot.getX() + dot.getWidth() > width*0.8 ||
                dot.getY() + dot.getHeight() > height*0.8);
    }

    private void CreateDotListener(Dot dot, ConstraintLayout layout) {
        dot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                boolean ReturnedValue = dot.onTouchEvent(e);
                if((e.getAction() == MotionEvent.ACTION_UP)) {
                    if (dot.isClicked()) {
                        layout.removeView(dot);
                        VectorOfDots.remove(dot);
                    } else {
                        int num1 = dot.getNumber();
                        Dot CollidedWithThis = CollidedWith(dot, VectorOfDots);
                        if(CollidedWithThis != null) {
                            int num2 = CollidedWithThis.getNumber();
                            int new_number = num1 + num2;
                            float new_posx = dot.getCenterX();
                            float new_posy = dot.getCenterY();
                            layout.removeView(dot);
                            layout.removeView(CollidedWithThis);
                            VectorOfDots.remove(dot);
                            VectorOfDots.remove(CollidedWithThis);
                            Dot new_dot = new Dot(getApplicationContext(), new_posx, new_posy, new_number);
                            layout.addView(new_dot);
                            CreateDotListener(new_dot, layout);
                            VectorOfDots.add(new_dot);

                        }
                    }
                }
                return ReturnedValue;
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}