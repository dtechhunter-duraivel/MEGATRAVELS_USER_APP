package com.megalatravels.duraivel.cabbooking;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.cast.framework.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
public class LoginActivity extends AppCompatActivity {
    EditText ed1,ed2;
    Button login;
    String uname,pass;
    AlertDialog alert;
    URL url;
    ProgressDialog pd;
    TextView fp;
    Intent in;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "sma" ;

    private RequestQueue mQueue,SQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed1=(EditText)findViewById(R.id.e1);
        fp=(TextView)findViewById(R.id.e5);
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p=new Intent(LoginActivity.this,ResetPass.class);
                startActivity(p);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
        ed1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ed1.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
        ed2=(EditText)findViewById(R.id.e2);
        login=(Button)findViewById(R.id.bt);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_MULTI_PROCESS);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd = new ProgressDialog(LoginActivity.this);
        SQueue = Volley.newRequestQueue(this);
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(LoginActivity.this, "INTERNET PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();

        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   gp();
                //  postdata();
                pd.setMessage("Checking Credentials");
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(false);
                pd.show();
                uname=ed1.getText().toString().trim();
                pass=ed2.getText().toString().trim();
                testing(uname,pass);
                //signup();

            }
        });


    }



   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void signup()
    {
        StringRequest request = new StringRequest(Request.Method.POST, "http://*****************/mobile/api/Account/Register", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

         Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                Log.i("My error",""+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<String, String>();
                map.put("FirstName", "Bilal ");
                map.put("LastName", "Mohame");
                map.put("Email", "mohamedbial@gmail.com");
                map.put("PhoneNumber", "9876543890");
                map.put("Address", "trichy");
                map.put("Password", "Test@1234");
                map.put("ConfirmPassword", "Test@1234");
                map.put("Gender", "0");
                return map;
            }
        };
        SQueue.add(request);

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

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getApplicationContext(),atoken,Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("username",user);
                        editor.putString("password",passw);
                        editor.putString("User", "1");
                        editor.putString("auth", atoken);
                        editor.putString("fno","hai");
                        editor.commit();
                        setName(atoken);


                    //   Toast.makeText(LoginActivity.this, "authtoken"+response, Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
pd.dismiss();
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

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(message).setTitle("Info")
                        .setCancelable(false)
                        .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                           alert.dismiss();
                            }
                        });
                alert = builder.create();
                alert.show();

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




























    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);

    }


   void setName (final String token)
    {
        String un;
        String mob;
        final String[] udet = new String[3];
    //    Toast.makeText(getApplicationContext(),"Token:"+token,Toast.LENGTH_SHORT).show();

        StringRequest request = new StringRequest(Request.Method.GET, "http://***************/mobile/api/Account/UserInfo", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
               // Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                try {

                    JSONObject udetails =new JSONObject(response);
                    String name=udetails.getString("UserName");
                    String num=udetails.getString("Email");
                    udet[0]=name;
                    udet[1]=num;
                    String[] firstname= name.split(" ");
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("fne","Hi, "+firstname[0]);
                    editor.putString("unum",num);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),firstname[0],Toast.LENGTH_SHORT).show();
                    in = new Intent(LoginActivity.this,MakeJourney.class);

                    startActivity(in);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finishAffinity();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                String message = null;
                String body = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "User details does not meets minimum the requirements of Megala Travels";
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
        })
        {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }


        };
        SQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}