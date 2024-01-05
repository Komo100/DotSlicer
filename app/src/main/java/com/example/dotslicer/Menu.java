package com.example.dotslicer;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Menu extends Fragment {

    private int difficulty_setting = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu, container, false);

        final Button settings_button = (Button) view.findViewById(R.id.settings_button);
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater Layoutinflater = (LayoutInflater)
                        getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = Layoutinflater.from(getActivity()).inflate(R.layout.settings_popup, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                Button Easy_button = popupView.findViewById(R.id.button_easy);
                Button Medium_button = popupView.findViewById(R.id.button_medium);
                Button Hard_Button = popupView.findViewById(R.id.button_hard);
                // dismiss the popup window when touched
                popupView.findViewById(R.id.button_exit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                Easy_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDifficulty(1);
                    }
                });
                Medium_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDifficulty(2);
                    }
                });
                Hard_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDifficulty(3);
                    }
                });
            }
        });

        return view;
    }

    public void setDifficulty(int x) {
        ((MainActivity)getActivity()).setDifficulty_setting(x);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Button play_button = (Button) view.findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).ChangeToFragment(new Game());
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
