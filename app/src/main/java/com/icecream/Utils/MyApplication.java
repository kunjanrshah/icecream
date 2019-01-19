package com.icecream.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.icecream.Dialogs.ProgressDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by dakshesh.khatri on 02-11-2017.
 */

public class MyApplication extends Application {

    private static Context context;
    private static Context ACTIVITYCONTEXT = null;
    private static ProgressDialogFragment progressDialogFragment;
    public static String BASE_URL="http://patelicecream.in/admin/api/";
    public static long RippleEffectsTime=500;
    public static String yyyy_mm_dd = "yyyy-MM-dd";
    public static String yyyy_mm_dd_hh_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static String dd_mm_yyyy = "dd-MM-yyyy";
    public static final String PENDING_ORDERS="PendingOrders";
    public static String CONFIRM_ORDER="ConfirmOrder";
    public static String COMPLETE_ORDER="CompleteOrder";

    public static String UPDATE_ORDER="UpdateOrder";
    public static final String CANCEL_ORDER="CancelOrder";
    public static final String PENDING_ORDER="PendingOrder";


    public void onCreate() {
        super.onCreate();
        MyApplication.context = this;

    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static Context getACTIVITYCONTEXT() {
        return ACTIVITYCONTEXT;
    }

    public static void setACTIVITYCONTEXT(Context aCTIVITYCONTEXT) {
        ACTIVITYCONTEXT = aCTIVITYCONTEXT;
    }

    public static void showProgressDialog(Context context1) {
        progressDialogFragment = new ProgressDialogFragment("");
        progressDialogFragment.show(((Activity) context1).getFragmentManager(), "");
        progressDialogFragment.setCancelable(false);
    }

    public static void showProgressDialog(Context context1, String MSG) {
        progressDialogFragment = new ProgressDialogFragment(MSG);
        progressDialogFragment.show(((Activity) context1).getFragmentManager(), "");
        progressDialogFragment.setCancelable(false);
    }


    public static void dismissProgressDialog() {
        try {
            if (progressDialogFragment != null) {
                progressDialogFragment.dismiss();
            }
        } catch (Exception e) {
        }

    }

    public static  boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static String parseDateToddMMyyyy(String strdate,String inputPattern,String outputPattern) {

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(strdate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
