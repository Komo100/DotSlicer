package com.example.dotslicer;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/*
 * Fragment displaying that the player won
 */
public class VictoryFragment extends Fragment {

    /*
     * This gets called when MainActivity switches to this fragment
     * Inflates the menu.xml layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.victory, container, false);
    }

    /*
     * This gets called after onCreateView gets called
     * Creates listener for the whole screen, after touching returns to the menu
     * Handles animating the "You won!" text
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextView victory_text = (TextView) getActivity().findViewById(R.id.victory_text);

        ObjectAnimator colorAnim = ObjectAnimator.ofInt(victory_text, "textColor",
                Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.RED);
        colorAnim.setRepeatCount(ObjectAnimator.INFINITE);
        colorAnim.setRepeatMode(ObjectAnimator.RESTART);
        colorAnim.setDuration(2000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity)getActivity()).ChangeToFragment(new Menu());
                    return true;
                }
                return false;
            }
        });
    }
}
