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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductList_Activity extends AppCompatActivity {
    RecyclerView productList_recyclerView;
    ProductList_adapter productList_adapter;
    TextView tv_empty;
    private SearchView searchView;
    List< ProductList_pojo> getProductList_adapter;
    LinearLayoutManager recyclerViewlayoutManager;
    String lic,khusali;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        productList_recyclerView=findViewById(R.id.productList_recyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Product List");
        tv_empty = findViewById(R.id.tv_empty);
        productList_recyclerView.setHasFixedSize(true);
        productList_recyclerView.setItemViewCacheSize(20);
        productList_recyclerView.setDrawingCacheEnabled(true);
        productList_recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //productList_recyclerView.addItemDecoration(new DividerItemDecoration(productList_recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        productList_recyclerView.setLayoutManager(recyclerViewlayoutManager);
        getProductList_adapter = new ArrayList<>();
        LoadData();

    }

    private void LoadData() {
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait......");
        pd.show();
        String url="https://superretails.com/khusali/app/product-list.php";

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
                Toast.makeText(ProductList_Activity.this, "" + error, Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(ProductList_Activity.this, "No Internnet", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(ProductList_Activity.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ProductList_Activity.this, "ServerError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(ProductList_Activity.this, "NetworkError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(ProductList_Activity.this, "ParseError", Toast.LENGTH_SHORT).show();
                }
            }
        }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>key=new HashMap<String,String>();
                key.put("licence_key",""+lic);
                key.put("khusali",""+khusali);
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
    //    Log.d("Res",""+response);
        JSONObject jsonObject=new JSONObject(response);
        JSONArray jsonArray=jsonObject.getJSONArray("Product_List");
            for (int a=0;a<jsonArray.length();a++){
                JSONObject jsonObject1=jsonArray.getJSONObject(a);
                ProductList_pojo getProductList_adapter2=new ProductList_pojo();
//                getProductList_adapter2.setProduct_id(jsonObject1.getString("product_id"));
                getProductList_adapter2.setBarcode_no(jsonObject1.getString("barcode_no"));
                getProductList_adapter2.setProduct_name(jsonObject1.getString("product_name"));
                getProductList_adapter2.setGroup_name(jsonObject1.getString("group_name"));
                getProductList_adapter2.setQuantity(jsonObject1.getString("quantity"));
                getProductList_adapter2.setSelling_price(jsonObject1.getString("selling_price"));
                getProductList_adapter.add(getProductList_adapter2);
                pd.dismiss();
            }
        productList_adapter=new ProductList_adapter(getProductList_adapter,this);
        productList_recyclerView.setAdapter(productList_adapter);

        if(getProductList_adapter.isEmpty())
        {
            pd.dismiss();
            productList_recyclerView.setVisibility(View.GONE);
            tv_empty.setVisibility(View.VISIBLE);
        }
        else
        {
            productList_recyclerView.setVisibility(View.VISIBLE);
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
                productList_adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                productList_adapter.getFilter().filter(query);
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
