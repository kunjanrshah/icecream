package com.icecream.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.icecream.Activities.HomeActivity;
import com.icecream.Fragments.FragmentCancelledOrders;
import com.icecream.Fragments.FragmentCancelledOrdersDetail;
import com.icecream.Fragments.FragmentCompleteOrdersDetail;
import com.icecream.Models.CancelledOrders.Msg;
import com.icecream.Models.CancelledOrders.OrderDetail;
import com.icecream.R;
import com.icecream.Utils.MyApplication;
import com.icecream.Utils.SharepreferenceUtils;
import com.icecream.Webservices.RetrofitAPI;
import com.icecream.Webservices.WebserviceInterface;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CancelledOrdersAdapter extends RecyclerView.Adapter<CancelledOrdersAdapter.MyViewHolder> {

    private List<Msg> arrOrders;
    private Activity activityContext;
    SharepreferenceUtils sharepreferenceUtils;
    Dialog dialog;

    public CancelledOrdersAdapter(Activity activity, List<Msg> arrResouceslist) {
        this.arrOrders =arrResouceslist;
        this.activityContext=activity;
        sharepreferenceUtils=new SharepreferenceUtils(activity);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtDate,txtAmount,txtCode,created_date;

        public ImageView imgEdit,imgDone,imgCancel;
        public LinearLayout lnMainlayout;
        public CardView FeatureCard;

        public MyViewHolder(View view) {
            super(view);

            txtCode = (TextView) view.findViewById(R.id.txtCode);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            created_date= (TextView) view.findViewById(R.id.created_date);
            txtAmount = (TextView) view.findViewById(R.id.txtAmount);
            imgEdit= (ImageView) view.findViewById(R.id.imgEdit);
            imgDone= (ImageView) view.findViewById(R.id.imgDone);
            imgCancel= (ImageView) view.findViewById(R.id.imgCancel);
            lnMainlayout= (LinearLayout) view.findViewById(R.id.lnMainlayout);
            FeatureCard= (CardView) view.findViewById(R.id.FeatureCard);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_pendingorders, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtCode.setText("#"+arrOrders.get(position).getCustomerOrderId());
        holder.txtName.setText(arrOrders.get(position).getFullName());

        final String date[]=arrOrders.get(position).getOrderDate().split(" ");
        String _date= MyApplication.parseDateToddMMyyyy(date[0],MyApplication.yyyy_mm_dd,MyApplication.dd_mm_yyyy);
        holder.created_date.setText(_date);

        final String date1[]=arrOrders.get(position).getUpdatedOn().split(" ");
        String _date1= MyApplication.parseDateToddMMyyyy(date1[0],MyApplication.yyyy_mm_dd,MyApplication.dd_mm_yyyy);
        holder.txtDate.setText(_date1);

        holder.txtAmount.setText("Rs. "+arrOrders.get(position).getActualAmount());
       /* final String date[]=arrOrders.get(position).getOrderDate().split(" ");
        String date1= MyApplication.parseDateToddMMyyyy(date[0],MyApplication.yyyy_mm_dd,MyApplication.dd_mm_yyyy);
        holder.txtDate.setText(date1);*/

        holder.imgEdit.setVisibility(View.GONE);
        holder.imgDone.setVisibility(View.GONE);
        holder.imgCancel.setVisibility(View.GONE);

        /*if(sharepreferenceUtils.getloginType().trim().equals("Admin")){
            holder.imgEdit.setVisibility(View.VISIBLE);
            holder.imgDone.setVisibility(View.VISIBLE);
            holder.imgCancel.setVisibility(View.VISIBLE);
        }else{

        }*/

        holder.lnMainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                addFragment(new FragmentCancelledOrdersDetail(), position);
               /* Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        FragmentTransaction transaction = activityContext.getFragmentManager().beginTransaction();
                        Fragment newFragment;
                        newFragment = new FragmentCancelledOrdersDetail();
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
        holder.imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MyApplication.isInternetAvailable(activityContext)) {
                    new CallWS(arrOrders.get(position).getOrderDetails(),arrOrders.get(position).getOrderId(),position).execute("CompleteOrder");
                } else {
                    ((HomeActivity)activityContext).ShowAlert("Internet connection not available.");
                }


            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowUpdateDialog(arrOrders.get(position),position);

            }
        });

        holder.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MyApplication.isInternetAvailable(activityContext)) {

                    callWebserviceCancelOrder(arrOrders.get(position).getOrderId());

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

    public void ShowUpdateDialog(final Msg msg, final int pos){
        dialog = new Dialog(activityContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = dialog.getWindow();

        Rect displayRectangle = new Rect();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater)activityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.updateorderdialog, null);
        layout.setMinimumWidth((int)(displayRectangle.width()));
        layout.setMinimumHeight((int)(displayRectangle.height()));

        dialog.setContentView(layout);

        RecyclerView recycl_orders= (RecyclerView) dialog.findViewById(R.id.recycl_orders);
        Button btnUpdate= (Button) dialog.findViewById(R.id.btnUpdate);

       // UpdateOrderDetailAdapter  adapter=new UpdateOrderDetailAdapter(activityContext,msg.getOrderDetails());
        LinearLayoutManager llm = new LinearLayoutManager(activityContext.getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycl_orders.setLayoutManager(llm);
        recycl_orders.setItemAnimator(new DefaultItemAnimator());
     //   recycl_orders.setAdapter(adapter);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (MyApplication.isInternetAvailable(activityContext)) {
                     dialog.dismiss();

                 //   new CallWS(UpdateOrderDetailAdapter.arrOrders,msg.getOrderId(),pos).execute("");

                } else {
                    ((HomeActivity)activityContext).ShowAlert("Internet connection not available.");
                }



            }
        });

        dialog.show();

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

                    ((HomeActivity) activityContext).MenuItemsClicks("CancelledOrders");

                    ((HomeActivity) activityContext).ShowAlert("Order Update Successfully");
                } else {

                    ((HomeActivity) activityContext).ShowAlert("Something went wrong, Plz try again later.");
                }

            } catch (Exception e) {

            }

        }
    }


    private void callWebserviceCancelOrder( String orderID) {

        WebserviceInterface api= RetrofitAPI.getObject();
        Call<String> Cancelled=api.CancelOrder("CancelOrder",orderID);

        MyApplication.showProgressDialog(activityContext); // show progressDialog
        Cancelled.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                MyApplication.dismissProgressDialog(); // Hide progress Dialog

                if (response.code() == 200) {

                    try{
                        String strResponse=response.body();
                        JSONObject loginObj=new JSONObject(strResponse);
                        String msg=loginObj.getString("msg");

                        if(loginObj.getInt("status")==1){

                            Fragment currentFragment=activityContext.getFragmentManager().findFragmentById(R.id.fragmentmain);

                            if(currentFragment  instanceof FragmentCancelledOrders){
                                ((FragmentCancelledOrders) currentFragment).callWebservice();
                            }
                        }

                        Toast.makeText(activityContext,msg,Toast.LENGTH_SHORT).show();

                    }catch (Exception e){

                        Log.e("Exception",e.getMessage());

                    }
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Exceptions===",t.getMessage());
                MyApplication.dismissProgressDialog();
                ((HomeActivity)activityContext).ShowAlert("Something went wrong, Plz try again later.");

            }
        });
    }



    @Override
    public int getItemCount() {
        return arrOrders.size();
    }



}