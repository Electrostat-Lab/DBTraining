package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.triggersUtils;

public class TriggerModel {
    private String btnText;
    private char btnID;
    private int btnIndex;

    public TriggerModel(final String btnText, final char btnID, final int btnIndex){
        this.btnText = btnText;
        this.btnID = btnID;
        this.btnIndex = btnIndex;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public char getBtnID() {
        return btnID;
    }

    public void setBtnID(char btnID) {
        this.btnID = btnID;
    }

    public int getBtnIndex() {
        return btnIndex;
    }

    public void setBtnIndex(int btnIndex) {
        this.btnIndex = btnIndex;
    }
}
