package com.scrappers.dbtraining.mainScreens.prefaceScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme3.app.LegacyApplication;
import com.jme3.app.jmeSurfaceView.JmeSurfaceView;
import com.scrappers.dbtraining.R;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Renderer;
import com.scrappers.superiorExtendedEngine.menuStates.UiStateManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import static com.scrappers.dbtraining.navigation.Navigation.drawerLayout;

public class PrefaceScreen extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prefacescreen,container,false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar=view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        JmeSurfaceView jmeSurfaceView=view.findViewById(R.id.jmeSurfaceView);
        UiStateManager uiStateManager=new UiStateManager(jmeSurfaceView);
        uiStateManager.attachUiState(uiStateManager.fromXML(R.layout.splash_screen)).setId('S');
        jmeSurfaceView.setLegacyApplication(new Renderer(jmeSurfaceView));
        jmeSurfaceView.setOnRendererCompleted((application, settings) -> {
            uiStateManager.getChildUiStateByIndex(0).
                    animate().translationYBy(200).setDuration(500).withEndAction(() -> uiStateManager.detachUiState(uiStateManager.getChildUiStateById(0))).start();
        });
        jmeSurfaceView.startRenderer(0);

        jmeSurfaceView.setOnExceptionThrown(e -> {

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
