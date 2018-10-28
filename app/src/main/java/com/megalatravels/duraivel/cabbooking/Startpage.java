package com.megalatravels.duraivel.cabbooking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Startpage extends AppCompatActivity {
Button si,lo;
RequestQueue SQueue;
Button about;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AlertDialog.Builder builder =new AlertDialog.Builder(Startpage.this);
        View mView =getLayoutInflater().inflate(R.layout.layout,null);
        builder.setTitle("Choose Category");
        builder.setView(mView);
        final AlertDialog d=builder.create();

        SQueue= Volley.newRequestQueue(this);

        si=(Button)findViewById(R.id.bt);
        lo=(Button)findViewById(R.id.signup);
        lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Startpage
                        .this,MobileNumber.class);
                Startpage.this.startActivity(mainIntent);

                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Startpage
                        .this,LoginActivity.class);
              Startpage.this.startActivity(mainIntent);

                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        testing("","");
    }
    void testing(final String user, final String passw)
    {


        StringRequest request = new StringRequest(Request.Method.POST, "http://***************/mobile/Token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String atoken = null;

                try
                {
                    JSONObject atok=new JSONObject(response);
                    atoken= atok.getString("access_token");
         //           Toast.makeText(getApplicationContext(),atoken,Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(),atoken,Toast.LENGTH_LONG).show();



                //   Toast.makeText(LoginActivity.this, "authtoken"+response, Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "Incorrect Username or Password";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<String, String>();
                map.put("username", user);
                map.put("Password", passw);
                map.put("grant_type", "password");

                return map;
            }
        };
        SQueue.add(request);

    }


}
