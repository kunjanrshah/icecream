package com.icecream.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icecream.Interface.SliderMenuClicks;
import com.icecream.R;
import com.icecream.Utils.SharepreferenceUtils;


public class FragmentSliderMenu extends Fragment implements View.OnClickListener {

    Context mcontext;
    SliderMenuClicks menuClicks;
    ImageView imgProfile;
    TextView txtUsername;
    LinearLayout  lnCreateOrder,lnPendingOrders, lnConfirmOrders,lnCompleteOrders,lnCancelledOrders,lnLogout;
    SharepreferenceUtils sharepreferenceUtils;

    RelativeLayout root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_slider, container, false);
        mcontext = getActivity();
        menuClicks = (SliderMenuClicks) getActivity();

        sharepreferenceUtils=new SharepreferenceUtils(getActivity());
        initControls(view);
        Clicks();

        return view;
    }

    private void initControls(View view) {
        imgProfile = (ImageView) view.findViewById(R.id.imgProfile);
        txtUsername = (TextView) view.findViewById(R.id.txtUsername);

        lnCancelledOrders=(LinearLayout) view.findViewById(R.id.lnCancelledOrders);
        lnConfirmOrders = (LinearLayout) view.findViewById(R.id.lnConfirmOrders);
        lnPendingOrders = (LinearLayout) view.findViewById(R.id.lnPendingOrders);
        lnCreateOrder= (LinearLayout) view.findViewById(R.id.lnCreateOrder);
        lnCompleteOrders= (LinearLayout) view.findViewById(R.id.lnCompleteOrders);
        Log.e("Tyep login",sharepreferenceUtils.getloginType());
        if(sharepreferenceUtils.getloginType().trim().equals("Distributor")){
            lnCreateOrder.setVisibility(View.VISIBLE);
        }

        lnLogout = (LinearLayout) view.findViewById(R.id.lnLogout);
        root=(RelativeLayout)view.findViewById(R.id.root);

    }

    private void Clicks() {

        lnConfirmOrders.setOnClickListener(this);
        lnPendingOrders.setOnClickListener(this);
        lnCreateOrder.setOnClickListener(this);
        lnCompleteOrders.setOnClickListener(this);
        lnCancelledOrders.setOnClickListener(this);
        lnLogout.setOnClickListener(this);
        root.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        mcontext = context;
        menuClicks = (SliderMenuClicks) context;
        super.onAttach(context);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.root:
                break;
            case R.id.lnConfirmOrders:
                menuClicks.MenuItemsClicks("ConfirmOrders");
                break;
            case R.id.lnPendingOrders:
                menuClicks.MenuItemsClicks("PendingOrders");
                break;
            case R.id.lnLogout:
                menuClicks.MenuItemsClicks("Logout");
                break;
            case R.id.lnCreateOrder:
                menuClicks.MenuItemsClicks("CreateOrder");
                break;
            case R.id.lnCompleteOrders:
                menuClicks.MenuItemsClicks("CompleteOrder");
                break;
            case R.id.lnCancelledOrders:
                menuClicks.MenuItemsClicks("CancelledOrder");
                break;
            default:
                menuClicks.MenuItemsClicks("Home");
                break;
        }
    }
}