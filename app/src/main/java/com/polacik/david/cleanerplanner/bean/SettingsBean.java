package com.polacik.david.cleanerplanner.bean;

public class SettingsBean {

    private String settingsId;
    private String settingsDefaultSms;
    private String settingsDefaultEmailSubject;
    private String settingsDefaultEmailText;

    public SettingsBean() {
    }

    public SettingsBean(String settingsId, String settingsDefaultSms, String settingsDefaultEmailSubject, String settingsDefaultEmailText) {
        this.settingsId = settingsId;
        this.settingsDefaultSms = settingsDefaultSms;
        this.settingsDefaultEmailSubject = settingsDefaultEmailSubject;
        this.settingsDefaultEmailText = settingsDefaultEmailText;
    }

    public String getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(String settingsId) {
        this.settingsId = settingsId;
    }

    public String getSettingsDefaultSms() {
        return settingsDefaultSms;
    }

    public void setSettingsDefaultSms(String settingsDefaultSms) {
        this.settingsDefaultSms = settingsDefaultSms;
    }

    public String getSettingsDefaultEmailSubject() {
        return settingsDefaultEmailSubject;
    }

    public void setSettingsDefaultEmailSubject(String settingsDefaultEmailSubject) {
        this.settingsDefaultEmailSubject = settingsDefaultEmailSubject;
    }

    public String getSettingsDefaultEmailText() {
        return settingsDefaultEmailText;
    }

    public void setSettingsDefaultEmailText(String settingsDefaultEmailText) {
        this.settingsDefaultEmailText = settingsDefaultEmailText;
    }

    @Override
    public String toString() {
        return "SettingsBean{" +
                "settingsId='" + settingsId + '\'' +
                ", settingsDefaultSms='" + settingsDefaultSms + '\'' +
                ", settingsDefaultEmailSubject='" + settingsDefaultEmailSubject + '\'' +
                ", settingsDefaultEmailText='" + settingsDefaultEmailText + '\'' +
                '}';
    }
}
