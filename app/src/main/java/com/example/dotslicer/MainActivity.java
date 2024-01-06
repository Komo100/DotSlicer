package com.example.dotslicer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;

/*
 * This is the main Activity
 */
public class MainActivity extends AppCompatActivity {
    /*
     * Difficulty level expressed in integers
     * 1 - easy
     * 2 - intermediate
     * 3 - hard
     */
    private int difficulty_setting = 1;

    /*
     * This function is called after the application starts
     * Opens the menu
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChangeToFragment(new Menu());
    }

    /*
     * Changes fragment in FragmentContainer in activity_main.xml
     */
    public void ChangeToFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setReorderingAllowed(true);
        ft.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        ft.addToBackStack(null);
        ft.replace(R.id.fragmentContainer, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    /*
     * Gets current difficulty level
     */
    public int getDifficulty_setting() {return difficulty_setting;}
    /*
     * Changes difficulty level
     */
    public void setDifficulty_setting(int x){difficulty_setting = x;}


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}