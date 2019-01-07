package com.icecream.Activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class HomeActivity1 extends AppCompatActivity implements SliderMenuClicks {
    public SharepreferenceUtils preferenceUtils;
    private DrawerLayout drawerlayout;
    private Context mcontext;
    private String strFragmentTag;

    private void InitControls() {
        this.drawerlayout = ((DrawerLayout) findViewById(R.id.drawerlayout));
        getFragmentManager().beginTransaction().replace(R.id.sliderFraagment, new FragmentSliderMenu(), "Slider").commit();
    }

    private void LogoutAlert() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        localBuilder.setTitle(getResources().getString(R.string.app_name));
        localBuilder.setMessage("Are you sure want to logout?");
        localBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                preferenceUtils.clearSession();
                dialog.dismiss();
                Intent login = new Intent(HomeActivity1.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
        localBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
            }
        });
        localBuilder.show();
    }

    public void CloseDrawer() {
        try {
            if (this.drawerlayout.isDrawerOpen(Gravity.LEFT)) {
                this.drawerlayout.closeDrawer(Gravity.LEFT);
            }
            return;
        } catch (Exception localException) {
        }
    }

    public void MenuItemsClicks(String tag) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                HomeActivity1.this.CloseDrawer();
            }
        }, MyApplication.RippleEffectsTime);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.sliderFraagment);
        Fragment newFragment;


        switch (tag) {
            case "Profile":
                break;
            case "PendingOrders":
                newFragment = new FragmentPendingOrders();
                strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment, strFragmentTag);
                transaction.commit();
                break;

            case "ConfirmOrders":
                newFragment = new FragmentConfirmOrders();
                strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment, strFragmentTag);
                transaction.commit();
                break;

            case "CreateOrder":
                newFragment = new FragmentCreateOrderListing();
                strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment, strFragmentTag);
                transaction.commit();
                break;

            case "CompleteOrder":
                newFragment = new FragmentCompleteOrders();
                strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment, strFragmentTag);
                transaction.commit();
                break;
            case "CancelledOrder":
                newFragment = new FragmentCancelledOrders();
                strFragmentTag = newFragment.toString();
                transaction.add(R.id.fragmentmain, newFragment, strFragmentTag);
                transaction.commit();
                break;
            case "Logout":
                LogoutAlert();
                break;
        }
    }

    public void OpenDrawer() {
        if (this.drawerlayout.isDrawerOpen(Gravity.LEFT)) {
            this.drawerlayout.closeDrawer(Gravity.LEFT);
            return;
        }
        this.drawerlayout.openDrawer(Gravity.LEFT);
    }

    public void ShowAlert(String paramString) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        localBuilder.setTitle(getResources().getString(R.string.app_name));
        localBuilder.setMessage(paramString);
        localBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
            }
        });
        localBuilder.show();
    }

    public void ShowDialogAlert(String paramString) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        localBuilder.setTitle(getResources().getString(R.string.app_name));
        localBuilder.setMessage(paramString);
        localBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
            }
        });
        localBuilder.show();
    }

   /* public void navigateAddOrderListing(String paramString)
    {
        FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
        FragmentAddOrderListing localFragmentAddOrderListing = new FragmentAddOrderListing();
        Bundle localBundle = new Bundle();
        localBundle.putString("orderId", "" + paramString);
        localFragmentAddOrderListing.setArguments(localBundle);
        this.strFragmentTag = localFragmentAddOrderListing.toString();
        localFragmentTransaction.add(R.id.fragmentmain, localFragmentAddOrderListing, this.strFragmentTag);
        localFragmentTransaction.addToBackStack(this.strFragmentTag);
        localFragmentTransaction.commit();
    }

    public void navigatePendingOrderListing()
    {
        FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
        FragmentPendingOrders localFragmentPendingOrders = new FragmentPendingOrders();
        this.strFragmentTag = localFragmentPendingOrders.toString();
        localFragmentTransaction.add(R.id.fragmentmain, localFragmentPendingOrders, this.strFragmentTag);
        localFragmentTransaction.commit();
    }*/

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.homeactivity);
        this.mcontext = this;
        this.preferenceUtils = new SharepreferenceUtils(this.mcontext);
        InitControls();
        if (this.preferenceUtils.getloginType().trim().equals("Admin")) {
            MenuItemsClicks("PendingOrders");
            return;
        }
        if ((getIntent().getExtras() != null) && (getIntent().getExtras().containsKey("notification"))) {
            MenuItemsClicks("PendingOrders");
            return;
        }
        MenuItemsClicks("CreateOrder");
    }
}
