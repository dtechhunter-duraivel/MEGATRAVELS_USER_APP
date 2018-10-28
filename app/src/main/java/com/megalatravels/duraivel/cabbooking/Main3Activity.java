
package com.megalatravels.duraivel.cabbooking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main3Activity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
GoogleMap map;
    String distanc;
    double mincost;
    double km;
    double costkm;
    String cost;
    AlertDialog alert;
    String pos;
    String picklocation;
    String droplocation;
    final Context context=this;
    String categ;
    RequestQueue SQueue;
    String ctype;
    Button dialogButton;
    SharedPreferences pref;
    String a;
String un;
String ps;
    String at;
    ProgressDialog pd;
    int approxkm;
TextView tv1,tv2,tv3,tv4,con;
String f;
Button b1,b2,b3;
    LocationManager locationManager;
    String[] carid;
    String[] catname;
    String[] seat;
    String[] ac;
    String[] nonac;
    String[] minchrge;
    String[] minkm;
    String[] nonacrg;
    String[] accrg;
    String fdis;
    String fne;
    String unum;
String bdate;
    private int mYear, mMonth, mDay;

String pidate;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy");
 bdate= ft.format(dNow);
        carid=new String[50];
        catname=new String[50];
        seat=new String[50];
        ac =new String[50];
        nonac=new String[50];
        minchrge=new String[50];
        minkm=new String[50];
        nonacrg=new String[50];
        accrg=new String[50];
        pd = new ProgressDialog(Main3Activity.this);
        pd.setTitle("Processing");
        pd.setMessage("Booking Your Car..Please Wait");
        SQueue = Volley.newRequestQueue(this);
        getTarrif();

        pref = getApplicationContext().getSharedPreferences("sma", Context.MODE_MULTI_PROCESS); // 0 - for private mode
        //SharedPreferences.Editor editor = pref.edit();

        if(pref.contains("username") && pref.contains("password"))
        {
            un = pref.getString("username", null).toString();
            ps = pref.getString("password", null).toString();
           // Toast.makeText(getApplicationContext(),un+ps,Toast.LENGTH_SHORT).show();

        }


        if(pref.contains("fne") && pref.contains("unum"))
        {
            fne = pref.getString("fne", null).toString();
            unum = pref.getString("unum", null).toString();
            // Toast.makeText(getApplicationContext(),un+ps,Toast.LENGTH_SHORT).show();

        }
        if(pref.contains("auth"))
        {
            f= pref.getString("auth", null).toString();
       //  Toast.makeText(getApplicationContext(),f,Toast.LENGTH_SHORT).show();
            // a="1";
        }

        AlertDialog.Builder builder =new AlertDialog.Builder(Main3Activity.this);
        View mView =getLayoutInflater().inflate(R.layout.layout,null);
final Button conboo =(Button)mView.findViewById(R.id.conb);
        final EditText datepick=(EditText)mView.findViewById(R.id.pd);
        final EditText timepicker=(EditText)mView.findViewById(R.id.pt);
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Main3Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datepick.setText((monthOfYear + 1) + "/" +dayOfMonth  + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Main3Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timepicker.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Pickup Time");
                mTimePicker.show();
            }
        });
        builder.setView(mView);
        final AlertDialog d=builder.create();
        conboo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
String date=datepick.getText().toString();
Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT).show();
String time=timepicker.getText().toString();
if(date.equals("")||time.equals(""))
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
        builder.setMessage("Please Select Your Pickup Date & Time").setTitle("Info")
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
else {
  //  Toast.makeText(getApplicationContext(), "Hello" + date, Toast.LENGTH_SHORT).show();
    pd.show();
    booking(f, date+" "+time);
}
            }
        });
      d.getWindow().getAttributes().windowAnimations = R.style.MyAnim; //style id
        tv1=(TextView)findViewById(R.id.t3);
        tv2=(TextView)findViewById(R.id.t2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.billdetails, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        TextView Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("Journey Details");

        getSupportActionBar().setCustomView(view,params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tv3=(TextView)findViewById(R.id.t8);
        tv4=(TextView)findViewById(R.id.t7);
        final Intent intent = getIntent();
        b1=(Button) findViewById(R.id.booknow);

        b2=(Button)findViewById(R.id.callnow);
        Bundle extras = intent.getExtras();
        if (extras != null) {
             distanc = extras.getString("distance");

pos=extras.getString("pos");
categ=extras.getString("cate");
String seat = null;
  if(pos.equals("0"))
            {
                ctype="Indica";
                categ="A/C & Non-A/C";
                seat="4+1";
            }
            else if(pos.equals("1")) {
      ctype = "Maruthi";
      categ="Non - A/C";
      seat="7+1";
  }         
  else if(pos.equals("2"))
  {
      ctype = "Tourister" ;
      categ="Non - A/C";
      seat="12+1";
  }         
  else if(pos.equals("3"))
  {
      ctype = "Hi-Tech Van";
      categ="A/C & Non - A/C";
      seat="12+1";
  }       
  else if(pos.equals("4"))
  {
      ctype = "Innovo";
      categ="A/C";
      seat="7+1";
  }
  else if(pos.equals("5"))
  {
      ctype = "Tavera";
      categ="A/C & Non - A/C";
      seat="7+1";
  }
  else if(pos.equals("6"))
  {
      ctype = "Etios";
      categ="A/C";
      seat="4+1";
  }
  else if (pos.equals("7"))
  {
      ctype = "Indigo" ;
      categ="A/C & Non - A/C";
      seat="4+1";
  }
  else if(pos.equals("8"))
  {
      ctype = "Tempo";
      categ="A/C & Non - A/C";
      seat="14+1";
  }
  else if(pos.equals("9"))
  {
      ctype = "Xylo";
      categ="A/C & Non - A/C";
      seat="7+1";
  }
  else if(pos.equals("10"))
  {
      ctype = "Fiesta" ;
      categ="A/C";
      seat="4+1";
  }
  else if(pos.equals("11"))
  {
      ctype = "Dezire";
      categ="A/C & Non - A/C";
      seat="4+1";
  }

            picklocation=extras.getString("picklocation");
           droplocation=extras.getString("droplocation");
           String dm=distanc;
           TextView sp=(TextView)findViewById(R.id.spoint);
           TextView ep=(TextView)findViewById(R.id.epoint);
           TextView ty=(TextView)findViewById(R.id.category);
            TextView car=(TextView)findViewById(R.id.carname);
            TextView seating=(TextView)findViewById(R.id.seat);

            sp.setText(picklocation);
           ep.setText(droplocation);
           ty.setText(categ);
           car.setText(ctype);
           seating.setText(seat);
           String digits = dm.replaceAll("km", "");
           double fdist=Double.parseDouble(digits);
           final double distr=fdist;
          approxkm=(int)fdist;
            fdis=String.valueOf(approxkm);

            // Toast.makeText(getApplicationContext(),String.valueOf(distr),Toast.LENGTH_LONG).show();
           if(pos.equals("0"))
            {
                mincost=100;
                km=4;
                    if (categ.equals("Non - A/C")) {
                        costkm = 6;
                        if (distr <= km) {
                            costkm = mincost;
                        } else if (distr > km) {
                            costkm = (distr - km) * costkm + mincost;
                        }
                    } else {
                        costkm = 7;
                        if (distr <= km)
                            costkm = mincost;
                        else
                            costkm = (distr - km) * costkm + mincost;
                    }


            }


           else if(pos.equals("1"))
            {
                mincost=100;
                km=4;

                    if (categ.equals("Non - A/C")) {
                        costkm = 6;
                        if (distr <= km) {
                            costkm = mincost;
                        } else if (distr > km) {
                            costkm = (distr - km) * costkm + mincost;
                        }
                    } else {
                        costkm = 6;
                        if (distr <= km)
                            costkm = mincost;
                        else
                            costkm = (distr - km) * costkm + mincost;
                    }


            }

            else if(pos.equals("2"))
            {

                mincost=300;
                km=4;

                    if (categ.equals("Non - A/C")) {
                        costkm = 9;
                        if (distr <= km)
                            costkm = mincost;
                        else
                            costkm = (distr - km) * costkm + mincost;
                    } else {
                        costkm = 9;
                        if (distr <= km)
                            costkm = mincost;
                        else
                            costkm = (distr - km) * costkm + mincost;
                    }


            }
           else if(pos.equals("3"))
           {

               mincost=300;
               km=4;

               if (categ.equals("Non - A/C")) {
                   costkm = 12;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               } else {
                   costkm = 15;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               }


           }
           else if(pos.equals("4"))
           {

               mincost=200;
               km=4;

               if (categ.equals("Non - A/C")) {
                   costkm = 9.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               } else {
                   costkm = 9.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               }


           }



           else if(pos.equals("5"))
           {

               mincost=200;
               km=4;

               if (categ.equals("Non - A/C")) {
                   costkm = 7.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               } else {
                   costkm = 8.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               }


           }


           else if(pos.equals("6"))
           {

               mincost=120;
               km=4;

               if (categ.equals("Non - A/C")) {
                   costkm = 8.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               } else {
                   costkm = 8.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               }


           }



           else if(pos.equals("7"))
           {

               mincost=120;
               km=4;

               if (categ.equals("Non - A/C")) {
                   costkm = 7.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               } else {
                   costkm = 8.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               }


           }


           else if(pos.equals("8"))
           {

               mincost=200;
               km=4;

               if (categ.equals("Non - A/C")) {
                   costkm = 9;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               } else {
                   costkm = 10;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               }


           }

           else if(pos.equals("9"))
           {

               mincost=200;
               km=4;

               if (categ.equals("Non - A/C")) {
                   costkm = 7.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               } else {
                   costkm = 8.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               }


           }

           else if(pos.equals("10"))
           {

               mincost=120;
               km=4;

               if (categ.equals("Non - A/C")) {
                   costkm = 0;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               } else {
                   costkm = 8.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               }


           }
           else if(pos.equals("11"))
           {

               mincost=120;
               km=4;

               if (categ.equals("Non - A/C")) {
                   costkm = 7.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               } else {
                   costkm = 8.5;
                   if (distr <= km)
                       costkm = mincost;
                   else
                       costkm = (distr - km) * costkm + mincost;
               }


           }
           int co =(int)costkm;
            tv2.setText(String.valueOf(co));

           tv1.setText("TOTAL DISTANCE: "+String.valueOf(distr)+" KMs");
//The key argument here must match that used in the other activity
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(getApplicationContext(),"Opening Dialpad",Toast.LENGTH_SHORT).show();
                Intent dial = new Intent(Intent.ACTION_DIAL);
                // Send phone number to intent as data
                dial.setData(Uri.parse("tel:" + "8110001000"));
                // Start the dialer app activity with number
                startActivity(dial);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


             d.show();


               // Toast.makeText(getApplicationContext(),"Your Car is Booked",Toast.LENGTH_SHORT).show();
            }
        });



        //int dis = Integer.parseInt(distanc);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
         //   Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1,1, this);

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
map =googleMap;
    }



    void booking(final String tok,final String pickdate)
    {
      //  Toast.makeText(getApplicationContext(),tok,Toast.LENGTH_SHORT).show();
     //  Toast.makeText(getApplicationContext(),fdis,Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(Request.Method.POST, "http://***************/mobile/api/Booking/BookMyTrip", new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

            pd.dismiss();
          //  Toast.makeText(getApplicationContext(),response+"Your Car ha",Toast.LENGTH_LONG).show();
            gp(unum,fne);
            AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
            builder.setMessage("Your car has been booked, soon our driver will contact you").setTitle("Info")
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
        }, new Response.ErrorListener() {
            String a[]=new String[100];
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                String message = "";
                String body = "";
          if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
              try {
                  body = new String(error.networkResponse.data, "UTF-8");
                  Pattern p = Pattern.compile("\\{(.*?)\\}");
                  Matcher m = p.matcher(body);
                  int i = 0;

                  while (m.find()) {
                      a[i] = m.group(1).replace("\"", "").replace("Message","").replaceAll("[\\-\\+\\.\\^:,]", "");
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
              try {
                  body = new String(error.networkResponse.data, "UTF-8");
                  Pattern p = Pattern.compile("\\{(.*?)\\}");
                  Matcher m = p.matcher(body);
                  int i = 0;

                  while (m.find()) {
                      a[i] = m.group(1).replace("\"", "").replace("Message","").replaceAll("[\\-\\+\\.\\^:,]", "");
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
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+tok);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date date = new Date();

                Map<String,String> map = new HashMap<String, String>();
                map.put("BookingDate", dateFormat.format(date));
                map.put("JourneyType", "1");
                map.put("PickupDateTime", pickdate);
                map.put("DropDate", pickdate);
                map.put("PickupLocation", picklocation);
                map.put("DropLocation", droplocation);
                map.put("VehicleCategory", pos);
                map.put("Facility", "1");
                map.put("PackageType", "1");
                map.put("Duration", "2");
                map.put("ApproxKM",fdis);
                return map;
            }

        };
        SQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    void test(final String tok)
    {
        //  Toast.makeText(getApplicationContext(),tok,Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(Request.Method.POST, "http://***************/mobile/api/Booking/BookMyTrip", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


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
                headers.put("Authorization", "Bearer "+tok);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                Map<String,String> map = new HashMap<String, String>();
                map.put("BookingDate", bdate);
                map.put("JourneyType", "1");
                map.put("PickupDateTime", "");
                map.put("DropDate", "07/15/2018");
                map.put("PickupLocation", picklocation);
                map.put("DropLocation", droplocation);
                map.put("VehicleCategory", pos);
                map.put("Facility", "1");
                map.put("PackageType", "1");
                map.put("Duration", "2");
                map.put("ApproxKM",fdis);
                return map;
            }

        };
        SQueue.add(request);

    }


    private void getTarrif() {

        String url = "http://***************/mobile/api/tariffList";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("TariffData"); //To get the inventory as an array

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject car= jsonArray.getJSONObject(i);
                              carid[i] =car.getString("CatetegoryID");
                                catname[i] =car.getString("CategoryName");
                                seat[i]=car.getString("SeatCapacity");
                               ac[i]=car.getString("AC");
                                nonac[i]=car.getString("NonAC");
                                minchrge[i]=car.getString("MinCharge");
                               minkm[i]=car.getString("MinKMs");
                               nonacrg[i]=car.getString("NonAcCharge");
                                accrg[i]=car.getString("AcCharge");

//Toast.makeText(getApplicationContext(),carid[i]+"\n"+catname[i]+"\n"+seat[i]+"\n"+ac[i]+"\n"+nonac[i]+"\n"+minchrge[i]+"\n"+minkm[i]+"\n"+nonacrg[i]+"\n"+accrg[i]+" ",Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }


Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }
        });

        SQueue.add(request);
    }


    void gp(final String phon, final String una)
    {
        String url = "http://bhashsms.com/api/sendmsg.php?user=megala&pass=rmt1729&sender=MEGALA&phone="+phon+"&text=Thank%20You%20For%20Booking%20Our%20Service.%20Our%20Driver%20Will%20Contact%20You%20Shortly.&priority=ndnd&stype=normal";
        StringRequest strReq = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                       // Toast.makeText(getApplicationContext(),""+phon,Toast.LENGTH_LONG).show();

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
