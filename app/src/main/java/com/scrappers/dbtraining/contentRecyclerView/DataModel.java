package com.scrappers.dbtraining.contentRecyclerView;

public class DataModel {
    private String title;
    private String illustrations;
    private String code;
    private String mode;
    private int editorHeight;

    public DataModel(String title,String illustrations, String code, String mode, int editorHeight){
        this.title=title;
        this.illustrations=illustrations;
        this.code=code;
        this.mode=mode;
        this.editorHeight=editorHeight;
    }

    public String getIllustrations() {
        return illustrations;
    }

    public void setIllustrations(String illustrations) {
        this.illustrations = illustrations;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getEditorHeight() {
        return editorHeight;
    }

    public void setEditorHeight(int editorHeight) {
        this.editorHeight = editorHeight;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
