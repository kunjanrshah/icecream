package com.icecream.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icecream.Fragments.FragmentCompleteOrdersDetail;
import com.icecream.Models.CompleteOrders.Msg;
import com.icecream.R;
import com.icecream.Utils.MyApplication;

import java.util.List;

public class CompleteOrdersAdapter extends RecyclerView.Adapter<CompleteOrdersAdapter.MyViewHolder> {

    private List<Msg> arrOrders;
    private Activity activityContext;

    public CompleteOrdersAdapter(Activity activity, List<Msg> arrResouceslist) {
        this.arrOrders =arrResouceslist;
        this.activityContext=activity;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtDate,txtAmount;

        public ImageView imgEdit,imgDone,imgCancel;
        public LinearLayout lnMainlayout;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtAmount = (TextView) view.findViewById(R.id.txtAmount);
            imgEdit= (ImageView) view.findViewById(R.id.imgEdit);
            imgDone= (ImageView) view.findViewById(R.id.imgDone);
            imgCancel= (ImageView) view.findViewById(R.id.imgCancel);
            lnMainlayout= (LinearLayout) view.findViewById(R.id.lnMainlayout);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_pendingorders, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.txtName.setText(arrOrders.get(position).getFullName());
        holder.txtAmount.setText(arrOrders.get(position).getTotalAmount());
        String date[]=arrOrders.get(position).getOrderDate().split(" ");
        Log.d(CompleteOrdersAdapter.class.getSimpleName(),"date: "+arrOrders.get(position).getOrderDate().split(" "));
        holder.txtDate.setText(date[0]);

        holder.lnMainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentTransaction transaction = activityContext.getFragmentManager().beginTransaction();
                Fragment newFragment;
                newFragment = new FragmentCompleteOrdersDetail();
                String strFragmentTag = newFragment.toString();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Details", arrOrders.get(position));
                newFragment .setArguments(bundle);
                transaction.add(R.id.fragmentmain, newFragment,strFragmentTag);
                transaction.addToBackStack(strFragmentTag);
                transaction.commit();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {



                    }
                }, MyApplication.RippleEffectsTime);
            }
        });
        holder.imgDone.setVisibility(View.VISIBLE);
        holder.imgEdit.setVisibility(View.GONE);
        holder.imgCancel.setVisibility(View.GONE);

        holder.imgDone.setOnClickListener(new View.OnClickListener() {
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