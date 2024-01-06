package com.example.dotslicer;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/*
 * Handles everything relating to the menu
 */
public class Menu extends Fragment {
    /*
     * Used to check if popup window is swiped up or clicked in
     */
    private Float y1, y2;

    /*
     * This gets called when MainActivity switches to this fragment
     * Inflates the menu.xml layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu, container, false);
    }
    /*
     * Sets difficulty
     */
    public void setDifficulty(int x) {
        ((MainActivity)getActivity()).setDifficulty_setting(x);
    }
    /*
     * Gets difficulty
     */
    public int getDifficulty() { return ((MainActivity)getActivity()).getDifficulty_setting(); }
    /*
     * This gets called after onCreateView gets called
     * Creates listeners for buttons
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Button play_button = (Button) view.findViewById(R.id.play_button);
        play_listener(play_button);
        final Button settings_button = (Button) view.findViewById(R.id.settings_button);
        settings_listener(settings_button);
        super.onViewCreated(view, savedInstanceState);
    }

    /*
     * Creates listener for the "Play" button
     * Switches to game fragment when clicked
     */
    public void play_listener(Button play_button) {
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).ChangeToFragment(new Game());
            }
        });
    }

    /*
     * Creates listener for the "Settings" button
     * Opens popup window when clicked
     */
    public void settings_listener(Button settings_button) {
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater Layoutinflater = (LayoutInflater)
                        getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = Layoutinflater.from(getActivity()).inflate(R.layout.settings_popup, null);


                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                popupWindow.setAnimationStyle(R.style.Animation);
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                /*
                 * Disables the settings button when popup window appears
                 */
                settings_button.setOnClickListener(null);
                /*
                 * Disables the play button when popup window appears
                 */
                getActivity().findViewById(R.id.play_button).setOnClickListener(null);

                /*
                 * Button that switches difficulty to easy
                 */
                Button Easy_button = popupView.findViewById(R.id.button_easy);
                /*
                 * Button that switches difficulty to intermediate
                 */
                Button Medium_button = popupView.findViewById(R.id.button_medium);
                /*
                 * Button that switches difficulty to hard
                 */
                Button Hard_button = popupView.findViewById(R.id.button_hard);

                changeColor(Easy_button, Medium_button, Hard_button);

                /*
                 * Created listener on the Easy button
                 */
                Easy_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resetColor(Easy_button, Medium_button, Hard_button, getDifficulty());
                        setDifficulty(1);
                        Easy_button.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.teal_200));
                    }
                });
                /*
                 * Created listener on the Intermediate button
                 */
                Medium_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resetColor(Easy_button, Medium_button, Hard_button, getDifficulty());
                        setDifficulty(2);
                        Medium_button.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.teal_200));
                    }
                });
                /*
                 * Created listener on the Hard button
                 */
                Hard_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resetColor(Easy_button, Medium_button, Hard_button, getDifficulty());
                        setDifficulty(3);
                        Hard_button.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.teal_200));
                    }
                });

                /*
                 * Enables Play and Settings buttons when the popup window disappears
                 */
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        play_listener(getActivity().findViewById(R.id.play_button));
                        settings_listener(settings_button);
                    }
                });

                /*
                 * Handles hiding the popup window when it gets swiped
                 */
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                y1 = event.getRawY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                y2 = event.getRawY();
                                if(y1 - y2 > 0.15*Resources.getSystem().getDisplayMetrics().heightPixels) {
                                    popupWindow.dismiss();
                                };
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }

    /*
     * Changes color of the button of the current difficulty
     */
    public void changeColor(Button easy, Button medium, Button hard) {
        switch (((MainActivity)getActivity()).getDifficulty_setting()) {
            case 1:
                changeColor(easy, R.color.teal_200);
                break;
            case 2:
                changeColor(medium, R.color.teal_200);
                break;
            case 3:
                changeColor(hard, R.color.teal_200);
                break;
            default:
                break;
        }
    }

    /*
     * Changes color of the button of to the color provided by the id
     */
    public void changeColor(Button button, int color) {
        button.setBackgroundTintList(getContext().getResources().getColorStateList(color));
    }

    /*
     * Changes color of the button back to normal
     */
    public void resetColor(Button easy, Button medium, Button hard, int difficulty_setting) {
        switch (difficulty_setting) {
            case 1:
                changeColor(easy, R.color.purple_500);
                break;
            case 2:
                changeColor(medium, R.color.purple_500);
                break;
            case 3:
                changeColor(hard, R.color.purple_500);
                break;
            default: break;
        }
    }
}
