package com.icecream.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.icecream.Activities.HomeActivity;
import com.icecream.Adapters.CancelledOrdersAdapter;
import com.icecream.Models.CancelledOrders.CancelledOrderResponse;
import com.icecream.R;
import com.icecream.Utils.MyApplication;
import com.icecream.Utils.SharepreferenceUtils;
import com.icecream.Webservices.RetrofitAPI;
import com.icecream.Webservices.WebserviceInterface;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mukesh-ubnt on 2/5/17.
 */

public class FragmentCancelledOrders extends Fragment implements View.OnClickListener{

    private Context context;
    private Button imgMenu;
    private TextView txtTitle;
    SharepreferenceUtils preferences;
    RelativeLayout root;
    String ActionType="CancelledOrders";
    RecyclerView recycl_orders;
    CancelledOrderResponse  cancelledOrderResponse;

    LinearLayout lnNoRecords;
    TextView txtNoRecords;
    CancelledOrdersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orders, null);
        context=getActivity();
        preferences=new SharepreferenceUtils(getActivity());
        InitControls(view);
        ClicksListener();
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // initialization of controls
        super.onViewCreated(view, savedInstanceState);
    }

    private void InitControls(View v) {


        imgMenu = (Button) v.findViewById(R.id.imgMenu);
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        root= (RelativeLayout) v.findViewById(R.id.root);
        recycl_orders= (RecyclerView) v.findViewById(R.id.recycl_orders);
        txtNoRecords= (TextView) v.findViewById(R.id.txtNoRecords);
        lnNoRecords= (LinearLayout) v.findViewById(R.id.lnNoRecords);

        txtTitle.setText("Cancelled Orders");

        if (MyApplication.isInternetAvailable(getActivity())) {

            callWebservice();

        } else {
            ((HomeActivity)getActivity()).ShowAlert("Internet connection not available.");
        }


    }

    private void ClicksListener(){
        imgMenu.setOnClickListener(this);
        root.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imgMenu:
                ((HomeActivity)context).OpenDrawer();
                break;
            case R.id.root:
                break;
        }

    }

    public void callWebservice() {

        WebserviceInterface api= RetrofitAPI.getObject();
        Call<String> Pending;

        if(preferences.getloginType().trim().equals("Distributor")){
            Pending=api.getPendingOrders(ActionType,preferences.getDistributionResponse().DistributorCode);
        }else{
            Pending=api.getPendingOrders(ActionType,null);
        }

        MyApplication.showProgressDialog(getActivity()); // show progressDialog
        Pending.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                MyApplication.dismissProgressDialog(); // Hide progress Dialog

                if (response.code() == 200) {

                    try{
                        String strResponse=response.body();
                        JSONObject loginObj=new JSONObject(strResponse);

                        if(loginObj.getInt("status")==1){

                            Gson gson = new Gson();
                            cancelledOrderResponse = gson.fromJson(strResponse, CancelledOrderResponse.class);
                            SetAdapter();

                        }else{

                            recycl_orders.setVisibility(View.GONE);
                            lnNoRecords.setVisibility(View.VISIBLE);
                            txtNoRecords.setText("No Cancelled Records");
                            ((HomeActivity)getActivity()).ShowAlert("No Cancelled Records");
                        }

                    }catch (Exception e){

                    }

                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Exceptions===",t.getMessage());
                MyApplication.dismissProgressDialog();
                ((HomeActivity)getActivity()).ShowAlert("Something went wrong, Plz try again later.");

            }
        });
    }


    private  void SetAdapter(){
        adapter=new CancelledOrdersAdapter(getActivity(),cancelledOrderResponse.getMsg().get(0));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycl_orders.setLayoutManager(llm);
        recycl_orders.setItemAnimator(new DefaultItemAnimator());
        recycl_orders.setAdapter(adapter);

    }
}
