package info.androidhive.searchmed.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.searchmed.activity.ChatActivity;
import info.androidhive.searchmed.activity.MainActivity;
import info.androidhive.searchmed.database.Database;


public class SyncChats {

    String chatId = "",Chat_Date="",Chat_Message="",Chat_time="",Customer_ID="",Customer_Name="",vendor_ID="",vendor_Name="",vendor_Type="",vendor_chat_type="";
    JSONObject c = null;
    JSONArray Chat_Array;

    public void Sync_all_Chats(final Context context, final String time,final String Customer_id,final String Vendor_id)
    {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(context, "Fetching Data", "Please Wait ...", true);

        final Database db = new Database(context);
        final Chat mChat = new Chat();

        Log.d("SyncChats ","Time " + time + "CustomerID " + Customer_id + "VendorID " + Vendor_id);

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, Constatnts.Sync_All_Chats, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                System.out.println("SyncChats Response: " + response);

                try
                {
                    c = new JSONObject(response);

                    Chat_Array = c.optJSONArray("Chats");

                    for (int i=0;i<Chat_Array.length();i++)
                    {
                        JSONObject mObj = Chat_Array.getJSONObject(i);
                        vendor_chat_type = mObj.getString("vendor_chat_type");

                        Log.d("SyncChats","VendorChatType"+vendor_chat_type);

                        if(vendor_chat_type.equals("1"))
                        {
                            Chat_Date = mObj.getString("Chat_Date");
                            Chat_Message = mObj.getString("Chat_Message");
                            Chat_time = mObj.getString("Chat_time");
                            Customer_ID = mObj.getString("Customer_ID");
                            Customer_Name = mObj.getString("Customer_Name");
                            vendor_ID = mObj.getString("vendor_ID");
                            vendor_Name = mObj.getString("vendor_Name");
                            vendor_Type = mObj.getString("vendor_Type");
                            vendor_chat_type = mObj.getString("vendor_chat_type");
                            chatId = mObj.getString("chat_id");

                            String Chat_Date_Append = Chat_Date +" ";
                            String date_time = Chat_Date_Append.concat(Chat_time);

                            System.out.println("ChatDate: " + date_time.toString());

                            mChat.setVendorID(Integer.parseInt(vendor_ID)) ;
                            mChat.setVendorName(vendor_Name);
                            mChat.setChatText("" + Chat_Message);
                            mChat.setDateTime("" + date_time);
                            mChat.setVendorType(Integer.parseInt(vendor_Type));
                            mChat.setChatType(Integer.parseInt(vendor_chat_type));
                            mChat.setChatID_Server(chatId);

                            db.deleteDuplicatesChatData(chatId);
                            db.createChat(mChat);

                        }
                        //Customer Received Chats
                        else if(vendor_chat_type.equals("2"))
                        {
                            Chat_Date = mObj.getString("Chat_Date");
                            Chat_Message = mObj.getString("Chat_Message");
                            Chat_time = mObj.getString("Chat_time");
                            Customer_ID = mObj.getString("Customer_ID");
                            Customer_Name = mObj.getString("Customer_Name");
                            vendor_ID = mObj.getString("vendor_ID");
                            vendor_Name = mObj.getString("vendor_Name");
                            vendor_Type = mObj.getString("vendor_Type");
                            vendor_chat_type = mObj.getString("vendor_chat_type");
                            chatId = mObj.getString("chat_id");


                            System.out.println("mObj " + mObj.toString());


                            String Chat_Date_Append = Chat_Date +" ";
                            String date_time = Chat_Date_Append.concat(Chat_time);

                            System.out.println("ChatDate: " + date_time.toString());

                            mChat.setVendorID(Integer.parseInt(vendor_ID));
                            mChat.setVendorName(vendor_Name);
                            mChat.setChatText("" + Chat_Message);
                            mChat.setDateTime("" + date_time);
                            mChat.setVendorType(Integer.parseInt(vendor_Type));
                            mChat.setChatType(Integer.parseInt(vendor_chat_type));
                            mChat.setChatID_Server(chatId);

                            db.deleteDuplicatesChatData(chatId);
                            db.createChat(mChat);

                        }
                    }

                    Toast.makeText(context, "Chat Synced", Toast.LENGTH_SHORT).show();
                    ringProgressDialog.dismiss();

                    if(Constatnts.FromChatActivity.equals("TRUE"))
                    {
                        Constatnts.FromChatActivity = "FALSE";
                        Constatnts.FromMainActivity = "FALSE";
                        Intent i = new Intent(context, ChatActivity.class);
                        context.startActivity(i);

                    }
                    else if(Constatnts.FromMainActivity.equals("TRUE"))
                    {
                        Constatnts.FromMainActivity = "FALSE";
                        Constatnts.FromChatActivity = "FALSE";
                        Intent i = new Intent(context, MainActivity.class);
                        context.startActivity(i);

                    }
                }

                catch (Exception e)
                {
                    Log.d("SyncChats","Catch " + e.toString());
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());

                        if(error instanceof NoConnectionError)
                        {
                            Toast.makeText(context,"No Internet Connection Please Try Again !! ",Toast.LENGTH_SHORT).show();

                            if(Constatnts.FromMainActivity.equals("TRUE"))
                            {
                                Constatnts.yourLocked = "TRUE";
                                Constatnts.FromMainActivity = "FALSE";
                                Intent i = new Intent(context, MainActivity.class);
                                context.startActivity(i);
                            }
                            else if(Constatnts.FromChatActivity.equals("TRUE"))
                            {
                                Constatnts.yourLocked = "TRUE";
                                Constatnts.FromChatActivity = "FALSE";
                                Intent i = new Intent(context, ChatActivity.class);
                                context.startActivity(i);
                            }

                        }
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();
                params.put("NO_OF_DAYS",time.toString());
                params.put("CUSTOMER_ID",Customer_id.toString());
                params.put("VENDOR_ID",Vendor_id.toString());

                System.out.println("ChatActivity SyncAllChats Params: " + params.toString());

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
                Toast.makeText(context,"Timeout Error !! ",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);
    }
}
