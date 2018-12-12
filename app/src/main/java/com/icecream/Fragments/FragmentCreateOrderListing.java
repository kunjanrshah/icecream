package com.icecream.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icecream.Activities.HomeActivity;
import com.icecream.Adapters.CategoryAutoAdapter;
import com.icecream.Adapters.CreatedOrdersAdapter;
import com.icecream.Adapters.ProductAutoAdapter;
import com.icecream.Models.Categories.Categoryresponse;
import com.icecream.Models.Categories.Msg;
import com.icecream.Models.CreateOrder.OrderDetails;
import com.icecream.Models.Products.Productsresponse;
import com.icecream.R;
import com.icecream.Utils.CustomAutoCompleteView;
import com.icecream.Utils.MyApplication;
import com.icecream.Utils.SharepreferenceUtils;
import com.icecream.Webservices.RetrofitAPI;
import com.icecream.Webservices.WebserviceInterface;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mukesh-ubnt on 2/5/17.
 */

public class FragmentCreateOrderListing extends Fragment implements View.OnClickListener{

    private Context context;
    private Button imgMenu;
    private TextView txtTitle,txtQty,txtPrice;
    SharepreferenceUtils preferences;
    RelativeLayout root;
    String ActionType="PendingOrders";
    RecyclerView recycl_orders;
    Categoryresponse categoryresponse;
    Productsresponse productsresponse;
    ArrayList<String> arr_category=new ArrayList<>();
    ArrayList<String> arr_products=new ArrayList<>();
    FloatingActionButton fab_create;
    private List<OrderDetails> arrOrders=new ArrayList<>();

    LinearLayout lnNoRecords;
    TextView txtNoRecords;
    CreatedOrdersAdapter adapter;
    String CategoryID ="0",ProductID="0";

    public String CategoryName;
    public String ProductName;
    public String Qty;
    public String PackingType;
    public String PricePerKG;
    Button btnSubmit;
    LinearLayout lnRecords;
    public CustomAutoCompleteView myAutoComplete,myautocompleteProduct;

    // adapter for auto-complete
    public ArrayAdapter<Msg> myAdapter;
    public ArrayAdapter<com.icecream.Models.Products.Msg> myProductAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_createorders, null);
        this.context = getActivity();
        preferences=new SharepreferenceUtils(getActivity());
        InitControls(view);
        SetAdapter();
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
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        root= (RelativeLayout) v.findViewById(R.id.root);
        recycl_orders= (RecyclerView) v.findViewById(R.id.recycl_orders);
        txtNoRecords= (TextView) v.findViewById(R.id.txtNoRecords);
        lnRecords= (LinearLayout) v.findViewById(R.id.lnRecords);
        lnNoRecords= (LinearLayout) v.findViewById(R.id.lnNoRecords);
        fab_create= (FloatingActionButton) v.findViewById(R.id.fab_create);
        fab_create.setVisibility(View.VISIBLE);
        btnSubmit= (Button) v.findViewById(R.id.btnSubmit);
        txtQty= (TextView) v.findViewById(R.id.txtQty);
        txtPrice= (TextView) v.findViewById(R.id.txtPrice);
        btnSubmit.setVisibility(View.VISIBLE);

        txtTitle.setText("Create Orders");

        if (MyApplication.isInternetAvailable(getActivity())) {

            callWebserviceCategory();


        } else {
            ((HomeActivity)getActivity()).ShowAlert("Internet connection not available.");
        }


    }

    private void ClicksListener(){
        imgMenu.setOnClickListener(this);
        root.setOnClickListener(this);
        fab_create.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

        @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imgMenu:
                ((HomeActivity)context).OpenDrawer();
                break;
            case R.id.root:
                break;
            case R.id.fab_create:

                ShowCreateDialog();

                break;

            case R.id.btnSubmit:

                MakeOrder();
                break;
        }

    }

    private void MakeOrder() {

        if (MyApplication.isInternetAvailable(getActivity())) {

            new CallWS().execute("");

        } else {
            ((HomeActivity)getActivity()).ShowAlert("Internet connection not available.");
        }





    }

    private void callWebserviceCategory() {

        WebserviceInterface api= RetrofitAPI.getObject();
        Call<String> pending=api.getCategory("GetCategoryList");
        MyApplication.showProgressDialog(getActivity()); // show progressDialog
        pending.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                MyApplication.dismissProgressDialog(); // Hide progress Dialog

                if (response.code() == 200) {

                    try{
                        String strResponse=response.body();
                        JSONObject loginObj=new JSONObject(strResponse);

                        Log.e("Categiry Response",strResponse);

                        if(loginObj.getInt("status")==1){

                            Gson gson = new Gson();
                            categoryresponse = gson.fromJson(strResponse, Categoryresponse.class);

                            for(int i=0;i<categoryresponse.getMsg().get(0).size();i++){
                                arr_category.add(categoryresponse.getMsg().get(0).get(i).getCategory());

                            }

                            CategoryID =categoryresponse.getMsg().get(0).get(0).getCategoryId();

                            CategoryName=categoryresponse.getMsg().get(0).get(0).getCategory();

//                            callWebserviceProducts();

                        }

                    }catch (Exception e){

                    }

                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Exceptions===",t.getMessage());
                MyApplication.dismissProgressDialog();
                ((HomeActivity)getActivity()).ShowAlert("Something went wrong, Plz try again later.");

            }
        });
    }
    private void callWebserviceProducts() {

        Log.e("CategiryID",CategoryID);
        WebserviceInterface api= RetrofitAPI.getObject();
        Call<String> pending=api.getProducts("GetProductList", CategoryID);
        MyApplication.showProgressDialog(getActivity()); // show progressDialog
        pending.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                MyApplication.dismissProgressDialog(); // Hide progress Dialog

                if (response.code() == 200) {

                    try{
                        String strResponse=response.body();
                        Log.e("Product Response",strResponse);
                        JSONObject loginObj=new JSONObject(strResponse);

                        if(loginObj.getInt("status")==1){

                            Gson gson = new Gson();
                            productsresponse = gson.fromJson(strResponse, Productsresponse.class);

                            myautocompleteProduct.setThreshold(2);
                            myProductAdapter = new ProductAutoAdapter(getActivity(), R.layout.raw_custom, productsresponse.getMsg().get(0));
                            myautocompleteProduct.setAdapter(myProductAdapter);
                        }

                    }catch (Exception e){

                    }

                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Exceptions===",t.getMessage());
                MyApplication.dismissProgressDialog();
                ((HomeActivity)getActivity()).ShowAlert("Something went wrong, Plz try again later.");

            }
        });
    }

    private  void SetAdapter(){
        adapter=new CreatedOrdersAdapter(getActivity(),arrOrders);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycl_orders.setLayoutManager(llm);
        recycl_orders.setItemAnimator(new DefaultItemAnimator());
        recycl_orders.setAdapter(adapter);

    }



    public void ShowCreateDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.orderdialog, null);
        layout.setMinimumWidth((int)(displayRectangle.width()));
        layout.setMinimumHeight((int)(displayRectangle.height()));

        dialog.setContentView(layout);


        final EditText edtQuantity= (EditText) dialog.findViewById(R.id.edtQuantity);
        Button btnAdd=(Button)dialog.findViewById(R.id.btnAdd);

        final ImageView imgCancel= (ImageView) dialog.findViewById(R.id.imgCancel);


        myAutoComplete = (CustomAutoCompleteView) dialog.findViewById(R.id.myautocomplete);
        myautocompleteProduct= (CustomAutoCompleteView) dialog.findViewById(R.id.myautocompleteProduct);
        myAutoComplete.setThreshold(2);
        myAdapter = new CategoryAutoAdapter(getActivity(), R.layout.raw_custom, categoryresponse.getMsg().get(0));
        myAutoComplete.setAdapter(myAdapter);

        myAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                myAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {

                LinearLayout rl = (LinearLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                TextView tvcategory = (TextView) rl.getChildAt(1);
                myAutoComplete.setText(tv.getText().toString());
                myAutoComplete.setSelection(tv.getText().toString().trim().length());

                Log.e("Category",tv.getText().toString());
                Log.e("ID",tvcategory.getText().toString());

                CategoryID =tvcategory.getText().toString();
                CategoryName=tv.getText().toString();
                if (MyApplication.isInternetAvailable(getActivity())) {
                    callWebserviceProducts();
                }

            }

        });

        myautocompleteProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                myProductAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        myautocompleteProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {

                LinearLayout rl = (LinearLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                TextView tvcategory = (TextView) rl.getChildAt(1);
                myautocompleteProduct.setText(tv.getText().toString());
                myautocompleteProduct.setSelection(tv.getText().toString().trim().length());

                ProductName=tv.getText().toString();
                ProductID=tvcategory.getText().toString();
                PackingType=((TextView) rl.getChildAt(2)).getText().toString();
                PricePerKG=((TextView) rl.getChildAt(3)).getText().toString();

            }

        });

//          callWebserviceProducts();

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderDetails order=new OrderDetails();
                order.ProductID=ProductID;
                order.CategoryID=CategoryID;
                order.CategoryName=CategoryName;
                order.ProductName=ProductName;
                order.Qty=""+Integer.parseInt(edtQuantity.getText().toString().trim());
                order.PackingType=PackingType;
                order.PricePerKG=PricePerKG;

                arrOrders.add(order);

                adapter.notifyDataSetChanged();
                getTotalQuantity();
                getTotalPrice();

                myAutoComplete.setText("");
                myautocompleteProduct.setText("");
                edtQuantity.setText("0");
                edtQuantity.setSelection(edtQuantity.getText().toString().length());

                Toast.makeText(getActivity(),"Order Item is added to OrderList.",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class CallWS extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MyApplication.showProgressDialog(getActivity());
        }

        @Override
        protected String doInBackground(String... data) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://livebroadcastevents.com/vanilla/api/orders.php");

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("ActionType", "CreateOrder"));
                nameValuePairs.add(new BasicNameValuePair("DistributorCode", preferences.getDistributionResponse().DistributorCode));
                for(int i=0;i<arrOrders.size();i++){

                    String strIDs="ProductList["+i+"][ProductId]";
                    String strQtys="ProductList["+i+"][Qty]";

                    nameValuePairs.add(new BasicNameValuePair(strIDs, arrOrders.get(i).ProductID));
                    nameValuePairs.add(new BasicNameValuePair(strQtys, arrOrders.get(i).Qty));
                }

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //execute http post
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                if(entity != null) {
                    String responseBody = EntityUtils.toString(entity);
                    Log.e("Response Make Order",responseBody);
                    return  responseBody;


                }


            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }
            return  "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MyApplication.dismissProgressDialog();

            try{
                JSONObject loginObj=new JSONObject(s);

                if(loginObj.getInt("status")==1){

                    ((HomeActivity)getActivity()).ShowAlert("Order Placed Successfully");
                    arrOrders.clear();
                    adapter.notifyDataSetChanged();
                }else{

                    ((HomeActivity)getActivity()).ShowAlert("Something went wrong, Plz try again later.");
                }

            }catch (Exception e){

            }

        }
    }

    public void getTotalPrice(){
        Double total=0.0;

        if(arrOrders.size()>0){
            for(int i=0;i<arrOrders.size();i++){
                OrderDetails detail=arrOrders.get(i);

                total+=(Double.parseDouble(detail.PricePerKG) * (Integer.parseInt(detail.Qty)));
            }
            txtPrice.setText("Price :"+total);
        }else{
            txtPrice.setText("Price");
        }
    }

    public  void getTotalQuantity(){
        Integer totalQTY=0;

        if(arrOrders.size()!=0){
            for(int i=0;i<arrOrders.size();i++){
                OrderDetails detail=arrOrders.get(i);

                totalQTY+=(Integer.parseInt(detail.Qty));
            }
            txtQty.setText("Qty :"+totalQTY);
        }
        else{
            txtQty.setText("Qty");
        }

    }

}
