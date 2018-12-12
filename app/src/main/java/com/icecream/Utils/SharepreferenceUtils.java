package com.icecream.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.icecream.Models.DistributionResponse;


import static android.content.Context.MODE_PRIVATE;



public class SharepreferenceUtils {

    private Context mcontext;
    private SharedPreferences prefs;
    private String MY_PREFS_NAME="Icecreams_detail";
    private SharedPreferences.Editor editor;

    public SharepreferenceUtils(Context mcontext) {
        this.mcontext = mcontext;
        this.prefs = mcontext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    }

    public void setAutologin(boolean isautologin){

        editor = prefs.edit();
        editor.putBoolean("isAutoLogin", isautologin);
        editor.commit();

    }
    public boolean getAutologin(){
        return prefs.getBoolean("isAutoLogin",false);
    }

    public void setloginType(String logintype){

        editor = prefs.edit();
        editor.putString("logintype", logintype);
        editor.commit();

    }
    public String getloginType(){
        return prefs.getString("logintype","");
    }


    public void saveDistributionResponse(DistributionResponse response){
        Gson gson = new Gson();
        String json = gson.toJson(response);
        editor = prefs.edit();
        editor.putString("loginResponse", json);
        editor.commit();

    }

    public void SetToken(String token){

        editor = prefs.edit();
        editor.putString("token", token);
        editor.commit();
    }


    public String getToken(){
        return  prefs.getString("token","No Token");
    }

    public DistributionResponse getDistributionResponse(){
        Gson gson = new Gson();
        String json = prefs.getString("loginResponse", null);
        return gson.fromJson(json, DistributionResponse.class);
    }

    public void clearSession(){
        editor = prefs.edit();
        editor.clear().commit();

    }

}
