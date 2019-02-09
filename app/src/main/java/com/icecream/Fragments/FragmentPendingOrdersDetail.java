package com.icecream.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icecream.Activities.HomeActivity;
import com.icecream.Adapters.PendingOrderDetailAdapter;
import com.icecream.Models.PendingOrder.Msg;
import com.icecream.Models.PendingOrder.PendingOrderResponse;
import com.icecream.R;
import com.icecream.Utils.MyApplication;
import com.icecream.Utils.SharepreferenceUtils;

import static com.icecream.Activities.HomeActivity.addFragment;
import static com.icecream.Activities.HomeActivity.preferenceUtils;


/**
 * Created by mukesh-ubnt on 2/5/17.
 */

public class FragmentPendingOrdersDetail extends Fragment implements View.OnClickListener {

    SharepreferenceUtils preferences;
    RelativeLayout root;
    String ActionType = MyApplication.PENDING_ORDERS;
    RecyclerView recycl_orders;
    PendingOrderResponse pendingOrderResponse;
    LinearLayout lnNoRecords;
    // TextView txtNoRecords;
    PendingOrderDetailAdapter adapter;
    Msg detailResponse;
    private Context context;
    private Button imgMenu, imgBack;
    private TextView txtTitle;//,txtDate;
    private TextView txtOrderID;
    private TextView txtCreatedDate;
    private TextView txtRequestedDate;
    private TextView txtName;
    private TextView txtAmount;
    private TextView txtQty;
    private TextView txtAddItem;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ordersdetail, null);
        this.context = getActivity();
        preferences = new SharepreferenceUtils(context);
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

        txtOrderID = (TextView) v.findViewById(R.id.txtOrderID);
        txtCreatedDate = (TextView) v.findViewById(R.id.txtCreatedDate);
        txtRequestedDate = (TextView) v.findViewById(R.id.txtRequestedDate);
        txtName = (TextView) v.findViewById(R.id.txtName);
        txtAmount = (TextView) v.findViewById(R.id.txtAmount);
        txtQty = (TextView) v.findViewById(R.id.txtQty);
        txtAddItem = (TextView) v.findViewById(R.id.txtAddItem);

        if (preferenceUtils.getloginType().trim().equals("Admin")) {
            txtAddItem.setVisibility(View.VISIBLE);
        } else {
            txtAddItem.setVisibility(View.GONE);
        }
        imgMenu = (Button) v.findViewById(R.id.imgMenu);
        imgBack = (Button) v.findViewById(R.id.imgBack);
        imgMenu.setVisibility(View.GONE);
        imgBack.setVisibility(View.VISIBLE);

        root = (RelativeLayout) v.findViewById(R.id.root);
        recycl_orders = (RecyclerView) v.findViewById(R.id.recycl_orders);
        lnNoRecords = (LinearLayout) v.findViewById(R.id.lnNoRecords);

        //txtNoRecords= (TextView) v.findViewById(R.id.txtNoRecords);
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        txtTitle.setText("Order Detail");
        //txtDate= (TextView) v.findViewById(R.id.txtDate);

        Bundle bundle = getArguments();
        detailResponse = (Msg) bundle.getSerializable("Details");

        // String date[] = detailResponse.getOrderDate().split(" ");
        // txtDate.setText(date[0]);
        try {
            txtName.setText(detailResponse.getFullName());
            txtAmount.setText("Rs. " + detailResponse.getActualAmount());
            txtOrderID.setText("#" + detailResponse.getCustomerOrderId());
            String updated = MyApplication.parseDateToddMMyyyy(detailResponse.getUpdatedOn(), MyApplication.yyyy_mm_dd_hh_mm_ss, MyApplication.dd_mm_yyyy);
            txtCreatedDate.setText(updated);
            String orderdate = MyApplication.parseDateToddMMyyyy(detailResponse.getOrderDate(), MyApplication.yyyy_mm_dd_hh_mm_ss, MyApplication.dd_mm_yyyy);
            txtRequestedDate.setText(orderdate);
           /* int qty = 0;
            for (int i = 0; i < detailResponse.getOrderDetails().size(); i++) {
                qty = qty + Integer.parseInt(detailResponse.getOrderDetails().get(i).getActualQty());
            }*/
            String qty=detailResponse.getCBInfo().get(0).getTB()+"B/"+detailResponse.getCBInfo().get(0).getTC()+"C";
            txtQty.setText("" + qty);
            SetAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ClicksListener() {
        imgMenu.setOnClickListener(this);
        root.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtAddItem.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgMenu:
                ((HomeActivity) context).OpenDrawer();
                break;
            case R.id.root:
                break;
            case R.id.imgBack:
                addFragment(new FragmentPendingOrders(), "");

                /*FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment fragment = getFragmentManager().findFragmentById(R.id.sliderFraagment);
                Fragment newFragment;
                newFragment = new FragmentPendingOrders();
                String strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment, strFragmentTag);
              //  transaction.addToBackStack(strFragmentTag);
                transaction.commit();*/
                //getActivity().onBackPressed();
                break;
            case R.id.txtAddItem:


                addFragment(new FragmentCreateOrderListing(), detailResponse.getDistributorCode());

                //FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                // Fragment fragment1 = getFragmentManager().findFragmentById(R.id.sliderFraagment);
                // Fragment newFragment1;
                /*String strFragmentTag1 = newFragment1.toString();
                transaction1.add(R.id.fragmentmain, newFragment1, strFragmentTag1);
               // transaction1.addToBackStack(strFragmentTag1);
                transaction1.commit();*/
                // ((HomeActivity)FragmentPendingOrdersDetail.this.context).navigateAddOrderListing(FragmentPendingOrdersDetail.this.detailResponse.getOrderId());
                break;
        }

    }


    private void SetAdapter() {
        adapter = new PendingOrderDetailAdapter(getActivity(), detailResponse.getOrderDetails());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycl_orders.setLayoutManager(llm);
        recycl_orders.setItemAnimator(new DefaultItemAnimator());
        recycl_orders.setAdapter(adapter);
    }
}
