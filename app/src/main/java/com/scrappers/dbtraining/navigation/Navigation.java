package com.scrappers.dbtraining.navigation;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.scrappers.dbtraining.R;
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
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.streams):
                        drawerLayout.closeDrawer(GravityCompat.START);
                        displayWindow(new IOStreamScreen());
                        break;
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
