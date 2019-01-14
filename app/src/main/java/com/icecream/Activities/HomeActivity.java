package com.icecream.Activities;



import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.icecream.Fragments.FragmentCancelledOrders;
import com.icecream.Fragments.FragmentCompleteOrders;
import com.icecream.Fragments.FragmentConfirmOrders;
import com.icecream.Fragments.FragmentCreateOrderListing;
import com.icecream.Fragments.FragmentPendingOrders;
import com.icecream.Fragments.FragmentSliderMenu;
import com.icecream.Interface.SliderMenuClicks;
import com.icecream.R;
import com.icecream.Utils.MyApplication;
import com.icecream.Utils.SharepreferenceUtils;


public class HomeActivity extends AppCompatActivity implements SliderMenuClicks {

    //    public LoginResponse loginResponse;
    public static SharepreferenceUtils preferenceUtils;
    private DrawerLayout drawerlayout;
    private String strFragmentTag;
    private Context mcontext;
    static android.app.FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);
        mcontext = this;
        manager = getFragmentManager();
        preferenceUtils = new SharepreferenceUtils(mcontext);
//        loginResponse=preferenceUtils.getLoginResponse();
        InitControls();

        if (preferenceUtils.getloginType().trim().equals("Admin")) {
            MenuItemsClicks("PendingOrders"); // default home screen display
        } else {
            MenuItemsClicks("CreateOrder"); // default home screen display
        }
    }

    private void InitControls() {
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        getFragmentManager().beginTransaction().replace(R.id.sliderFraagment, new FragmentSliderMenu(), "Slider").commit();
    }

    // Sliding Open
    public void OpenDrawer() {

        if (drawerlayout.isDrawerOpen(Gravity.LEFT)) {
            drawerlayout.closeDrawer(Gravity.LEFT);
        } else {
            drawerlayout.openDrawer(Gravity.LEFT);
        }
    }

    // SLiding Close
    public void CloseDrawer() {
        try {
            if (drawerlayout.isDrawerOpen(Gravity.LEFT)) {
                drawerlayout.closeDrawer(Gravity.LEFT);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void MenuItemsClicks(String tag) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                CloseDrawer();
            }
        }, MyApplication.RippleEffectsTime);


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.sliderFraagment);


        switch (tag) {
            case "Profile":
                break;
            case "PendingOrders":
                addFragmentHome(new FragmentPendingOrders());
               /* newFragment = new FragmentPendingOrders();
                strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment,strFragmentTag);
                transaction.commit();*/
                break;

            case "ConfirmOrders":

                addFragmentHome(new FragmentConfirmOrders());
               /* strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment,strFragmentTag);
                transaction.commit();*/
                break;

            case "CreateOrder":

                addFragmentHome(new FragmentCreateOrderListing());
              /*  strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment,strFragmentTag);
                transaction.commit();*/
                break;

            case "CompleteOrder":

                addFragmentHome(new FragmentCompleteOrders());
                /*strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment,strFragmentTag);
                transaction.commit();*/
                break;

            case "CancelledOrder":
                addFragmentHome(new FragmentCancelledOrders());
                /*strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment,strFragmentTag);
                transaction.commit();*/
                break;

            case "Logout":
                LogoutAlert();
                break;
        }
    }


    private void addFragmentHome(Fragment fragment){
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(R.id.fragmentmain, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

   public static void addFragment(Fragment fragment){
        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(R.id.fragmentmain, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() <= 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    public void ShowAlert(String msg) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(HomeActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.show();

}

    public void ShowDialogAlert(String msg) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(HomeActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.show();

    }

    private void LogoutAlert() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(HomeActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage("Are you sure want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {

                preferenceUtils.clearSession();
                dialog.dismiss();
                Intent login = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();

    }

}