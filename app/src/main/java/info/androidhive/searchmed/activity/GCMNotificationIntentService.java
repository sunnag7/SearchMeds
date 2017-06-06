package info.androidhive.searchmed.activity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.searchmed.R;
import info.androidhive.searchmed.database.Database;
import info.androidhive.searchmed.model.Chat;
import info.androidhive.searchmed.model.Constatnts;
import info.androidhive.searchmed.model.Notifications;
import info.androidhive.searchmed.model.Order;

public class GCMNotificationIntentService extends IntentService {

	public static int NOTIFICATION_ID = 1;
	public static int NOTIFICATION_ID_ = 0;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	boolean notificationType= false;

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";

	public String chatType;

	@Override
	protected void onHandleIntent(Intent intent)
	{
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		String msg = intent.getStringExtra("message");
		String time = intent.getStringExtra("time");

		Constatnts.NOTIFYMSG = msg;
		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {


				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				sendNotification(msg);
				updateMyActivity(this,msg );

				Log.i(TAG, "Received: " + extras.toString());
			}
		}

		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg)
	{
		Database db = new Database(this);
		Chat mChat = new Chat();
		JSONArray orderArray;
		JSONObject c = null;
		Notifications notifications = new Notifications();

		try
		{
			c = new JSONObject(msg);

			chatType = c.getInt("vendor_chat_type") + "";
			//OrderStatus_ID = c.getString("Order_Status_Id").toString();

			//Received text chat msg from vendor
			if (chatType.equals("2"))
			{
				mChat.setVendorID(Integer.parseInt(c.getString("vendorID")));
				mChat.setVendorName(c.getString("vendor_Name"));
				mChat.setChatText("" + c.getString("vendor_chatMesg"));
				mChat.setDateTime("" + c.getString("vendor_sendDate_time"));
				mChat.setVendorType(Integer.parseInt(c.getString("vendor_Type")));
				mChat.setChatType(c.getInt("vendor_chat_type"));
				mChat.setChatID_Server(c.getString("chat_id"));

				db.createChat(mChat);
			}

			//Received bill
			else if(chatType.contains("0"))
			{
				mChat.setVendorID(Integer.parseInt(c.getString("vendorID")));
				mChat.setVendorName(c.getString("vendor_Name"));
				mChat.setChatText("" + c.getString("vendor_chatMesg"));
				mChat.setDateTime("" + c.getString("vendor_sendDate_time"));
				mChat.setVendorType(Integer.parseInt(c.getString("vendor_Type")));
				mChat.setChatType(c.getInt("vendor_chat_type"));


				System.out.println("Chat from vendor :" + mChat.toString());

				long chID = db.createChat(mChat);
				orderArray = c.getJSONArray("OrderDetails");

				if (orderArray.length()>0)
				{
					JSONObject mObj = orderArray.getJSONObject(0);
					Order mOrder = new Order();

					mOrder.setDeliveryAmnt(Double.parseDouble(mObj.getString("Order_Delivery_Charges")));
					mOrder.setOrderAmount(Double.parseDouble(mObj.getString("Order_Amount")));
					mOrder.setTotalOrdAmnt(Double.parseDouble(mObj.getString("Order_Bill_Amount")));
					mOrder.setBillNo(mObj.getString("Order_BillNO"));
					mOrder.setPaymentRequestNo(mObj.getString("Order_No"));
					mOrder.setOrderStateConfirmation(0);
					mOrder.setChatID((int) chID);
					mOrder.setVendorID(Integer.parseInt(c.getString("vendorID")));

					Constatnts.OrderNo_FromVender = mObj.getString("Order_No");

					//for confirm status
					mOrder.setOrdConfirmState(0);
					db.createOrder(mOrder);

					System.out.println("Bill from vendor :" + mOrder.toString());
				}
			}

			// out for delivery ORDER_STATUS_ID = 3
			else if(chatType.contains("5"))
			{
				mChat.setVendorID(Integer.parseInt(c.getString("vendorID")));
				mChat.setVendorName(c.getString("vendor_Name"));
				mChat.setChatText(c.getString("OrderNo"));
				mChat.setChatType(c.getInt("vendor_chat_type"));
				mChat.setVendorType(Integer.parseInt(c.getString("vendor_Type")));
				mChat.setDateTime(c.getString("date_time"));
				db.createChat(mChat);
			}

			// delivered successfully  delivered = 4
			else if(chatType.contains("6"))
			{
				//{'vendor_chat_type':'6','vendorID':'1734','vendor_Name':'prachi k','vendor_type':'3','OrderNo':'ORDERNO:SM20164920,Order delivered','Order_Status_Id':'4'}
				mChat.setVendorID(Integer.parseInt(c.getString("vendorID")));
				mChat.setVendorName(c.getString("vendor_Name"));
				mChat.setChatText(c.getString("OrderNo"));
				mChat.setChatType(c.getInt("vendor_chat_type"));
				mChat.setVendorType(Integer.parseInt(c.getString("vendor_type")));
				mChat.setDateTime(c.getString("date_time"));
				db.createChat(mChat);
				System.out.println("GCMNOTIFICATIONINTENT: " +c.getString("OrderNo"));
			}
			//Received Notification from vendor
			else if(chatType.contains("7"))
			{
				notifications.setNotificationChatTpe(c.getString("vendor_chat_type"));
				notifications.setNotificationVendorId(c.getString("vendorID"));
				notifications.setNotificationVendorType(c.getString("vendorType"));
				notifications.setNotificationVendorName(c.getString("vendor_Name"));
				notifications.setNotificationDate(c.getString("Date"));
				notifications.setNotificationTime(c.getString("Time"));
				notifications.setNotificationText(c.getString("Notification_Text"));
				notifications.setNotificationCity(c.getString("vendor_City_Name"));
				db.createNotifications(notifications);

				Constatnts.CheckNotif = true;
			}
			//Received msg from Panel
			else if(chatType.contains("9"))
			{
				mChat.setVendorID(Integer.parseInt(c.getString("vendor_id")));
				mChat.setVendorName(c.getString("vendor_name"));
				mChat.setChatText("" + c.getString("message"));
				mChat.setDateTime("" + c.getString("date_time"));
				mChat.setVendorType(Integer.parseInt(c.getString("vendor_type")));
				mChat.setChatType(c.getInt("vendor_chat_type"));
				mChat.setChatType(1);

				db.createChat(mChat);
			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		Log.d(TAG, "Preparing to send notification...: " + msg);
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, "Umbrella Notification", when);

		NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notify);
		contentView.setImageViewResource(R.id.image, R.drawable.ic_launcher);

		if(chatType.equals("2"))
		{
			// received text mesg from vendor
			contentView.setTextViewText(R.id.title, "Vendor "+mChat.getVendorName());
			contentView.setTextViewText(R.id.text,"" +mChat.getChatText().toString());
		}
		else if(chatType.equals("0"))
		{
			//received bill from vendor
			contentView.setTextViewText(R.id.title, "You have received Bill from " + mChat.getVendorName());
		}
		// out for delivery ORDER_STATUS_ID = 3 , delivered = 4;
		else if(chatType.equals("5")){

			contentView.setTextViewText(R.id.title, "Vendor "+mChat.getVendorName());
			contentView.setTextViewText(R.id.text,"" +mChat.getChatText().toString());
		}
		else if(chatType.equals("6")){

			contentView.setTextViewText(R.id.title, "Vendor "+mChat.getVendorName());
			System.out.println("GCMNOTIFICATIONINTENT: CHAT_TYPE 6 " +mChat.getChatText().toString());
			contentView.setTextViewText(R.id.text, "" + mChat.getChatText().toString());
		}

		else if(chatType.equals("7"))
		{

			contentView.setTextViewText(R.id.title, "Notifications ");
			contentView.setTextViewText(R.id.text, notifications.getNotificationText());

		}
		/*else if(chatType.equals("9"))
		{
			contentView.setTextViewText(R.id.title, "Vendor "+mChat.getVendorName());
			contentView.setTextViewText(R.id.text,"" +mChat.getChatText().toString());
		}*/


		if(chatType.equals("7"))
		{
			notification.contentView = contentView;

		/*	PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, NotificationsActivity.class).putExtra("json_msg",msg).
							setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
									|Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_UPDATE_CURRENT);

			notification.contentIntent = contentIntent;*/

			Intent notificationIntent = new Intent(this, MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent.putExtra("json_msg",msg),0);
			/*PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent.putExtra("json_msg",msg).
					setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_UPDATE_CURRENT);*/

			notification.contentIntent = contentIntent;

			/*FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.containerView,new NotificationsFragment()).commit();*/

			// notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
			notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
			notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
			notification.defaults |= Notification.DEFAULT_SOUND; // Sound
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			mNotificationManager.notify(NOTIFICATION_ID_, notification);

		}
		else
		{
			if(chatType.equals("9"))
			{

			}
			else
			{
				notification.contentView = contentView;
				Constatnts.RECEIVED_NEW_MSG = true;

				PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ChatActivity.class).putExtra("json_msg", msg).
						setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_UPDATE_CURRENT);
				notification.contentIntent = contentIntent;

				// notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
				notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
				notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
				notification.defaults |= Notification.DEFAULT_SOUND; // Sound
				notification.flags |= Notification.FLAG_AUTO_CANCEL;

				mNotificationManager.notify(NOTIFICATION_ID, notification);
			}



		}
	}

	static void updateMyActivity(Context context, String message) {

		Intent broadcast = new Intent();
		broadcast.setAction("MyBroadcast").putExtra("message", message);
		context.sendBroadcast(broadcast);

	}
}