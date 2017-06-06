package info.androidhive.materialdesign.model;

/**
 * Created by Sanny on 05/12/2015.
 */

public class Constatnts {
    public static String GOOGLE_PROJECT_ID = "311597513504";

    //http://54.169.227.89:94/DataService.svc/GetCities
    public static String baseUrl = "http://54.169.227.89:94/DataService.svc/";
    public static String getBase_URL = "http://54.169.227.89:94/";

    public static String prasad = "prasad";


    public static String regUrl =  baseUrl +"RegisterCustomer";
    public static String CityUrl =  baseUrl +"GetCities";
    public static String AreaUrl =  baseUrl +"GetAreas/";
    public static String searchUrl =  baseUrl +"GetVendorsByType/";
    // http://192.168.1.10:90/DataService.svc/GetAreas/2
    public static String LOGINUrl  = baseUrl +"ValidateUser";
    public static String GCMUrl  = baseUrl +"UpdateGCMForUser";
    public static String postChatUrl  = baseUrl +"POSTChatMessage";
    public static String postOrderUrl  = baseUrl +"POSTOrderConfirmation";
    public static String getPendingCustomerOrders  = baseUrl +"getPendingCustomerOrders/";
    public static String GetCustomerByUSERID  = baseUrl +"GetCustomerByUSERID/";

    public static String postCustomerSearchLogs = baseUrl + "POSTCustomerSearchLogs";
    public static String getVendorsByPinCode = baseUrl+ "GetVendorsByPINCODE/";
    public static String GET_VENDOR_IMAGE_NAMES = baseUrl + "PostFetchVendorImage";

    public static String postCustomerFavourite_vendors = baseUrl + "POSTCustomerFavourites";

    public static String Sync_All_Chats = baseUrl + "POSTSyncChats";


    public static final String MyPREFERENCES = "UmbrellaPrefs" ;
    public static final String MyPREFERENCES_drawer = "UmbrellaPrefs_drawer" ;
    public static final String UserID = "USER_ID";
    public static final String USERTYPE = "USER_TYPE";
    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    public static final String GCMId = "gcmID", REG_ID = "regId";
    public static final String SL_CITY_ID = "CITY_SELECT";
    public static final String SL_CITY_NAME = "CITY";
    public static final String ADDRESS = "ADDRESS";

    public static final String USER_NAME = "USER_NAME";
    public static final String FULL_NAME = "FULL_NAME";
    public static final String USER_CITY = "USER_CITY";
    public static final String USER_MOBILENO = "USER_MOBILENO";

    public static final String USER_LINK_ID = "USER_LINK_ID";
    public static final String VENDOR_TYPE_ID = "VENDOR_TYPE_ID";
    public static final String NAME = "NAME";
    public static final String CITY_NAME = "CITY_NAME";
    public static final String EMAIL_ID = "EMAIL_ID";
    public static String yourLocked = "FALSE";
    public static String FromMainActivity = "FALSE";
    public static String FromChatActivity = "FALSE";

    public static final String CHAT_ID = "CHAT_ID";


    //Upload Image Response
    public static final String ORDER_NO = "ORDER_NO";
    public static final String ORDER_TYPE_ID = "ORDER_TYPE_ID";
    public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String ORDER_CUSTOMER_ID = "ORDER_CUSTOMER_ID";


    public static String SL_CAT_ID = "";
    public static String NOTIFYMSG = "";
    public static String GCM_ID = "";

    public static String CITYID = "";

    //Variables used in ChatListFragment
    public static String VENDOR_ID = "";
    public static String VENDOR_NAME = "";
    public static String VENDOR_ADDRESS = "";

    //Vendor Image Names
    public static String VENDOR_Image_1 = "";
    public static String VENDOR_Image_2 = "";
    public static String VENDOR_Image_3 = "";




// order Confirmation
    public static String OrderNo,VendorID,GCMID;
    public static String VendorTypeCat;

    public static String GPS_Area;
    public static String Vendor_Count;

    public static String OrderNo_FromVender;

    public static String Temp_VENDOR_ID = "";

    public static String DBCITY = "";

    public static boolean CheckNotif = false;

    public static boolean RECEIVED_NEW_MSG = false;


    //Terms And Conditions
    public static String getTermsCondPolicy_URL= "http://searchmeds.iflotech.in/";

    //Terms And Conditions
    public static String privacyPolicy_URL = getTermsCondPolicy_URL + "policy/customer/cust_privacy_policy.html";
    public static String refundPolicy_URL = getTermsCondPolicy_URL + "policy/customer/cust_refund_policy.html";
    public static String shpDelPolicy_URL = getTermsCondPolicy_URL + "policy/customer/cust_ship_and_delivery.html";
    public static String termsCondPolicy_URL = getTermsCondPolicy_URL + "policy/customer/cust_terms_and_conditions.html";


}

