package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.model.Constatnts;

public class LoginActivity extends Activity implements View.OnClickListener{
    Button loginNow;
    TextView signupText;
    EditText mUserName, mPassword;
    ProgressDialog ringProgressDialog1;
    SharedPreferences sharedpreferences;
    CheckBox isAcceptedTerms;

    EditText editTxt_UserName,editTxt_password;
    FloatingActionButton btnLogin;
    TextView txtV_forgotPassword,txtV_SignUp;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        txtV_SignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        hideSoftKeyboard();

        sharedpreferences = getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);
        String isLoggedIn = sharedpreferences.getString(Constatnts.USERTYPE, null);
      //  isLoggedIn = "";
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        if(isLoggedIn != null)
        {
            Intent i=new Intent(getBaseContext(),MainActivity.class);
            startActivity(i);
            finish();
        } else {
            showTandCDialog();
        }
    }

    private void findView() {

        editTxt_UserName = (EditText) findViewById(R.id.editTxt_UserName);
        editTxt_password = (EditText) findViewById(R.id.editTxt_password);
        btnLogin = (FloatingActionButton) findViewById(R.id.btn_login);
        txtV_SignUp = (TextView) findViewById(R.id.txtV_SignUp);

    }

    //hide keyboard
    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
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


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txtV_SignUp:
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                //finish();
                break;

            case R.id.btn_login:
                ringProgressDialog1 = ProgressDialog.show(LoginActivity.this, "Please wait ...", "Loading ...", true);
                // postLoginDetails(this);
                if ( editTxt_UserName.getText().toString().length()>0 &&  editTxt_password.getText().toString().length()>=6)
                {
                    Pattern ps = Pattern.compile("^[a-zA-Z0-9 ]+$");
                    Matcher ms = ps.matcher(editTxt_password.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false)
                    {
                        editTxt_password.setError("Special Characters Not Allowed ");
                        ringProgressDialog1.dismiss();
                    }
                    else if(bs == true)
                    {
                        postLoginDetails(this);
                        ringProgressDialog1.dismiss();
                    }
                    ringProgressDialog1.dismiss();

                }
                else
                {
                    if((editTxt_UserName.getText().toString().isEmpty()) ||(editTxt_UserName.getText().toString().length() < 3))
                    {
                        editTxt_UserName.setError("Enter proper username");
                    }
                    if((editTxt_password.getText().toString().isEmpty()) || (editTxt_password.getText().toString().length() <6))
                    {
                        editTxt_password.setError("Invalid password");
                    }

                    ringProgressDialog1.dismiss();
                }

                break;
            default:
                break;
        }
    }


    public void postLoginDetails(Context context){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, Constatnts.LOGINUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //"SUCCESS:1,USER_ID:61,USER_TYPE_ID:3,USER_LINK_ID:45,VENDOR_TYPE_ID:0,NAME:shruti p,CITY_NAME:,EMAIL_ID:etv@gmail.com"

                System.out.println("LoginActivity Login Response: " + response);
                String line = response.replaceAll("\"", "");
                String[] splitted = line.split(",");
                SharedPreferences.Editor editor = sharedpreferences.edit();

                if( splitted[0].replace("SUCCESS:", "").equals("1"))
                {
                    if (splitted[2].equals("USER_TYPE_ID:3"))
                    {
                        for(String str: splitted)
                        {
                            System.out.println(str);
                        }

                        boolean logStatus = true;

                        //"SUCCESS:1,USER_ID:489,USER_TYPE_ID:3,USER_LINK_ID:110,VENDOR_TYPE_ID:0,NAME:Ashish Kamat ,CITY_NAME:,EMAIL_ID:ashishkamat@yahoo.com,ADDRESS:Margao"

                        editor.putString(Constatnts.UserID, splitted[1].replace("USER_ID:", ""));
                        editor.putString(Constatnts.USERTYPE, splitted[2].replace("USER_TYPE_ID:", ""));
                        editor.putString(Constatnts.CUSTOMER_ID, splitted[3].replace("USER_LINK_ID:", ""));
                        editor.putString(Constatnts.VENDOR_TYPE_ID, splitted[4].replace("VENDOR_TYPE_ID:", ""));
                        editor.putString(Constatnts.NAME, splitted[5].replace("NAME:", ""));
                        editor.putString(Constatnts.CITY_NAME, splitted[6].replace("CITY_NAME:", ""));
                        editor.putString(Constatnts.EMAIL_ID, splitted[7].replace("EMAIL_ID:", ""));
                        editor.putString(Constatnts.ADDRESS,splitted[8].replace("ADDRESS:", ""));
                        Constatnts.yourLocked = "TRUE";
                        editor.commit();

                        try {
                            if (logStatus)
                            {
                                Toast.makeText(getApplicationContext(), "Successfully Logged in "+
                                        sharedpreferences.getString(Constatnts.FULL_NAME, ""), Toast.LENGTH_LONG).show();
                                hideSoftKeyboard();
                                ringProgressDialog1.dismiss();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                        catch (Exception e)
                        {
                            ringProgressDialog1.dismiss();
                            Toast.makeText(getApplicationContext(),"INVALID LOGSTATUS NOT TRUE ",Toast.LENGTH_SHORT).show();
                        }
                        ringProgressDialog1.dismiss();
                    }
                    else
                    {
                        ringProgressDialog1.dismiss();
                        Toast.makeText(getApplicationContext(),"INVALID USER TYPE ",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    ringProgressDialog1.dismiss();
                    Toast.makeText(getApplicationContext(),
                            " Invalid UserName or Password !!",Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());

                if(error instanceof NoConnectionError){
                    Toast.makeText(getApplicationContext(),
                            "No Internet Connection Please Try Again !! ",Toast.LENGTH_SHORT).show();
                    ringProgressDialog1.dismiss();
                }

                ringProgressDialog1.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();
                params.put("UserName",editTxt_UserName.getText().toString());
                params.put("UserPassword",editTxt_password.getText().toString());

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

    public void showTandCDialog() {

        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View layout = inflater.inflate(R.layout.category_terms, null );

        ImageView imageV_privacyPolicy,imageV_refund,imageV_shipnDeliveryPolicy,imageV_terms;

        Button mButton = (Button) layout.findViewById(R.id.acept_button);
        Button mButtonExit = (Button) layout.findViewById(R.id.rejct_button );

        imageV_privacyPolicy = (ImageView) layout.findViewById(R.id.imgV_privacy);
        imageV_refund = (ImageView) layout.findViewById(R.id.imgV_refund);
        imageV_shipnDeliveryPolicy = (ImageView) layout.findViewById(R.id.imgV_shipndelivery);
        imageV_terms = (ImageView) layout.findViewById(R.id.imgV_terms);

        imageV_privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(Constatnts.privacyPolicy_URL));
                startActivity(intent);

            }
        });

        imageV_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(Constatnts.refundPolicy_URL));
                startActivity(intent);
            }
        });

        imageV_shipnDeliveryPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(Constatnts.shpDelPolicy_URL));
                startActivity(intent);
            }
        });

        imageV_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(Constatnts.termsCondPolicy_URL));
                startActivity(intent);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        mButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.show();
    }
}