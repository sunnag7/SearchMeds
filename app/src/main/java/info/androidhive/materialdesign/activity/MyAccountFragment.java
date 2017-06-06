package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.model.Constatnts;


public class MyAccountFragment extends Fragment {

    TextView TxtV_UserName,TxtV_CityName,TxtV_UserMobile,TxtV_UserEmail;
    ImageView image;

    // public static final String MyPREFERENCES = "UmbrellaPrefs" ;
    SharedPreferences sharedpreferences;
    Context context;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_layout, container, false);
        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        sharedpreferences = getActivity().getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);

        String user_ID = sharedpreferences.getString(Constatnts.UserID, null);
        getMyDetails(user_ID);


        // Inflate the layout for this fragment
        TxtV_UserName = (TextView) rootView.findViewById(R.id.TxtV_UserName);
        TxtV_CityName = (TextView) rootView.findViewById(R.id.TxtV_CityName);
        TxtV_UserEmail = (TextView) rootView.findViewById(R.id.TxtV_UserEmail);
        TxtV_UserMobile = (TextView) rootView.findViewById(R.id.TxtV_UserMobile);
        image = (ImageView) rootView.findViewById(R.id.ImgV_Profile);


        //set Values to TextView

        return rootView;
    }


    public void getMyDetails(String userid){

        final ProgressDialog ringProgressDialog = ProgressDialog.show(getActivity(), "Fetching Profile Data", "Please Wait ...", true);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        System.out.println("User ID " +userid);

        JsonArrayRequest jreq = new JsonArrayRequest(Constatnts.GetCustomerByUSERID+userid,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject c = response.getJSONObject(i);

                                System.out.println("Customer Name : " + c.getString("CUSTOMER_NAME"));

                                String UName = c.getString("CUSTOMER_NAME");
                                TxtV_UserName.setText(UName.toUpperCase());
                                TxtV_CityName.setText(c.getString("CITY"));
                                TxtV_UserEmail.setText(c.getString("CUSTOMER_EMAIL"));
                                TxtV_UserMobile.setText(c.getString("CUSTOMER_MOBILE"));

                                TextDrawable drawable = TextDrawable.builder()
                                        .beginConfig()
                                        .bold()
                                        .toUpperCase()
                                        .withBorder(4)
                                        .endConfig()
                                        .buildRound(String.valueOf(UName.charAt(0)), Color.BLACK);

                                image.setImageDrawable(drawable);

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                                ringProgressDialog.dismiss();
                            }
                        }
                        ringProgressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof NoConnectionError)
                {

                    ringProgressDialog.dismiss();
                    Toast.makeText(getContext(), "No Internet Connection. Please Try Again !! ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    System.out.println(error.toString());
                    ringProgressDialog.dismiss();
                    Toast.makeText(getContext(), "TimeOut Error !! ", Toast.LENGTH_LONG).show();
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}