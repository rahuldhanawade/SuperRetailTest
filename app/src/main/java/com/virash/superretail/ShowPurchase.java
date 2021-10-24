package com.virash.superretail;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class ShowPurchase extends AppCompatActivity {
    RecyclerView recyclerView;
    GetAdapterPurchase MyAdapter;
    TextView tv_empty;
    private SearchView searchView;
    List<ShowPurchase_POJO> getPackageAdapter1;
    LinearLayoutManager recyclerViewlayoutManager;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_purchase);
        recyclerView = findViewById(R.id.recyclerview_package_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Show Purchase");
        tv_empty = findViewById(R.id.tv_empty);
        recyclerView.setClickable(true);
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        getPackageAdapter1 = new ArrayList<>();
        loadData();

    }

    private void loadData() {
        pd= new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://superretails.com/khusali/ajax/showPurchase.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(ShowPurchase.this, "Server is not connected to internet.",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(ShowPurchase.this, "server couldn't find the authenticated request.",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ShowPurchase.this, "Server is not responding.Please try Again Later", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ShowPurchase.this, "Your device is not connected to internet.",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(ShowPurchase.this, "Parse Error (because of invalid json or xml).",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    public void showJSON(String response){
        try {
            //Log.d("response",""+response);
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                pd.dismiss();
                JSONObject jsonObject1= jsonArray.getJSONObject(i);
                ShowPurchase_POJO getPackageAdapter2 = new ShowPurchase_POJO();
                getPackageAdapter2.setId(jsonObject1.getString("purchase_id"));
                getPackageAdapter2.setDate(jsonObject1.getString("purchase_date"));
                getPackageAdapter2.setInvoice(jsonObject1.getString("invoice_no"));
                getPackageAdapter2.setName(jsonObject1.getString("vendor_name"));
                getPackageAdapter2.setTotalItem(jsonObject1.getString("total_item"));
                getPackageAdapter2.setTaxableAmt(jsonObject1.getString("taxable_amt"));
                getPackageAdapter2.setQuantity(jsonObject1.getString("total_qty"));
                getPackageAdapter2.setAmount(jsonObject1.getString("net_amt"));
                getPackageAdapter2.setRemark(jsonObject1.getString("remark"));
                getPackageAdapter2.setCgst(jsonObject1.getString("total_cgst"));
                getPackageAdapter2.setSgst(jsonObject1.getString("total_sgst"));
                getPackageAdapter1.add(getPackageAdapter2);


            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        MyAdapter = new GetAdapterPurchase(getPackageAdapter1, this);
        recyclerView.setAdapter(MyAdapter);

        if(getPackageAdapter1.isEmpty())
        {
            pd.dismiss();
            recyclerView.setVisibility(View.GONE);
            tv_empty.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
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
                MyAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                MyAdapter.getFilter().filter(query);
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

