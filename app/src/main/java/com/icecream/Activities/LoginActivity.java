package com.icecream.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;
import com.icecream.Models.DistributionResponse;
import com.icecream.Models.UserResponse;
import com.icecream.R;
import com.icecream.Utils.MyApplication;
import com.icecream.Utils.SharepreferenceUtils;
import com.icecream.Webservices.RetrofitAPI;
import com.icecream.Webservices.WebserviceInterface;


import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtUsername,edtPassword;
    private  Button btnSignIn;
    private String ActionType="AccountAccess",Username,DeviceId="No Token",Password,LoginType="Distributor"; // Distributor, User
    private Context mcontext;
    private SharepreferenceUtils preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mcontext=this;
        preferences=new SharepreferenceUtils(mcontext);
        InitControls();
    }

    private void InitControls(){
        edtUsername=(EditText)findViewById(R.id.edtUsername);
        edtPassword= (EditText) findViewById(R.id.edtPassword);
        btnSignIn= (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
    }

    private void validations(){
        Username=edtUsername.getText().toString().trim();
        Password=edtPassword.getText().toString().trim();

        if(Username.length()==0){

            ShowAlert("Please enter username");
        }else if(Password.length()==0){
            ShowAlert("Please enter password");
        }else{
            if (MyApplication.isInternetAvailable(mcontext)) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        callWebservice();

                    }
                }, MyApplication.RippleEffectsTime);

            } else {
                ShowAlert("Internet connection not available.");
            }
        }

    }

    private void callWebservice() {

        DeviceId=preferences.getToken();
        Log.e("DeviceId",DeviceId);

        WebserviceInterface api= RetrofitAPI.getObject();
        Call<String> logindata=api.WSLogin(ActionType,Username,Password,LoginType,DeviceId);
        MyApplication.showProgressDialog(mcontext); // show progressDialog
        logindata.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                MyApplication.dismissProgressDialog(); // Hide progress Dialog

                if (response.code() == 200) {

                        try{
                            String strResponse=response.body();
                            JSONObject loginObj=new JSONObject(strResponse);
                            if(loginObj.getInt("status")==1){
                                JSONArray loginArray=loginObj.getJSONArray("msg").getJSONArray(0);
                                JSONObject detailObj=loginArray.getJSONObject(0);
                                if(strResponse.contains("DistributorCode")){
                                    DistributionResponse distributor=new DistributionResponse();
                                    distributor.DistributorId=detailObj.getString("DistributorId");
                                    distributor.FullName=detailObj.getString("FullName");
                                    distributor.Username=detailObj.getString("Username");
                                    distributor.CompanyName=detailObj.getString("CompanyName");
                                    distributor.DistributorCode=detailObj.getString("DistributorCode");
                                    preferences.saveDistributionResponse(distributor);
                                    preferences.setloginType("Distributor");
                                }else if(strResponse.contains("isAdmin")){
                                    UserResponse user=new UserResponse();
                                    user.UserId=detailObj.getString("UserId");
                                    user.FullName=detailObj.getString("FullName");
                                    user.Username=detailObj.getString("Username");
                                    user.isAdmin=detailObj.getString("isAdmin");
                                    preferences.setloginType("Admin");
                                }
                                preferences.setAutologin(true);
                                Intent homeIntent=new Intent(mcontext, HomeActivity.class);
                                startActivity(homeIntent);
                                finish();
                            }else{
                                ShowAlert("Invalid Username or Password");
                            }

                        }catch (Exception e){

                        }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Exceptions===",t.getMessage());
                MyApplication.dismissProgressDialog();
                ShowAlert("Something went wrong, Plz try again later.");
            }
        });
    }
    private void ShowAlert(String msg){

        AlertDialog.Builder builder =  new AlertDialog.Builder(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSignIn:
                validations();
                break;
        }

    }
}
