package info.androidhive.searchmed.model;

/**
 * Created by AndroidPC2 on 06-Feb-16.
 */
public class PendingOrder {
    String order_amount;
    String order_delivery_amount;
    String order_no;
    String order_status_id;
    String order_type_id;
    String order_vendor_amount;
    String order_date;
    String payment_type_id;
    String vendor_bill_no;
    String vendor_id;
    String customer_id;
    String Customer_Name;
    String Vendor_Name;
    String Vendor_Type_Name;
    String Order_Status;

    public String getOrder_Status() {
        return Order_Status;
    }

    public void setOrder_Status(String order_Status) {
        Order_Status = order_Status;
    }

    public String getVendor_Type_Name() {
        return Vendor_Type_Name;
    }

    public void setVendor_Type_Name(String vendor_Type_Name) {
        Vendor_Type_Name = vendor_Type_Name;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public String getVendor_Name() {
        return Vendor_Name;
    }

    public void setVendor_Name(String vendor_Name) {
        Vendor_Name = vendor_Name;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getOrder_delivery_amount() {
        return order_delivery_amount;
    }

    public void setOrder_delivery_amount(String order_delivery_amount) {
        this.order_delivery_amount = order_delivery_amount;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_status_id() {
        return order_status_id;
    }

    public void setOrder_status_id(String order_status_id) {
        this.order_status_id = order_status_id;
    }

    public String getOrder_type_id() {
        return order_type_id;
    }

    public void setOrder_type_id(String order_type_id) {
        this.order_type_id = order_type_id;
    }

    public String getOrder_vendor_amount() {
        return order_vendor_amount;
    }

    public void setOrder_vendor_amount(String order_vendor_amount) {
        this.order_vendor_amount = order_vendor_amount;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getPayment_type_id() {
        return payment_type_id;
    }

    public void setPayment_type_id(String payment_type_id) {
        this.payment_type_id = payment_type_id;
    }

    public String getVendor_bill_no() {
        return vendor_bill_no;
    }

    public void setVendor_bill_no(String vendor_bill_no) {
        this.vendor_bill_no = vendor_bill_no;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }


}
