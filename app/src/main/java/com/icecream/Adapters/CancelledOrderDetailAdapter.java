package com.icecream.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icecream.Models.CancelledOrders.OrderDetail;
import com.icecream.R;

import java.util.List;

public class CancelledOrderDetailAdapter extends RecyclerView.Adapter<CancelledOrderDetailAdapter.MyViewHolder> {

    private List<OrderDetail> arrOrders;
    private Activity activityContext;

    public CancelledOrderDetailAdapter(Activity activity, List<OrderDetail> arrResouceslist) {
        this.arrOrders =arrResouceslist;
        this.activityContext=activity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtQty,txtAmount,txtCategory;

        public LinearLayout lnMainlayout;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtQty = (TextView) view.findViewById(R.id.txtQty);
            txtAmount = (TextView) view.findViewById(R.id.txtAmount);
            txtCategory = (TextView) view.findViewById(R.id.txtCategory);
            lnMainlayout= (LinearLayout) view.findViewById(R.id.lnMainlayout);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_orderdetail, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.txtName.setText(arrOrders.get(position).getProductName());
        holder.txtCategory.setText(arrOrders.get(position).getCategory());
        holder.txtAmount.setText(arrOrders.get(position).getActualTotalPrice());
        holder.txtQty.setText(arrOrders.get(position).getActualQty());


        holder.lnMainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    @Override
    public int getItemCount() {
        return arrOrders.size();
    }



}