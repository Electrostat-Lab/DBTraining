package com.scrappers.dbtraining.navigation;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.scrappers.dbtraining.R;
import com.scrappers.dbtraining.mainScreens.aboutScreen.AboutScreen;
import com.scrappers.dbtraining.mainScreens.ioBufferScreen.IOBufferScreen;
import com.scrappers.dbtraining.mainScreens.ioStreamScreen.IOStreamScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {
    private final AppCompatActivity context;
    public static DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public Navigation(@NonNull AppCompatActivity context) {
        this.context=context;
    }
    public void start(){
        drawerLayout=context.findViewById(R.id.drawer);
        navigationView=context.findViewById(R.id.navigation);
        ImageButton aboutProfile=navigationView.getHeaderView(0).findViewById(R.id.aboutProfile);
        aboutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                displayWindow(new AboutScreen());
            }
        });

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.streams):
                        drawerLayout.closeDrawer(GravityCompat.START);
                        displayWindow(new IOStreamScreen());
                        break;
                    case (R.id.buffers):
                        drawerLayout.closeDrawer(GravityCompat.START);
                        displayWindow(new IOBufferScreen());
                }
                return true;
            }
        });
    }
    private void displayWindow(@NonNull Fragment fragment){
        FragmentTransaction fragmentTransaction= context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content,fragment);
        fragmentTransaction.commit();
    }
}
