package info.androidhive.searchmed.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

import java.util.ArrayList;

import info.androidhive.searchmed.R;
import info.androidhive.searchmed.adapter.CardAdapterOrders;
import info.androidhive.searchmed.model.Constatnts;
import info.androidhive.searchmed.model.PendingOrder;

public class MyOrderFragment extends Fragment {

    ArrayList<PendingOrder> mArrArray ;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    // RecyclerView.Adapter mAdapter;
    CardAdapterOrders mAdapter;
    SharedPreferences sharedpreferences;

    private OnFragmentInteractionListener mListener;
    RelativeLayout rel_noOrders;
    SwipeRefreshLayout swipeContainer;

    public MyOrderFragment() {
        // Required empty public constructor
    }


    public static MyOrderFragment newInstance(String param1, String param2) {
        MyOrderFragment fragment = new MyOrderFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        sharedpreferences = getActivity().getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);

        View rootview =  inflater.inflate(R.layout.fragment_myorder, container, false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mRecyclerView = (RecyclerView)rootview.findViewById(R.id.recycler_viewMyOrders);
        mRecyclerView.setHasFixedSize(true);

        swipeContainer = (SwipeRefreshLayout) rootview.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getMyOrders();
                swipeContainer.setRefreshing(false);

            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light);


        getMyOrders();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        rel_noOrders = (RelativeLayout) rootview.findViewById(R.id.rel_noOrders);


        return rootview;

    }

    public void getMyOrders(){

      //  final ProgressDialog ringProgressDialog = ProgressDialog.show(getActivity(), "Fetching Data", "Please Wait ...", true);
        String userId = sharedpreferences.getString(Constatnts.UserID,null);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jreq = new JsonArrayRequest(Constatnts.getPendingCustomerOrders+userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mArrArray = new ArrayList<PendingOrder>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                PendingOrder aOrders = new PendingOrder();
                                JSONObject c = response.getJSONObject(i);



                                aOrders.setCustomer_id(c.getString("CUSTOMER_ID"));
                                aOrders.setCustomer_Name("CUSTOMER_NAME");
                                aOrders.setOrder_amount(c.getString(("ORDER_AMOUNT")));
                                aOrders.setOrder_delivery_amount(c.getString("ORDER_DELIVERY_AMOUNT"));
                                aOrders.setOrder_no(c.getString("ORDER_NO"));
                                aOrders.setOrder_Status(c.getString(("ORDER_STATUS")));
                                aOrders.setOrder_status_id(c.getString("ORDER_STATUS_ID"));
                                aOrders.setOrder_type_id(c.getString("ORDER_TYPE_ID"));
                                aOrders.setOrder_vendor_amount(c.getString("ORDER_VENDOR_AMOUNT"));
                                aOrders.setOrder_date(c.getString("Order_Date"));
                                aOrders.setPayment_type_id(c.getString("PAYMENT_TYPE_ID"));
                                aOrders.setVendor_bill_no(c.getString("VENDOR_BILL_NO"));
                                aOrders.setVendor_id(c.getString("VENDOR_ID"));
                                aOrders.setVendor_Name(c.getString(("VENDOR_NAME")));
                                aOrders.setVendor_Type_Name(c.getString(("VENDOR_TYPE_NAME")));

                                mArrArray.add(aOrders);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(mArrArray.size() > 0){

                            mAdapter = new CardAdapterOrders(getActivity(),mArrArray);
                            mRecyclerView.setAdapter(mAdapter);
                            rel_noOrders.setVisibility(View.INVISIBLE);
                         //   ringProgressDialog.dismiss();
                        }
                        else {

                            rel_noOrders.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.INVISIBLE);
                            //ringProgressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof NoConnectionError){
                    Toast.makeText(getContext(), "No Internet Connection. Please Try Again !! ", Toast.LENGTH_LONG).show();
                   // ringProgressDialog.dismiss();
                }
                else {
                    Toast.makeText(getContext(), "Error !! " + error.toString(), Toast.LENGTH_LONG).show();
                    System.out.println(error.toString());
                   // ringProgressDialog.dismiss();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
