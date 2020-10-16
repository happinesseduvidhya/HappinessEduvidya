package com.happiness.eduvidhya.datamodels;

public class ModelMeetingHistories {

    String strMeetingName;
    String strMeetingID;
    String strMeetingDate;
    String strMeetingTime;

    public String getStrMeetingStatus() {
        return strMeetingStatus;
    }

    public void setStrMeetingStatus(String strMeetingStatus) {
        this.strMeetingStatus = strMeetingStatus;
    }

    public ModelMeetingHistories(String strMeetingName, String strMeetingID, String strMeetingDate, String strMeetingTime, String strMeetingStatus, String strClassName) {
        this.strMeetingName = strMeetingName;
        this.strMeetingID = strMeetingID;
        this.strMeetingDate = strMeetingDate;
        this.strMeetingTime = strMeetingTime;
        this.strMeetingStatus = strMeetingStatus;
        this.strClassName = strClassName;
    }

    String strMeetingStatus;
    String strClassName;



    public String getStrMeetingName() {
        return strMeetingName;
    }

    public void setStrMeetingName(String strMeetingName) {
        this.strMeetingName = strMeetingName;
    }

    public String getStrMeetingID() {
        return strMeetingID;
    }

    public void setStrMeetingID(String strMeetingID) {
        this.strMeetingID = strMeetingID;
    }

    public String getStrMeetingDate() {
        return strMeetingDate;
    }

    public void setStrMeetingDate(String strMeetingDate) {
        this.strMeetingDate = strMeetingDate;
    }

    public String getStrMeetingTime() {
        return strMeetingTime;
    }

    public void setStrMeetingTime(String strMeetingTime) {
        this.strMeetingTime = strMeetingTime;
    }

    public String getStrClassName() {
        return strClassName;
    }

    public void setStrClassName(String strClassName) {
        this.strClassName = strClassName;
    }
}
