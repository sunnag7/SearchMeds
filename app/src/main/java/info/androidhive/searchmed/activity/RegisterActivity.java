package info.androidhive.searchmed.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.androidhive.searchmed.R;
import info.androidhive.searchmed.model.Constatnts;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner mSpinTextCity;
    static TelephonyManager telephonyManager = null;
    String possibleEmail = "";

    Context context;
    // public static final String TaxiID = "txid";
    SharedPreferences sharedpreferences;
    EditText editTxt_FullName,editTxt_mobileNo,editTxt_address,editTxt_emailId,editTxt_password;
    Button btn_register;
    TextView txt_BackToLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        telephonyManager = (TelephonyManager)getSystemService
                (Context.TELEPHONY_SERVICE);
        findViews();
        // getCities();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(RegisterActivity.this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
            }
        }
        editTxt_emailId.setText("" + possibleEmail);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String email = editTxt_emailId.getText().toString();
                if (!isValidEmail(email)) {
                    editTxt_emailId.setError("Invalid Email");
                }

                final String pass = editTxt_password.getText().toString();
                if (!isValidPassword(pass)) {
                    editTxt_password.setError("Invalid Password");
                }

                final String user = editTxt_FullName.getText().toString();
                if (!isValidPassword(user)) {
                    editTxt_FullName.setError("Invalid UserName");
                }

                final String no = editTxt_mobileNo.getText().toString();
                if (!isValidPhone(no)) {
                    editTxt_mobileNo.setError("Invalid Phone Number");
                }

                if (isValidPhone(no) && isValidPassword(user)
                        && isValidPassword(pass) && isValidEmail(email)) {
                    Pattern ps = Pattern.compile("^[a-zA-Z0-9 ]+$");
                    Matcher ms = ps.matcher(editTxt_password.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false)
                    {
                        editTxt_password.setError("Special Characters Not Allowed ");
                    }
                    else if(bs == true)
                    {
                        postNewComment(RegisterActivity.this);
                    }



                }
            }
        });

        //back to login
        txt_BackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void findViews() {

//registration new
        editTxt_FullName = (EditText) findViewById(R.id.editTxt_FullName);
        editTxt_mobileNo = (EditText) findViewById(R.id.editTxt_mobileNo);
        editTxt_address = (EditText) findViewById(R.id.editTxt_address);
        editTxt_emailId = (EditText) findViewById(R.id.editTxt_emailId);
        editTxt_password = (EditText) findViewById(R.id.editTxt_password);

        btn_register = (Button) findViewById(R.id.btn_register);
        txt_BackToLogin = (TextView) findViewById(R.id.txt_BackToLogin);


    }

    public void postNewComment(Context context){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, Constatnts.regUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //SUCCESS:1,USER_ID:24,USER_TYPE_ID:3,CUSTOMER_ID=28
                sharedpreferences = getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);

                System.out.println(response);
                String line = response.replaceAll("\"", "");
                String[] splitted = line.split(",");
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if( splitted[0].replace("SUCCESS:", "").equals("1"))
                {
                    for(String str: splitted)
                    {
                        System.out.println(str);
                    }

                    boolean logStatus = true;

                    editor.putString(Constatnts.UserID, splitted[1].replace("USER_ID:", ""));
                    editor.putString(Constatnts.USERTYPE, splitted[2].replace("USER_TYPE_ID:", ""));
                    editor.putString(Constatnts.CUSTOMER_ID, splitted[3].replace("CUSTOMER_ID:", ""));
                    editor.putString(Constatnts.NAME, editTxt_FullName.getText().toString());

                    editor.commit();


                    try {
                        if (logStatus) {
                            Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"INVALID ",Toast.LENGTH_SHORT).show();
                    }
                }

                //If Already Registered
                else if( splitted[0].replace("SUCCESS:", "").equals("0") ){
                    Toast.makeText(getApplicationContext(),"Already Registered !! ",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());

                if(error instanceof NoConnectionError){
                    Toast.makeText(getApplicationContext(),"No Internet. Please Try Again !! ",Toast.LENGTH_SHORT).show();
                }

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();
                params.put("FullName",editTxt_FullName.getText().toString());
                params.put("mobilenumber",editTxt_mobileNo.getText().toString());
                params.put("emailId", editTxt_emailId.getText().toString());
                params.put("address",editTxt_address.getText().toString());
                params.put("password",editTxt_password.getText().toString());
                params.put("cityname","");
                params.put("state","State"/*mEditTextName.getText().toString()*/);
                params.put("pincode","");
                params.put("device_id", telephonyManager.getDeviceId().toString());
                params.put("country", "India");

                System.out.println("Customer Details Sent in Registration: "+params);


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
                Toast.makeText(getApplicationContext(),"Timeout Error !! ",Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(sr);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case  R.id.btn_register:
                //validate and submit

                final String email = editTxt_emailId.getText().toString();
                if (!isValidEmail(email)) {
                    editTxt_emailId.setError("Invalid Email");
                }

                final String pass = editTxt_password.getText().toString();
                if (!isValidPassword(pass)) {
                    editTxt_password.setError("Invalid Password");
                }

                final String user = editTxt_FullName.getText().toString();
                if (!isValidPassword(user)) {
                    editTxt_FullName.setError("Invalid UserName");
                }
              /*  final String city = mEditTextCity.getText().toString();
                if (!isValidCity(city)) {
                    mEditTextCity.setError("Invalid City name");
                }*/
                final String no = editTxt_mobileNo.getText().toString();
                if (!isValidPhone(no)) {
                    editTxt_mobileNo.setError("Invalid Phone Number");
                }
                if(Constatnts.REG_ID.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),
                            "Registration failed", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegisterActivity.this, RegisterActivity.class);
                    startActivity(i);
                    finish();
                }

                if (isValidPhone(no) /*&& isValidCity(city)*/ &&!isValidPassword(user)
                        && !isValidPassword(pass)&& isValidEmail(email) && !Constatnts.REG_ID.isEmpty() )
                {
                    postNewComment(this);
                }
                //finish();
                break;

            default:
                break;

        }
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 4) {
            return true;
        }
        return false;
    }

    private boolean isValidCity(String city) {
        if (city != null && city.length() > 3) {
            return true;
        }
        return false;
    }

    private boolean isValidPhone(String city) {
        if (city != null && city.length() > 3) {
            return true;
        }
        return false;
    }

    public void getCities() {

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jreq = new JsonArrayRequest(Constatnts.CityUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<String> aCities = new ArrayList<String>();
                        ArrayList<String> aCityIds = new ArrayList<String>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject c = response.getJSONObject(i);

                                aCities.add(c.getString("CITY_NAME"));
                                aCityIds.add(c.getString("CITY_ID"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayAdapter<String> dataAdapterCity = new ArrayAdapter<String>
                                (RegisterActivity.this, android.R.layout.simple_spinner_item, aCities);

                        dataAdapterCity.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);
                        mSpinTextCity.setAdapter(dataAdapterCity);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jreq);
    }


    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}