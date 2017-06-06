package info.androidhive.materialdesign.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.database.Database;
import info.androidhive.materialdesign.model.Chat;
import info.androidhive.materialdesign.model.Constatnts;
import info.androidhive.materialdesign.model.Order;
import info.androidhive.materialdesign.model.SyncChats;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ChatActivity extends AppCompatActivity {

    //Used in OnCreate()
    TextView TxtV_venderName,TxtV_address;
    public String catID = "", chatVenID = "", userID = "", custID = "", vendorName, vendorArea,
            userName, Email_id, orderNumber = "",typed_Message, chatID_ ,ONumber,oCR_status;
    SharedPreferences sharedpreferences;

    //Userd in FindView()
    private EditText msg;
    private FloatingActionButton send;
    RecyclerView mRecyclerView;
    CardAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    Chat mChat;

    //Used in LoadChatList()
    Database db;
    ArrayList<Chat> aChatArr;

    //Used in CardAdapter
    Order dbOrderConfirmation_Status;
    int adapter_position,ORDER_STATUS,Ord_status = 0;
    boolean order_update = false, Received_new_msg = false,status = false;

    //Required For Image Zoom
    PhotoViewAttacher mAttacher;
    //onResume
    Timer autoUpdate;
    AlertDialog levelDialog;
    String Customer_address;
    String Sync_Time="7";
    Context context = ChatActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // autoUpdate.cancel();
                onBackPressed();
            }
        });

        //on toolbar
        toolbar.setTitle("");

        findViews();

        chatVenID = Constatnts.VENDOR_ID;
        vendorName = Constatnts.VENDOR_NAME;
        vendorArea = Constatnts.VENDOR_ADDRESS;
        catID = Constatnts.SL_CAT_ID;

        //Shared Prefrences
        sharedpreferences = getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);

        userID = sharedpreferences.getString(Constatnts.UserID, null);
        custID = sharedpreferences.getString(Constatnts.CUSTOMER_ID, null);
        userName = sharedpreferences.getString(Constatnts.NAME, null);
        Email_id = sharedpreferences.getString(Constatnts.EMAIL_ID, null);
        Customer_address = sharedpreferences.getString(Constatnts.ADDRESS, null);

        TxtV_venderName = (TextView) toolbar.findViewById(R.id.txtV_venderName);
        TxtV_address = (TextView) toolbar.findViewById(R.id.txtV_address);

        TxtV_venderName.setText("" + vendorName);
        TxtV_address.setText("" + vendorArea);

        loadChatList();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (msg.getText().length() > 0) {

                    typed_Message = msg.getText().toString();
                    postNewComment(ChatActivity.this);
                    //   msg.setText("");
                }
            }
        });
    }

    private void findViews() {

        msg = (EditText) findViewById(R.id.msg);
        msg.setTypeface(EasyFonts.caviarDreams(ChatActivity.this));
        send = (FloatingActionButton) findViewById(R.id.send);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvSearchRecycle);

        mLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

    }

    private int loadChatList()
    {

        db = new Database(ChatActivity.this);
        aChatArr = new ArrayList<Chat>();

        if(Received_new_msg == true)
        {
            if (!chatVenID.equals(""))
            {
                aChatArr = (ArrayList<Chat>) db.getAllChatForVendor(Integer.parseInt(chatVenID));
              //  db.closeDB();
            }
            if (aChatArr.size() !=0)
            {
                System.out.println("Chat data: " + aChatArr.toString());
                mAdapter = new CardAdapter(ChatActivity.this,aChatArr);
                mRecyclerView.setAdapter(mAdapter);

                System.out.println("adapter_position in loadChatlist " + adapter_position);
                mRecyclerView.scrollToPosition(adapter_position);
            }
            Received_new_msg = false;
        }
        else
        {
            if (!chatVenID.equals(""))
            {
                aChatArr = (ArrayList<Chat>) db.getAllChatForVendor(Integer.parseInt(chatVenID));
                //db.closeDB();
            }

            if (aChatArr.size() != 0)
            {
                System.out.println("Chat data: " + aChatArr.toString());
                mAdapter = new CardAdapter(ChatActivity.this,aChatArr);
                mRecyclerView.setAdapter(mAdapter);

                System.out.println("adapter_position in loadChatlist " + adapter_position);
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
            }
            Received_new_msg = false;
        }

        return aChatArr.size();
    }


    private String  createChat() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        final JSONObject contact = new JSONObject();
        try {
           /* contact.put("vendor_id",chatVenID);
            contact.put("vendor_type",catID);
            contact.put("vendor_name", vendorName);
            contact.put("date_time",currentDateandTime);
            contact.put("message", typed_Message);
            contact.put("chat_type","1");
            contact.put("cust_id",custID);
            contact.put("cust_name",userName);
            contact.put("full_name",Email_id);
            contact.put("Address",sharedpreferences.getString(Constatnts.CITY_NAME,""));
            contact.put("OrderNO",orderNumber);
*/

            //change 12.5.2016
            contact.put("vendorID",chatVenID);
            contact.put("vendor_Type",catID);
            contact.put("vendor_Name", vendorName);
            contact.put("vendor_sendDate_time",currentDateandTime);
            contact.put("vendor_chatMesg", typed_Message);
            contact.put("chat_type","1");
            contact.put("cust_id",custID);
            contact.put("cust_name",userName);
            contact.put("full_name",Email_id);
            contact.put("Address",sharedpreferences.getString(Constatnts.CITY_NAME,""));
            contact.put("OrderNO",orderNumber);

            System.out.println("Chat data(ChatFragment)"+contact.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contact.toString();
    }


    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>
    {
        Context aContext;
        ArrayList<Chat> aChatArr;
        Order mOrder;
        EndlessScrollListener endlessScrollListener;

        public void setEndlessScrollListener(EndlessScrollListener endlessScrollListener) {
            this.endlessScrollListener = endlessScrollListener;
        }


        public CardAdapter(Context mContext, ArrayList<Chat> mChatArr)
        {
            super();
            aChatArr = mChatArr;
            aContext = mContext;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v;
            ViewHolder viewHolder = null;

            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_layout, viewGroup, false);

            viewHolder = new ViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i)
        {
            final int VISIBLE_THRESHOLD = 5;

            if(i == getItemCount() - VISIBLE_THRESHOLD) {
                if(endlessScrollListener != null) {
                    endlessScrollListener.onLoadMore(i);
                }
            }

            final Chat items = aChatArr.get(i);

            adapter_position = viewHolder.getAdapterPosition();
            System.out.println("adapter_position in onBind " + adapter_position);

            //customer typng mesg
            if (items.getChatType()== 1)
            {
                viewHolder.venCard_VendorsChat.setVisibility(View.GONE);
                viewHolder.venCardRight_CustomersChat.setVisibility(View.VISIBLE);
                viewHolder.vendorBill.setVisibility(View.GONE);
                viewHolder.rel_prescriptionUpload.setVisibility(View.GONE);
                viewHolder.chatMSGRight.setText(items.getChatText());
                viewHolder.chatTimeRight.setText(items.getDateTime());
                viewHolder.txtVMesgFromCustomer.setText(sharedpreferences.getString(Constatnts.NAME, null));
            }

            else if(items.getChatType() == 3)
            {

                if(!items.getPrescriptionOrderNo().equals(""))
                {

                    viewHolder.venCard_VendorsChat.setVisibility(View.GONE);
                    viewHolder.venCardRight_CustomersChat.setVisibility(View.GONE);
                    viewHolder.vendorBill.setVisibility(View.GONE);
                    viewHolder.rel_prescriptionUpload.setVisibility(View.VISIBLE);
                    viewHolder.txtV_custName_pU.setText(sharedpreferences.getString(Constatnts.NAME, null));
                    viewHolder.txtV_time_pU.setText(items.getDateTime());

                    final String fetchImage = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Download/Customer Docs/" + items.getPrescriptionOrderNo() + ".jpeg";

                    Glide.with(context).load(fetchImage)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(viewHolder.imageV_uploadPrescription);

                   /* viewHolder.TxtV_Download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // viewHolder.imageV_uploadPrescription.setImageBitmap(image);
                           // Picasso.with(ChatActivity.this).load(new File(fetchImage)).fit().into(viewHolder.imageV_uploadPrescription);
                            Glide.with(context).load(fetchImage)
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(viewHolder.imageV_uploadPrescription);

                        }
                    });*/

                    viewHolder.imageV_uploadPrescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final Dialog nagDialog = new Dialog(ChatActivity.this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            nagDialog.setCancelable(false);
                            nagDialog.setContentView(R.layout.image_fullscreen);

                            Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);

                            ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);

                            ivPreview.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
                            viewHolder.imageV_uploadPrescription.setAdjustViewBounds(true);

                            //Picasso.with(ChatActivity.this).load(new File(fetchImage)).fit().into(ivPreview);

                            Glide.with(context).load(fetchImage)
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(ivPreview);
                            ivPreview.setScaleType(ImageView.ScaleType.FIT_XY);

                            mAttacher = new PhotoViewAttacher(ivPreview);

                            btnClose.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0)
                                {

                                    nagDialog.dismiss();

                                }
                            });
                            nagDialog.show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"ord no not received", Toast.LENGTH_SHORT).show();
                }
            }
            else if (items.getChatType()== 2)   //from vendor
            {
                viewHolder.venCard_VendorsChat.setVisibility(View.VISIBLE);
                viewHolder.venCardRight_CustomersChat.setVisibility(View.GONE);
                viewHolder.vendorBill.setVisibility(View.GONE);
                viewHolder.rel_prescriptionUpload.setVisibility(View.GONE);
                viewHolder.chatMSG.setText(items.getChatText());
                viewHolder.chatTime.setText(items.getDateTime());
                viewHolder.txtMesgFromVendor.setText(items.getVendorName());
                System.out.println("Vendor Name :" + items.getVendorName());
            }

            else if (items.getChatType() == 0)
            {
                mOrder = new Order();
                mOrder = db.getOrderVendor(items.getChatID());

                viewHolder.venCard_VendorsChat.setVisibility(View.GONE);
                viewHolder.venCardRight_CustomersChat.setVisibility(View.GONE);
                viewHolder.rel_prescriptionUpload.setVisibility(View.GONE);
                viewHolder.vendorBill.setVisibility(View.VISIBLE);

                // viewHolder.txtV_BillNo.setText(mOrder.getBillNo());
                viewHolder.txtV_Amount.setText(mOrder.getOrderAmount() + "");
                viewHolder.txtV_DeliveryCharges.setText(mOrder.getDeliveryAmnt() + "");
                viewHolder.txtV_TotalAmount.setText(mOrder.getTotalOrdAmnt() + "");
                viewHolder.txt_BillTime.setText(items.getDateTime());
                viewHolder.txtV_sendorName_bill.setText(items.getVendorName());

                viewHolder.txtV_chatId.setText(items.getChatID() + "");
                viewHolder.txtV_BillNo.setText(mOrder.getPaymentRequestNo());

                viewHolder.txtV_TotalAmount.setTypeface(EasyFonts.robotoBold(ChatActivity.this));

                viewHolder.rBtn_cod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (viewHolder.rBtn_cod.isChecked())
                        {
                            viewHolder.rBtn_onlinePayment.setChecked(false);
                            Toast.makeText(getApplication(), "You have chosen COD", Toast.LENGTH_SHORT).show();
                            /*cod_checked = true;
                            online_checked = false;*/
                        }

                    }
                });

                viewHolder.rBtn_onlinePayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (viewHolder.rBtn_onlinePayment.isChecked())
                        {
                            viewHolder.rBtn_cod.setChecked(false);
                            Toast.makeText(getApplication(), "You have chosen Online Payment", Toast.LENGTH_SHORT).show();
                          /*  cod_checked = false;
                            online_checked = true;*/
                        }
                    }
                });


                chatID_ = viewHolder.txtV_chatId.getText().toString();
                ONumber =  viewHolder.txtV_BillNo.getText().toString();

                dbOrderConfirmation_Status = new Order();

                dbOrderConfirmation_Status = db.getOderConfirmationStatus(chatID_);

                //Bill Confirm
                if(dbOrderConfirmation_Status.getOrderStateConfirmation() == 0)
                {

                    viewHolder.imageV_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (mOrder.getTotalOrdAmnt() != 0 && mOrder.getOrderAmount() != 0 && mOrder.getDeliveryAmnt() != 0)
                            {

                                ONumber =  viewHolder.txtV_BillNo.getText().toString();
                                chatID_ = viewHolder.txtV_chatId.getText().toString();

                                if (!ONumber.equals(""))
                                {
                                    ORDER_STATUS = 2;
                                    Ord_status = 1;

                                    postNewOrder(aContext);

                                    viewHolder.txtV_confirmedStatus.setVisibility(View.GONE);

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Order no not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Order details not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // cancel order
                    viewHolder.imageV_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ONumber =  viewHolder.txtV_BillNo.getText().toString();
                            chatID_ = viewHolder.txtV_chatId.getText().toString();
                            //!Constatnts.OrderNo_FromVender.equals("")
                            if (!ONumber.equals(""))
                            {

                                ORDER_STATUS = 5;
                                Ord_status = 2;

                                postNewOrder(aContext);

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Order no not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else if(dbOrderConfirmation_Status.getOrderStateConfirmation() == 1)
                {
                    viewHolder.imageV_cancel.setVisibility(View.GONE);
                    viewHolder.imageV_confirm.setVisibility(View.GONE);
                    viewHolder.txtV_confirmedStatus.setVisibility(View.VISIBLE);

                    viewHolder.TxtV_BillNo.setVisibility(View.VISIBLE);
                    viewHolder.txtV_BillNo.setVisibility(View.VISIBLE);
                    viewHolder.txtV_BillNo.setText(mOrder.getPaymentRequestNo());
                    viewHolder.rBtn_cod.setEnabled(false);
                    viewHolder.rBtn_onlinePayment.setEnabled(false);

                }
                else if(dbOrderConfirmation_Status.getOrderStateConfirmation() == 2)
                {
                    viewHolder.venCard_VendorsChat.setVisibility(View.GONE);
                    viewHolder.venCardRight_CustomersChat.setVisibility(View.GONE);
                    viewHolder.rel_prescriptionUpload.setVisibility(View.GONE);
                    viewHolder.vendorBill.setVisibility(View.VISIBLE);

                    viewHolder.rel_confrmOrder.setVisibility(View.GONE);
                    viewHolder.rel_cancelOrder.setVisibility(View.VISIBLE);

                    viewHolder.txtV_sendorName_bill_cancel.setText(items.getVendorName());
                    viewHolder.txtBill_cancel_Time.setText(items.getDateTime());
                    viewHolder.textV_paymentReqNo_cancel.setText(mOrder.getPaymentRequestNo());
                }
            }

            //Chat Message Out For Delivery
            else if(items.getChatType() == 5)
            {
                // out for delivery
                viewHolder.venCard_VendorsChat.setVisibility(View.VISIBLE);
                viewHolder.venCardRight_CustomersChat.setVisibility(View.GONE);
                viewHolder.vendorBill.setVisibility(View.GONE);
                viewHolder.rel_prescriptionUpload.setVisibility(View.GONE);
                viewHolder.chatMSG.setText(items.getChatText());
                viewHolder.chatTime.setText(items.getDateTime());
                viewHolder.txtMesgFromVendor.setText(items.getVendorName());

            }
            //Chat Message Delivered
            else if(items.getChatType() == 6)
            {
                // delivered
                viewHolder.venCard_VendorsChat.setVisibility(View.VISIBLE);
                viewHolder.venCardRight_CustomersChat.setVisibility(View.GONE);
                viewHolder.vendorBill.setVisibility(View.GONE);
                viewHolder.rel_prescriptionUpload.setVisibility(View.GONE);
                viewHolder.chatMSG.setText(items.getChatText());
                viewHolder.chatTime.setText(items.getDateTime());
                viewHolder.txtMesgFromVendor.setText(items.getVendorName());

            }
        }

        @Override
        public int getItemCount() {

            return aChatArr .size();
        }

        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView chatMSG, chatTime, chatMSGRight, chatTimeRight;
            RelativeLayout venCard_VendorsChat,venCardRight_CustomersChat, vendorBill,rel_prescriptionUpload;
            TextView TxtV_BillNo,txtV_BillNo,txtV_Amount,txtV_DeliveryCharges,txtV_TotalAmount,txt_BillTime,
                    txtVMesgFromCustomer,txtMesgFromVendor,txtV_sendorName_bill,txtV_chatId,
                    txtV_custName_pU,txtV_time_pU,txtV_confirmedStatus,TxtV_Download;

            RadioButton rBtn_cod,rBtn_onlinePayment;
            ImageView imageV_cancel,imageV_confirm,imageV_uploadPrescription;
            // cancel Order
            TextView txtV_sendorName_bill_cancel,textV_paymentReqNo_cancel,txtBill_cancel_Time;
            RelativeLayout rel_cancelOrder,rel_confrmOrder;


            public ViewHolder(View itemView) {
                super(itemView);
                chatMSG = (TextView) itemView.findViewById(R.id.txtMsgFrom);
                chatMSG.setTypeface(EasyFonts.caviarDreams(aContext));

                chatTime = (TextView) itemView.findViewById(R.id.txtTimeFrom);
                chatTime.setTypeface(EasyFonts.caviarDreams(aContext));

                chatMSGRight = (TextView) itemView.findViewById(R.id.txtMsgFrom_);
                chatMSGRight.setTypeface(EasyFonts.caviarDreams(aContext));

                chatTimeRight = (TextView) itemView.findViewById(R.id.txtTimeFrom_);
                chatTimeRight.setTypeface(EasyFonts.caviarDreams(aContext));

                venCard_VendorsChat = (RelativeLayout) itemView.findViewById(R.id.rel_layout_senderMesg);
                venCardRight_CustomersChat = (RelativeLayout) itemView.findViewById(R.id.rel_layout_receivedMesg);

                TxtV_BillNo = (TextView) itemView.findViewById(R.id.TxtV_BillNo);
                vendorBill = (RelativeLayout) itemView.findViewById(R.id.rel_layout_senderMesgBill);
                txtV_BillNo = (TextView) itemView.findViewById(R.id.txtV_BillNo);
                txtV_Amount = (TextView) itemView.findViewById(R.id.txtV_Amount);
                txtV_DeliveryCharges = (TextView) itemView.findViewById(R.id.txtV_DeliveryCharges);
                txtV_TotalAmount = (TextView) itemView.findViewById(R.id.txtV_TotalAmount);
                txt_BillTime = (TextView) itemView.findViewById(R.id.txtBill_Time);

                txtVMesgFromCustomer = (TextView) itemView.findViewById(R.id.txtV_receiverName);
                txtMesgFromVendor = (TextView) itemView.findViewById(R.id.txtV_sendorName);
                txtV_sendorName_bill = (TextView) itemView.findViewById(R.id.txtV_sendorName_bill);

                txtV_sendorName_bill.setTypeface(EasyFonts.caviarDreams(aContext));
                txtV_BillNo.setTypeface(EasyFonts.caviarDreams(aContext));
                txtV_Amount.setTypeface(EasyFonts.caviarDreams(aContext));
                txtV_DeliveryCharges.setTypeface(EasyFonts.caviarDreams(aContext));
                txtV_TotalAmount.setTypeface(EasyFonts.caviarDreams(aContext));
                txt_BillTime.setTypeface(EasyFonts.caviarDreams(aContext));

                TxtV_Download = (TextView) itemView.findViewById(R.id.TxtV_download);

                imageV_cancel = (ImageView) itemView.findViewById(R.id.imageV_cancel);
                imageV_confirm = (ImageView) itemView.findViewById(R.id.imageV_confirm);

                rBtn_cod = (RadioButton) itemView.findViewById(R.id.rBtn_cod);
                rBtn_onlinePayment = (RadioButton) itemView.findViewById(R.id.rBtn_onlinePayment);
                txtV_chatId = (TextView) itemView.findViewById(R.id.txtV_chatId);

                imageV_uploadPrescription = (ImageView) itemView.findViewById(R.id.imageV_prescriptionUpload);
                txtV_custName_pU = (TextView) itemView.findViewById(R.id.txtV_receiverName_pU);
                txtV_time_pU = (TextView) itemView.findViewById(R.id.txtTimeFrom_pU);
                rel_prescriptionUpload = (RelativeLayout) itemView.findViewById(R.id.rel_layout_uploadPrescription);

                // confirmed status  for vendor bill
                txtV_confirmedStatus = (TextView) itemView.findViewById(R.id.txtV_confirmedStatus);

                //cancel Order
                txtV_sendorName_bill_cancel = (TextView) itemView.findViewById(R.id.txtV_sendorName_bill_cancel);
                textV_paymentReqNo_cancel = (TextView) itemView.findViewById(R.id.textV_paymentReqNo_cancel);
                txtBill_cancel_Time = (TextView) itemView.findViewById(R.id.txtBill_cancel_Time);

                rel_cancelOrder = (RelativeLayout) itemView.findViewById(R.id.rel_cancelOrder);
                rel_confrmOrder = (RelativeLayout) itemView.findViewById(R.id.rel_confrmOrder);

            }
        }
    }


    // Chat Messages Sending
    public void postNewComment(final Context context){

        createChat();
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest sr = new StringRequest(Request.Method.POST, Constatnts.postChatUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //"SUCCESS:1,CHAT_ID:2636,GCM_ID:GCM Response,id=0:1462882402814839%86ceb2e366d6cf16"

                Log.e("ChatActivity", "PostChatMessage Response " + response);

                //"GCM Response,Error=NotRegistered (Vendor not Registered gcm id not found)"


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

                    editor.putString(Constatnts.CHAT_ID, splitted[1].replace("CHAT_ID:", ""));
                    editor.commit();
                    try {
                        if (logStatus)
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            String currentDateandTime = sdf.format(new Date());

                            mChat = new Chat();
                            mChat.setVendorID(Integer.parseInt(chatVenID));
                            mChat.setChatText("" + typed_Message);
                            mChat.setDateTime("" + currentDateandTime);
                            mChat.setVendorName("" + vendorName);
                            mChat.setVendor_AREA("" + vendorArea);
                            mChat.setChatType(1);
                            mChat.setPrescriptionOrderNo("");
                            mChat.setChatID_Server(splitted[1].replace("CHAT_ID:", ""));

                            Database db = new Database(ChatActivity.this);
                            db.createChat(mChat);

                            loadChatList();

                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                            msg.setText("");

                            Toast.makeText(ChatActivity.this, "Message SENT Successfully",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"INVALID LOGSTATUS NOT TRUE ",Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(ChatActivity.this, "Message failed to Sent",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof NoConnectionError){
                    Toast.makeText(ChatActivity.this,"No Internet Connection. Please Try Again !!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ChatActivity.this, "INVALID " + error.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("USER_ID",userID);
                params.put("CUSTOMER_ID",custID);
                params.put("VENDOR_ID", chatVenID);
                params.put("VENDOR_NAME",TxtV_venderName.getText().toString());
                params.put("CHAT_TYPE_ID","1");
                params.put("CHAT_MESSAGE", createChat());

                Log.d("ChatActivity",params.toString());

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
                Toast.makeText(ChatActivity.this,"TimeOut Error !! ",Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(sr);
    }


    // confirm or reject order
    public void postNewOrder(final Context context)
    {

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest sr = new StringRequest(Request.Method.POST, Constatnts.postOrderUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                sharedpreferences = getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);
                System.out.println("ChatActivity Response PostNewOrder " + response);
                String line = response.replaceAll("\"", "");
                String[] splitted = line.split(",");
                SharedPreferences.Editor editor = sharedpreferences.edit();

                if( splitted[0].replace("SUCCESS:", "").equals("1") )
                {
                    for (String str : splitted)
                    {
                        System.out.println("ChatActivity: "+str);
                    }

                    editor.putString(Constatnts.OrderNo, splitted[1].replace("ORDER_NO:", ""));
                    editor.putString(Constatnts.VendorID, splitted[2].replace("VENDOR_ID:", ""));
                    editor.putString(Constatnts.GCMID, splitted[3].replace("GCM_ID:", ""));
                    editor.commit();

                    if(!splitted[1].replace("ORDER_NO:", "").equals(""))
                    {

                        if (Ord_status == 1)
                        {
                            oCR_status = "1";
                            db.update_Confirmation_StatusOrder_new(chatID_,
                                    oCR_status);
                            System.out.println("ChatActivity ChatId in PostNewOrder " +chatID_);
                            Toast.makeText(ChatActivity.this, "Your order has been placed successfully ", Toast.LENGTH_SHORT).show();
                            Ord_status = 0;
                            order_update = true;
                            status = true;
                        }
                        else if (Ord_status == 2)
                        {
                            oCR_status = "2";
                            db.update_Confirmation_StatusOrder_new(chatID_, oCR_status);
                            System.out.println("ChatActivity ChatId in PostNewOrder " +chatID_);
                            Toast.makeText(ChatActivity.this, "Your order has been cancelled successfully ", Toast.LENGTH_SHORT).show();
                            Ord_status = 0;
                            order_update = true;
                            status = true;
                        }

                    }
                    else
                    {
                        Toast.makeText(ChatActivity.this, "Did Not Receive OrderNO. ", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(ChatActivity.this, "No Response from Server !!", Toast.LENGTH_SHORT).show();
                }


                //       exportDB();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NoConnectionError)
                {
                    Toast.makeText(ChatActivity.this, "No Internet Connection. Please try Again !! ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ChatActivity.this, "INVALID " + error.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("ChatActivity "+error.toString());
                }
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("CUSTOMER_ID",custID);
                params.put("VENDOR_ID", chatVenID);
                params.put("PAYMENT_TYPE_ID","1");
                params.put("ORDER_STATUS_ID", ORDER_STATUS + "");
                params.put("ORDER_NO",ONumber);

                System.out.print("ChatActivity Order Confirmation :" + params.toString());

                return params;
            }
        };

        queue.add(sr);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();
        //To Upload Prescription
        if (id == R.id.action_uploadPrescription)
        {
            Intent i = new Intent(getApplicationContext(), UploadPrescriptionActivity.class);
            startActivity(i);
            finish();
        }
        //To Sync all Chat
        if(id == R.id.action_sync)
        {
            SyncAllChatDialog();
        }
        //To Share Customers Address
        if(id == R.id.action_ShareAddress)
        {
            typed_Message = Customer_address;
            postNewComment(ChatActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

  /*      Intent intent = getIntent();
        String msg = intent.getStringExtra("json_msg");*/

        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                        if (Constatnts.RECEIVED_NEW_MSG == true) {

                            Constatnts.RECEIVED_NEW_MSG = false;
                            Received_new_msg = true;
                            loadChatList();
                            order_update = false;
                            //    Toast.makeText(ChatActivity.this, "In onResume Received_Msg !!", Toast.LENGTH_SHORT).show();
                        }

                        if (order_update == true) {

                            loadChatList();
                            order_update = false;
                            // mRecyclerView.smoothScrollToPosition(adapter_position);
                            System.out.println("adapter_position in on Resume " + adapter_position);
                            Constatnts.RECEIVED_NEW_MSG = false;
                            order_update = false;
                            //     Toast.makeText(ChatActivity.this, "In onResume Order_update !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, 0, 5000); // updates each 40 secs

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    public void SyncAllChatDialog()
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
                            Constatnts.FromChatActivity = "TRUE";
                             // clear chat table and load new chat
                            //db.clearChatData();

                            syncChats.Sync_all_Chats(ChatActivity.this,Sync_Time,custID,chatVenID);
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


}
