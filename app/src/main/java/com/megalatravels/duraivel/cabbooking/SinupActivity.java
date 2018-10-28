
package com.megalatravels.duraivel.cabbooking;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.search.SearchAuth;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SinupActivity extends AppCompatActivity {
    private RequestQueue mQueue,SQueue;
    private  Spinner spinner;
    AlertDialog alert;
    Button signu;
    ProgressDialog pd;
    String mob;
    String gender,fn,ln,mail,addres,padss,cpadss,gen;
    EditText fname,lname,email,phn,add,addone,addtwo,pass,cpass;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        Intent inte =getIntent();
        Bundle extras = inte.getExtras();
        if (extras != null)
        {
            mob= extras.getString("mobil");
        }

        SQueue = Volley.newRequestQueue(this);
        signu=(Button)findViewById(R.id.but);
        spinner=(Spinner) findViewById(R.id.e4);
        fname=(EditText)findViewById(R.id.e1);
        lname=(EditText)findViewById(R.id.e2);
        email=(EditText)findViewById(R.id.e3);
        add=(EditText)findViewById(R.id.address);
        addone=(EditText)findViewById(R.id.alone);
        addtwo=(EditText)findViewById(R.id.altwo);
        pass=(EditText)findViewById(R.id.pass);
        cpass=(EditText)findViewById(R.id.confirmpass);
        String[] arraySpinner = new String[] {
               "Male","Female"
        };
        pd = new ProgressDialog(SinupActivity.this);


        pd.setTitle("Requesting");
        pd.setMessage("Fetching Data");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        signu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
               gender="0";
                fn=fname.getText().toString().trim();
                ln=lname.getText().toString().trim();
                mail=email.getText().toString().trim();
                addres=add.getText().toString().trim()+","+addone.getText().toString().trim()+","+addtwo.getText().toString().trim()+",Tamilnadu,India";
               padss=pass.getText().toString().trim();
               cpadss=cpass.getText().toString().trim();
               gen=spinner.getSelectedItem().toString().trim();
                if(gen.equals("Male")) {
                    gender ="1";
                }
                else
                {
                     gender="0";
                }
              //  Toast.makeText(getApplicationContext(),fn+ln+mail+addres+pass+cpass+gender,Toast.LENGTH_LONG).show();




                signup(fn,ln,mail,mob,addres,padss,cpadss,gender);
            }
        });

    }




    void signup(final String fname, final String lasname, final String mail, final String phonenum, final String address, final String password, final String cpassword, final String gender)
    {
        StringRequest request = new StringRequest(Request.Method.POST, "http://***************/mobile/api/Account/Register", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Intent i = new Intent(SinupActivity.this, LoginActivity.class);
                Bundle extras = new Bundle();
                extras.putString("mobil", phonenum);
                i.putExtras(extras);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                pd.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message ="";
String body =null;
String filtered="";
                String a[]=new String[100];

                pd.dismiss();
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    try {
                        body = new String(error.networkResponse.data, "UTF-8");
                        Pattern p = Pattern.compile("\\[(.*?)\\]");
                        Matcher m = p.matcher(body);
                        int i=0;
                        while(m.find()) {
                            a[i] =m.group(1).replace("\"","").replaceAll("[\\-\\+\\.\\^:,]","");
                            i++;
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    for(int j=0;j<a.length;j++) {

                        if (a[j] == null) {
                            break;
                        }
                        message =   message + (j+1)+". "+a[j]+"*" + "\n\n";
                    }
                    } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(SinupActivity.this);
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> map = new HashMap<String, String>();
                map.put("FirstName", fname);
                map.put("LastName", lasname);
                map.put("Email", mail);
                map.put("PhoneNumber", phonenum);
                map.put("Address", address);
                map.put("Password", password);
                map.put("ConfirmPassword", cpassword);
                map.put("Gender", gender);
                return map;
            }
        };
        SQueue.add(request);
       request.setRetryPolicy(new DefaultRetryPolicy(
               2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);

    }
}
