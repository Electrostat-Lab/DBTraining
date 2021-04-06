package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer;

import com.jme3.app.SimpleApplication;
import com.scrappers.superiorExtendedEngine.jmeSurfaceView.JmeSurfaceView;

public class Renderer extends SimpleApplication {
    private final JmeSurfaceView jmeSurfaceView;
    public Renderer(JmeSurfaceView jmeSurfaceView){
        this.jmeSurfaceView=jmeSurfaceView;
    }
    @Override
    public void simpleInitApp() {
        stateManager.attach(new Scene(rootNode,jmeSurfaceView));
    }

    public JmeSurfaceView getJmeSurfaceView() {
        return jmeSurfaceView;
    }
}
