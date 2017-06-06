package info.androidhive.searchmed.model;

public class Notifications {

    private String notificationDate;
    private String notificationTime;
    private String notificationText;
    private String notificationVendorName;
    private String notificationVendorType;
    private String notificationVendorId;
    private String notificationChatTpe;
    private String notificationCity;

    public String getNotificationVendorId() {
        return notificationVendorId;
    }

    public void setNotificationVendorId(String notificationVendorId) {
        this.notificationVendorId = notificationVendorId;
    }

    public String getNotificationChatTpe() {
        return notificationChatTpe;
    }

    public void setNotificationChatTpe(String notificationChatTpe) {
        this.notificationChatTpe = notificationChatTpe;
    }

    public String getNotificationCity() {
        return notificationCity;
    }

    public void setNotificationCity(String notificationCity) {
        this.notificationCity = notificationCity;
    }

    public String getNotificationVendorType() {
        return notificationVendorType;
    }

    public void setNotificationVendorType(String notificationVendorType) {
        this.notificationVendorType = notificationVendorType;
    }

    public String getNotificationVendorName() {
        return notificationVendorName;
    }

    public void setNotificationVendorName(String notificationVendorName) {
        this.notificationVendorName = notificationVendorName;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }
}
