package info.androidhive.materialdesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.model.Notifications;

public class CardAdapterNotifications extends RecyclerView.Adapter<CardAdapterNotifications.ViewHolder>   {

    ArrayList<Notifications> aNotificationsArr;
    Context aContext;

    public CardAdapterNotifications(Context mContext, ArrayList<Notifications> mNotificationsArr) {
        super();
        aNotificationsArr = new  ArrayList<Notifications>();
        aContext = mContext;
        aNotificationsArr = mNotificationsArr;


        for(int i=0;i<aNotificationsArr.size();i++)
        {
            Notifications items = new Notifications();

            items.setNotificationDate(aNotificationsArr.get(i).getNotificationDate());
            items.setNotificationTime(aNotificationsArr.get(i).getNotificationTime());
            items.setNotificationText(aNotificationsArr.get(i).getNotificationText());
            items.setNotificationVendorName(aNotificationsArr.get(i).getNotificationVendorName());
            items.setNotificationVendorId(aNotificationsArr.get(i).getNotificationVendorId());
            items.setNotificationVendorType(aNotificationsArr.get(i).getNotificationVendorType());
            items.setNotificationCity(aNotificationsArr.get(i).getNotificationCity());
            items.setNotificationChatTpe(aNotificationsArr.get(i).getNotificationChatTpe());

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_notifications, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Notifications items = aNotificationsArr.get(i);

        viewHolder.txtV_notificationDate.setText(items.getNotificationDate());
        viewHolder.txtV_notificationTime.setText(items.getNotificationTime());
        viewHolder.txtV_notificationMesg.setText(items.getNotificationText());
        viewHolder.txtV_NotificationVendorName.setText(items.getNotificationVendorName());
    }

    @Override
    public int getItemCount() {
        return aNotificationsArr.size();
    }

    public long getItemId(int position) {
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtV_notificationDate,txtV_notificationTime,txtV_NotificationVendorName,txtV_notificationMesg;

        public ViewHolder(View itemView) {
            super(itemView);

            txtV_notificationDate = (TextView) itemView.findViewById(R.id.txtV_notificationDate);
            txtV_notificationTime = (TextView) itemView.findViewById(R.id.txtV_notificationTime);
            txtV_notificationMesg = (TextView) itemView.findViewById(R.id.txtV_notificationsMesg);
            txtV_NotificationVendorName = (TextView) itemView.findViewById(R.id.txtV_NotificationVendorName);

            txtV_notificationDate.setTypeface(EasyFonts.caviarDreams(aContext));
            txtV_notificationTime.setTypeface(EasyFonts.caviarDreams(aContext));
            txtV_notificationMesg.setTypeface(EasyFonts.caviarDreams(aContext));
            txtV_NotificationVendorName.setTypeface(EasyFonts.caviarDreams(aContext));

        }
    }
}
