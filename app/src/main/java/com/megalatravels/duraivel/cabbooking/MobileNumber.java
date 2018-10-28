package com.megalatravels.duraivel.cabbooking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Random;

public class MobileNumber extends AppCompatActivity {
private RequestQueue SQueue;
String phno,otp="234522";
int n;
ProgressDialog pd;
EditText mob;
long otpgen;
Button gen;
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
        setContentView(R.layout.activity_mobile_number);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd = new ProgressDialog(MobileNumber.this);

        SQueue = Volley.newRequestQueue(this);
        mob=(EditText)findViewById(R.id.e2);
        mob.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
       mob.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});

        gen=(Button)findViewById(R.id.genotp);
pd.setMessage("Sending OTP");
pd.setCancelable(false);

        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phno=mob.getText().toString().trim();
pd.show();
                Random rnd = new Random();
                n = 100000 + rnd.nextInt(900000);
                otp=String.valueOf(n);
                gp(phno,otp);

                }
        });

    }



    void gp(final String phon, final String otpv)
    {
        String url = "http://bhashsms.com/api/sendmsg.php?user=megala&pass=******&sender=MEGALA&phone="+phon+"&text=OTP:"+otpv+"&priority=ndnd&stype=normal";
        StringRequest strReq = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                   pd.dismiss();

                         Intent mainIntent = new Intent(MobileNumber.this
                                ,OTPPage.class);
                        Bundle extras = new Bundle();
                        extras.putString("mobile", phon);
                        extras.putString("otp", otpv);
                        mainIntent.putExtras(extras);
                        finish();
                        MobileNumber.this.startActivity(mainIntent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        SQueue.add(strReq);
    }

}
