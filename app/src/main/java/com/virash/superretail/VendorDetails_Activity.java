package com.virash.superretail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VendorDetails_Activity extends AppCompatActivity {

    EditText edt_vendorName,edt_mobileNo,edt_email,edt_city,edt_state,edt_pinCode,edt_address;
    TextView tv_save;
    String name,mobile,email,city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendordetails);

        name = getIntent().getStringExtra("name");
        mobile = getIntent().getStringExtra("mobile");
        email = getIntent().getStringExtra("email");
        city = getIntent().getStringExtra("city");

        edt_vendorName=findViewById(R.id.edt_vendorName);
        edt_vendorName.setText(name);
        edt_mobileNo=findViewById(R.id.edt_mobileNo);
        edt_mobileNo.setText(mobile);
        edt_email=findViewById(R.id.edt_email);
        edt_email.setText(email);
        edt_city=findViewById(R.id.edt_city);
        edt_city.setText(city);
        edt_state=findViewById(R.id.edt_state);
        edt_pinCode=findViewById(R.id.edt_pinCode);
        edt_address=findViewById(R.id.edt_address);
        tv_save=findViewById(R.id.tv_save);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Vendor Details");

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_vendorName.getText().toString().trim().equalsIgnoreCase("")){
                    edt_vendorName.setError("Please Enter Vendor Name");
                }
                if(edt_mobileNo.getText().toString().trim().equalsIgnoreCase("")){
                    edt_mobileNo.setError("Please Enter Mobile Number ");
                }
                if (edt_email.getText().toString().trim().equalsIgnoreCase("")){
                    edt_email.setError("Please Enter Email Id");
                }
                if (edt_city.getText().toString().trim().equalsIgnoreCase("")){
                    edt_city.setError("Please Enter City");
                }
                if (edt_state.getText().toString().trim().equalsIgnoreCase("")){
                    edt_state.setError("Please Enter State ");
                }
                if(edt_pinCode.getText().toString().trim().equalsIgnoreCase("")){
                    edt_pinCode.setError("Please Enter Pincode");
                }
                if(edt_address.getText().toString().trim().equalsIgnoreCase("")){
                    edt_address.setError("Please Enter Address ");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
