package com.icecream.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
import com.icecream.Adapters.CancelledOrderDetailAdapter;
import com.icecream.Models.CancelledOrders.CancelledOrderResponse;
import com.icecream.Models.CancelledOrders.Msg;
import com.icecream.R;
import com.icecream.Utils.SharepreferenceUtils;

/**
 * Created by mukesh-ubnt on 2/5/17.
 */

public class FragmentCancelledOrdersDetail extends Fragment implements View.OnClickListener {

    SharepreferenceUtils preferences;
    RelativeLayout root;
    String ActionType = "CancelledOrders";
    RecyclerView recycl_orders;
    CancelledOrderResponse cancelledOrderResponse;
    LinearLayout lnNoRecords;
    TextView txtNoRecords;
    CancelledOrderDetailAdapter adapter;
    Msg detailResponse;
    private Context context;
    private Button imgMenu, imgBack;
    private TextView txtTitle, txtDate, txtName, txtAmount;

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


        imgMenu = (Button) v.findViewById(R.id.imgMenu);
        imgBack = (Button) v.findViewById(R.id.imgBack);
        imgMenu.setVisibility(View.GONE);
        imgBack.setVisibility(View.VISIBLE);
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        root = (RelativeLayout) v.findViewById(R.id.root);
        recycl_orders = (RecyclerView) v.findViewById(R.id.recycl_orders);
        txtNoRecords = (TextView) v.findViewById(R.id.txtNoRecords);
        lnNoRecords = (LinearLayout) v.findViewById(R.id.lnNoRecords);

        txtDate = (TextView) v.findViewById(R.id.txtDate);
        txtName = (TextView) v.findViewById(R.id.txtName);
        txtAmount = (TextView) v.findViewById(R.id.txtAmount);

        txtTitle.setText("Order Detail");

        Bundle bundle = getArguments();
        detailResponse = (Msg) bundle.getSerializable("Details");

        String date[] = detailResponse.getOrderDate().split(" ");
        txtDate.setText(date[0]);
        txtName.setText(detailResponse.getFullName());
        txtAmount.setText(detailResponse.getActualAmount());


        SetAdapter();


    }

    private void ClicksListener() {
        imgMenu.setOnClickListener(this);
        root.setOnClickListener(this);
        imgBack.setOnClickListener(this);
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

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment fragment = getFragmentManager().findFragmentById(R.id.sliderFraagment);
                Fragment newFragment;
                newFragment = new FragmentCancelledOrders();
                String strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment,strFragmentTag);
                transaction.commit();

               // getActivity().onBackPressed();
                break;
        }

    }

    private void SetAdapter() {
        adapter = new CancelledOrderDetailAdapter(getActivity(), detailResponse.getOrderDetails());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycl_orders.setLayoutManager(llm);
        recycl_orders.setItemAnimator(new DefaultItemAnimator());
        recycl_orders.setAdapter(adapter);

    }
}
