package com.virash.superretail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerList_Activity extends AppCompatActivity {
    RecyclerView customerList_recyclerView;
    CustomerList_adapter customerList_adapter;
    TextView tv_empty;
    String mob_no;
    private SearchView searchView;
    FloatingActionButton Add_cust;
    List< CustomerList_pojo> getCustomerList_adapter;
    LinearLayoutManager recyclerViewlayoutManager;
    String mobileNumber,number;
    ProgressDialog pd;
    SharedPreferences login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        login = getSharedPreferences("LOGIN", MODE_PRIVATE);
        mob_no = login.getString("mobile_number", "0");
        Add_cust=findViewById(R.id.Add_Custlist);
        Add_cust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CustomerList_Activity.this,Add_Customer.class);
                startActivity(i);
            }
        });
        customerList_recyclerView=findViewById(R.id.customer_recyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Customer List");
        tv_empty = findViewById(R.id.tv_empty);
        customerList_recyclerView.setHasFixedSize(true);
        customerList_recyclerView.setItemViewCacheSize(20);
        customerList_recyclerView.setDrawingCacheEnabled(true);
        customerList_recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    //    customerList_recyclerView.addItemDecoration(new DividerItemDecoration(customerList_recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        customerList_recyclerView.setLayoutManager(recyclerViewlayoutManager);
        getCustomerList_adapter = new ArrayList<>();
        LoadData();
    }

    private void LoadData() {

        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait......");
        pd.show();
        String url="https://superretails.com/khusali/app/customer-list.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    showJson(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CustomerList_Activity.this, "" + error, Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(CustomerList_Activity.this, "No Internnet", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(CustomerList_Activity.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(CustomerList_Activity.this, "ServerError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(CustomerList_Activity.this, "NetworkError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(CustomerList_Activity.this, "ParseError", Toast.LENGTH_SHORT).show();
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>key=new HashMap<String,String>();
                Log.d("mob_nomob_no",mob_no);
                key.put("mobile_number",mob_no);
                return key;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(this);
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private void showJson(String response) throws JSONException {
        Log.d("Res",""+response);
        JSONObject jsonObject=new JSONObject(response);
        JSONArray jsonArray=jsonObject.getJSONArray("Customer_List");
        for (int a=0;a<jsonArray.length();a++){
            JSONObject jsonObject1=jsonArray.getJSONObject(a);
            CustomerList_pojo getCustomerList_adapter2=new CustomerList_pojo();
            getCustomerList_adapter2.setFull_name(jsonObject1.getString("full_name"));
            getCustomerList_adapter2.setDate(jsonObject1.getString("date"));
            getCustomerList_adapter2.setArea(jsonObject1.getString("area"));
            getCustomerList_adapter2.setMobile(jsonObject1.getString("mobile"));
            getCustomerList_adapter2.setEmail(jsonObject1.getString("email"));
            getCustomerList_adapter.add(getCustomerList_adapter2);
            pd.dismiss();
        }

        customerList_adapter=new CustomerList_adapter(getCustomerList_adapter,this);
        customerList_recyclerView.setAdapter(customerList_adapter);

        if(getCustomerList_adapter.isEmpty())
        {
            pd.dismiss();
            customerList_recyclerView.setVisibility(View.GONE);
            tv_empty.setVisibility(View.VISIBLE);
        }
        else
        {
            customerList_recyclerView.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customerList_adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                customerList_adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        else
        {
            onBackPressed();
            return true;
        }
    }
}
