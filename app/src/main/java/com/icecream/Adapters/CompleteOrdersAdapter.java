package com.icecream.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icecream.Activities.HomeActivity;
import com.icecream.Fragments.FragmentCompleteOrdersDetail;
import com.icecream.Fragments.FragmentConfirmOrdersDetail;
import com.icecream.Models.CompleteOrders.Msg;
import com.icecream.Models.CompleteOrders.OrderDetail;
import com.icecream.R;
import com.icecream.Utils.MyApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompleteOrdersAdapter extends RecyclerView.Adapter<CompleteOrdersAdapter.MyViewHolder> {

    private List<Msg> arrOrders;
    private Activity activityContext;

    public CompleteOrdersAdapter(Activity activity, List<Msg> arrResouceslist) {
        this.arrOrders =arrResouceslist;
        this.activityContext=activity;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtDate,created_date,txtAmount,txtCode;

        public ImageView imgEdit,imgDone,imgCancel,imgPending;
        public LinearLayout lnMainlayout;

        public MyViewHolder(View view) {
            super(view);
            txtCode = (TextView) view.findViewById(R.id.txtCode);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            created_date = (TextView) view.findViewById(R.id.created_date);
            txtAmount = (TextView) view.findViewById(R.id.txtAmount);
            imgEdit= (ImageView) view.findViewById(R.id.imgEdit);
            imgDone= (ImageView) view.findViewById(R.id.imgDone);
            imgPending= (ImageView) view.findViewById(R.id.imgPending);
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

        holder.txtCode.setText("#"+arrOrders.get(position).getCustomerOrderId());
        holder.txtName.setText(arrOrders.get(position).getFullName());
        holder.txtAmount.setText("Rs. "+arrOrders.get(position).getTotalAmount());
        String date[]=arrOrders.get(position).getOrderDate().split(" ");
        String date1= MyApplication.parseDateToddMMyyyy(date[0],MyApplication.yyyy_mm_dd,MyApplication.dd_mm_yyyy);
        holder.txtDate.setText(date1);

        final String date_up[]=arrOrders.get(position).getUpdatedOn().split(" ");
        String _date= MyApplication.parseDateToddMMyyyy(date_up[0],MyApplication.yyyy_mm_dd,MyApplication.dd_mm_yyyy);
        holder.created_date.setText(_date);


        holder.lnMainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFragment(new FragmentCompleteOrdersDetail(), position);


               /* Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        FragmentTransaction transaction = activityContext.getFragmentManager().beginTransaction();
                        Fragment newFragment;
                        newFragment = new FragmentCompleteOrdersDetail();
                        String strFragmentTag = newFragment.toString();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Details", arrOrders.get(position));
                        newFragment .setArguments(bundle);
                        transaction.replace(R.id.fragmentmain, newFragment,strFragmentTag);
                      //  transaction.addToBackStack(strFragmentTag);
                        transaction.commit();

                    }
                }, MyApplication.RippleEffectsTime);*/
            }
        });
        holder.imgDone.setVisibility(View.GONE);
        holder.imgEdit.setVisibility(View.GONE);
        holder.imgCancel.setVisibility(View.GONE);
        if(HomeActivity.preferenceUtils.getloginType().equalsIgnoreCase("Admin"))
        {
            holder.imgPending.setVisibility(View.VISIBLE);
        }else
        {
            holder.imgPending.setVisibility(View.GONE);
        }


        holder.imgPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.isInternetAvailable(activityContext)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activityContext, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle(activityContext.getResources().getString(R.string.app_name));
                    builder.setMessage("Are you sure want to Convert to Pending Order?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            new CallWS(arrOrders.get(position).getOrderDetails(),arrOrders.get(position).getOrderId(),position).execute("MarkAsPending");
                        }
                    });
                    builder.show();
                } else {
                    ((HomeActivity)activityContext).ShowAlert("Internet connection not available.");
                }
            }
        });

    }

    private void addFragment(Fragment fragment,int position){
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = activityContext.getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Details", arrOrders.get(position));
            fragment .setArguments(bundle);
            ft.add(R.id.fragmentmain, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public int getItemCount() {
        return arrOrders.size();
    }

    private class CallWS extends AsyncTask<String, String, String> {

        public  List<OrderDetail> order;
        Integer position;
        String OrderID="";

        public CallWS(List<OrderDetail> arrOrders,String orderId, int pos) {
            order = arrOrders;
            position=pos;
            OrderID=orderId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MyApplication.showProgressDialog(activityContext);
        }

        @Override
        protected String doInBackground(String... data) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://patelicecream.in/admin/api/orders.php");

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("ActionType", data[0]));
                nameValuePairs.add(new BasicNameValuePair("OrderId", OrderID));
                for (int i = 0; i < order.size(); i++) {

                    String strIDs = "ProductList[" + i + "][ProductId]";
                    String strQtys = "ProductList[" + i + "][Qty]";

                    nameValuePairs.add(new BasicNameValuePair(strIDs, order.get(i).getProductId()));
                    nameValuePairs.add(new BasicNameValuePair(strQtys, order.get(i).getActualQty()));

                    Log.e("Qty=="+i,order.get(i).getActualQty());
                    Log.e("Product=="+i,order.get(i).getProductId());
                }

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //execute http post
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseBody = EntityUtils.toString(entity);
                    Log.e("Response Update Order", responseBody);
                    return responseBody;
                }
            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MyApplication.dismissProgressDialog();

            try {
                JSONObject loginObj = new JSONObject(s);

                if (loginObj.getInt("status") == 1) {

                    ((HomeActivity) activityContext).MenuItemsClicks(MyApplication.PENDING_ORDERS);

                    ((HomeActivity) activityContext).ShowAlert("Order Update Successfully");
                } else {

                    ((HomeActivity) activityContext).ShowAlert("Something went wrong, Plz try again later.");
                }

            } catch (Exception e) {

            }

        }
    }


}