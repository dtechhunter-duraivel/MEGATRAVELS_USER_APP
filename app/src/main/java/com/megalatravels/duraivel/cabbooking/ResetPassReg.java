package com.megalatravels.duraivel.cabbooking;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResetPassReg extends AppCompatActivity {
    EditText otp;
    String otpv,otpc,phonenum;
    TextView time;
    Button verify;
    String min;
    int minutes;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_reg);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        time=(TextView)findViewById(R.id.eresend);
        otp=(EditText)findViewById(R.id.eotp);
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
                    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
                    @Override
                    public void onClick(View v) {
                        Intent s=new Intent(ResetPassReg.this,ResetPass.class);
                        finish();
                        startActivity(s);
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);

                    }
                });
            }

        }.start();

        verify=(Button)findViewById(R.id.everify);
        Intent inte =getIntent();
        Bundle extras = inte.getExtras();
        if (extras != null)
        {
            phonenum= extras.getString("mobile");
            otpv=extras.getString("otp");
        }
        verify.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onClick(View v) {
                otpc=otp.getText().toString().trim();
                if(otpv.equals(otpc)) {
                    Intent i = new Intent(ResetPassReg.this, ResetPassword.class);
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
}
