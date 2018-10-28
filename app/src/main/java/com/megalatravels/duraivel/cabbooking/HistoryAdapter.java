package com.megalatravels.duraivel.cabbooking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>

{
    String f="";
    String auth;
AlertDialog alert;
ProgressDialog pd;
    RequestQueue SQueue;
    private void CancelBook(final String tok, final String bookid) {
pd=new ProgressDialog(sContext);
pd.setTitle("Please Wait");
pd.setMessage("Requesting...");
pd.show();
pd.setCancelable(false);
        String url = "http://*********/mobile/api/Booking/CancelBooking/"+bookid;
        StringRequest strReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // Toast.makeText(sContext,response+"You Cancelled Your Booking",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(sContext);
                        builder.setMessage("Booking Cancelled Successfully").setTitle("Info")
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
pd.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(sContext);
                        builder.setMessage("Something Went Wrong").setTitle("Info")
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
                protected Response<String> parseNetworkResponse (NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    return super.parseNetworkResponse(response);

                }
        };

        SQueue.add(strReq);
    }
    private Context sContext;
    private ArrayList<ModelHistory> sList;
    private  OnItemClickListener slistener;
    public  interface  OnItemClickListener

    {
        void OnItemClick(int position);
        void OnBookClick(int position);
    }
    public  void setOnItemClickListener(OnItemClickListener listener)
    {
        slistener=listener;
    }

    HistoryAdapter(Context context, ArrayList<ModelHistory> list,String auth)
    {
        sContext =context;
        sList=list;
        this.auth=auth;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater =LayoutInflater.from(sContext);
        View view= layoutInflater.inflate(R.layout.bookhistory,parent,false);
        ViewHolder viewHolder =new ViewHolder(view,slistener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ModelHistory modelProduct = sList.get(position);
        final TextView date,from,to,bid,cancelt;
        final TextView cancelim;
        date=holder.date;
        cancelim=holder.cancel;
        from =holder.from;

        to=holder.to;
        cancelt=holder.cancelt;
        date.setText(modelProduct.getDate());
        from.setText(modelProduct.getFrom());
        to.setText(modelProduct.getTo());
              if(modelProduct.getCancel().equals("true"))
        {
        cancelim.setVisibility(View.GONE);
            cancelt.setText("BOOKING HAS CANCELLED");
        }

        final String s=modelProduct.getFrom();
        ((ViewHolder)holder).cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SQueue= Volley.newRequestQueue(sContext);
                Toast.makeText(sContext,s,Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(sContext);
                builder.setMessage("Do you want to cancel your booking?").setTitle("Info")
                        .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                CancelBook(auth,modelProduct.getBid());
                            }
                        }).setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
alert.dismiss();
                    }
                });
                alert = builder.create();
                alert.show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return sList.size();
    }


    public  static class ViewHolder extends  RecyclerView.ViewHolder
    {

        TextView date,from,cancelt,to,bid;
      TextView cancel;
        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            from=itemView.findViewById(R.id.from);
            to=itemView.findViewById(R.id.to);
            cancel=itemView.findViewById(R.id.can);

            cancelt=itemView.findViewById(R.id.canceled);
          /*  cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.OnBookClick(position);
                        }
                    }
                }
            });*/

        }
    }
}
