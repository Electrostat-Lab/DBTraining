package com.scrappers.dbtraining.mainScreens.prefaceScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme3.app.jmeSurfaceView.JmeSurfaceView;
import com.jme3.math.FastMath;
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
        final Toolbar toolbar=view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        final JmeSurfaceView jmeSurfaceView = view.findViewById(R.id.jmeSurfaceView);
        jmeSurfaceView.setLegacyApplication(new Renderer(jmeSurfaceView));
        jmeSurfaceView.startRenderer(100);
        final UiStateManager uiStateManager=new UiStateManager((ViewGroup) jmeSurfaceView.getParent());
        uiStateManager.attachUiState(uiStateManager.fromXML(R.layout.splash_screen)).setId('S');
        uiStateManager.getChildUiStateByIndex(0).setOnClickListener((_view)->{});
        jmeSurfaceView.setOnRendererCompleted((application, settings) ->
                uiStateManager.getChildUiStateByIndex(0).
                animate().rotationBy(FastMath.interpolateLinear(uiStateManager.getChildUiStateByIndex(0).getRotation(), 180, 360))
                .scaleX(0).scaleY(0).setDuration(1000).withEndAction(() -> uiStateManager.detachUiState(uiStateManager.getChildUiStateById('S'))).start()
        );

        jmeSurfaceView.setOnExceptionThrown(e -> {

        });
    }

}
