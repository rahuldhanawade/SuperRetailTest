package com.virash.superretail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class Homepage extends AppCompatActivity {
    CardView product_list,_Purchase,scan,Sales_,stock_,_Vendor,customer_,Product_;
    private static final int PERMISSION_REQUEST_CODE = 200;


    SharedPreferences login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        scan = findViewById(R.id.scan_);
        Sales_ = findViewById(R.id.Sales_);
        product_list = findViewById(R.id.product_list);
        stock_ = findViewById(R.id.stock_);
        //Product_ = findViewById(R.id.Product_);
        customer_ = findViewById(R.id.customer_);
        _Vendor = findViewById(R.id._Vendor);
        _Purchase = findViewById(R.id._Purchase);

        if(!checkPermission())
        {
            Toast.makeText(this, "Please request permission.", Toast.LENGTH_SHORT).show();
            requestPermission();
        }

        login = getSharedPreferences("LOGIN", MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        String comname;
        comname=login.getString("companyName","Null");
        actionBar.setTitle(""+comname);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homepage.this, Qr_read.class);
                startActivity(i);
            }
        });

        product_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homepage.this, ProductList_Activity.class);
                startActivity(i);
            }
        });

        _Vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homepage.this, ShowVendor.class);
                startActivity(i);
            }
        });

        _Purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homepage.this, ShowPurchase.class);
                startActivity(i);
            }
        });

        Sales_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homepage.this, ShowSales.class);
                startActivity(i);
            }
        });

        stock_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homepage.this, ShowStock.class);
                startActivity(i);
            }
        });

//
//        Product_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Homepage.this, ShowSales.class);
//                startActivity(i);
//            }
//        });


        customer_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homepage.this, CustomerList_Activity.class);
                startActivity(i);
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Homepage.this, CAMERA);
      //  int result1 = ContextCompat.checkSelfPermission(Homepage.this, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Homepage.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean CALL_PHONEAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                   // boolean SStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (CALL_PHONEAccepted)
                    {

                    }
                        //getLastLocation();
                        //Toast.makeText(this, "Permission Granted,Thank you", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Homepage.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
