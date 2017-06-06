package info.androidhive.materialdesign.model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import java.net.InetAddress;

import info.androidhive.materialdesign.R;

/**
 * Created by Administrator on 02-05-2016.
 */
public class ConnectivityCheck {

    public boolean isInternetAvailable(Context context) {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNetworkConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    public void showAlertDialog(final Context context) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AppCompatAlertDialog);
        builder1.setMessage("Internet not available");
        builder1.setTitle("Connect Now?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
                dialog.cancel();
            }
        });

        builder1.setNeutralButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder1.create();
        alert.show();
    }
}
