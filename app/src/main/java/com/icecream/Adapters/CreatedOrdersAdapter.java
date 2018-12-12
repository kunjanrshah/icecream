package com.icecream.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.icecream.Fragments.FragmentCreateOrderListing;
import com.icecream.Models.CreateOrder.OrderDetails;
import com.icecream.R;
import com.icecream.Utils.SharepreferenceUtils;

import java.util.List;

public class CreatedOrdersAdapter extends RecyclerView.Adapter<CreatedOrdersAdapter.MyViewHolder> {

    private List<OrderDetails> arrOrders;
    private Activity activityContext;
    SharepreferenceUtils sharepreferenceUtils;

    public CreatedOrdersAdapter(Activity activity, List<OrderDetails> arrResouceslist) {
        this.arrOrders =arrResouceslist;
        this.activityContext=activity;
        sharepreferenceUtils=new SharepreferenceUtils(activity);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtcategory,txtQty,txtPrice;

        public ImageView imgDelete;
        public LinearLayout lnMainlayout;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtcategory = (TextView) view.findViewById(R.id.txtcategory);
            txtQty = (TextView) view.findViewById(R.id.txtQty);
            txtPrice = (TextView) view.findViewById(R.id.txtPrice);
            imgDelete= (ImageView) view.findViewById(R.id.imgDelete);
            lnMainlayout= (LinearLayout) view.findViewById(R.id.lnMainlayout);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_createorderdetail, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.txtName.setText(arrOrders.get(position).ProductName);
        holder.txtcategory.setText(arrOrders.get(position).CategoryName);

        Double totalPrice=Double.parseDouble(arrOrders.get(position).PricePerKG) * (Integer.parseInt(arrOrders.get(position).Qty));

        holder.txtPrice.setText(""+totalPrice);
        holder.txtQty.setText(arrOrders.get(position).Qty);

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                arrOrders.remove(position);

                Fragment oldFragment=activityContext.getFragmentManager().findFragmentById(R.id.fragmentmain);

                if(oldFragment instanceof FragmentCreateOrderListing){
                    ((FragmentCreateOrderListing)oldFragment).getTotalPrice();
                    ((FragmentCreateOrderListing)oldFragment).getTotalQuantity();

                }


                notifyDataSetChanged();

            }
        });

    }


    @Override
    public int getItemCount() {
        return arrOrders.size();
    }



}