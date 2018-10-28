package com.megalatravels.duraivel.cabbooking;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.TimeUnit;

public class OTPPage extends AppCompatActivity {
EditText otp;
String otpv,otpc,phonenum;
TextView time;
Button verify;
String min;
RequestQueue SQueue;
AlertDialog alert;
int minutes;
CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otppage);
        SQueue = Volley.newRequestQueue(this);
        Intent inte =getIntent();
        Bundle extras = inte.getExtras();
        if (extras != null)
        {
            phonenum= extras.getString("mobile");
            otpv=extras.getString("otp");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        time=(TextView)findViewById(R.id.resend);
        otp=(EditText)findViewById(R.id.e2);
        countDownTimer =  new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished)
            {
              long minutes= millisUntilFinished/1000;
              min=String.valueOf("RESEND IN 00 : "+minutes);
              time.setText(min);
            }

            public void onFinish()
            {
             time.setText("RESEND OTP ?");
             time.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
               /*      Intent s=new Intent(OTPPage.this,MobileNumber.class);
                     finish();
                     startActivity(s);
                     overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);
*/
               gp(phonenum,otpv);
                 }
             });
            }

        }.start();

verify=(Button)findViewById(R.id.verify);

verify.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        otpc=otp.getText().toString().trim();
        if(otpv.equals(otpc)) {
            Intent i = new Intent(OTPPage.this, SinupActivity.class);
            Bundle extras = new Bundle();
            extras.putString("mobil", phonenum);
            i.putExtras(extras);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }
        else
            Toast.makeText(getApplicationContext(),"Wrong OTP",Toast.LENGTH_LONG).show();
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
    void gp(final String phon, final String otpv)
    {
        String url = "http://bhashsms.com/api/sendmsg.php?user=megala&pass=******&sender=MEGALA&phone="+phon+"&text=Your%20OneTime%20Verification%20Code%20is%20:"+otpv+"&priority=ndnd&stype=normal";
        StringRequest strReq = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
            String message="OTP has been resent to "+phon;
                    @Override
                    public void onResponse(String response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OTPPage.this);
                        builder.setMessage(message).setTitle("Info")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                        alert.dismiss();
                                    }
                                });
                        alert = builder.create();
                        alert.show();


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
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                // message is the fetching OTP
            }
        }
    };
}
