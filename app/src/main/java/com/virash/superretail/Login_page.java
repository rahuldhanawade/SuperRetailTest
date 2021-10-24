package com.virash.superretail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Login_page extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    EditText lic_no,mob,login_pass;
    Button btn_sub;
    SharedPreferences login;
    SharedPreferences.Editor edit;
    String mob_no,pass,lic_id;
    private ProgressDialog progress;
    TextView frgt_pass;
    private static final String REGISTER_URL="http://superretails.com/khusali/app/login.php";
    CheckConnection chkCon = new CheckConnection();
    boolean conMsg = false;

//    SignInButton signInButton;
//    private GoogleApiClient googleApiClient;
//    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        googleApiClient=new GoogleApiClient.Builder(this)
//                .enableAutoManage(this,this)
//                    .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
//                    .build();


    conMsg = chkCon.checkConnection();
        if (conMsg == true)
        {
            setContentView(R.layout.activity_login_page);
            lic_no = findViewById(R.id.lic_no);
            mob = findViewById(R.id.mob_no);
            login_pass = findViewById(R.id.login_pass);
            frgt_pass = findViewById(R.id.forgot);
            btn_sub = findViewById(R.id.button);
            conMsg = chkCon.checkConnection();

//            signInButton=(SignInButton)findViewById(R.id.sign_in_button);
//            signInButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
//                    startActivityForResult(i,RC_SIGN_IN);
//                }
//            });


            btn_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lic_no.getText().toString().trim().equalsIgnoreCase("")) {
                        lic_no.setError("Please Enter Register Mobile Number");
                    }
                    if (mob.getText().toString().compareToIgnoreCase("") == 0) {
                        mob.setError("Please Enter Mobile Number");
                    }
                    if (login_pass.getText().toString().compareToIgnoreCase("") != 0
                            && login_pass.getText().toString().compareToIgnoreCase("") != 0) {
                        int a =  generateOTP();
                        savedata(a);
                    }
                }
            });

            frgt_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Login_page.this, Forgot_password.class);
                    startActivity(i);
                }
            });
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

    private void savedata(final Integer Otp)
    {
        lic_id = lic_no.getText().toString();
        mob_no = mob.getText().toString();
        pass = login_pass.getText().toString();
        progress = ProgressDialog.show(this, "Please wait...", "Validating...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    parseJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(Login_page.this, "Server is not connected to internet.",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Login_page.this, "server couldn't find the authenticated request.",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(Login_page.this, "Server is not responding.Please try Again Later", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Login_page.this, "Your device is not connected to internet.",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(Login_page.this, "Parse Error (because of invalid json or xml).",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("licence_key", lic_id);
                param.put("mobile_number", mob_no);
                param.put("password", pass);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String response)
        throws JSONException {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int success= Integer.parseInt(jsonObject.optString("success"));
                if(success==1)
                {
                    login = getSharedPreferences("LOGIN", MODE_PRIVATE);
                    edit = login.edit();
                    edit.putString("LoginKey", "YES");
                    edit.putString("lic_id", lic_id);
                    edit.putString("companyName", jsonObject.optString("success").toString());
                    edit.putString("mobile_number", mob_no);
                    edit.apply();
                    Intent intent=new Intent(Login_page.this,Homepage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Wrong Credentials, Please try again with proper credentials", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    public int generateOTP()
    {
        final int min = 1000;
        final int max = 9999;
        Random r = new Random();
        final int random = r.nextInt((max - min) + 1) + min;
        return random;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( progress!=null && progress.isShowing() ){
            progress.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==RC_SIGN_IN){
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }
//
//    private void handleSignInResult(GoogleSignInResult result) {
//        if(result.isSuccess()){
//            gotoProfile();
//        }else{
//            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
//        }
//    }
//    private void gotoProfile(){
//        Intent intent=new Intent(Login_page.this,ProfileActivity.class);
//        startActivity(intent);
//    }
}
