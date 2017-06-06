package info.androidhive.searchmed.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.nio.channels.FileChannel;

import info.androidhive.searchmed.R;

public class SplashscreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);
        checkAndPass();
        exportDB();

    }

    boolean isConn = false;
    public boolean checkAndPass()
    {

        if (isInternetAvailable() || isNetworkConnected())
        {
            isConn = true;

            Thread timerThread = new Thread()
            {
                public void run()
                {
                    try
                    {
                        sleep(3000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        Intent intent = new Intent(SplashscreenActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timerThread.start();
        }
        else
        {
            showAlertDialog() ;
            isConn = false;
        }

        return  isConn;
    }

    public static boolean isInternetAvailable()
    {
        try
        {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            if (ipAddr.equals(""))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private  boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        }
        else
            return true;
    }


    AlertDialog alert;
    public void showAlertDialog()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SplashscreenActivity.this,R.style.AppCompatAlertDialog);
        builder1.setMessage("Internet not available");
        builder1.setTitle("Connect Now?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Intent intent=new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });
        builder1.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                dialog.cancel();
            }
        });
        builder1.setNeutralButton("Check Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                checkAndPass();
                dialog.cancel();
            }
        });
        alert = builder1.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (alert!=null)
        {
            alert.dismiss();
        }
        checkAndPass();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume()
    {

        checkAndPass();
        super.onResume();
    }

    public void exportDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "info.androidhive.materialdesign"
                        + "//databases//" + "searchMeds.sqlite";
                String backupDBPath = "/searchMeds.sqlite";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                // Toast.makeText(getActivity(), backupDB.toString(),Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //  Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}