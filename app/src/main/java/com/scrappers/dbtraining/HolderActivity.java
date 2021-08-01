package com.scrappers.dbtraining;

import android.os.Bundle;

import com.scrappers.dbtraining.mainScreens.prefaceScreen.PrefaceScreen;
import com.scrappers.dbtraining.navigation.Navigation;
import com.scrappers.superiorExtendedEngine.jmeSurfaceView.uiUtils.SystemVisibilityUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);
        Navigation navigation=new Navigation(HolderActivity.this);
        navigation.start();
        //display the default fragment screen
        navigation.displayWindow(new PrefaceScreen());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        SystemVisibilityUI systemVisibilityUI=new SystemVisibilityUI(HolderActivity.this);
        systemVisibilityUI.setGameMode();
    }
}