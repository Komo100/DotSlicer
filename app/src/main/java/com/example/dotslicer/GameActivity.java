package com.example.dotslicer;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.color.MaterialColors;

import java.util.Random;
import java.util.Vector;


public class GameActivity extends AppCompatActivity {

    private Vector<Dot> vector_of_dots;

    //float dX, dY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        vector_of_dots = GenerateDots();
        findViewById(R.id.button_exit_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainActivityIntent = new Intent(GameActivity.this, MainActivity.class);
                GameActivity.this.startActivity(MainActivityIntent, ActivityOptions.makeSceneTransitionAnimation(GameActivity.this).toBundle());
                finish();
            }
        });
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

            do {
                Random random = new Random();
                number = random.nextInt(9) + 1;
                pos_y = (float)random.nextInt(height - 400) + 100;
                pos_x = (float)random.nextInt(width - 400) + 100;
            } while(DotTooClose(vector_of_dots, pos_x, pos_y));

            Dot dot = new Dot(getApplicationContext(), pos_x, pos_y, number);
            layout.addView(dot);
            vector_of_dots.add(dot);
        }

        return vector_of_dots;
    }

    private float distance(float x0, float y0, float x1, float y1){
        return (float)Math.sqrt((x0 - x1)*(x0 - x1) + (y0 - y1)*(y0 - y1));
    };

    private boolean DotTooClose(Vector<Dot> vector, float x0, float y0) {
        for(int i = 0; i < vector.size(); i++) {

            float x1 = vector.get(i).getX();
            float y1 = vector.get(i).getY();
            float radius = vector.get(i).getRadius();

            if(distance(x0, y0, x1, y1) <= radius*2) return true;
        }
        return false;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}