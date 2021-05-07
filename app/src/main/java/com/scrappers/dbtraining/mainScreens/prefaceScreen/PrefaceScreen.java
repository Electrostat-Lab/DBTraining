package com.scrappers.dbtraining.mainScreens.prefaceScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private JmeSurfaceView jmeSurfaceView;
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

        jmeSurfaceView=view.findViewById(R.id.jmeSurfaceView);
        jmeSurfaceView.setLegacyApplication(new Renderer(jmeSurfaceView));
        jmeSurfaceView.startRenderer(100);
        UiStateManager uiStateManager=new UiStateManager((ViewGroup) jmeSurfaceView.getParent());
        uiStateManager.attachUiState(uiStateManager.fromXML(R.layout.splash_screen)).setId('S');
        jmeSurfaceView.setOnRendererCompleted((application, settings) -> uiStateManager.getChildUiStateByIndex(0).
                animate().translationY(-uiStateManager.getChildUiStateById('S').getLayoutParams().height*2)
                .setDuration(500).withEndAction(() -> uiStateManager.detachUiState(uiStateManager.getChildUiStateById('S'))).start());

        jmeSurfaceView.setOnExceptionThrown(e -> {

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        jmeSurfaceView.destroy();
    }
}
