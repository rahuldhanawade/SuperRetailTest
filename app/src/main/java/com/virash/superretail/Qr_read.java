package com.virash.superretail;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.Result;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static android.Manifest.permission.CAMERA;

public class Qr_read extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    String TAG="QRREADER";
    String mob_no,lic_id;
    Button sbt;
    ProgressDialog pd;
    EditText barcode;
    private static final int PERMISSION_REQUEST_CODE = 200;
    SharedPreferences login;

    @Override
    public void onCreate(Bundle state)
    {
        super.onCreate(state);
        setContentView(R.layout.qr_read);
        login = getSharedPreferences("LOGIN", MODE_PRIVATE);
        mob_no = login.getString("mobile_number", "0");
        lic_id = login.getString("lic_id", "0");
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        barcode = findViewById(R.id.barcode_id);
        contentFrame.addView(mScannerView);
        sbt = findViewById(R.id.sbmt_barcode);
        if(!checkPermission())
        {
            Toast.makeText(this, "Please request permission.", Toast.LENGTH_SHORT).show();
            requestPermission();
        }
        //mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        //setContentView(mScannerView);                // Set the scanner view as the content view
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Qr_read.this,MainActivity.class);
//                startActivity(i);
//            }
//        });
        sbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Qr_read.this, Cart.class);
                startActivity(i);
            }
        });
    }

    private void LoadData(final String Barcode) {
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait..... fetching Details");
        pd.show();
        String url="https://superretails.com/khusali/app/product-details.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ShowJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ( error instanceof NoConnectionError) {
                    Toast.makeText(Qr_read.this, "Server is not connected to internet.",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Qr_read.this, "Server couldn't find the authenticated request.",
                            Toast.LENGTH_SHORT).show();
                }else if (error instanceof TimeoutError) {
                    Toast.makeText(Qr_read.this, "Cannot Find Server",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Qr_read.this, "Server is not responding.Please try Again Later",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Qr_read.this, "Your device is not connected to internet.",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Qr_read.this, "Parse Error(because of invalid json",
                            Toast.LENGTH_SHORT).show();
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>key=new HashMap<String, String>();
                key.put("licence_key",lic_id);
               // key.put("barcode_no","8901188702194");
                key.put("barcode_no",Barcode);
                return key;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        int socketTimeout = 30000;
        RetryPolicy policy=new DefaultRetryPolicy(socketTimeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void ShowJSON(String response) {
        String product_id,barcode_no,product_name,quantity,selling_price;
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=  jsonObject.getJSONArray("Product_List");
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject data = jsonArray.optJSONObject(i);
                product_id= data.getString("product_id").toString();
                barcode_no= data.getString("barcode_no").toString();
                product_name= data.getString("product_name").toString();
                quantity=data.getString("quantity").toString();
                selling_price=data.getString("selling_price").toString();
                pd.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Qr_read.this, CAMERA);
       // int result1 = ContextCompat.checkSelfPermission(Qr_read.this, ACCESS_FINE_LOCATION);
      //  return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        return result == PackageManager.PERMISSION_GRANTED;
    }
    
    private void requestPermission() {
        ActivityCompat.requestPermissions(Qr_read.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean CAMERAAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                   // boolean SStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    //if (CALL_PHONEAccepted  && SStorageAccepted)
                    if (CAMERAAccepted)
                    {
                    }
                        //getLastLocation();
                    else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to permissions",
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
        new AlertDialog.Builder(Qr_read.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString());
        // If you would like to resume scanning, call this method below:
        String qr_code = rawResult.getText().toString();
        LoadData(qr_code);
   //     Toast.makeText(this, qr_code, Toast.LENGTH_SHORT).show();
        // If you would like to resume scanning, call this method below:
//        Intent intent = new Intent(Qr_read.this, Cart.class);
//        intent.putExtra("qr_result", qr_code);
//        startActivity(intent);
    }
}



