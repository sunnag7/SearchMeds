package info.androidhive.materialdesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.model.PendingOrder;

public class CardAdapterOrders extends RecyclerView.Adapter<CardAdapterOrders.ViewHolder>  {

    List<PendingOrder> aPendingOrders_Arr;
    Context aContext;
    String currentData;

    public CardAdapterOrders(Context mContext, ArrayList<PendingOrder> mPendingOrders_Arr) {
        super();
        aPendingOrders_Arr = new ArrayList<PendingOrder>();
        aContext = mContext;
        aPendingOrders_Arr = mPendingOrders_Arr;

        for(int i=0;i<aPendingOrders_Arr.size();i++)
        {
            PendingOrder items = new PendingOrder();

            items.setOrder_delivery_amount(aPendingOrders_Arr.get(i).getOrder_delivery_amount());
            items.setOrder_no(aPendingOrders_Arr.get(i).getOrder_no());
            items.setOrder_status_id(aPendingOrders_Arr.get(i).getOrder_status_id());

            items.setOrder_vendor_amount(aPendingOrders_Arr.get(i).getOrder_vendor_amount());
            items.setOrder_date(aPendingOrders_Arr.get(i).getOrder_date());

            items.setVendor_id(aPendingOrders_Arr.get(i).getVendor_id());
            items.setOrder_amount(aPendingOrders_Arr.get(i).getOrder_amount());
            items.setOrder_Status(aPendingOrders_Arr.get(i).getOrder_Status());

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_my_orders, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        PendingOrder items = aPendingOrders_Arr.get(i);

        viewHolder.txtV_OrderNo.setText(items.getOrder_no());
        viewHolder.txtV_OrderDate.setText(items.getOrder_date());
        viewHolder.txtV_BillAmount.setText(items.getOrder_amount()); //Amount
        viewHolder.txtV_DeliveryCharges.setText(items.getOrder_delivery_amount());
        viewHolder.txtV_OrderAmount.setText(items.getOrder_vendor_amount());  // Total Amount

        Log.e("Date", items.getOrder_date());


       /* String date = items.getOrder_date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(date);
        viewHolder.txtV_OrderDate.setText(formattedDate);*/

        //DOCTORS LABS PHARMACY

        if(items.getVendor_Type_Name().equals("PHARMACY") ){
            viewHolder.imageV_VendorType.setImageResource(R.drawable.pharmacy_new);
        }
        else if(items.getVendor_Type_Name().equals("DOCTORS")){
            viewHolder.imageV_VendorType.setImageResource(R.drawable.docto_new);
        }
        else if(items.getVendor_Type_Name().equals("LABS")){
            viewHolder.imageV_VendorType.setImageResource(R.drawable.lab_new);
        }

        viewHolder.txtV_VendorName.setText(items.getVendor_Name());

        // tracking customer pending order status
        // proccessing= 2, out for delivery = 3, delivered =4
        if(items.getOrder_status_id().equals("2"))
        {
            viewHolder.imageV_processing.setImageResource(R.drawable.processing_fill);
            viewHolder.imageV_outForDelivery.setImageResource(R.drawable.out_for_delivery);
            viewHolder.imageV_delivered.setImageResource(R.drawable.delivered);

           // viewHolder.imageV_processing.setBackgroundResource(R.drawable.processing_fill);
        }
         else if(items.getOrder_status_id().equals("3"))
        {
            viewHolder.imageV_processing.setImageResource(R.drawable.processing_fill);
            viewHolder.imageV_outForDelivery.setImageResource(R.drawable.out_for_delivery_fill);
            viewHolder.imageV_delivered.setImageResource(R.drawable.delivered);
        }
        else if(items.getOrder_status_id().equals("4"))
        {
            viewHolder.imageV_processing.setImageResource(R.drawable.processing_fill);
            viewHolder.imageV_outForDelivery.setImageResource(R.drawable.out_for_delivery_fill);
            viewHolder.imageV_delivered.setImageResource(R.drawable.delivered_fill);
        }
        else if(items.getOrder_status_id().equals("5")){

            viewHolder.rel_trackingStatus_cancelled.setVisibility(View.VISIBLE);
            viewHolder.rel_trackingStatus.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount()
    {
        return aPendingOrders_Arr.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtV_OrderNo,txtV_VendorName,txtV_VendorType,txtV_OrderDate,txtV_BillAmount,txtV_DeliveryCharges,txtV_OrderAmount,txtV_OrderStatus;
        ImageView imageV_processing,imageV_outForDelivery,imageV_delivered,imageV_VendorType;
        RelativeLayout rel_trackingStatus_cancelled,rel_trackingStatus;


        public ViewHolder(View itemView) {
            super(itemView);

            txtV_OrderNo = (TextView)itemView.findViewById(R.id.TxtV_OrderNo);
            txtV_OrderDate = (TextView)itemView.findViewById(R.id.TxtV_OrderDate);
            txtV_BillAmount = (TextView)itemView.findViewById(R.id.TxtV_BillAmount);
            txtV_DeliveryCharges = (TextView)itemView.findViewById(R.id.TxtV_DeliveryCharges);
            txtV_OrderAmount = (TextView)itemView.findViewById(R.id.TxtV_OrderAmount);

            // tracking order status
            imageV_processing = (ImageView) itemView.findViewById(R.id.imageV_processing);
            imageV_outForDelivery = (ImageView) itemView.findViewById(R.id.imageV_outFordelivery);
            imageV_delivered = (ImageView) itemView.findViewById(R.id.imageV_delivered);


            txtV_VendorName = (TextView)itemView.findViewById(R.id.TxtV_VendorName);
            imageV_VendorType = (ImageView)itemView.findViewById(R.id.imageV_venderType);

            rel_trackingStatus_cancelled = (RelativeLayout) itemView.findViewById(R.id.rel_trackingStatus_cancelled);
            rel_trackingStatus = (RelativeLayout) itemView.findViewById(R.id.rel_trackingStatus);

        }
    }
}