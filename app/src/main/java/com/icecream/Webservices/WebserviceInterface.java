package com.icecream.Webservices;





import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WebserviceInterface {

    @POST("login.php")
    @FormUrlEncoded
    Call<String> WSLogin(@Field("ActionType") String ActionType, @Field("Username") String username, @Field("Password") String password, @Field("LoginType") String LoginType, @Field("DeviceId") String DeviceId);
//
//
//    @Headers("Content-Type: application/x-www-form-urlencoded")
//    @POST("search.php")
//    @FormUrlEncoded
//    Call<SearchResponse> Searchdata(@Field("Gender") String Gender, @Field("fcountry") String fcountry,@Field("MToungh") String MToungh,@Field("fromAge") String fromAge,@Field("toAge") String toAge,@Field("MStatus") String MStatus);
//
//

    @GET("orders.php")
    Call<String> getPendingOrders(@Query("ActionType") String ActionType, @Query("DistributorCode") String DistributorCode);


    @FormUrlEncoded
    @POST("orders.php")
    Call<String> CancelOrder(@Field("ActionType") String ActionType, @Field("OrderId") String OrderId);

    @GET("categories.php")
    Call<String> getCategory(@Query("ActionType") String ActionType);

    @GET("products.php")
    Call<String> getProducts(@Query("ActionType") String ActionType, @Query("CID") String CID);




}
