package com.icecream.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.icecream.R;
import com.icecream.Utils.SharepreferenceUtils;


public class SplashActivity extends AppCompatActivity {
    private int SPLASH_TIME_OUT = 3000;
    SharepreferenceUtils prefrenceUtils;
    Context mcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mcontext=this;
        prefrenceUtils=new SharepreferenceUtils(mcontext);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {

                if(prefrenceUtils.getAutologin()){
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                }

                    finish();

            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {

    }
}
