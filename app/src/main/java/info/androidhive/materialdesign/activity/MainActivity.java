package info.androidhive.materialdesign.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.database.Database;
import info.androidhive.materialdesign.model.AppLocationService;
import info.androidhive.materialdesign.model.Chat;
import info.androidhive.materialdesign.model.Constatnts;
import info.androidhive.materialdesign.model.CurrentCity;
import info.androidhive.materialdesign.model.SyncChats;

public class MainActivity extends AppCompatActivity
{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    String regId = "";
    SharedPreferences sharedpreferences;
    private CoordinatorLayout coordinatorLayout;

    ImageView ImgV_search_dialog;

    public static final String PROPERTY_REG_ID = "registration_id";

    int searchType = 3;
    Database db = null;

    String GPScity,address,cityID,Temp_cityID,Selected_cityName,city_name,city_ID, ID;

    AppLocationService appLocationService;
    Geocoder geocoder;
    List<Address> addresses;
    double latitude,longitude;
    ImageView imageV_confirmSelection;

    ArrayList<String> aCities,aCityIds;
    AutoCompleteTextView choosecity;
    ImageView ImgV_GetGPS;
    public RadioButton radioPharma, radioDoc, radioLab;
    TextView selected_city;
    String Selected_city_DB;

    String PROJECT_NUMBER="311597513504";
    AlertDialog levelDialog;
    String Sync_Time="7";
    String customer_id = "",vendor_id="0";
    String User_id="";
    String Check;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedpreferences = getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);

        customer_id = sharedpreferences.getString(Constatnts.CUSTOMER_ID, null);
        Log.d("MainActivity","prasad"+customer_id);

        User_id = sharedpreferences.getString(Constatnts.UserID, null);

        selected_city = (TextView) findViewById(R.id.TxtV_selected_city);

        hideSoftKeyboard();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        //get Last Selected City From Database
        db = new Database(MainActivity.this);
        CurrentCity currentCityName =  db.getCityName();

        if(currentCityName == null )
        {
            selected_city.setText("No City");
        }
        else
        {
            Selected_city_DB = currentCityName.getCityName().toString();
            selected_city.setText(currentCityName.getCityName().toString());
        }

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationview) ;

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();

        regId = sharedpreferences.getString(PROPERTY_REG_ID, "");

        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

              //  Toast.makeText(MainActivity.this,"Registration ID is: " + registrationId ,Toast.LENGTH_LONG).show();

                Toast.makeText(MainActivity.this,"Registered with searchMeds server",Toast.LENGTH_SHORT).show();

                if(!registrationId.isEmpty())
                {
                    postRegData(MainActivity.this, registrationId);
                    Log.d("MainActivity","Called PostRegData");
                }
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
                Toast.makeText(MainActivity.this,"ERROR IN GCM : " ,Toast.LENGTH_SHORT).show();
            }
        });

        try
        {
            // String data = getIntent().getStringExtra("json_msg");
            if(Constatnts.NOTIFYMSG != "")
            {
                if( Constatnts.CheckNotif == true)
                {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new NotificationsFragment()).commit();
                    Constatnts.CheckNotif = false;
                }
            }
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
        }

        /**
         * Setup click events on the Navigation View Items.
         */

             mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
             {

                 /**
                  * Navigation Drawer Item Listeners
                  */
             @Override
             public boolean onNavigationItemSelected(MenuItem menuItem)
             {
                mDrawerLayout.closeDrawers();

                 if (menuItem.getItemId() == R.id.search)
                 {
                     FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                     xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
                 }

                 if (menuItem.getItemId() == R.id.myProfile)
                 {
                     FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.containerView,new MyAccountFragment()).commit();

                 }

                 if(menuItem.getItemId() == R.id.myOrders)
                 {
                     FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.containerView,new MyOrderFragment()).commit();
                 }

                 if(menuItem.getItemId() == R.id.notifications)
                 {
                     FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.containerView,new NotificationsFragment()).commit();

                 }

                 if(menuItem.getItemId() == R.id.logout)
                 {
                     logOutDialog();
                 }

                 return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

                android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_main);
                ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name, R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);

        ImgV_search_dialog = (ImageView) toolbar.findViewById(R.id.ImgV_searchdialog);

        ImgV_search_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSelectionDialog();

            }
        });
                mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        //Call Sync Chats to sync all chats with vendors

        Check = Constatnts.yourLocked;
        System.out.println("Check MainActivity Check: " + Check);
        System.out.println("Check MainActivity Constants yourLocked: " + Constatnts.yourLocked);
        if(Check.toString().equals("TRUE"))
        {
            Sync_Dialog();
            Constatnts.yourLocked = "FALSE";
            Check = "FALSE";
        }
    }

    public void Sync_Dialog()
    {
        final SyncChats syncChats = new SyncChats();
        final CharSequence[] Sync_Options = {" One Week "," Two Weeks "," One Month "};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sync Chat");
        builder.setSingleChoiceItems(Sync_Options, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        Sync_Time = "7";
                        break;
                    case 1:
                        Sync_Time = "14";
                        break;
                    case 2:
                        Sync_Time = "30";
                        break;
                }
            }
        });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Constatnts.FromMainActivity = "TRUE";
                        // clear chat table and load new chat
                       // db.clearChatData();
                       //
                       //
                        //db.deleteDuplicatesChatData();
                        syncChats.Sync_all_Chats(MainActivity.this,Sync_Time,customer_id,vendor_id);
                        levelDialog.dismiss();

                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        levelDialog.dismiss();
                    }
                });

        levelDialog = builder.create();
        levelDialog.show();
    }



    //hide keyboard
    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void getCities()
    {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(MainActivity.this,
                "Please wait ...", "Loading ...", true);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonArrayRequest jreq = new JsonArrayRequest(Constatnts.CityUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        aCities = new ArrayList<String>();
                        aCityIds = new ArrayList<String>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject c = response.getJSONObject(i);

                                aCities.add(c.getString("CITY_NAME"));
                                cityID = c.getString("CITY_ID");
                                aCityIds.add(cityID);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(aCities.size() != 0)
                        {
                            //Data Adapter for Cities
                            ArrayAdapter<String> dataAdapterCity = new ArrayAdapter<String>
                                    (MainActivity.this, (android.R.layout.simple_list_item_1), aCities);
                            choosecity.setAdapter(dataAdapterCity);
                            hideSoftKeyboard();
                            ringProgressDialog.dismiss();
                        }
                        else
                        {
                            ringProgressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Cannot fetch cities Due To Network Issues:", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();
                if(error instanceof NoConnectionError)
                {
                    Toast.makeText(MainActivity.this, "No Network. . . Please Try Again !!:", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Cannot fetch cities Due To Network Issues:", Toast.LENGTH_SHORT).show();
                }

            }
        });

        jreq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        queue.add(jreq);
    }


    public void showSelectionDialog()
    {
        if(checkAndPass())
        {
            getCities();
        }

        final SharedPreferences.Editor editor = sharedpreferences.edit();

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Layout Inflator
        LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.category_selection, null );

       /* final PopupWindow popupWindow = new PopupWindow(layout, AbsListView.LayoutParams.FILL_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);*/


        appLocationService = new AppLocationService(MainActivity.this);

        choosecity = (AutoCompleteTextView) layout.findViewById(R.id.editTextCity);
        choosecity.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        choosecity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideSoftKeyboard();
                choosecity.showDropDown();
            }
        });

        final RadioGroup mRadioGroup = (RadioGroup) layout.findViewById(R.id.myRadioGroup);

        //Button to get GPS Location
        ImgV_GetGPS = (ImageView) layout.findViewById(R.id.ImgV_GetGPS);

        TextView tv_hello = (TextView) layout.findViewById(R.id.textView4);
        tv_hello.setTypeface(EasyFonts.caviarDreams( MainActivity.this));

        TextView tv_City = (TextView) layout.findViewById(R.id.textView2);
        tv_City.setTypeface(EasyFonts.caviarDreams(MainActivity.this));

        // FloatingActionButton mButtonExit;

        imageV_confirmSelection = (ImageView) layout.findViewById(R.id.imageV_confirmSelection);

        //OnClick method for GPS location
        ImgV_GetGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

                if (nwLocation != null)
                {
                    latitude = nwLocation.getLatitude();
                    longitude = nwLocation.getLongitude();
                    Toast.makeText(MainActivity.this, "Mobile Location (NW): \nLatitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_SHORT).show();

                    if(checkAndPass())
                    {
                        address(); //Find GPS
                    }

                }
                else
                {
                    showSettingsAlert("NETWORK");
                }
            }
        });


        radioPharma = (RadioButton)layout.findViewById(R.id.radioMini);
        radioPharma.setTypeface(EasyFonts.caviarDreamsBold( MainActivity.this));

        radioLab = (RadioButton) layout.findViewById(R.id.radioMUV);
        radioLab.setTypeface(EasyFonts.caviarDreamsBold(MainActivity.this));

        radioDoc = (RadioButton) layout.findViewById(R.id.radioSedan);
        radioDoc.setTypeface(EasyFonts.caviarDreamsBold(MainActivity.this));

        radioPharma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    searchType = 3;   //Pharmacy
                    Constatnts.SL_CAT_ID = "" + searchType;
                    Constatnts.VendorTypeCat = "" + searchType;
                }
            }
        });

        radioDoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    searchType = 1;   //Doctor
                    Constatnts.SL_CAT_ID = "" + searchType;
                    Constatnts.VendorTypeCat = "" + searchType;
                }
            }
        });

        radioLab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    searchType = 2;  //Lab
                    Constatnts.SL_CAT_ID = "" + searchType;
                    Constatnts.VendorTypeCat = "" + searchType;
                }
            }
        });

        imageV_confirmSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!choosecity.getText().toString().equals(""))
                {

                    ID = getCityIDFromArray(choosecity.getText().toString());
                    System.out.println("ID: " + ID);

                    if(ID != "")
                    {
                        //set Selected city to toolbar
                        selected_city.setText(choosecity.getText().toString());

                        editor.putString("CITY_ID_NEW", ID);
                        editor.putString("CITY_NAME_NEW", choosecity.getText().toString());

                        Constatnts.SL_CAT_ID = ""+searchType;
                        Constatnts.VendorTypeCat = ""+searchType;
                        Constatnts.CITYID = ID;

                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView , new TabFragment()).commit();


                        if (choosecity.getText().toString().length() > 0) {

                            db.deleteCity();
                            // oCurrentCity.setCityName(choosecity.getText().toString());
                            db.createCurrentCity(choosecity.getText().toString() , ID, Constatnts.SL_CAT_ID);
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"City Does Not Match Database !!",Toast.LENGTH_SHORT).show();
                        showSelectionDialog();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this ,"Please Choose City !!",Toast.LENGTH_SHORT).show();
                    showSelectionDialog();

                }
                dialog.dismiss();
            }
        });


        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setContentView(layout);
        dialog.setCancelable(true);
        dialog.show();

        //Check and Load Exiting City from DB OR GPS

        //curCityName = new ArrayList<CurrentCity> ();

        db = new Database(MainActivity.this);
        CurrentCity curCityName =  db.getCityName();

    }

    //Finding address
    public void address() {

        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try
        {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        GPScity = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); //

        Toast.makeText(MainActivity.this, "GPS Area "+GPScity+" State "+state, Toast.LENGTH_SHORT).show();

        try
        {
            CompareCities(aCities, aCityIds);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

       // CompareCities(aCities, aCityIds);
        System.out.println("GPS CITY: " + GPScity);

        // Constatnts.GPS_Area = address;
        Constatnts.GPS_Area = GPScity;
        System.out.println("Address: " + address);

    }

    public void showSettingsAlert(String provider) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialog);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    public String getCityIDFromArray(String pStrCity_Name){
        String pStrCity_ID ="";
        for(int i=0;i<aCities.size();i++)
        {
            if(aCities.get(i).toString().equals(pStrCity_Name) )
            {
                pStrCity_ID = aCityIds.get(i);
                return pStrCity_ID;
            }
        }

        return pStrCity_ID;
    }


    private void CompareCities(ArrayList aCities, ArrayList aCityIds)
    {
        if(aCities.size() == 0)
        {
            if(Selected_city_DB.toString().equals(GPScity.toUpperCase()))
            {
                Selected_cityName = city_name;
                // choosecity.setText(city_name);
                choosecity.setText(GPScity);
                Temp_cityID = city_ID;

            }
            else
            {
                Temp_cityID = city_ID;
                // Toast.makeText(getActivity(), " GPS city Name Does Not Match Database "/*+error.toString()*/, Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            for (int i=0;i<aCities.size();i++)
            {

                String city_name = (String) aCities.get(i);
                String city_ID = (String) aCityIds.get(i);

                if (GPScity == null)
                {
                    GPScity="";
                    // Toast.makeText(getActivity(), " GPS city Name Does Not Match Database "/*+error.toString()*/, Toast.LENGTH_LONG).show();
                    break;
                }
                GPScity = GPScity.toUpperCase();
                System.out.println("CAPITAL " +GPScity);
                System.out.println("FROM DROPDOWN " +city_name);

                if(city_name.equals(GPScity.toUpperCase())){
                    Selected_cityName = city_name;
                    // choosecity.setText(city_name);
                    choosecity.setText(GPScity);
                    Temp_cityID = city_ID;

                    break;
                }
                else
                {
                    Temp_cityID = city_ID;
                    // Toast.makeText(getActivity(), " GPS city Name Does Not Match Database "/*+error.toString()*/, Toast.LENGTH_LONG).show();

                }
            }
        }
    }


    public void postRegData(Context context, final String regID) {

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, Constatnts.GCMUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("MainActivity PostRegdata: " + response);
                        try
                        {
                            if (response!=null)
                            {
                                if (response.contains("success"))
                                {
                                    Toast.makeText(MainActivity.this, "Server updated "+response, Toast.LENGTH_SHORT).show();
                                }
                                //showCustomAlert("Successfully Logged in");
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Server Error " +response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(MainActivity.this, "Server Error " +response, Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if( error instanceof NoConnectionError){
                    Toast.makeText(MainActivity.this, "No Internet connection. Please Try Again !!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Server Error ", Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("UID", User_id);
                params.put("GCM_ID",regID);

                Log.d("MainActivity","PostRegData Params "+params.toString());

                return params;
            }
        };

        sr.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Toast.makeText(MainActivity.this, "TimeOut Error !!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);
    }



    public void logOutDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialog);
        builder.setTitle("Logout");
        builder.setMessage("You will lose All data.");
        builder.setCancelable(true);
        builder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                final Database db = new Database(MainActivity.this);

                sharedpreferences.edit().clear().commit();
                db.clearChatData();
                db.clearFavData();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onBackPressed()
    {
        ExitAppSnackBar();
    }


    boolean isExit = false;
    public void ExitAppSnackBar()
    {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Press EXIT to close the app", Snackbar.LENGTH_LONG);
        snackbar.setAction("EXIT", new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                android.app.FragmentManager fm = getFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i)
                {
                    if (fm.getBackStackEntryAt(1).equals(""))
                        fm.popBackStack();
                }
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finish();
            }
        });
        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#ffffff"));
        isExit = true;
        snackbar.show();

    }


    boolean isConn = false;
    public boolean checkAndPass()
    {

        if (isInternetAvailable() || isNetworkConnected())
        {
            isConn = true;

            Thread timerThread = new Thread()
            {
                public void run()
                {
                    try
                    {
                        sleep(3000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            timerThread.start();
        }
        else
        {
            showAlertDialog() ;
            isConn = false;
        }

        return  isConn;
    }

    public static boolean isInternetAvailable()
    {
        try
        {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            if (ipAddr.equals(""))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private  boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        }
        else
            return true;
    }


    AlertDialog alert;
    public void showAlertDialog()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialog);
        builder1.setMessage("Internet not available");
        builder1.setTitle("Connect Now?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });

        builder1.setNeutralButton("Check Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                checkAndPass();
                dialog.cancel();
            }
        });
        alert = builder1.create();
        alert.show();
    }

}