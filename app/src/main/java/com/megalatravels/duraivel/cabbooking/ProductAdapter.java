package com.megalatravels.duraivel.cabbooking;

import android.content.Context;
import android.content.Intent;
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

import com.megalatravels.duraivel.cabbooking.ModelProduct;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>

{
    private Context mContext;
    private  ArrayList<ModelProduct> mList;
    private  OnItemClickListener mlistener;
    public  interface  OnItemClickListener
    {
        void OnItemClick(int position);
        void OnBookClick(int position);
    }
    public  void setOnItemClickListener(OnItemClickListener listener)
    {
        mlistener=listener;
    }

    ProductAdapter(Context context, ArrayList<ModelProduct> list)
    {
        mContext =context;
        mList=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(mContext);
        View view= layoutInflater.inflate(R.layout.activity_listview,parent,false);
        ViewHolder viewHolder =new ViewHolder(view,mlistener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelProduct modelProduct = mList.get(position);
        ImageView image= holder.item_image;
        TextView hed,des,company,cost,amnt,cat;
        hed=holder.h1;
        des =holder.desc;
        company=holder.desc1;
        cost=holder.cst;
        amnt=holder.amt;
        cat=holder.cat;

        image.setImageResource(modelProduct.getImage());
        hed.setText(modelProduct.getHead());
        des.setText(modelProduct.getDesc());
        company.setText(modelProduct.getComp());
        cost.setText(modelProduct.getCost());
        amnt.setText(modelProduct.getAmount());
        cat.setText(modelProduct.getCg());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public  static class ViewHolder extends  RecyclerView.ViewHolder
    {
        ImageView item_image;
        TextView h1,desc,desc1,cst,amt,cat;
        Button book;
        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            item_image=itemView.findViewById(R.id.item_img);
            book=itemView.findViewById(R.id.book);
            h1=itemView.findViewById(R.id.head);
            desc=itemView.findViewById(R.id.desc);
            desc1=itemView.findViewById(R.id.desc1);
            cst=itemView.findViewById(R.id.cst);
            amt=itemView.findViewById(R.id.amount);
            cat=itemView.findViewById(R.id.cg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.OnItemClick(position);
                        }
                    }
                }
            });

            book.setOnClickListener(new View.OnClickListener() {
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
            });
        }
    }
}
