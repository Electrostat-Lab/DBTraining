package com.scrappers.dbtraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.scrappers.dbtraining.mainScreens.ioStreamScreen.IOStreamScreen;
import com.scrappers.dbtraining.navigation.Navigation;

import static com.scrappers.dbtraining.navigation.Navigation.drawerLayout;

public class HolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);

        Navigation navigation=new Navigation(HolderActivity.this);
        navigation.start();

        displayWindow(new IOStreamScreen());

    }
    private void displayWindow(@NonNull Fragment fragment){
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content,fragment);
        fragmentTransaction.commit();
    }
}