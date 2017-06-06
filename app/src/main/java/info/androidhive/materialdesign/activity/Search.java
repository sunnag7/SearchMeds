package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.CardAdapter_VenderList;
import info.androidhive.materialdesign.database.Database;
import info.androidhive.materialdesign.model.Areas;
import info.androidhive.materialdesign.model.Constatnts;
import info.androidhive.materialdesign.model.CurrentCity;
import info.androidhive.materialdesign.model.Vendor;

public class Search extends Fragment{
    AutoCompleteTextView mAreaSpinner,acmpTxtV_searchByPostalCode;
    SharedPreferences sharedpreferences;
    String getSelectionCity ="";
    ArrayList<Areas> mArrArray ;
    ArrayList<String> mArray_AreaName,mArray_AreaId;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    CardAdapter_VenderList mAdapter;
    RelativeLayout unAvail,unAvail1;
    ImageView pImgVSearchArea,imageV_searchVendorByPinCode;
    ArrayList<Vendor> mVenArray;
    ProgressDialog ringProgressDialog1;

    AutoCompleteTextView autoCompleteTxtV_currentArea;
    String Temp_areaID;
    String Selected_areaName;
    Database db;
    CurrentCity curCityName;
    int resultMatch_status;
    String dbCityName;
    String str_gpsArea;
    String pinCode;
    String currentDate,currentTime;

    //Call Customer Care
    ImageView Call_CustomerCare;
    String Phone_no = "";

    TextView CustomerCare_Chat;

    public Search() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        hideSoftKeyboard();

        pImgVSearchArea = (ImageView) rootView.findViewById(R.id.imageV_searchVendorByArea);
        mAreaSpinner = (AutoCompleteTextView) rootView.findViewById(R.id.editText);

        // current area
        autoCompleteTxtV_currentArea = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTxtV_currentArea);
        // imageV_currentArea = (ImageView) rootView.findViewById(R.id.imageV_currentArea);

        acmpTxtV_searchByPostalCode = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTxtV_seachBy_pinCode);
        imageV_searchVendorByPinCode = (ImageView) rootView.findViewById(R.id.imageV_searchVendorByPinCode);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvSearchRecycle);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        sharedpreferences = getActivity().getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);

        getSelectionCity = sharedpreferences.getString(Constatnts.SL_CITY_ID, "CITY_ID_NEW");
        unAvail = (RelativeLayout) rootView.findViewById(R.id.unavail);  // Data Unavailable
        unAvail1 = (RelativeLayout) rootView.findViewById(R.id.unavail1);  // Please Select City

        /** Customer Care Call
         * */
        Call_CustomerCare = (ImageView) rootView.findViewById(R.id.ImgV_Call);

        Call_CustomerCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone_no = "07842024202";
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Phone_no));
                startActivity(callIntent);
            }
        });

        /** Customer Care Chat
         * */
        CustomerCare_Chat = (TextView) rootView.findViewById(R.id.TxtV_Chat);

        CustomerCare_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constatnts.VENDOR_ID = "1786";
                Constatnts.VENDOR_NAME = "Customer Chat Support";
                Constatnts.VENDOR_ADDRESS = "Hyderabad";

                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = this.getArguments();

        //String cityID = bundle.getString("CITY_ID", "");
     /*   String catID = bundle.getString("CAT_ID", "");*/

        db = new Database(getContext());
        CurrentCity currentCityName =  db.getCityName();

        if(currentCityName != null)
        {

            if(currentCityName.getCity_ID().toString().equals("") && currentCityName.getCatid().toString().equals(""))
            {
                Toast.makeText(getContext(),"No City in DB !!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                unAvail1.setVisibility(View.INVISIBLE);
                Constatnts.SL_CAT_ID = currentCityName.getCatid().toString();
               // getArea(currentCityName.getCity_ID().toString());
            }

        }
        else
        {
            String cityID = Constatnts.CITYID;

            if(cityID != "")
            {
                unAvail1.setVisibility(View.INVISIBLE);
               // getArea(cityID);
            }

        }

        mAreaSpinner.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        if(Constatnts.GPS_Area != null){

            mAreaSpinner.setText(Constatnts.GPS_Area.toUpperCase());
        }

        //mVenArray.clear();

        mAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ringProgressDialog1 = ProgressDialog.show(getActivity(), "Please wait ...", "Loading ...", true);

                Integer Area_ID = getAreaIDFromArray(mAreaSpinner.getText().toString());

                if (Area_ID != 0)
                {
                    mVenArray.clear();
                    getVendorSearch(Constatnts.SL_CAT_ID, Area_ID);
                }
                else {
                    Toast.makeText(getActivity(), "Areas could not be loaded..Please try again", Toast.LENGTH_SHORT).show();
                }

                System.out.println("AREA_NAME -" + mArrArray.get(position).getArea_name());
                System.out.println("AreaID" + mArrArray.get(position).getAreaID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAreaSpinner.showDropDown();

            }
        });
        mAreaSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaSpinner.showDropDown();
            }
        });


        if(Constatnts.GPS_Area != null){
            // current area
            autoCompleteTxtV_currentArea.setText(Constatnts.GPS_Area.toUpperCase());
        }
        else {
            autoCompleteTxtV_currentArea.setText("Current Area Not available");
        }


        pImgVSearchArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard();

                Integer Area_ID = getAreaIDFromArray(mAreaSpinner.getText().toString());
               // mVenArray.clear();
                //Constatnts.Vendor_Count = "0";
                if(Area_ID != 0)
                {
                    getVendorSearch(Constatnts.SL_CAT_ID, Area_ID);
                }
                else if(Area_ID == 0)
                {

                   Toast.makeText(getActivity(),"No vendors in this area please select different area",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    mAreaSpinner.setError("Select area to search Vendor");
                }


               /* ringProgressDialog1 = ProgressDialog.show(getActivity(),
                        "Please wait...", "Loading...", true);
                // no of vendors found in area
                postCustomerSearchLogs(getContext());*/


            }
        });

        acmpTxtV_searchByPostalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // search using pincode
        imageV_searchVendorByPinCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                if(Constatnts.SL_CAT_ID != "")
                {

                    // pinCode = Integer.parseInt(acmpTxtV_searchByPostalCode.getText().toString());
                    pinCode = acmpTxtV_searchByPostalCode.getText().toString();
                    if (acmpTxtV_searchByPostalCode.getText().toString().length() != 0 &&
                            acmpTxtV_searchByPostalCode.getText().toString().length() < 6)
                    {

                        acmpTxtV_searchByPostalCode.setError("Invalid! Pincode should consist of 6 characters");
                    }

                    else if (acmpTxtV_searchByPostalCode.getText().toString().length() == 0)
                    {
                        acmpTxtV_searchByPostalCode.setError("Please enter valid Pincode");
                    }

                    else
                    {
                        if (!pinCode.equals(""))
                        {
                            mVenArray = new ArrayList<Vendor>();
                            getVendorSearchByPinCode(Constatnts.SL_CAT_ID, pinCode);
                        }
                    }
                }
                else
                {
                    unAvail.setVisibility(View.INVISIBLE);
                    unAvail1.setVisibility(View.VISIBLE);
                }

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    //hide keyboard
    public void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    public void getArea(String cID)
    {
        mArrArray = new ArrayList<Areas>();
        mArray_AreaName = new ArrayList<String>();
        mArray_AreaId = new ArrayList<String>();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jreq = new JsonArrayRequest(Constatnts.AreaUrl + cID,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Areas aAreas = new Areas();
                                JSONObject c = response.getJSONObject(i);

                                aAreas.setAreaID(Integer.parseInt(c.getString("AREA_ID")));
                                aAreas.setArea_name(c.getString("AREA_NAME"));

                                mArrArray.add(aAreas);

                                mArray_AreaName.add(c.getString("AREA_NAME"));
                                mArray_AreaId.add(c.getString("AREA_ID"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayAdapter<String> dataAdapterCity = new ArrayAdapter<String>
                                (getActivity(), (android.R.layout.simple_list_item_1), mArray_AreaName);
                        mAreaSpinner.setAdapter(dataAdapterCity);

                        System.out.println("Area Name" + mArray_AreaName.toString());

                        CompareAreas(mArray_AreaName, mArray_AreaId);

                        // loadAreas();
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NoConnectionError){
                    Toast.makeText(getContext(), "No Internet Connection. Please Try again !! ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Error !!"+error.toString(), Toast.LENGTH_LONG).show();
                    System.out.println(error.toString());
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
                Toast.makeText(getContext(), "TimeOut Error !!", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jreq);
    }


    public void getVendorSearch(String vID, final int areaID) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jreq = new JsonArrayRequest(Constatnts.searchUrl + "VENDOR_TYPE_ID=" + vID + ",AREA_ID=" + areaID,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mVenArray = new ArrayList<Vendor>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Vendor aVendor = new Vendor();
                                JSONObject c = response.getJSONObject(i);
                                aVendor.setAREANAME(c.getString("AREA_NAME"));
                                aVendor.setCITY(c.getString("CITY"));
                                aVendor.setSTATE(c.getString("STATE"));
                                aVendor.setVENDORADDRESS(c.getString("VENDOR_ADDRESS"));
                                aVendor.setVENDORCONTACTNAME(c.getString("VENDOR_CONTACT_NAME"));
                                aVendor.setVENDOREMAIL(c.getString("VENDOR_EMAIL"));
                                aVendor.setVENDORID(c.getString("VENDOR_ID"));
                                aVendor.setVENDORLANDLINE(c.getString("VENDOR_LANDLINE"));
                                aVendor.setVENDORMOBILE(c.getString("VENDOR_MOBILE"));
                                aVendor.setVENDORNAME(c.getString("VENDOR_NAME"));
                                //aVendor.setVEN_TYPE(Integer.parseInt(Constatnts.VendorTypeCat));
                                aVendor.setVEN_TYPE(Integer.parseInt(Constatnts.SL_CAT_ID));
                                aVendor.setAREAID(areaID);
                                aVendor.setFROM_TIME(c.getString("FROM_TIME"));
                                aVendor.setTO_TIME(c.getString("TO_TIME"));
                                aVendor.setOFF_DAY(c.getString("OFF_DAY"));
                             //   aVendor.setPINCODE(c.getInt("PINCODE"));
                                mVenArray.add(aVendor);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(),
                                        "Something went wrong while fetching vendor list!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        /*ringProgressDialog1 = ProgressDialog.show(getActivity(),
                                "Please wait...", "Loading...", true);*/
                        // no of vendors found in area


                        if (mVenArray.size()>=0)
                        {
                            mAdapter = new CardAdapter_VenderList(getActivity(),mVenArray);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mAdapter);
                            unAvail.setVisibility(View.INVISIBLE);

                            resultMatch_status = 1;
                            Constatnts.Vendor_Count = mVenArray.size() + "";

                        }
                        if(mVenArray.size()==0) {
                            unAvail.setVisibility(View.VISIBLE);
                            resultMatch_status = 0;
                            showAlertDialog();
                        }

                        postCustomerSearchLogs(getContext());

                        //ringProgressDialog1.dismiss();

                    }
                },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NoConnectionError){
                    Toast.makeText(getContext(), "No Internet Connection. Please try again !!", Toast.LENGTH_LONG).show();
                }
                else {

                    Toast.makeText(getActivity(), "Error!" + error.toString() , Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
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
                Toast.makeText(getContext(), "TimeOut Error !!", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(jreq);

    }


    public int getAreaIDFromArray(String pStrArea_Name){
        String pStrArea_ID ="0";
        if(mArrArray != null)
        {
            for(int i=0;i<mArrArray.size();i++)
            {
                if(mArrArray.get(i).getArea_name().toString().equals(pStrArea_Name) ){
                    pStrArea_ID = mArrArray.get(i).getAreaID()+"";
                    return Integer.parseInt(pStrArea_ID);
                }
            }

        }
        else {
            Toast.makeText(getContext(),"Please Select City To Search Vendors !!",Toast.LENGTH_LONG).show();
        }

        return Integer.parseInt(pStrArea_ID);
    }


    private void CompareAreas(ArrayList mArray_AreaName, ArrayList mArray_AreaId) {

        for (int i=0;i<mArray_AreaName.size();i++){

            String areaName =  mArray_AreaName.get(i).toString();
            String areaID =  mArray_AreaId.get(i).toString();


            if (Constatnts.GPS_Area == null)
            {
                Constatnts.GPS_Area="";

            }
            else{

                Constatnts.GPS_Area = Constatnts.GPS_Area.toUpperCase();
                System.out.println("FROM DROPDOWN " +areaName);

                if(areaName.equals(Constatnts.GPS_Area.toUpperCase())){
                    Selected_areaName = areaName;
                    mAreaSpinner.setText(areaName);
                    Temp_areaID = areaID;
                    System.out.println(" TEMP in IF LOOP " +Temp_areaID);

                    mVenArray.clear();
                    getVendorSearch(Constatnts.SL_CAT_ID, Integer.parseInt(Temp_areaID));
                    break;

                }
            }
        }


    }


    public void postCustomerSearchLogs(Context context){

        // finding current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
        Calendar c = Calendar.getInstance();

        currentDate = dateFormat.format(c.getTime());
        currentTime = timeFormat.format(c.getTime());

        System.out.println("currentDate" + currentDate); //currentDate20160303_102422
        System.out.println("currentTime" + currentTime);

// dbCityName and dbArea
        db = new Database(getActivity());
        if(db != null)
        {
            curCityName =  db.getCityName();

            if(curCityName != null)
            {

                dbCityName = curCityName.getCityName();
                System.out.println("dbCity:"+dbCityName);

            }
            else
            {
                dbCityName = "City not found";
            }
        }

        if(Constatnts.GPS_Area != null)
        {
            str_gpsArea = Constatnts.GPS_Area;

        }
        else
        {
            str_gpsArea = "GPS area not available";
        }


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, Constatnts.postCustomerSearchLogs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //"SUCCESS:1,"

                sharedpreferences = getActivity().getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);
                System.out.println(response);
                String line = response.replaceAll("\"", "");
                String[] splitted = line.split(",");

                if( splitted[0].replace("SUCCESS:", "").equals("1"))
                {
                    for(String str: splitted)
                    {
                        System.out.println(str);
                    }
                    boolean logStatus = true;

                    try {
                        if (logStatus) {
                            //Toast.makeText(getActivity(), "Vendors found in your area ",Toast.LENGTH_LONG).show();

                        }
                    } catch (Exception e)
                    {
                      Toast.makeText(getActivity(),"No Vendors found ",Toast.LENGTH_SHORT).show();
                    }
                 //   ringProgressDialog1.dismiss();
                }
                else
                {
                  //  ringProgressDialog1.dismiss();
                    Toast.makeText(getActivity(),"No Response From Server",Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof NoConnectionError)
                {
                    Toast.makeText(getContext(), "No Internet Connection. Please try again !!", Toast.LENGTH_LONG).show();
                }
                else {
                    System.out.println("Error response" + error.toString());
                    Toast.makeText(getActivity(), "No vendors found in Your Area ", Toast.LENGTH_SHORT).show();
                }
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();
                params.put("USER_ID",sharedpreferences.getString(Constatnts.UserID,null));
                params.put("CUSTOMER_ID",sharedpreferences.getString(Constatnts.CUSTOMER_ID,null));
                params.put("GPS_LOCATION",str_gpsArea); //Constatnts.GPS_Area
                params.put("DB_LOCATION",mAreaSpinner.getText().toString());
                params.put("RESULT_MATCH_STATUS",resultMatch_status + "");
                params.put("NO_OF_VENDORS", Constatnts.Vendor_Count);
                params.put("VENDOR_TYPE",Constatnts.SL_CAT_ID);
                params.put("SEARCH_DATE",currentDate);
                params.put("SEARCH_TIME",currentTime);

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
                Toast.makeText(getContext(), "Timeout Error !!", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(sr);
    }

    //search using postal address
    public void getVendorSearchByPinCode(String vID, final String pinCode) {

        //VENDOR_TYPE_ID=3,PINCODE=403297
        System.out.println(Constatnts.getVendorsByPinCode + "VENDOR_TYPE_ID=" + vID + ",PINCODE=" + pinCode);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jreq = new JsonArrayRequest(Constatnts.getVendorsByPinCode + "VENDOR_TYPE_ID=" + vID +",PINCODE=" + pinCode,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mVenArray = new ArrayList<Vendor>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Vendor aVendor = new Vendor();
                                JSONObject c = response.getJSONObject(i);
                                aVendor.setAREANAME(c.getString("AREA_NAME"));
                                aVendor.setCITY(c.getString("CITY"));
                                aVendor.setSTATE(c.getString("STATE"));
                                aVendor.setVENDORADDRESS(c.getString("VENDOR_ADDRESS"));
                                aVendor.setVENDORCONTACTNAME(c.getString("VENDOR_CONTACT_NAME"));
                                aVendor.setVENDOREMAIL(c.getString("VENDOR_EMAIL"));
                                aVendor.setVENDORID(c.getString("VENDOR_ID"));
                                aVendor.setVENDORLANDLINE(c.getString("VENDOR_LANDLINE"));
                                aVendor.setVENDORMOBILE(c.getString("VENDOR_MOBILE"));
                                aVendor.setVENDORNAME(c.getString("VENDOR_NAME"));
                              //  aVendor.setVEN_TYPE(Integer.parseInt(Constatnts.VendorTypeCat));
                                aVendor.setPINCODE(pinCode);
                                aVendor.setFROM_TIME(c.getString("FROM_TIME"));
                                aVendor.setTO_TIME(c.getString("TO_TIME"));
                                aVendor.setOFF_DAY(c.getString("OFF_DAY"));
                                mVenArray.add(aVendor);
/*
                                if(success = 1){
                                    mVenArray.add(aVendor);
                                }
                               else
                                {
                                  Toast.makeText(getActivity(),"No vendors found in the specified pincode..",Toast.LENGTH_SHORT).show();
                                }
*/
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(),
                                        "Something went wrong while fetching vendor list!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (mVenArray.size()>=0)
                        {
                            mAdapter = new CardAdapter_VenderList (getActivity(),mVenArray);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mAdapter);
                            unAvail.setVisibility(View.INVISIBLE);

                        }
                        if(mVenArray.size() == 0) {
                            unAvail.setVisibility(View.VISIBLE);

                        }

                        //ringProgressDialog1.dismiss();

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(getContext(), "No Internet Connection. Please try again !!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "Error !" + error.toString() , Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
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
                Toast.makeText(getContext(), "Timeout Error !!", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jreq);

    }


    public void showAlertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialog);
        builder1.setMessage("Vendors not available in your selected area..Try using Pincode");
       // builder1.setTitle("Vendors not available for Pincode");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder1.show();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}