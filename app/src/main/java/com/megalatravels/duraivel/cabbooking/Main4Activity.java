package com.megalatravels.duraivel.cabbooking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Main4Activity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2500;
    TextView tv,tv2,tv4,con;
    ImageView img;
    String a="0";
    //SharedPreferences pref;
    Intent mainIntent;
    RequestQueue SQueue;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        SQueue = Volley.newRequestQueue(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pref = getApplicationContext().getSharedPreferences("sma", Context.MODE_MULTI_PROCESS); // 0 - for private mode
              //SharedPreferences.Editor editor = pref.edit();
if(pref.contains("User")) {
    a = pref.getString("User", null).toString();
    // a="1";
}
else
{
    a="0";
}
        img= (ImageView)findViewById(R.id.t1);
        tv2=(TextView)findViewById(R.id.t2) ;
        tv4=(TextView)findViewById(R.id.t6);
        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anim);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        img.startAnimation(anim);
        tv2.startAnimation(anim);
        tv4.startAnimation(anim);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the First Page. */


                if(a.equals("1")) {


                    mainIntent = new Intent(Main4Activity
                            .this,MakeJourney.class);

                }
                else
                {
                    mainIntent = new Intent(Main4Activity
                            .this,Startpage.class);

                }

                Main4Activity.this.startActivity(mainIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                Main4Activity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
    void testing(final String user, final String passw)
    {
        StringRequest request = new StringRequest(Request.Method.POST, "http://***************/mobile/Token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                //   Toast.makeText(Main4Activity.this, "authtoken"+response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            /* Create an Intent that will start the First Page. */


                            if(a.equals("1")) {


                                mainIntent = new Intent(Main4Activity
                                        .this,MapAct.class);

                            }
                            else
                            {
                                mainIntent = new Intent(Main4Activity
                                        .this,Startpage.class);

                            }

                            Main4Activity.this.startActivity(mainIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            Main4Activity.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                } else if (error instanceof ServerError) {
                    message = "Incorrect Username or Password";
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            /* Create an Intent that will start the First Page. */


                            if(a.equals("1")) {


                                mainIntent = new Intent(Main4Activity
                                        .this,MakeJourney.class);

                            }
                            else
                            {
                                mainIntent = new Intent(Main4Activity
                                        .this,Startpage.class);

                            }

                            Main4Activity.this.startActivity(mainIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            Main4Activity.this.finish();
                        }
                    }, 100);

                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            /* Create an Intent that will start the First Page. */


                            if(a.equals("1")) {


                                mainIntent = new Intent(Main4Activity
                                        .this,MapAct.class);

                            }
                            else
                            {
                                mainIntent = new Intent(Main4Activity
                                        .this,Startpage.class);

                            }

                            Main4Activity.this.startActivity(mainIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            Main4Activity.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            /* Create an Intent that will start the First Page. */


                            if(a.equals("1")) {


                                mainIntent = new Intent(Main4Activity
                                        .this,MapAct.class);

                            }
                            else
                            {
                                mainIntent = new Intent(Main4Activity
                                        .this,Startpage.class);

                            }

                            Main4Activity.this.startActivity(mainIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            Main4Activity.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            /* Create an Intent that will start the First Page. */


                            if(a.equals("1")) {


                                mainIntent = new Intent(Main4Activity
                                        .this,MapAct.class);

                            }
                            else
                            {
                                mainIntent = new Intent(Main4Activity
                                        .this,Startpage.class);

                            }

                            Main4Activity.this.startActivity(mainIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            Main4Activity.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            /* Create an Intent that will start the First Page. */


                            if(a.equals("1")) {


                                mainIntent = new Intent(Main4Activity
                                        .this,MapAct.class);

                            }
                            else
                            {
                                mainIntent = new Intent(Main4Activity
                                        .this,Startpage.class);

                            }

                            Main4Activity.this.startActivity(mainIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            Main4Activity.this.finish();
                        }
                    }, 100);



                }

             //   Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put("username", "dd");
                map.put("Password", "ddd");
                map.put("grant_type", "password");
               return map;
            }
        };
        SQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                500,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}
