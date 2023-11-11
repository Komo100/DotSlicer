package com.example.dotslicer;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MainActivity extends AppCompatActivity {

    private int difficulty_setting = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                MainActivity.this.startActivity(GameActivityIntent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                finish();
            }
        });
        findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.settings_popup, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.findViewById(R.id.button_exit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                popupView.findViewById(R.id.button_easy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        difficulty_setting = 0;
                    }
                });
                popupView.findViewById(R.id.button_medium).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        difficulty_setting = 1;
                    }
                });
                popupView.findViewById(R.id.button_hard).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        difficulty_setting = 2;
                    }
                });
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