package com.virash.superretail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Add_Customer extends AppCompatActivity {

    EditText customer_name,contact,email,city,address,gst_name,add_gst,add_scc,add_gst_address;
    Button btn_add_gst;
    String fullname,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__customer);

        fullname = getIntent().getStringExtra("fullname");
        mobile = getIntent().getStringExtra("mobile");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Customer");
        customer_name=findViewById(R.id.customer_name);
        customer_name.setText(fullname);
        contact=findViewById(R.id.contact);
        contact.setText(mobile);
        email=findViewById(R.id.email);
        city=findViewById(R.id.city);
        address=findViewById(R.id.address);
        gst_name=findViewById(R.id.gst_name);
        add_gst=findViewById(R.id.add_gst);
        add_scc=findViewById(R.id.add_scc);
        add_gst_address=findViewById(R.id.add_gst_address);
        btn_add_gst=findViewById(R.id.btn_add_gst);
        btn_add_gst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(customer_name.getText().toString().trim().equalsIgnoreCase("")){
                    customer_name.setError("Please Enter Customer Name");
                }
                if(contact.getText().toString().trim().equalsIgnoreCase("")){
                    contact.setError("Please Enter Contact Number ");
                }
                if (email.getText().toString().trim().equalsIgnoreCase("")){
                    email.setError("Please Enter Email Id");
                }
                if (city.getText().toString().trim().equalsIgnoreCase("")){
                    city.setError("Please Enter City");
                }
                if (address.getText().toString().trim().equalsIgnoreCase("")){
                    address.setError("Please Enter Address ");
                }
                if(gst_name.getText().toString().trim().equalsIgnoreCase("")){
                    gst_name.setError("Please Enter Firm Name");
                }
                if(add_gst.getText().toString().trim().equalsIgnoreCase("")){
                    add_gst.setError("Please Enter GST No. ");
                }
                if(add_scc.getText().toString().trim().equalsIgnoreCase("")){
                    add_scc.setError("Please Enter SCC Code.");
                }
                if(add_gst_address.getText().toString().trim().equalsIgnoreCase("")){
                    add_gst_address.setError("Please Enter Registered GST Address ");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
