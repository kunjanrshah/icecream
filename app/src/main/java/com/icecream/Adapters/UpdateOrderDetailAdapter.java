package com.icecream.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.icecream.Models.PendingOrder.OrderDetail;
import com.icecream.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateOrderDetailAdapter extends RecyclerView.Adapter<UpdateOrderDetailAdapter.MyViewHolder> {

    public static  List<OrderDetail> arrOrders;
    private Activity activityContext;

    public UpdateOrderDetailAdapter(Activity activity, List<OrderDetail> arrResouceslist) {
        arrOrders=new ArrayList<>();
        this.arrOrders =arrResouceslist;
        this.activityContext=activity;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public EditText edtQuantity;
        public LinearLayout lnMainlayout;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            edtQuantity= (EditText) view.findViewById(R.id.edtQuantity);

            lnMainlayout= (LinearLayout) view.findViewById(R.id.lnMainlayout);


        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_updateorderdetail, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtName.setText(arrOrders.get(position).getProductName());
        holder.edtQuantity.setText(arrOrders.get(position).getActualQty());

        holder.edtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(holder.edtQuantity.getText().toString().trim().length()>0) {
                    arrOrders.get(position).setActualQty(holder.edtQuantity.getText().toString());
                }else{
                    arrOrders.get(position).setActualQty("0");
                }
                holder.edtQuantity.setSelection(holder.edtQuantity.getText().toString().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        try {
            notifyDataSetChanged();
        }catch (Exception e){

        }
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