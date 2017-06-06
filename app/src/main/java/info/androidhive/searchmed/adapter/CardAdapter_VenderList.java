package info.androidhive.searchmed.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vstechlab.easyfonts.EasyFonts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.searchmed.R;
import info.androidhive.searchmed.activity.Order_Options_Activity;
import info.androidhive.searchmed.database.Database;
import info.androidhive.searchmed.model.Constatnts;
import info.androidhive.searchmed.model.Vendor;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class CardAdapter_VenderList  extends RecyclerView.Adapter<CardAdapter_VenderList.ViewHolder>  /*implements Filterable */{
    ArrayList<Vendor> aVenddArr;
    ArrayList<Vendor> permVenrArr;
    ArrayList<Vendor> dbVenArr;
    Context aContext;
    Database db = null;
    SharedPreferences sharedpreferences;
    String vendorType,favourite_vendorId;
    boolean favStatus = false;
    String venid;
    String image1;
    String vendor_id;

    boolean downloaded = false;



    public CardAdapter_VenderList(Context mContext, ArrayList<Vendor> mVenArr) {
        super();
        aVenddArr = new ArrayList<Vendor>();
        aContext = mContext;
        aVenddArr = mVenArr;
        permVenrArr = mVenArr;
        dbVenArr = new ArrayList<Vendor>();


        db = new Database(aContext);
    //    db.checkDuplicate_favouritelist();
        //dbVenArr = (ArrayList<Vendor>) db.getAllFavVendors();

        for (int i = 0; i < aVenddArr.size(); i++) {

            Vendor items = new Vendor();

            items.setVENDORNAME(aVenddArr.get(i).getVENDORNAME());
            items.setVENDORMOBILE(aVenddArr.get(i).getVENDORMOBILE());
            items.setVENDORID(aVenddArr.get(i).getVENDORID());
            items.setVENDORLANDLINE(aVenddArr.get(i).getVENDORLANDLINE());
            items.setVENDORCONTACTNAME(aVenddArr.get(i).getVENDORCONTACTNAME());
            items.setCITY(aVenddArr.get(i).getCITY());
            items.setVENDORADDRESS(aVenddArr.get(i).getVENDORADDRESS());
            items.setAREAID(aVenddArr.get(i).getAREAID());
            items.setSTATE(aVenddArr.get(i).getSTATE());
            items.setAREANAME(aVenddArr.get(i).getAREANAME());
            items.setVEN_TYPE(aVenddArr.get(i).getVEN_TYPE());
            items.setAREAID(aVenddArr.get(i).getAREAID());
            items.setVEN_DELSTATE(aVenddArr.get(i).getVEN_DELSTATE());
            items.setVENDOREMAIL(aVenddArr.get(i).getVENDOREMAIL());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_vendor_list, viewGroup, false);

        sharedpreferences = aContext.getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);

        vendorType = Constatnts.VendorTypeCat;
        System.out.println("Vendor type Id" + vendorType);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Animation animationFadeOut = AnimationUtils.loadAnimation(aContext, R.anim.fadeout);
        final Vendor items = aVenddArr.get(i);
        viewHolder.txtV_vendorName.setText(items.getVENDORNAME());

        viewHolder.txtV_fromTime.setText(items.getFROM_TIME());
        viewHolder.txtV_toTime.setText(items.getTO_TIME());
        viewHolder.txtV_offDays.setText(items.getOFF_DAY());

        dbVenArr = db.getAllFavourite_Vendors();

        if(dbVenArr.size() != 0)
        {
            for (int j = 0; j < dbVenArr.size(); j++)
            {
                if (dbVenArr.get(j).getVENDORID().equals(items.getVENDORID()))
                {
                    viewHolder.Product_imageFav.setImageResource(R.drawable.heart);
                    viewHolder.Product_imageFav.startAnimation(animationFadeOut);

                }
                else
                {
                    viewHolder.Product_imageFav.setImageResource(R.drawable.heart_);
                    viewHolder.Product_imageFav.startAnimation(animationFadeOut);
                }

            } //End For loop


        }

        //Favourites Click !!


        viewHolder.Product_imageFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                boolean isFound = true;

                if(dbVenArr.size() != 0)
                {
                    for (int j = 0; j < dbVenArr.size(); j++)
                    {
                        if (dbVenArr.get(j).getVENDORID().equals(items.getVENDORID()))
                        {
                            isFound = true;
                            viewHolder.Product_imageFav.setImageResource(R.drawable.heart);
                            viewHolder.Product_imageFav.startAnimation(animationFadeOut);
                            Toast.makeText(aContext, items.getVENDORNAME() + " Already Added to Favourite !!", Toast.LENGTH_LONG).show();
                            break;

                        }

                        isFound = false;
                    } //End For loop


                    if(isFound == false)
                    {

                        dbVenArr.contains(items.getVENDORID());

                        isFound = true;
                        db.createFav(items);
                            // posting favourite vendors on server
                        favourite_vendorId = items.getVENDORID();
                        postCustomerFavourite_vendors(aContext);

                        Toast.makeText(aContext, items.getVENDORNAME() + " Added to favorite ", Toast.LENGTH_LONG).show();

                        viewHolder.Product_imageFav.setImageResource(R.drawable.heart);
                        viewHolder.Product_imageFav.startAnimation(animationFadeOut);

                    }

                }
                else
                {
                    db.createFav(items);
                    // posting favourite vendors on server
                    favourite_vendorId = items.getVENDORID();
                    postCustomerFavourite_vendors(aContext);

                    Toast.makeText(aContext, items.getVENDORNAME() + " Added to favorite ", Toast.LENGTH_LONG).show();

                    viewHolder.Product_imageFav.setImageResource(R.drawable.heart);

                    viewHolder.Product_imageFav.startAnimation(animationFadeOut);
                }


            }
        });



        viewHolder.venCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentJump(items);
            }
        });


        // show image of the vendor shop
        viewHolder.Product_imageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vendor_id = items.getVENDORID();

                if(downloaded == false)
                {
                    DownloadImageFromFTP oDownloadImage = new DownloadImageFromFTP();
                    oDownloadImage.execute();

                    String fetchImage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/Vendor Profile/" + vendor_id + "_1" + ".jpeg";

                    File checkImage = new File(fetchImage);
                    if(checkImage.exists())
                    {
                        //Picasso.with(aContext).load(new File(fetchImage)).fit().into(viewHolder.Product_imageImageView);
                        Glide.with(aContext).load(fetchImage)
                                .thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(viewHolder.Product_imageImageView);

                      Log.e("Image Set","prachi");
                    }
                    else
                    {
                        viewHolder.Product_imageImageView.setImageResource(R.drawable.no_image_);
                    }


                }
                else if(downloaded == true)
                {
                    String fetchImage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/Vendor Profile/" + vendor_id + "_1" + ".jpeg";

                    File checkImage = new File(fetchImage);
                    if(checkImage.exists())
                    {
                        //Picasso.with(aContext).load(new File(fetchImage)).fit().into(viewHolder.Product_imageImageView);
                        Glide.with(aContext).load(fetchImage)
                                .thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(viewHolder.Product_imageImageView);

                        Log.e("Image Set", "prachi1");
                    }
                    else
                    {
                        viewHolder.Product_imageImageView.setImageResource(R.drawable.no_image_);
                    }


                }

            }
        });

    }

    private void fragmentJump(Vendor mItemSelected)
    {
        Constatnts.VENDOR_ID = mItemSelected.getVENDORID();
        Constatnts.VENDOR_NAME = mItemSelected.getVENDORNAME();
        Constatnts.VENDOR_ADDRESS = mItemSelected.getVENDORADDRESS();

        System.out.println("Vendor Id in CardAdapter fragment jump " +mItemSelected.getVENDORID());

        Intent intent = new Intent(aContext, Order_Options_Activity.class);
        aContext.startActivity(intent);

    }


    @Override
    public int getItemCount() {
        return aVenddArr.size();
    }

    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtV_vendorName,txtV_address,txtV_fromTime,txtV_toTime,txtV_offDays;
        ImageView Product_imageImageView, Product_imageFav,imageViewVendorType;
        RelativeLayout venCard;
        public View view;


        public ViewHolder(View itemView) {
            super(itemView);
            txtV_vendorName = (TextView) itemView.findViewById(R.id.txtV_vendorName);
            txtV_address = (TextView) itemView.findViewById(R.id.txtV_address);
            txtV_fromTime = (TextView) itemView.findViewById(R.id.txtV_From_time);
            txtV_toTime = (TextView) itemView.findViewById(R.id.txtV_To_time);

            txtV_offDays = (TextView) itemView.findViewById(R.id.txtV_offDays);

            Product_imageImageView = (ImageView) itemView.findViewById(R.id.product_image_imageView);
            Product_imageFav = (ImageView) itemView.findViewById(R.id.imageView6);
            venCard = (RelativeLayout) itemView.findViewById(R.id.top_layout);
            imageViewVendorType = (ImageView) itemView.findViewById(R.id.imageViewVendorType);

            // txtV_vendorName.setTypeface(EasyFonts.caviarDreams(aContext));
            txtV_address.setTypeface(EasyFonts.caviarDreams(aContext));
            txtV_fromTime.setTypeface(EasyFonts.caviarDreams(aContext));
            txtV_toTime.setTypeface(EasyFonts.caviarDreams(aContext));
            txtV_offDays.setTypeface(EasyFonts.caviarDreams(aContext));

        }
    }

    public void postCustomerFavourite_vendors(Context context)
    {

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, Constatnts.postCustomerFavourite_vendors, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                //"SUCCESS:1,"

                sharedpreferences = aContext.getSharedPreferences(Constatnts.MyPREFERENCES, Context.MODE_PRIVATE);
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

                        //    Toast.makeText(aContext, "Vendors added to favourite list",Toast.LENGTH_LONG).show();

                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("Fav_Vendor exceptn",e.toString());
                    }
                }
                else
                {
                    //  ringProgressDialog1.dismiss();
                    Toast.makeText(aContext,"Vendors not added to favourite list",Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof NoConnectionError)
                {
                    Toast.makeText(aContext, "No Internet Connection. Please try again !!", Toast.LENGTH_LONG).show();
                }
                else {
                    System.out.println("Error response" + error.toString());
                    Toast.makeText(aContext, "Vendors not added to favourite", Toast.LENGTH_SHORT).show();
                }
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();

                params.put("VENDOR_ID",favourite_vendorId);
                params.put("CUSTOMER_ID",sharedpreferences.getString(Constatnts.CUSTOMER_ID,null));

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
                Toast.makeText(aContext, "Timeout Error !!", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(sr);
    }



    //Download Image
    public class DownloadImageFromFTP extends AsyncTask<String, Integer, String>
    {

        String mTAG = "DownloadImageFromFTP";
        String result = "";


        @Override
        protected String doInBackground(String... arg) {
            Log.d(mTAG, "Just started doing stuff in asynctask");

            try
            {
                String filePathImage = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Download/Vendor Profile/" + vendor_id + "_1" + ".jpeg";

                File filepath = Environment.getExternalStorageDirectory();

                // Create a new folder in SD Card
                File dir = new File(filepath.getAbsolutePath() + "/Download/Vendor Profile/");
                dir.mkdir();
                System.out.println("Vendor Id in CardAdapter Vendor List " +vendor_id );


                FTPClient oFTP = new FTPClient();

                try {

                    //Connect TO FTP Location
                    oFTP.connect("54.169.227.89");

                    oFTP.login("FTP_SearchMeds", "Search_Meds9999");

                    File oFileToDownload = new File(filePathImage);

                    oFTP.download(vendor_id + "_1.jpeg", oFileToDownload);

                    // Close Connect
                    oFTP.disconnect(true);


                }
                catch (FTPException e)
                {
                    e.printStackTrace();


                }
                catch (FTPIllegalReplyException e)
                {
                    e.printStackTrace();


                }
                catch (FTPDataTransferException e)
                {
                    e.printStackTrace();

                }
                catch (FTPAbortedException e)
                {
                    e.printStackTrace();


                }

                result = "";

            }

            catch (IOException e)
            {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {

         //   Toast.makeText(aContext, "Completed Downloading Images !", Toast.LENGTH_LONG).show();
            downloaded = true;

        }

        @Override
        protected void onProgressUpdate(Integer... a) {
            Log.d(mTAG, "Progress is " + a[0] + " % done.");

        }

    }

}
