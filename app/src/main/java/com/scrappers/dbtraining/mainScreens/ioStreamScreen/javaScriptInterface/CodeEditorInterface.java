package com.scrappers.dbtraining.mainScreens.ioStreamScreen.javaScriptInterface;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class CodeEditorInterface {
    private final Context context;
    private String code;
    private String mode;
    private int containerHeight;
    public CodeEditorInterface(Context context) {
        this.context=context;
    }

    @JavascriptInterface
    public String getCode(){
        return code;
    }

    @JavascriptInterface
    public void setCode(String code) {
        this.code = code;
    }
    public int getContainerHeight(){
        return containerHeight;
    }
    @JavascriptInterface
    public void setContainerHeight(int containerHeight) {
        this.containerHeight = containerHeight;
    }
    @JavascriptInterface
    public void setMode(String mode) {
        this.mode = mode;
    }
    @JavascriptInterface
    public String getMode() {
        return mode;
    }
}
