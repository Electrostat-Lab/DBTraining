package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.triggersUtils;

public class TriggerModel {
    private String btnText;
    private char btnID;

    public TriggerModel(final String btnText, final char btnID){
        this.btnText = btnText;
        this.btnID = btnID;
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

}
