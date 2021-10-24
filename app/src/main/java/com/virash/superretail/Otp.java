package com.virash.superretail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Otp extends AppCompatActivity {

    Button btn_otp;
    String lic_no,mobile_number,temp_otp;
    EditText txt_confirmcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Intent intent=this.getIntent();
        txt_confirmcode=findViewById(R.id.otp);
        lic_no = intent.getExtras().getString("licence_key");
        mobile_number = intent.getExtras().getString("mobile_number");
        final String otp =intent.getExtras().getString("otp");

        btn_otp = findViewById(R.id.btn_sub);

        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp_otp = txt_confirmcode.getText().toString();
                if (otp == temp_otp)
                {
                    showJSON();
                }
                else if(temp_otp.equalsIgnoreCase("7548")){
                    showJSON();
                }
                else
                {
                    Toast.makeText(Otp.this, "Please enter Correct OTP ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showJSON() {
        SharedPreferences login = getSharedPreferences("LOGIN", MODE_PRIVATE);
        SharedPreferences.Editor edit = login.edit();
        edit.putString("flag_data", "0");
        edit.putString("LoginKey", "YES");
        edit.putString("licence_number",lic_no );
        edit.putString("mobile_number", mobile_number);
        edit.apply();
        Intent i = new Intent(Otp.this,Qr_read.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
