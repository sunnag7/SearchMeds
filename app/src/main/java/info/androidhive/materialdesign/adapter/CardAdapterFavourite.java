package info.androidhive.materialdesign.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.activity.ChatActivity;
import info.androidhive.materialdesign.database.Database;
import info.androidhive.materialdesign.model.Constatnts;
import info.androidhive.materialdesign.model.Vendor;

public class CardAdapterFavourite extends RecyclerView.Adapter<CardAdapterFavourite.ViewHolder>{

    ArrayList<Vendor> aVenddArrFv, dbVenArrFv;
    Context aContext;
    Database db = null;

    public CardAdapterFavourite(Context mContext, ArrayList<Vendor> mVenArrFv) {
        super();
        aVenddArrFv = new ArrayList<Vendor>();
        aContext = mContext;
        aVenddArrFv = mVenArrFv;

  /*      db = new Database(aContext);
        db.checkDuplicate_favouritelist();*/



        for (int i = 0; i < aVenddArrFv.size(); i++) {

            Vendor items = new Vendor();

            items.setVENDORNAME(aVenddArrFv.get(i).getVENDORNAME());
            items.setVENDORMOBILE(aVenddArrFv.get(i).getVENDORMOBILE());
            items.setVENDORID(aVenddArrFv.get(i).getVENDORID());
            items.setVENDORLANDLINE(aVenddArrFv.get(i).getVENDORLANDLINE());
            items.setVENDORCONTACTNAME(aVenddArrFv.get(i).getVENDORCONTACTNAME());
            items.setCITY(aVenddArrFv.get(i).getCITY());
            items.setVENDORADDRESS(aVenddArrFv.get(i).getVENDORADDRESS());
            items.setAREAID(aVenddArrFv.get(i).getAREAID());
            items.setSTATE(aVenddArrFv.get(i).getSTATE());
            items.setAREANAME(aVenddArrFv.get(i).getAREANAME());
            items.setVEN_TYPE(aVenddArrFv.get(i).getVEN_TYPE());
            items.setAREAID(aVenddArrFv.get(i).getAREAID());
            items.setVEN_DELSTATE(aVenddArrFv.get(i).getVEN_DELSTATE());
            items.setVENDOREMAIL(aVenddArrFv.get(i).getVENDOREMAIL());

        }
        //prod_arr.add(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_vendor_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        final Vendor items = aVenddArrFv.get(i);

        viewHolder.txtV_vendorName.setText(items.getVENDORNAME());
        viewHolder.txtV_fromTime.setText(items.getFROM_TIME());
        viewHolder.txtV_toTime.setText(items.getTO_TIME());

        viewHolder.venCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentJump(items);
            }
        });


    }


    @Override
    public int getItemCount() {
        return aVenddArrFv.size();
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

            txtV_vendorName.setTypeface(EasyFonts.caviarDreams(aContext));
            txtV_address.setTypeface(EasyFonts.caviarDreams(aContext));
            txtV_fromTime.setTypeface(EasyFonts.caviarDreams(aContext));
            txtV_toTime.setTypeface(EasyFonts.caviarDreams(aContext));
            txtV_offDays.setTypeface(EasyFonts.caviarDreams(aContext));

        }
    }

    private void fragmentJump(Vendor mItemSelected) {

        Constatnts.VENDOR_ID = mItemSelected.getVENDORID();
        Constatnts.VENDOR_NAME = mItemSelected.getVENDORNAME();
        Constatnts.VENDOR_ADDRESS = mItemSelected.getVENDORADDRESS();

        Intent intent = new Intent(aContext, ChatActivity.class);
        aContext.startActivity(intent);

    }


}
