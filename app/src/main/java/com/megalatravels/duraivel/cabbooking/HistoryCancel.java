package com.megalatravels.duraivel.cabbooking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;

   public class HistoryCancel extends AppCompatActivity {

       public boolean onOptionsItemSelected(MenuItem item) {
           int id = item.getItemId();

           if (id == android.R.id.home) {
               onBackPressed();
               return true;
           }

           return super.onOptionsItemSelected(item);
       }

    RequestQueue SQueue;
    SharedPreferences pref;
    String f;
    ArrayAdapter adapter;
    AlertDialog alert;
    RecyclerView recyclerView;
    ArrayList<ModelHistory> productList;
    String[] from;
    String[] to;
    String[] id;
    String[] cancel ;
    String[] date;
    ProgressDialog pd;

    @RequiresApi(api = Build.VERSION_CODES.O)

    void adapt(String from, String to, String id, String cancel, String date ) throws ParseException
    {
      String ond[]=date.split("T");
        String d=ond[0];
String esc[] =d.split("-");
String findate=esc[2]+"-"+esc[1]+"-"+esc[0];
    /*    String dm[]=d.split("-");
        String mn= Month.of(Integer.parseInt(dm[1])).name();
        String fulldate=mn+" "+dm[1]+","+dm[0];*/
        productList.add(new ModelHistory("On "+findate+" "+ond[1],from, to,id,cancel));
        //productList.add(new ModelProduct(R.drawable.indica,"Indica","140 kMph","Tata","Min. Rs.100/4 kms","Additional Charges 15 Rs/km","A/C"));
    }
    void setList(String authtoken) {
        HistoryAdapter adapter = new HistoryAdapter(this, productList,authtoken);
        recyclerView.setAdapter(adapter);
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_cancel);
        SQueue = Volley.newRequestQueue(this);
        pd=new ProgressDialog(HistoryCancel.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Requesting...");
        pd.show();
        pd.setCancelable(false);
        from=new String[50];
       to=new String[50];
       id=new String[50];
      cancel=new String[50];
     date=new String[50];
        recyclerView = findViewById(R.id.rs);
        productList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLayoutManager = layoutManager;
        recyclerView.setLayoutManager(rvLayoutManager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("sma", Context.MODE_MULTI_PROCESS);
        if(pref.contains("auth"))
        {
            f= pref.getString("auth", null).toString();
            //  Toast.makeText(getApplicationContext(),f,Toast.LENGTH_SHORT).show();// a="1";
        }
        // 0 - for private mode
        //SharedPreferences.Editor editor = pref.edit();
        //CancelBook(f);
        getTarrif(f);
        //setList(f);
       // String cusname = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.head)).getText().toString();
    }


    private void getTarrif(final String tok)
    {
        String url = "http://***************/mobile/api/Booking/History";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        try
                        {
                            JSONArray jsonArray = response.getJSONArray("HistoryData"); //To get the inventory as an array
                            int i;
                            for (i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject book = jsonArray.getJSONObject(i);
                                from[i] = book.getString("From");
                                to[i] = book.getString("To");
                                id[i] = book.getString("BookingID");
                                cancel[i] = book.getString("Cancelled");
                                date[i] = book.getString("BookingDate");
                                adapt(from[i],to[i],id[i],cancel[i],date[i]);
                                if(i==jsonArray.length()-1)
                                {
                                    setList(tok);
                                }
                            }
                              //  Toast.makeText(getApplicationContext(),String.valueOf(i)+String.valueOf(jsonArray.length()-1),Toast.LENGTH_SHORT).show();

                        }
                        catch (JSONException e)
                        {
                           // Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Authorization has been denied for this request!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! This may failed to update the tarrif details";
                }
                //   prog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryCancel.this);
                builder.setMessage(message).setTitle("Info")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                // alert.dismiss();
                            }
                        });
                alert = builder.create();
                alert.show();
                //adaptoffline();


             //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        })
        {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+tok);
                return headers;
            }
        };

        SQueue.add(request);
    }
    private void CancelBook(final String tok) {

        String url = "http://***************/mobile/CancelBooking/1057";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  prog.dismiss();
                          //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show(); //To get the inventory as an array
                            int i;
                              /*  for (i = 0; i < jsonArray.length(); i++) {
                                    JSONObject car= jsonArray.getJSONObject(i);
                                    carid[i] =car.getString("CatetegoryID");
                                    catname[i] =car.getString("CategoryName");
                                    seat[i]=car.getString("SeatCapacity");
                                    aci[i]=car.getString("AC");
                                    nonac[i]=car.getString("NonAC");
                                    minchrge[i]=car.getString("MinCharge");
                                    minkm[i]=car.getString("MinKMs");
                                    nonacrg[i]=car.getString("NonAcCharge");
                                    accrg[i]=car.getString("AcCharge");
                                    //Toast.makeText(getApplicationContext(),carid[i]+"\n"+catname[i]+"\n"+seat[i]+"\n"+aci[i]+"\n"+nonac[i]+"\n"+minchrge[i]+"\n"+minkm[i]+"\n"+nonacrg[i]+"\n"+accrg[i]+" ",Toast.LENGTH_SHORT).show();
                                    String typ = null;
                                    String accharge=null;
                                    if(aci[i].equals("true")&&nonac[i].equals("true"))
                                    {
                                        typ="A/C & Non-A/C";
                                        accharge=nonacrg[i];
                                    }
                                    else if(aci[i].equals("true") &&nonac[i].equals("false"))
                                    {
                                        typ="A/C";
                                        accharge=accrg[i];
                                    }
                                    else if(aci[i].equals("false") &&nonac[i].equals("true"))
                                    {
                                        typ="Non-A/C";
                                        accharge=nonacrg[i];

                                    }
                                    adapt(carid[i],catname[i],seat[i],"Tata",minchrge[i],minkm[i],accharge,typ);

                                    if(i==11)
                                    {
                                        setList();
                                    }


                                }

*/
                        } catch (Exception e) {
                          //  Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    message = "Connection TimeOut! This may failed to update the tarrif details";
                }
           //     Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                //   prog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryCancel.this);
                builder.setMessage(message).setTitle("Info")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                // alert.dismiss();
                            }
                        });
                // alert = builder.create();
                //alert.show
                // ();
                //adaptoffline();


            }
        })
        {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+tok);
                return headers;
            }
        };

        SQueue.add(request);
    }
}
