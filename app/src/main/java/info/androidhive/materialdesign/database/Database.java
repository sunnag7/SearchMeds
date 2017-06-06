package info.androidhive.materialdesign.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.model.Chat;
import info.androidhive.materialdesign.model.CurrentCity;
import info.androidhive.materialdesign.model.Notifications;
import info.androidhive.materialdesign.model.Order;
import info.androidhive.materialdesign.model.Vendor;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "searchMeds.sqlite";

    private static final String TABLE_FAV = "CustomerFavorite";
    private static final String TABLE_CHAT = "Chat_";
    private static final String TABLE_ORDER = "Order_";
    private static final String TABLE_CURRENT_CITY = "currentCity";
    private static final String TABLE_NOTIFICATIONS = "notifications";

    private static final String KEY_ID = "id"; // Primary Key

    // Vendor table column names
    private static final String VENDORID   = "venid"; //cust_id
    private static final String VENDORNAME  = "ven_name";
    private static final String VENDOREMAIL = "ven_Email"; //
    private static final String VENDORCONTACTNAME = "ven_contact_person";  //contact person name
    private static final String VENDORMOBILE = "ven_mobile"; //mobile
    private static final String VENDORADDRESS = "ven_Address"; //address
    private static final String VEN_STATE = "ven_state";
    private static final String VEN_DELSTATE = "ven_DelState";
    private static final String VEN_CITY = "ven_city";
    private static final String VEN_TYPE = "ven_Type";
    private static final String VENDORLANDLINE = "ven_landline";
    private static final String VEN_AREANAME = "ven_area";
    private static final String VEN_AREAID = "ven_area_id";
    private static final String FAV_STATUS = "favStatus";
    private static final String FROM_TIME = "fromTime";
    private static final String TO_TIME = "toTime";


    // Chat table column names
    private static final String CHAT_ID   = "chatid";
    private static final String CHAT_VEN_ID  = "ch_ven_id";
    private static final String CHAT_VENDOR_NAME  = "ch_vendor_name";
    private static final String CHAT_VENDOR_TYPE  = "ch_vendor_type";
    private static final String CHAT_VENDOR_AREA = "ch_vendor_area";
    private static final String CHAT_TEXT = "chat_txt";
    private static final String CHAT_DATETIME = "chat_date_time";
    private static final String CHAT_DEL_STATE = "del_state";
    private static final String CHAT_TYPE = "ch_type";
    private static final String CHAT_PRESCRIPTIONuPLOAD = "prescriptionUpload";
    private static final String CHAT_ID_SERVER = "chatIdServer";

    private static final String ORD_ID   = "ordid";
    private static final String ORD_VEN_ID  = "ord_ven_id";
    private static final String ORD_AMOUNT = "amount";
    private static final String ORD_TOTAL = "total_amount";
    private static final String ORD_DELIVERY = "del_amount";
    private static final String ORD_BILL_NO = "bill_no";
    private static final String ORD_STATUS = "status";
    private static final String ORD_CHAT_ID = "chat_id";
    private static final String ORD_CONFIRM_STATE = "ord_confirm_state";
    private static final String ORD_STATE_CONFIRMATION = "orderState_Confirmation";  //allowing user to confirm order only once
    private static final String PAYMENT_REQUEST_NO = "paymentRequestNo";

    private static final String CITY_NAME = "cityName";
    private static final String AREA_NAME = "areaName";
    private static final String CITY_ID = "cityid";
    private static final String CAT_ID = "catid";


    private static final String NOTIFICATION_DATE = "notificationDate";
    private static final String NOTIFICATION_TIME= "notificationTime";
    private static final String NOTIFICATION_MESG = "notificationMesg";
    private static final String NOTIFICATION_VENDOR_NAME = "notificationVendorName";
    private static final String NOTIFICATION_VENDOR_ID = "notificationVendorId";
    private static final String NOTIFICATION_VENDOR_TYPE = "notificationVendorType";
    private static final String NOTIFICATION_VENDOR_CHAT_TYPE = "notificationVendorChatType";
    private static final String NOTIFICATION_CITY = "notificationCity";


    private static final String CREATE_TABLE_FAV = "CREATE TABLE IF NOT EXISTS "
            + TABLE_FAV
            + "("
            + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + VENDORID
            + " INTEGER,"
            + VENDORNAME
            + " TEXT,"
            + VENDORCONTACTNAME
            + " TEXT,"
            + VEN_CITY
            + " TEXT,"
            + VENDOREMAIL
            + " TEXT,"
            + VENDORMOBILE
            + " TEXT,"
            + VENDORADDRESS
            + " TEXT,"
            + VEN_STATE
            + " TEXT,"
            + VEN_DELSTATE
            + " TEXT,"
            + VEN_AREANAME
            + " TEXT,"
            + VENDORLANDLINE
            + " TEXT,"
            + VEN_AREAID
            + " INTEGER,"
            + VEN_TYPE
            + " INTEGER, "
            + FROM_TIME
            + " TEXT,"
            + TO_TIME
            + " TEXT,"
            + FAV_STATUS
            + " INTEGER "
            + ")";

    private static final String CREATE_TABLE_CHAT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CHAT
            + "("
            + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CHAT_ID
            + " INTEGER,"
            + CHAT_TEXT
            + " TEXT,"
            + CHAT_VEN_ID
            + " INTEGER,"
            + CHAT_VENDOR_NAME
            + " TEXT,"
            + CHAT_VENDOR_TYPE
            + " TEXT,"
            + CHAT_VENDOR_AREA
            + " TEXT,"
            + CHAT_DEL_STATE
            + " INTEGER,"
            + CHAT_DATETIME
            + " TEXT,"
            + CHAT_TYPE
            + " INTEGER,"
            + CHAT_ID_SERVER
            + " INTEGER,"
            + CHAT_PRESCRIPTIONuPLOAD
            + " INTEGER "
            + ")";

    private static final String CREATE_TABLE_ORDER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ORDER
            + "("
            + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ORD_ID
            + " INTEGER,"
            + ORD_CHAT_ID
            + " INTEGER,"
            + ORD_AMOUNT
            + " TEXT,"
            + ORD_VEN_ID
            + " INTEGER,"
            + ORD_BILL_NO
            + " TEXT,"
            + ORD_TOTAL
            + " TEXT,"
            + ORD_DELIVERY
            + " TEXT, "
            + ORD_STATUS
            + " TEXT, "
            + ORD_CONFIRM_STATE
            + " INTEGER, "
            + ORD_STATE_CONFIRMATION
            +  " INTEGER, "
            + PAYMENT_REQUEST_NO
            + " INTEGER "
            + ")";


    private static final String CREATE_TABLE_CURRENT_CITY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CURRENT_CITY
            + "("
            + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CITY_NAME
            + " TEXT, "
            + CITY_ID
            + " INTEGER, "
            + CAT_ID
            + " INTEGER "
            + ")";


    private static final String CREATE_TABLE_NOTIFICATIONS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NOTIFICATIONS
            + "("
            + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NOTIFICATION_DATE
            + " TEXT,"
            + NOTIFICATION_TIME
            + " TEXT,"
            + NOTIFICATION_MESG
            + " TEXT, "
            + NOTIFICATION_VENDOR_NAME
            + " TEXT, "
            + NOTIFICATION_VENDOR_ID
            + " INTEGER,"
            + NOTIFICATION_VENDOR_TYPE
            + " INTEGER,"
            + NOTIFICATION_VENDOR_CHAT_TYPE
            + " INTEGER, "
            + NOTIFICATION_CITY
            + " INTEGER "
            + ")";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAV);
        db.execSQL(CREATE_TABLE_CHAT);
        db.execSQL(CREATE_TABLE_ORDER);
        db.execSQL(CREATE_TABLE_CURRENT_CITY);
        db.execSQL(CREATE_TABLE_NOTIFICATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //to get all chat customers
    public List<Chat> getAllChatCustomer() {

        List<Chat> chat = new ArrayList<Chat>();

        String selectQuery = "SELECT MIN(id) AS id,ch_ven_id\n" +
                ", ch_vendor_name\n" +
                ", ch_vendor_area\n" +
                ",ch_vendor_type\n" +
                ", id FROM Chat_ GROUP BY ch_vendor_name ORDER BY id";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        //if(c.getCount()>0) {
        if (c != null && c.moveToFirst() ) {
            do {
                Chat r = new Chat();

                r.setVendorID(c.getInt(c.getColumnIndex(CHAT_VEN_ID)));
                r.setVendorName(c.getString(c.getColumnIndex(CHAT_VENDOR_NAME)));
                r.setVendor_AREA(c.getString(c.getColumnIndex(CHAT_VENDOR_AREA)));
                r.setVendorType(c.getInt(c.getColumnIndex(CHAT_VENDOR_TYPE)));
                chat.add(r);

            } while (c.moveToNext());
        }
        if (c != null) {
            c.close();
        }
        db.close();

        return chat;
    }

    /**
     * This function will create a Vendor item in Vendor table
     */
    public long createFav(Vendor aVendor)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        System.out.println("ID :"+ aVendor.getVENDORID());

        values.put(VENDORID, aVendor.getVENDORID());
        values.put(VENDORNAME, aVendor.getVENDORNAME());
        values.put(VEN_AREANAME, aVendor.getAREANAME());
        values.put(VEN_AREAID, aVendor.getAREAID());
        values.put(VEN_CITY, aVendor.getCITY());
        values.put(VEN_STATE, aVendor.getSTATE());
        values.put(VENDORADDRESS, aVendor.getVENDORADDRESS());
        values.put(VEN_TYPE, aVendor.getVEN_TYPE());
        values.put(VENDOREMAIL , aVendor.getVENDOREMAIL());
        values.put(VEN_DELSTATE , aVendor.getVEN_DELSTATE());
        values.put(VENDORCONTACTNAME, aVendor.getVENDORCONTACTNAME());
        values.put(VENDORLANDLINE, aVendor.getVENDORLANDLINE());
        values.put(VENDORMOBILE, aVendor.getVENDORMOBILE());
        values.put(FAV_STATUS,"1");

        values.put(FROM_TIME,aVendor.getFROM_TIME());
        values.put(TO_TIME,aVendor.getTO_TIME());

        // insert row
        // "row_ID" contains the value of row ID of row which has
        // been inserted
        long row_id = db.insert(TABLE_FAV, null, values);
        db.close();
        Log.d("Database.class", "Parent class object values are added to DB");

        System.out.println("CreateEmployee :"+values);

        return row_id;
    }


    public ArrayList<Vendor> getAllFavourite_Vendors() {

        ArrayList<Vendor> fav_Vendor = new ArrayList<Vendor>();

        String selectQuery = "SELECT * FROM " + TABLE_FAV;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.d("Favourite Cursor", c.toString());

        if (c != null && c.moveToFirst()) {
            do {
                Vendor f = new Vendor();
                f.setVENDORID(c.getString(c.getColumnIndex(VENDORID)));
                f.setVENDORNAME(c.getString(c.getColumnIndex(VENDORNAME)));
                f.setVENDORCONTACTNAME(c.getString(c.getColumnIndex(VENDORCONTACTNAME)));
                f.setCITY(c.getString(c.getColumnIndex(VEN_CITY)));
                f.setVENDOREMAIL(c.getString(c.getColumnIndex(VENDOREMAIL)));
                f.setVENDORMOBILE(c.getString(c.getColumnIndex(VENDORMOBILE)));
                f.setVENDORADDRESS(c.getString(c.getColumnIndex(VENDORADDRESS)));
                f.setSTATE(c.getString(c.getColumnIndex(VEN_STATE)));
                f.setVENDORAREA(c.getString(c.getColumnIndex(VEN_AREANAME)));
                f.setVENDORLANDLINE(c.getString(c.getColumnIndex(VENDORLANDLINE)));
                f.setVEN_TYPE(c.getInt(c.getColumnIndex(VEN_TYPE)));
                f.setAREAID(c.getInt(c.getColumnIndex(VEN_AREAID)));
                f.setVEN_DELSTATE(c.getString(c.getColumnIndex(VEN_DELSTATE)));
                f.setFavStatus(c.getString(c.getColumnIndex(FAV_STATUS)));

                f.setFROM_TIME(c.getString(c.getColumnIndex(FROM_TIME)));
                f.setTO_TIME(c.getString(c.getColumnIndex(TO_TIME)));
                fav_Vendor.add(f);

                Log.d("Favourite Vendor",fav_Vendor.toString());

            } while (c.moveToNext());
        }
        else
        {
            Log.d("PRACHI","");
        }
        c.close();
        db.close();

        return fav_Vendor;
    }


    //This function will create a Chat

    public long createChat(Chat aChat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        System.out.println("ID :"+ aChat.getChatID());

        //values.put(CHAT_ID, aChat.getChatID());
        values.put(CHAT_DATETIME, aChat.getDateTime());
        values.put(CHAT_DEL_STATE, aChat.getChatDelState());
        values.put(CHAT_TEXT, aChat.getChatText());
        values.put(CHAT_TYPE, aChat.getChatType());
        values.put(CHAT_VEN_ID, aChat.getVendorID());
        values.put(CHAT_VENDOR_NAME, aChat.getVendorName());
        values.put(CHAT_VENDOR_AREA, aChat.getVendor_AREA());

        values.put(CHAT_VENDOR_TYPE, aChat.getVendorType());
        values.put(CHAT_PRESCRIPTIONuPLOAD,aChat.getPrescriptionOrderNo());
        values.put(CHAT_ID_SERVER,aChat.getChatID_Server());


        long row_id = db.insert(TABLE_CHAT, null, values);
        db.close();
        Log.d("Database.class", "Parent class object values are added to DB"+row_id);

        System.out.println("CreateEmployee :" + values);

        return row_id;
    }

    public List<Chat> getAllChatForVendor(int vendID) {

        List<Chat> chat = new ArrayList<Chat>();

        //  String selectQuery = "SELECT * FROM " + TABLE_CHAT+ " WHERE "+CHAT_VEN_ID+" = "+vendID;

        // to display chat date time wise
        String selectQuery = "SELECT * FROM " + TABLE_CHAT+ " WHERE "+CHAT_VEN_ID+" = "+vendID + " ORDER BY " + CHAT_DATETIME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Chat r = new Chat();
                r.setChatID(c.getInt(c.getColumnIndex(KEY_ID)));
                r.setDateTime(c.getString(c.getColumnIndex(CHAT_DATETIME)));
                r.setChatText(c.getString(c.getColumnIndex(CHAT_TEXT)));
                r.setChatType(Integer.parseInt(c.getString(c.getColumnIndex(CHAT_TYPE))));
                r.setVendorID(Integer.parseInt(c.getString(c.getColumnIndex(CHAT_VEN_ID))));
                r.setVendorName(c.getString(c.getColumnIndex(CHAT_VENDOR_NAME)));

                r.setVendorType(c.getInt(c.getColumnIndex(CHAT_VENDOR_TYPE)));
                r.setPrescriptionOrderNo(c.getString(c.getColumnIndex(CHAT_PRESCRIPTIONuPLOAD)));
                r.setChatID_Server(c.getString(c.getColumnIndex(CHAT_ID_SERVER)));

                // adding to Parent list
                chat.add(r);

            } while (c.moveToNext());
        }
        db.close();

        return chat;
    }

    public void clearChatData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_CHAT);
        db.close();
    }

    // delete the duplicate chat by checking chatId from server
    public void deleteDuplicatesChatData(String ChatIdServer){

      //  getWritableDatabase().execSQL("delete from " + TABLE_CHAT + " where " + CHAT_ID_SERVER + " not in (SELECT MIN(chatIdServer) FROM "+ TABLE_CHAT + " ORDER BY chat_date_time)");
        getWritableDatabase().execSQL("delete from " + TABLE_CHAT + " where " + CHAT_ID_SERVER + " = " + ChatIdServer);
   }

    public void clearFavData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_FAV);
        db.close();
    }

    public long createOrder(Order aOrder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        System.out.println("ID :"+ aOrder.getVendorID());

        values.put(ORD_AMOUNT, aOrder.getOrderAmount());
        values.put(ORD_VEN_ID, aOrder.getVendorID());
        values.put(ORD_BILL_NO, aOrder.getBillNo());
        values.put(ORD_TOTAL, aOrder.getTotalOrdAmnt());
        values.put(ORD_DELIVERY, aOrder.getDeliveryAmnt());
        values.put(ORD_STATUS, aOrder.getVendorID());
        values.put(ORD_CHAT_ID, aOrder.getChatID());
        values.put(ORD_CONFIRM_STATE,aOrder.getOrdConfirmState());
        values.put(ORD_STATE_CONFIRMATION,aOrder.getOrderStateConfirmation());
        values.put(PAYMENT_REQUEST_NO,aOrder.getPaymentRequestNo());

        // insert row
        // "row_ID" contains the value of row ID of row which has
        // been inserted
        long row_id = db.insert(TABLE_ORDER, null, values);
        db.close();
        Log.d("Database.class", "Parent class object values are added to DB");

        System.out.println("CreateEmployee :"+values);

        return row_id;
    }

    public Order getOrderVendor(int chatID) {

        Order aOrder = new Order();

        String selectQuery = "SELECT * FROM " + TABLE_ORDER+ " WHERE "+ORD_CHAT_ID+" = "+chatID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                aOrder.setChatID(c.getInt(c.getColumnIndex(ORD_CHAT_ID)));
                aOrder.setVendorID(c.getInt(c.getColumnIndex(ORD_VEN_ID)));
                aOrder.setBillNo(c.getString(c.getColumnIndex(ORD_BILL_NO)) );
                aOrder.setTotalOrdAmnt(Double.parseDouble(c.getString(c.getColumnIndex(ORD_TOTAL))));
                aOrder.setOrderAmount(Double.parseDouble(c.getString(c.getColumnIndex(ORD_AMOUNT))));
                aOrder.setDeliveryAmnt(Double.parseDouble(c.getString(c.getColumnIndex(ORD_DELIVERY))));

                aOrder.setOrderStateConfirmation(c.getInt(c.getColumnIndex(ORD_STATE_CONFIRMATION)));
                aOrder.setPaymentRequestNo(c.getString(c.getColumnIndex(PAYMENT_REQUEST_NO)));
                // adding to Parent list

            } while (c.moveToNext());
        }
        db.close();

        return aOrder;
    }


    // Favourite Code
    public void checkDuplicate_favouritelist(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FAV);
        db.close();
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public long createCurrentCity(String aCurrentCity, String aCurrentCityID, String aCurrentCat_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CITY_NAME, aCurrentCity.toString());
        values.put(CITY_ID, aCurrentCityID.toString());
        values.put(CAT_ID, aCurrentCat_ID.toString());
        // values.put(AREA_NAME,aCurrentCity.getAreaName());

        long row_id = db.insert(TABLE_CURRENT_CITY, null, values);
        db.close();
        Log.d("Database.class", "Parent class object values are added to DB");

        System.out.println("CurrentCity :" + values);

        return row_id;
    }


    public CurrentCity getCityName() {
        //changed string to long
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_CURRENT_CITY ;
        /*+ " WHERE "
                + " empid " + " = " + Id;*/

        Cursor c = db.rawQuery(selectQuery, null);
        CurrentCity r = null;
        if (c != null)
        {
            //c.moveToFirst();

            if(c.moveToFirst())
            {
                r = new CurrentCity();
                //   r.setEmpID(c.getInt(c.getColumnIndex(EMP_ID)));
                r.setCityName((c.getString(c.getColumnIndex(CITY_NAME))));
                r.setCity_ID((c.getString(c.getColumnIndex(CITY_ID))));
                r.setCatid((c.getString(c.getColumnIndex(CAT_ID))));

            }
        }

        db.close();
        return r;
    }

    // Deleting single contact
    public void deleteCity() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CURRENT_CITY);
        db.close();
    }



    public void  update_Confirmation_StatusOrder_new(String chatId,String status) {

        Order objOrder = new Order();

        //String selectQuery = "Update Order_ set orderState_Confirmation = 1 where chat_id ="+chatId;
        String selectQuery = "Update Order_ set orderState_Confirmation ="+status+" where chat_id ="+chatId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                objOrder.setOrderStateConfirmation(c.getInt(c.getColumnIndex(ORD_STATE_CONFIRMATION)));
                objOrder.setBillNo(c.getString(c.getColumnIndex(ORD_CHAT_ID)));
                //    objOrder.setConfirmRejectionStatus(c.getString(c.getColumnIndex(CONFIRMREJECTION_STATUS)));

            } while (c.moveToNext());
        }
        db.close();

    }


    public Order getOderConfirmationStatus(String chatId)
    {

        Order objOrder = new Order();

        //  String selectQuery = "SELECT "+ ORD_STATE_CONFIRMATION +" FROM " + TABLE_ORDER + " WHERE "+ORD_BILL_NO+" = "+BillNo;

        String selectQuery = "SELECT orderState_Confirmation FROM Order_ WHERE chat_id ="+chatId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                objOrder.setOrderStateConfirmation(c.getInt(c.getColumnIndex(ORD_STATE_CONFIRMATION)));
              //  objOrder.setChatID(c.getInt(c.getColumnIndex(ORD_CHAT_ID)));

            } while (c.moveToNext());
        }
        db.close();

        return objOrder;
    }

    // Updating single contact
    public int update_StatusOrder(Order orderStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        //  ORD_BILL_NO ORD_CONFIRM_STATE

        ContentValues values = new ContentValues();
        values.put(ORD_BILL_NO, orderStatus.getBillNo());
        values.put(ORD_CONFIRM_STATE, 1);

        // updating row
        return db.update(TABLE_ORDER, values, ORD_CONFIRM_STATE + " = ?",
                new String[] { String.valueOf(orderStatus.getOrdConfirmState()) });
    }



    public long createNotifications(Notifications aNotifications) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOTIFICATION_DATE, aNotifications.getNotificationDate() );
        values.put(NOTIFICATION_TIME, aNotifications.getNotificationTime());
        values.put(NOTIFICATION_MESG, aNotifications.getNotificationText());
        values.put(NOTIFICATION_VENDOR_NAME,aNotifications.getNotificationVendorName());
        values.put(NOTIFICATION_VENDOR_CHAT_TYPE,aNotifications.getNotificationChatTpe());
        values.put(NOTIFICATION_VENDOR_ID,aNotifications.getNotificationVendorId());
        values.put(NOTIFICATION_VENDOR_TYPE,aNotifications.getNotificationVendorType());
        values.put(NOTIFICATION_CITY,aNotifications.getNotificationCity());

        long row_id = db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();

        return row_id;
    }

    public ArrayList<Notifications> getAllNotifications() {

        ArrayList<Notifications> notifications = new ArrayList<Notifications>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTIFICATIONS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Notifications n = new Notifications();
                n.setNotificationDate(c.getString(c.getColumnIndex(NOTIFICATION_DATE)));
                n.setNotificationTime(c.getString(c.getColumnIndex(NOTIFICATION_TIME)));
                n.setNotificationText(c.getString(c.getColumnIndex(NOTIFICATION_MESG)));
                n.setNotificationVendorName(c.getString(c.getColumnIndex(NOTIFICATION_VENDOR_NAME)));
                n.setNotificationVendorId(c.getString(c.getColumnIndex(NOTIFICATION_VENDOR_ID)));
                n.setNotificationChatTpe(c.getString(c.getColumnIndex(NOTIFICATION_VENDOR_CHAT_TYPE)));
                n.setNotificationVendorType(c.getString(c.getColumnIndex(NOTIFICATION_VENDOR_TYPE)));
                n.setNotificationCity(c.getString(c.getColumnIndex(NOTIFICATION_CITY)));

                // adding to Parent list
                notifications.add(n);

            } while (c.moveToNext());
        }
        db.close();

        return notifications;
    }

}