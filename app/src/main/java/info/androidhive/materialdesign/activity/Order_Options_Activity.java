package info.androidhive.materialdesign.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import info.androidhive.materialdesign.R;

public class Order_Options_Activity extends AppCompatActivity {

    //Declarations
    Button prescription,cosmetics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__options_);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar
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

        //Initialization
        prescription = (Button) findViewById(R.id.btn_Prescription);
        cosmetics = (Button) findViewById(R.id.btn_Cosmetics);

        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UploadPrescriptionActivity.class);
                startActivity(i);
                finish();

            }
        });

        cosmetics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

}
