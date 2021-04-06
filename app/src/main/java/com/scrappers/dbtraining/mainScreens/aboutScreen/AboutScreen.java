package com.scrappers.dbtraining.mainScreens.aboutScreen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.scrappers.dbtraining.R;
import com.scrappers.dbtraining.navigation.Navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutScreen extends Fragment {
    private RelativeLayout linearLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about,container,false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        linearLayout=view.findViewById(R.id.aboutView);
        linearLayout.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.thow_up));
        Navigation.drawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    linearLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.thow_down));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
}
