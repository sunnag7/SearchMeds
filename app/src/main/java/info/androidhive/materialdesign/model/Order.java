package info.androidhive.materialdesign.model;

/**
 * Created by Administrator on 24/12/2015.
 */
public class Order {

    int vendorID;
    int chatID;
    int orderID;
    double orderAmount;
    double totalOrdAmnt;
    double deliveryAmnt;
    String billNo;
    int ordConfirmState;
    int orderStateConfirmation = 0;
    String paymentRequestNo;

    public String getPaymentRequestNo() {
        return paymentRequestNo;
    }

    public void setPaymentRequestNo(String paymentRequestNo) {
        this.paymentRequestNo = paymentRequestNo;
    }

    public int getOrderStateConfirmation() {
        return orderStateConfirmation;
    }

    public void setOrderStateConfirmation(int orderStateConfirmation) {
        this.orderStateConfirmation = orderStateConfirmation;
    }

    public int getOrdConfirmState() {
        return ordConfirmState;
    }

    public void setOrdConfirmState(int ordConfirmState) {
        this.ordConfirmState = ordConfirmState;
    }

    public int getVendorID() {
        return vendorID;
    }

    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    public int getOrderID() {
        return orderID;
    }
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public double getTotalOrdAmnt() {
        return totalOrdAmnt;
    }

    public void setTotalOrdAmnt(double totalOrdAmnt) {
        this.totalOrdAmnt = totalOrdAmnt;
    }

    public double getDeliveryAmnt() {
        return deliveryAmnt;
    }

    public void setDeliveryAmnt(double deliveryAmnt) {
        this.deliveryAmnt = deliveryAmnt;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }


}
