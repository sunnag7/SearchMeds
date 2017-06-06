package info.androidhive.searchmed.model;

/**
 * Created by Sanny on 11/12/2015.
 */
public class Chat {

    int chatID;
    int vendorID;
    int chatType;
    int vendorType;
    int chatDelState;
    String dateTime;
    String vendorName;
    String chatText;
    String Vendor_AREA;
    String prescriptionOrderNo;
    String chatID_Server;

    public String getChatID_Server() {
        return chatID_Server;
    }

    public void setChatID_Server(String chatID_Server) {
        this.chatID_Server = chatID_Server;
    }

    public String getPrescriptionOrderNo() {
        return prescriptionOrderNo;
    }

    public void setPrescriptionOrderNo(String prescriptionOrderNo) {

        this.prescriptionOrderNo = prescriptionOrderNo;
    }

    public String getVendor_AREA() {
        return Vendor_AREA;
    }

    public void setVendor_AREA(String vendor_AREA) {
        Vendor_AREA = vendor_AREA;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getChatText() {
        return chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }

    public int getChatDelState() {
        return chatDelState;
    }

    public void setChatDelState(int chatDelState) {
        this.chatDelState = chatDelState;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public int getVendorID() {
        return vendorID;
    }

    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public int getVendorType() {
        return vendorType;
    }

    public void setVendorType(int vendorType) {
        this.vendorType = vendorType;
    }



}
