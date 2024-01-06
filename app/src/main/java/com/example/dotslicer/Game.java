package com.example.dotslicer;

import androidx.fragment.app.Fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;
import java.util.Vector;

/*
 * This Fragment contains all gameplay
 */
public class Game extends Fragment {
    /*
     * Stores all currently displayed dots
     */
    private Vector<Dot> VectorOfDots;
    /*
     * Width and height of the screen
     * Value needed to achieve in order to win
     */
    private Integer height, width, value;
    /*
     * Value needed to achieve in easy mode
     * difficulty_settings is multiplied by it to get value for different difficulties
     */
    private static final int diff_weight= 20;

    /*
     * This gets called when MainActivity switches to this fragment
     * Inflates the game.xml layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game, container, false);
    }

    /*
     * This gets called after onCreateView gets called
     * Creates listener for exit button
     * Creates dots
     * Gets height and width
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Button exit_button = (Button) view.findViewById(R.id.button_exit_game);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).ChangeToFragment(new Menu());
            }
        });

        final TextView num_to_get = (TextView) view.findViewById(R.id.number_to_get);
        value = ((MainActivity)getActivity()).getDifficulty_setting()*diff_weight;
        num_to_get.setText("Number to get: " + value.toString());
        super.onViewCreated(view, savedInstanceState);
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        VectorOfDots = GenerateDots();
    }
    /*
     * Generates dots at random positions
     */
    private Vector<Dot> GenerateDots() {
        ConstraintLayout layout = (ConstraintLayout) getActivity().findViewById(R.id.game);
        Vector<Dot> vector_of_dots = new Vector();

        for(int i = 0; i < 5; i++) {
            float pos_x;
            float pos_y;
            int number;
            Dot dot;
            do {
                Random random = new Random();
                number = random.nextInt(5) + 12;
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

    /*
     * Calculates Euclidian distance in 2 dimensions
     */
    private float distance(float x0, float y0, float x1, float y1){
        return (float)Math.sqrt((x0 - x1)*(x0 - x1) + (y0 - y1)*(y0 - y1));
    };

    /*
     * Checks if dot is not intersecting dots in the vector
     */
    private boolean DotTooClose(Vector<Dot> vector, Dot dot) {
        if(!CollidedWith(dot, vector).isEmpty()) return true;
        return false;
    }

    /*
     * Returns vector that contains all dots intersecting the input dot
     */
    private Vector<Dot> CollidedWith(Dot dot, Vector<Dot> vector_of_dots) {
        Vector<Dot> ReturnVector = new Vector();
        for (Integer i = 0; i < vector_of_dots.size(); i++) {
            Dot dot1 = vector_of_dots.get(i);
            float dotCenterX = dot.getX() + dot.getWidth()/2;
            float dotCenterY = dot.getY() + dot.getHeight()/2;
            float dot1CenterX = dot1.getX() + dot1.getWidth()/2;
            float dot1CenterY = dot1.getY() + dot1.getHeight()/2;
            Boolean isIntersecting = (distance(dotCenterX, dotCenterY, dot1CenterX, dot1CenterY)
                    < dot.getRadius() + dot1.getRadius());
            if(vector_of_dots.get(i) != dot && isIntersecting) {
                ReturnVector.add(vector_of_dots.get(i));
            };

        }
        return ReturnVector;
    }

    /*
     * Deletes the dots in the input vector and the single dot
     * Creates new dot with its number being the sum of numbers of dots in the input
     */
    private Integer NewNumber(Vector<Dot> vector, ConstraintLayout layout, Dot dot) {
        layout.removeView(dot);
        VectorOfDots.remove(dot);
        Integer number = dot.getNumber();

        while(!vector.isEmpty()) {
            layout.removeView(vector.get(0));
            VectorOfDots.remove(vector.get(0));
            number += vector.get(0).getNumber();
            vector.remove(vector.get(0));
        }
        Dot new_dot = new Dot(getActivity().getApplicationContext(),
                dot.getCenterX(), dot.getCenterY(), number);
        CreateDotListener(new_dot, layout);
        layout.addView(new_dot);
        VectorOfDots.add(new_dot);
        return number;
    }
    /*
     * Deletes the input dot
     * Creates 2 new dots with their numbers summing up to the input dot's number
     */
    private void splitDot(ConstraintLayout layout, Dot dot) {
        layout.removeView(dot);
        VectorOfDots.remove(dot);
        Dot new_dot1 = new Dot(getActivity().getApplicationContext(),
                    dot.getX(), dot.getY(), dot.getNumber() / 2);
        Dot new_dot2 = new Dot(getActivity().getApplicationContext(),
                dot.getX()+dot.getWidth(),
                dot.getY()+dot.getHeight(),
                dot.getNumber() - dot.getNumber()/2);
        layout.addView(new_dot1);
        CreateDotListener(new_dot1, layout);
        layout.addView(new_dot2);
        CreateDotListener(new_dot2, layout);
        VectorOfDots.add(new_dot1);
        VectorOfDots.add(new_dot2);
    }
    /*
     * Dot listener
     * Handles when new dots should be created or deleted
     */
    private void CreateDotListener(Dot dot, ConstraintLayout layout) {
        dot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                boolean ReturnedValue = dot.onTouchEvent(e);
                if (dot.isClicked() && dot.getNumber() > 1 && e.getAction() == MotionEvent.ACTION_UP) {
                    splitDot(layout, dot);
                }
                else if(e.getAction() == MotionEvent.ACTION_UP) {
                    Vector<Dot> CollidedWithThis = CollidedWith(dot, VectorOfDots);
                        if(!CollidedWithThis.isEmpty() &&
                                NewNumber(CollidedWithThis, layout, dot) == value ) {
                            ((MainActivity)getActivity()).ChangeToFragment(new VictoryFragment());
                        }
                    }
                return ReturnedValue;
            }
        });
    }
}