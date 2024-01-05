package com.example.dotslicer;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;
import java.util.Vector;


public class Game extends Fragment {

    private Vector<Dot> VectorOfDots;
    private Integer height;
    private Integer width;
    private static final int diff_weight= 20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Button play_button = (Button) view.findViewById(R.id.button_exit_game);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).ChangeToFragment(new Menu());
            }
        });
        final TextView num_to_get = (TextView) view.findViewById(R.id.number_to_get);
        Integer value = ((MainActivity)getActivity()).getDifficulty_setting()*diff_weight;
        num_to_get.setText("Number to get: " + value.toString());
        VectorOfDots = GenerateDots();
        super.onViewCreated(view, savedInstanceState);
    }

    private Vector<Dot> GenerateDots() {
        ConstraintLayout layout = (ConstraintLayout) getActivity().findViewById(R.id.game);
        Vector<Dot> vector_of_dots = new Vector();

        width = 1000;
        height = 1700;

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
                dot = new Dot(getActivity().getApplicationContext(), pos_x, pos_y, number);
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
            Boolean isIntersecting = (distance(dotCenterX, dotCenterY, dot1CenterX, dot1CenterY) < dot.getRadius() + dot1.getRadius());
            if(vector_of_dots.get(i) != dot && isIntersecting) {
                return vector_of_dots.get(i);
            };

        }
        return null;
    }

    private boolean TooCloseToEdge(Dot dot) {

        return (dot.getX() < 100 ||
                dot.getY() < 100 ||
                dot.getX() + dot.getWidth() > width - 100 ||
                dot.getY() + dot.getHeight() > height - 100);
    }

    private void CreateDotListener(Dot dot, ConstraintLayout layout) {
        dot.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                boolean ReturnedValue = dot.onTouchEvent(e);
                int num1 = dot.getNumber();

                if (dot.isClicked() && num1 > 1 && e.getAction() == MotionEvent.ACTION_UP) {
                    int new_num1 = num1/2;
                    int new_num2 = num1 - new_num1;
                    float new_X1 = dot.getX();
                    float new_Y1 = dot.getY();
                    float new_X2 = dot.getWidth() + new_X1;
                    float new_Y2 = dot.getHeight() + new_Y1;
                    layout.removeView(dot);
                    VectorOfDots.remove(dot);
                    Dot new_dot1 = new Dot(getActivity().getApplicationContext(), new_X1, new_Y1, new_num1);
                    Dot new_dot2 = new Dot(getActivity().getApplicationContext(), new_X2, new_Y2, new_num2);
                    layout.addView(new_dot1);
                    CreateDotListener(new_dot1, layout);
                    layout.addView(new_dot2);
                    CreateDotListener(new_dot2, layout);
                    VectorOfDots.add(new_dot1);
                    VectorOfDots.add(new_dot2);
                } else if(e.getAction() == MotionEvent.ACTION_UP) {
                    Dot CollidedWithThis = CollidedWith(dot, VectorOfDots);
                    if(CollidedWithThis != null) { int num2 = CollidedWithThis.getNumber();
                        int new_number = num1 + num2;
                        float new_posx = dot.getCenterX();
                        float new_posy = dot.getCenterY();
                        layout.removeView(dot);
                        layout.removeView(CollidedWithThis);
                        VectorOfDots.remove(dot);
                        VectorOfDots.remove(CollidedWithThis);
                        Dot new_dot = new Dot(getActivity().getApplicationContext(), new_posx, new_posy, new_number);
                        layout.addView(new_dot);
                        CreateDotListener(new_dot, layout);
                        VectorOfDots.add(new_dot);
                        }
                    }
                return ReturnedValue;
            }
        });
    }
}