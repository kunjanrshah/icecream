package com.icecream.Webservices;

import android.provider.Settings;


import com.icecream.Utils.MyApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by dakshesh.khatri on 02-11-2017.
 */

public class RetrofitAPI {

    public static WebserviceInterface getObject(){
        OkHttpClient.Builder builderOkHttp = new OkHttpClient.Builder();
        builderOkHttp.connectTimeout(60, TimeUnit.SECONDS);
        builderOkHttp.readTimeout(60, TimeUnit.SECONDS);

        OkHttpClient client = builderOkHttp.build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(MyApplication.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         return  retrofit.create(WebserviceInterface.class);
    }
}
