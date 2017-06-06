package info.androidhive.materialdesign.activity;

import  android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.database.Database;
import info.androidhive.materialdesign.model.Chat;
import info.androidhive.materialdesign.model.ConnectivityCheck;
import info.androidhive.materialdesign.model.Constatnts;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;


public class UploadPrescriptionActivity extends AppCompatActivity {

    ImageView img_view,imageV_chooseprescription_gallery,imageV_chooseprescription_camera;
    FloatingActionButton btn_upload,btn_Cancel;
    Database db;

    //Image Upload parameters
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 11;
    private String UPLOAD_URL = "http://54.169.227.89:94/DataService.svc/PrescriptionImageUpload";
    SharedPreferences sharedpreferences;
    public String custID = "";
    String image_name, ord_no,VenID = "";
    String Order_type_id_received;
    String realPath;
    OutputStream output;
    Chat oChat;
    int REQUEST_CAMERA =  0, SELECT_FILE = 1;

    public String catID = "", chatVenID = "", userID = "", vendorName, vendorArea, userName,
            FullName, orderNumber = "";
    boolean imageValid = false;
    ConnectivityCheck connectivityCheck = new ConnectivityCheck();
    Context context = UploadPrescriptionActivity.this;
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar.setNavigationIcon(R.drawable.back_arrow);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // autoUpdate.cancel();
                Intent i = new Intent(getApplicationContext(), Order_Options_Activity.class);
                startActivity(i);
                finish();
            }
        });


        //Initializations
        imageV_chooseprescription_gallery = (ImageView) findViewById(R.id.imageV_choosePrescription_gallery);
        imageV_chooseprescription_camera = (ImageView) findViewById(R.id.imageV_choosePrescription_camera);
        btn_upload = (FloatingActionButton) findViewById(R.id.btn_upload);
        btn_Cancel = (FloatingActionButton) findViewById(R.id.btn_Cancel);
        img_view = (ImageView) findViewById(R.id.img_display_presc);

        chatVenID = Constatnts.VENDOR_ID;
        vendorName = Constatnts.VENDOR_NAME;
        vendorArea = Constatnts.VENDOR_ADDRESS;
        catID = Constatnts.SL_CAT_ID;


        sharedpreferences = getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);
        custID = sharedpreferences.getString(Constatnts.CUSTOMER_ID, null);
        VenID = Constatnts.VENDOR_ID;
        userID = sharedpreferences.getString(Constatnts.UserID, null);
        userName = sharedpreferences.getString(Constatnts.NAME, null);
        FullName = sharedpreferences.getString(Constatnts.EMAIL_ID, null);

        File delete_file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
        delete_file.delete();

        //Button OnClick Listeners
        imageV_chooseprescription_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image*//*");*/
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST);

            }
        });

        imageV_chooseprescription_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                REQUEST_CAMERA = 108;
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

       getOrder_no();

        //Button OnClick Upload
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                  // getOrder_no();


                        Log.e("Boolean", String.valueOf(imageValid));
                        if (imageValid == true)
                        {

                            if (!ord_no.toString().equals(""))
                            {
                                try {

                                    Log.e("Order No upld Prescriptn",ord_no);
                                    savelocalImage(ord_no);
                                    UploadImageToFTP oUploadImage = new UploadImageToFTP();
                                    oUploadImage.execute();
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    String currentDateandTime = sdf.format(new Date());

                                    File delete_file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                                    delete_file.delete();

                                    oChat = new Chat();
                                    oChat.setVendorID(Integer.parseInt(chatVenID));
                                    oChat.setChatText("Prescription Upload");
                                    oChat.setDateTime("" + currentDateandTime);
                                    oChat.setVendorName("" + vendorName);
                                    oChat.setVendor_AREA("" + vendorArea);
                                    oChat.setChatType(3);
                                    oChat.setPrescriptionOrderNo(ord_no);
                                    Database db = new Database(UploadPrescriptionActivity.this);
                                    db.createChat(oChat);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(UploadPrescriptionActivity.this, "Could NOt Receive OrderNo! Please Upload Again. ", Toast.LENGTH_LONG).show();
                                    bitmap.recycle();
                                }
                            } else {
                                Toast.makeText(UploadPrescriptionActivity.this, "Could NOt Receive OrderNo! Please Upload Again. ", Toast.LENGTH_LONG).show();
                                bitmap.recycle();
                                bitmap = null;
                            }

                            imageValid = false;
                        }
                        else {

                            File delete_file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                            delete_file.delete();
                            String temp_img_path = Environment.getExternalStorageDirectory()+File.separator + "image.jpg";
                            Picasso.with(getApplicationContext()).load(new File(temp_img_path)).fit().into(img_view);

                            Toast.makeText(UploadPrescriptionActivity.this, "Please Choose Image !!", Toast.LENGTH_LONG).show();
                            imageValid = false;
                            bitmap = null;

                        }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Order_Options_Activity.class);
                startActivity(i);
                finish();
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        requestCode = 11;
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null)
        {
            Uri filePath = data.getData();
            image_name = getPath(filePath);

            System.out.println("Image: " + filePath.getPath());
            System.out.println("Image2: " + image_name);


            //GET Image Size to Compare
            File file = new File(image_name);
            String Image_name = file.getName();
            System.out.println("Image Name: " + Image_name);

            long length = file.length();
            long fileSizeInKB = length / 1024;
            System.out.println("Image size in kb: " + fileSizeInKB);
            long fileSizeInMB = fileSizeInKB / 1024;
            System.out.println("Image size in mb: " + fileSizeInMB);


            if (fileSizeInMB >= 2)
            {
                Toast.makeText(UploadPrescriptionActivity.this, "Size > 2MB. Upload Another File. ", Toast.LENGTH_LONG).show();
            }
            else
            {
                try
                {

                    //Getting the Bitmap from Gallery

                    bitmap = decodeSampledBitmapFromFile(image_name, 1000, 700);

                    //    bitmap = decodeSampledBitmapFromFile(filePath, 1000, 700);

                    //Setting the Bitmap to ImageView
                    Picasso.with(getApplicationContext()).load(new File(image_name)).fit().into(img_view);

                 //   getOrder_no();

                    imageValid = true;

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
        //if Image chosen from Camera
        else if (REQUEST_CAMERA == 108)
        {
            REQUEST_CAMERA = 0;

            try
            {
                //Get our saved file into a bitmap object:
                File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                if(file.exists())
                {
                    bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
                    byte[] imageInByte = bytes.toByteArray();
                    long lengthbmp = imageInByte.length;
                    long size_in_KB = lengthbmp / 1024;
                    long size_in_MB = size_in_KB / 1024;

                    System.out.println("Image Size in camera " + lengthbmp);
                    System.out.println("Image Size in KB " + size_in_KB);
                    System.out.println("Image Size in MB " + size_in_MB);
                    String temp_img_path = Environment.getExternalStorageDirectory()+File.separator + "image.jpg";

                    if (size_in_MB >= 2)
                    {
                        Toast.makeText(UploadPrescriptionActivity.this, "Size > 2MB. Upload Another File. ", Toast.LENGTH_LONG).show();
                        File delete_file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                        delete_file.delete();
                    }
                    else
                    {
                        //picaso
                        Picasso.with(getApplicationContext()).load(new File(temp_img_path)).fit().into(img_view);

                        imageValid = true;

                     //   getOrder_no();

                    }
                }
            }
            catch (Exception e)
            {
                System.out.println("Error in Upload Camera" +e);
                Log.e("Upload exception ",e.toString() );
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }


    //Get Image Name
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }



    private Void getOrder_no() {
        //Showing the progress dialog

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Getting response from server
                        sharedpreferences = getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);
                        System.out.println(s);
                        String line = s.replaceAll("\"", "");
                        String[] splitted = line.split(",");

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        //"'ORDER_NO:'SM2016225,"
                        editor.putString(Constatnts.ORDER_NO, splitted[0].replace("ORDER_NO:", ""));
                        editor.putString(Constatnts.ORDER_TYPE_ID, splitted[1].replace("ORDER_TYPE_ID:", ""));

                        ord_no = splitted[0].replace("ORDER_NO:", "");
                        Order_type_id_received = splitted[1].replace("ORDER_TYPE_ID:", "");

                        editor.commit();

                        if(ord_no.toString().equals(""))
                        {
                            Toast.makeText(UploadPrescriptionActivity.this, "Could Not Receive OrderNo From Server! Please try Again. ", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(UploadPrescriptionActivity.this, UploadPrescriptionActivity .class);
                            startActivity(i);
                            finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(volleyError instanceof NoConnectionError)
                        {
                            Toast.makeText(UploadPrescriptionActivity.this, "No Internet Connection. Please try again !!", Toast.LENGTH_LONG).show();
                          /*  Intent i = new Intent(getApplicationContext(), UploadPrescriptionActivity.class);
                            startActivity(i);
                            finish();*/
                        }
                        else {
                            //Showing toast volleyError.getMessage().toString()
                            Toast.makeText(UploadPrescriptionActivity.this, "Error UPLOADING IMAGE !!!" + volleyError, Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //String image = getStringImage(bitmap);

                //Getting Image Name
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("KEY_IMAGE", "");
                params.put("KEY_NAME", "");
                params.put("CUSTOMER_ID", custID);
                params.put("VENDOR_ID", VenID);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new RetryPolicy() {
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
                Toast.makeText(UploadPrescriptionActivity.this, "Timeout Error !!!", Toast.LENGTH_LONG).show();
            }
        });

        return null;
    }

    public void savelocalImage(String Order_no1) {
        File filepath = Environment.getExternalStorageDirectory();

        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath() + "/Download/Customer Docs/");
        dir.mkdirs();

        realPath = filepath.getAbsolutePath() + "/Download/Customer Docs/";

        // Create a name for the saved image
        File image_temp = new File(dir, Order_no1 + ".jpeg");

        try {

            output = new FileOutputStream(image_temp);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //FTP Upload Image Test
    public class UploadImageToFTP extends AsyncTask<String, Integer, String> {
        String mTAG = "UploadImageToFTP";
        String result = "";

        final ProgressDialog ringProgressDialog = ProgressDialog.show(UploadPrescriptionActivity.this, "Uploading Image...","Please Wait ...", true);

        @Override
        protected String doInBackground(String... arg) {
            Log.d(mTAG, "Just started doing stuff in asynctask");
            ProgressDialog dialog;
            try {

                if(imageValid == true)
                {

                    String filePathImage = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Download/Customer Docs/" + ord_no + ".jpeg";

                    FTPClient oFTP = new FTPClient();

                    try {

                        //Connect TO FTP Location
                        oFTP.connect("54.169.227.89");

                        oFTP.login("FTP_SearchMeds", "Search_Meds9999");

                        //Change to a new working Directory
                        File Checkfile = new File(filePathImage);


                        //Upload Files
                        if (!Checkfile.exists())
                        {
                            Toast.makeText(UploadPrescriptionActivity.this, "The Image is Not Set", Toast.LENGTH_LONG).show();
                            ringProgressDialog.dismiss();
                        }
                        else
                        {
                            File oFileToUpload = new File(filePathImage);
                            oFTP.upload(oFileToUpload);
                            // Close Connect
                            oFTP.disconnect(true);

                            sendOrderNo(UploadPrescriptionActivity.this);

                            ringProgressDialog.dismiss();

                            Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                            startActivity(i);
                            finish();

                        }
                    }
                    catch (FTPException e)
                    {
                        Toast.makeText(UploadPrescriptionActivity.this, "Error in FTP Exception !!!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        ringProgressDialog.dismiss();
                       /* Intent i = new Intent(getApplicationContext(), UploadPrescriptionActivity.class);
                        startActivity(i);
                        finish();*/
                    }
                    catch (FTPIllegalReplyException e)
                    {
                        Toast.makeText(UploadPrescriptionActivity.this, "FTP illegal Reply !!!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        ringProgressDialog.dismiss();
                       /* Intent i = new Intent(getApplicationContext(), UploadPrescriptionActivity.class);
                        startActivity(i);
                        finish();*/
                    }
                    catch (FTPDataTransferException e)
                    {
                        Toast.makeText(UploadPrescriptionActivity.this, "FTP DataTransfer Exception !!!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        ringProgressDialog.dismiss();
                      /*  Intent i = new Intent(getApplicationContext(), UploadPrescriptionActivity.class);
                        startActivity(i);
                        finish();*/
                    }
                    catch (FTPAbortedException e)
                    {
                        Toast.makeText(UploadPrescriptionActivity.this, "Ftp Abort Error !!!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        ringProgressDialog.dismiss();
                        /*Intent i = new Intent(getApplicationContext(), UploadPrescriptionActivity.class);
                        startActivity(i);
                        finish();*/
                    }

                    result = "";
                    ringProgressDialog.dismiss();
                }
                else {
                    //Toast.makeText(UploadPrescriptionActivity.this, "Please choose image.. ", Toast.LENGTH_LONG).show();
                    System.out.println("Please choose image..");
                }
            }
            catch (IOException e)
            {
                ringProgressDialog.dismiss();
                e.printStackTrace();
            }

            ringProgressDialog.dismiss();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("onPostExecute upload image",result.toString());
            Toast.makeText(UploadPrescriptionActivity.this, "Image Upload Complete", Toast.LENGTH_LONG).show();
            ringProgressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(Integer... a) {
            Log.d(mTAG, "Progress is " + a[0] + " % done.");


        }
    }

    private void sendOrderNo(final Context context) {
        createChat();
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest sr = new StringRequest(Request.Method.POST, Constatnts.postChatUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //"GCM Response,Error=NotRegistered (Vendor not Registered gcm id not found)"
                Toast.makeText(UploadPrescriptionActivity.this, "message SENT", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UploadPrescriptionActivity.this, "INVALID " + error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("USER_ID", userID);
                params.put("CUSTOMER_ID", custID);
                params.put("VENDOR_ID", VenID);
                params.put("VENDOR_NAME", vendorName);
                params.put("CHAT_TYPE_ID", "1");
                params.put("CHAT_MESSAGE", createChat());

                Log.e("Upload prescription",params.toString());

                return params;
            }
        };
        queue.add(sr);
    }


    private String createChat() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        final JSONObject contact = new JSONObject();
        try {
            /*contact.put("vendor_id", VenID);
            contact.put("vendor_type", catID);
            contact.put("vendor_name", vendorName);
            contact.put("date_time", currentDateandTime);
            contact.put("message",userName+ " " + "Prescription");
            contact.put("chat_type", "3");
            contact.put("cust_id", custID);
            contact.put("cust_name", userName);
            contact.put("full_name", FullName);
            contact.put("Address", vendorArea);
            contact.put("OrderNO", ord_no);
            contact.put("Order_Type_ID", Order_type_id_received);*/
            //ContactArray.put(i, contact);

            //change 12.5.2016
            contact.put("vendorID", VenID);
            contact.put("vendor_Type", catID);
            contact.put("vendor_name", vendorName);
            contact.put("vendor_sendDate_time", currentDateandTime); //date_time
            contact.put("vendor_chatMesg",userName+ " " + "Prescription");
            contact.put("chat_type", "3");
            contact.put("cust_id", custID);
            contact.put("cust_name", userName);
            contact.put("full_name", FullName);
            contact.put("Address", vendorArea);
            contact.put("OrderNO", ord_no);
            contact.put("Order_Type_ID", Order_type_id_received);


            System.out.println("Chat data" + contact.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contact.toString();
    }

}