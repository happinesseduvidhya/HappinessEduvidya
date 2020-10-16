package com.happiness.eduvidhya.datamodels;

public class ModelAttendenceUser {

    String strUserEmail;
    String strMeetingID;

    public ModelAttendenceUser(String strUserEmail, String strMeetingID, String strMeetingReceivedDate, String strMeetingAttendence) {
        this.strUserEmail = strUserEmail;
        this.strMeetingID = strMeetingID;
        this.strMeetingReceivedDate = strMeetingReceivedDate;
        this.strMeetingAttendence = strMeetingAttendence;
    }

    String strMeetingReceivedDate;
    String strMeetingAttendence;


    public String getStrMeetingAttendence() {
        return strMeetingAttendence;
    }

    public void setStrMeetingAttendence(String strMeetingAttendence) {
        this.strMeetingAttendence = strMeetingAttendence;
    }





    public String getStrUserEmail() {
        return strUserEmail;
    }

    public void setStrUserEmail(String strUserEmail) {
        this.strUserEmail = strUserEmail;
    }

    public String getStrMeetingID() {
        return strMeetingID;
    }

    public void setStrMeetingID(String strMeetingID) {
        this.strMeetingID = strMeetingID;
    }

    public String getStrMeetingReceivedDate() {
        return strMeetingReceivedDate;
    }

    public void setStrMeetingReceivedDate(String strMeetingReceivedDate) {
        this.strMeetingReceivedDate = strMeetingReceivedDate;
    }



}
