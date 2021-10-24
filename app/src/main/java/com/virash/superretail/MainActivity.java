package com.virash.superretail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView re_hide,su_hide;
    Animation av,av1;
    SharedPreferences login;
    String getStatus,uname,upass,token;
    CheckConnection chkCon = new CheckConnection();
    boolean conMsg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login = getSharedPreferences("LOGIN", MODE_PRIVATE);
        getStatus = login.getString("LoginKey", "N/A");
        uname = login.getString("username", "N/A");
        conMsg = chkCon.checkConnection();
        if (conMsg == true)
        {
            setContentView(R.layout.activity_main);
            re_hide= (TextView) findViewById(R.id.hide);
            su_hide= (TextView) findViewById(R.id.su_hide);
            av = AnimationUtils.loadAnimation(this,R.anim.move_r);
            re_hide.setAnimation(av);
            av1 = AnimationUtils.loadAnimation(this,R.anim.move_l);
            su_hide.setAnimation(av1);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getStatus.equals("YES")) {
                        Intent i = new Intent(MainActivity.this, Homepage.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        Intent mainIntent = new Intent(MainActivity.this, Login_page.class);
                        MainActivity.this.startActivity(mainIntent);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainActivity.this.finish();
                    }
                }

            }, 3500);
        }
        else
        {
            setContentView(R.layout.layout_no_connection);
            Button btn_retry = findViewById(R.id.btn_tryagain);
            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
        }
    }
}