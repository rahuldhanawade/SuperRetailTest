package com.virash.superretail;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends AppCompatActivity {

    ProgressDialog pd;
    String product_id,barcode_no,product_name,quantity,selling_price,product_code,unit,gst,hsn_code,status,group_name,brand_name;
    TextView tv_product_name,tv_price,tv_product_code_no,tv_BrandView,tv_category,tv_quantity,tv_unit,tv_barcode_no,tv_status,tv_gst_no,tv_hsn_code;
    String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        barcode = getIntent().getStringExtra("barcode");
        Log.d("123",""+barcode);

        tv_product_name=findViewById(R.id.tv_product_name);
        tv_price=findViewById(R.id.tv_price);
        tv_product_code_no=findViewById(R.id.tv_product_code_no);
        tv_BrandView=findViewById(R.id.tv_BrandView);
        tv_category=findViewById(R.id.tv_category);
        tv_quantity=findViewById(R.id.tv_quantity);
        tv_unit=findViewById(R.id.tv_unit);
        tv_barcode_no=findViewById(R.id.tv_barcode_no);
        tv_status=findViewById(R.id.tv_status);
        tv_gst_no=findViewById(R.id.tv_gst_no);
        tv_hsn_code=findViewById(R.id.tv_hsn_code);

        LoadData();
    }

    private void LoadData() {
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait.....");
        pd.show();
        String url="https://superretails.com/khusali/app/product-details.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("8901188659047",response);
                ShowJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ( error instanceof NoConnectionError) {
                    Toast.makeText(ProductDetails.this, "Server is not connected to internet.",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(ProductDetails.this, "Server couldn't find the authenticated request.",
                            Toast.LENGTH_SHORT).show();
                }else if (error instanceof TimeoutError) {
                    Toast.makeText(ProductDetails.this, "Cannot Find Server",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ProductDetails.this, "Server is not responding.Please try Again Later",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(ProductDetails.this, "Your device is not connected to internet.",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(ProductDetails.this, "Parse Error(because of invalid json",
                            Toast.LENGTH_SHORT).show();
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>key=new HashMap<String, String>();
                key.put("licence_key","Khusali");
                key.put("barcode_no",barcode);
                Log.d("bar",""+barcode);
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
                product_code= data.getString("product_code").toString();
                unit= data.getString("unit").toString();
                gst= data.getString("gst").toString();
                hsn_code=data.getString("hsn_code").toString();
                status=data.getString("status").toString();
                group_name=data.getString("group_name").toString();
                brand_name=data.getString("brand_name").toString();
                pd.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_product_name.setText(product_name);
        tv_price.setText(selling_price);
        tv_product_code_no.setText(product_code);
        tv_BrandView.setText(brand_name);
        tv_barcode_no.setText(barcode_no);
        tv_quantity.setText(quantity);
        tv_unit.setText(unit);
        tv_gst_no.setText(gst);
        tv_hsn_code.setText(hsn_code);
        tv_status.setText(status);
        tv_category.setText(group_name);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


