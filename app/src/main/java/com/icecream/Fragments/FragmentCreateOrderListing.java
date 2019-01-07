package com.icecream.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icecream.Activities.HomeActivity;
import com.icecream.Adapters.CreatedOrdersAdapter;
import com.icecream.Models.Categories.Categoryresponse;
import com.icecream.Models.Categories.Msg;
import com.icecream.Models.CreateOrder.OrderDetails;
import com.icecream.Models.Products.Productsresponse;
import com.icecream.R;
import com.icecream.Utils.MyApplication;
import com.icecream.Utils.SharepreferenceUtils;
import com.icecream.Webservices.RetrofitAPI;
import com.icecream.Webservices.WebserviceInterface;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mukesh-ubnt on 2/5/17.
 */

public class FragmentCreateOrderListing extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public String CategoryName;
    public String ProductName;
    public String Qty;
    public String PackingType;
    public String PricePerKG;
    //  public CustomAutoCompleteView myAutoComplete, myautocompleteProduct;
    public AppCompatSpinner spinnerCategory, spinnerProduct, spinnerQuantity;
    // adapter for auto-complete
    public ArrayAdapter<Msg> myAdapter;
    public ArrayAdapter<com.icecream.Models.Products.Msg> myProductAdapter;
    SharepreferenceUtils preferences;
    RelativeLayout root;
    String ActionType = "PendingOrders";
    RecyclerView recycl_orders;
    Categoryresponse categoryresponse;
    Productsresponse productsresponse;
    ArrayList<String> arr_category = new ArrayList<>();

    FloatingActionButton fab_create;
    LinearLayout lnNoRecords;
    TextView txtNoRecords;
    CreatedOrdersAdapter adapter;
    String CategoryID = "0", ProductID = "0";
    Button btnSubmit;
    LinearLayout lnRecords, lnMainlayout;
    ArrayList<String> arr_productsId;
    ArrayList<String> arr_products;
    ArrayList<String> arr_categoryId;
    ArrayList<String> arr_packingType;
    ArrayList<String> arr_price_per_kg;
    private Context context;
    private Button imgMenu;
    private TextView txtTitle, txtTotal;
    private List<OrderDetails> arrOrders = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_createorders, null);
        this.context = getActivity();
        preferences = new SharepreferenceUtils(getActivity());
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
        root = (RelativeLayout) v.findViewById(R.id.root);
        recycl_orders = (RecyclerView) v.findViewById(R.id.recycl_orders);
        txtNoRecords = (TextView) v.findViewById(R.id.txtNoRecords);
        lnRecords = (LinearLayout) v.findViewById(R.id.lnRecords);
        lnNoRecords = (LinearLayout) v.findViewById(R.id.lnNoRecords);
        fab_create = (FloatingActionButton) v.findViewById(R.id.fab_create);
        fab_create.setVisibility(View.VISIBLE);
        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        lnMainlayout = (LinearLayout) v.findViewById(R.id.lnMainlayout);
        txtTotal = (TextView) v.findViewById(R.id.txtTotal);
        txtTitle.setText("Create Orders");

        if (MyApplication.isInternetAvailable(getActivity())) {
            callWebserviceCategory();
        } else {
            ((HomeActivity) getActivity()).ShowAlert("Internet connection not available.");
        }
    }

    private void ClicksListener() {
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

        switch (v.getId()) {
            case R.id.imgMenu:
                ((HomeActivity) context).OpenDrawer();
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

        Calendar now = Calendar.getInstance();

        DatePickerDialog dpd = DatePickerDialog.newInstance(this,now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH)        );
        // If you're calling this from a support Fragment
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setThemeDark(false);
        dpd.setTitle("Patel IceCream");
        dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
    }

    private void callWebserviceCategory() {

        WebserviceInterface api = RetrofitAPI.getObject();
        Call<String> pending = api.getCategory("GetCategoryList");
        MyApplication.showProgressDialog(getActivity()); // show progressDialog
        pending.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                MyApplication.dismissProgressDialog(); // Hide progress Dialog

                if (response.code() == 200) {

                    try {
                        String strResponse = response.body();
                        JSONObject loginObj = new JSONObject(strResponse);

                        Log.e("Categiry Response", strResponse);

                        if (loginObj.getInt("status") == 1) {

                            Gson gson = new Gson();
                            arr_categoryId = new ArrayList<>();
                            categoryresponse = gson.fromJson(strResponse, Categoryresponse.class);
                            arr_category.add("Select Category");
                            arr_categoryId.add("");
                            for (int i = 0; i < categoryresponse.getMsg().get(0).size(); i++) {
                                arr_category.add(categoryresponse.getMsg().get(0).get(i).getCategory());
                                arr_categoryId.add(categoryresponse.getMsg().get(0).get(i).getCategoryId());
                            }
                            CategoryID = "0";
                            CategoryName = arr_category.get(0);
//                            callWebserviceProducts();
                        }

                    } catch (Exception e) {

                    }

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Exceptions===", t.getMessage());
                MyApplication.dismissProgressDialog();
                ((HomeActivity) getActivity()).ShowAlert("Something went wrong, Plz try again later.");

            }
        });
    }

    private void callWebserviceProducts(String categoryID) {
        CategoryID = categoryID;
        Log.e("CategiryID", categoryID);
        WebserviceInterface api = RetrofitAPI.getObject();
        Call<String> pending = api.getProducts("GetProductList", categoryID);
        MyApplication.showProgressDialog(getActivity()); // show progressDialog
        pending.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                MyApplication.dismissProgressDialog(); // Hide progress Dialog

                if (response.code() == 200) {

                    try {
                        String strResponse = response.body();
                        Log.e("Product Response", strResponse);
                        JSONObject loginObj = new JSONObject(strResponse);

                        if (loginObj.getInt("status") == 1) {
                            Gson gson = new Gson();
                            productsresponse = gson.fromJson(strResponse, Productsresponse.class);
                            arr_products = new ArrayList<>();
                            arr_productsId = new ArrayList<>();
                            arr_packingType = new ArrayList<>();
                            arr_price_per_kg = new ArrayList<>();
                            arr_products.add("Select Products");
                            arr_productsId.add("");
                            for (int i = 0; i < productsresponse.getMsg().get(0).size(); i++) {
                                arr_products.add(productsresponse.getMsg().get(0).get(i).getProductName());
                                arr_productsId.add(productsresponse.getMsg().get(0).get(i).getProductId());
                                arr_packingType.add(productsresponse.getMsg().get(0).get(i).getPackingType());
                                arr_price_per_kg.add(productsresponse.getMsg().get(0).get(i).getPricePerKG());
                            }
                            ArrayAdapter productsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arr_products);
                            spinnerProduct.setAdapter(productsAdapter);

                            /*myautocompleteProduct.setThreshold(2);
                            myProductAdapter = new ProductAutoAdapter(getActivity(), R.layout.raw_custom, productsresponse.getMsg().get(0));
                            myautocompleteProduct.setAdapter(myProductAdapter);
                            myProductAdapter.setDropDownViewResource(R.layout.spinner_item);
                            spinnerProduct.setAdapter(myProductAdapter);*/
                        }

                    } catch (Exception e) {

                    }

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Exceptions===", t.getMessage());
                MyApplication.dismissProgressDialog();
                ((HomeActivity) getActivity()).ShowAlert("Something went wrong, Plz try again later.");

            }
        });
    }

    private void SetAdapter() {
        adapter = new CreatedOrdersAdapter(getActivity(), arrOrders);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycl_orders.setLayoutManager(llm);
        recycl_orders.setItemAnimator(new DefaultItemAnimator());
        recycl_orders.setAdapter(adapter);

    }


    public void ShowCreateDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.orderdialog, null);
        layout.setMinimumWidth((int) (displayRectangle.width()));
        // layout.setMinimumHeight((int)(displayRectangle.height()));

        dialog.setContentView(layout);


        final EditText edtQuantity = (EditText) dialog.findViewById(R.id.edtQuantity);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        final ImageView imgCancel = (ImageView) dialog.findViewById(R.id.imgCancel);
        //  myautocompleteProduct = (CustomAutoCompleteView) dialog.findViewById(R.id.myautocompleteProduct);

        spinnerProduct = (AppCompatSpinner) dialog.findViewById(R.id.spinnerProduct);
        spinnerCategory = (AppCompatSpinner) dialog.findViewById(R.id.spinnerCategory);
        spinnerQuantity = (AppCompatSpinner) dialog.findViewById(R.id.spinnerQuantity);

        String[] some_array = getResources().getStringArray(R.array.array_Quantity);
        ArrayAdapter arr_quality = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, some_array);
        arr_quality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantity.setAdapter(arr_quality);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCategory.setSelection(position);
                if (MyApplication.isInternetAvailable(getActivity())) {
                    //  callWebserviceProducts();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arr_category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (MyApplication.isInternetAvailable(getActivity())) {
                    if (position != 0) {
                        callWebserviceProducts(arr_categoryId.get(position));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


      /*  myAutoComplete = (CustomAutoCompleteView) dialog.findViewById(R.id.myautocomplete);
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

                Log.e("Category", tv.getText().toString());
                Log.e("ID", tvcategory.getText().toString());

                CategoryID = tvcategory.getText().toString();
                CategoryName = tv.getText().toString();
                if (MyApplication.isInternetAvailable(getActivity())) {
                //    callWebserviceProducts();
                }

            }

        });


        myautocompleteProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                myProductAdapter.getFilter().filter(s);
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

                ProductName = tv.getText().toString();
                ProductID = tvcategory.getText().toString();
                PackingType = ((TextView) rl.getChildAt(2)).getText().toString();
                PricePerKG = ((TextView) rl.getChildAt(3)).getText().toString();
            }

        });*/

        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerProduct.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                CategoryName = spinnerCategory.getSelectedItem().toString();
                ProductName = spinnerProduct.getSelectedItem().toString();
                if (!CategoryName.toLowerCase().contains("select") && !ProductName.toLowerCase().contains("select")) {
                    OrderDetails order = new OrderDetails();
                    order.ProductID = arr_productsId.get(spinnerProduct.getSelectedItemPosition());
                    order.CategoryID = CategoryID;
                    order.CategoryName = CategoryName;
                    order.ProductName = ProductName;
                    order.Qty = "" + Integer.parseInt(spinnerQuantity.getSelectedItem().toString().trim());
                    order.PackingType = arr_packingType.get(spinnerProduct.getSelectedItemPosition() - 1);
                    order.PricePerKG = arr_price_per_kg.get(spinnerProduct.getSelectedItemPosition() - 1);
                    arrOrders.add(order);
                    adapter.notifyDataSetChanged();
                    //  getTotalQuantity();
                    getTotalPrice();

                    // myAutoComplete.setText("");
                    // myautocompleteProduct.setText("");
                    edtQuantity.setText("0");
                    edtQuantity.setSelection(edtQuantity.getText().toString().length());
                    Toast.makeText(getActivity(), "Order Item is added to OrderList.", Toast.LENGTH_SHORT).show();
                    btnSubmit.setVisibility(View.VISIBLE);
                    lnMainlayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Select Order Item.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getTotalPrice() {
        Double total = 0.0;
        Integer totalQTY = 0;

        if (arrOrders.size() > 0) {
            for (int i = 0; i < arrOrders.size(); i++) {
                OrderDetails detail = arrOrders.get(i);
                total += (Double.parseDouble(detail.PricePerKG) * (Integer.parseInt(detail.Qty)));
                totalQTY += (Integer.parseInt(detail.Qty));
            }
            txtTotal.setText("Qty: " + totalQTY + "\n" + "Rs: " + total);
        } else {
            txtTotal.setText("Qty: " + totalQTY + "\n" + "Rs: " + total);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month=(monthOfYear + 1);

        String months=""+month;
        String dayOfMonths=""+dayOfMonth;
        if(month<10)
        {
            months="0"+months;
        }
        if(dayOfMonth<10)
        {
            dayOfMonths="0"+dayOfMonth;
        }
        String date =  year+ "-" + months +"-"+dayOfMonths ;
        Toast.makeText(getActivity(), "" + date, Toast.LENGTH_SHORT).show();
        if (MyApplication.isInternetAvailable(getActivity())) {
            new CallWS().execute(date);
        } else {
            ((HomeActivity) getActivity()).ShowAlert("Internet connection not available.");
        }
    }

    /*public void getTotalQuantity() {
        Integer totalQTY = 0;

        if (arrOrders.size() != 0) {
            for (int i = 0; i < arrOrders.size(); i++) {
                OrderDetails detail = arrOrders.get(i);

                totalQTY += (Integer.parseInt(detail.Qty));
            }
            txtQty.setText("Qty :" + totalQTY);
        } else {
            txtQty.setText("Qty");
        }

    }*/

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
            HttpPost httppost = new HttpPost("http://patelicecream.in/admin/api/orders.php");

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("date", data[0]));
                nameValuePairs.add(new BasicNameValuePair("ActionType", "CreateOrder"));
                nameValuePairs.add(new BasicNameValuePair("DistributorCode", preferences.getDistributionResponse().DistributorCode));
                for (int i = 0; i < arrOrders.size(); i++) {

                    String strIDs = "ProductList[" + i + "][ProductId]";
                    String strQtys = "ProductList[" + i + "][Qty]";

                    nameValuePairs.add(new BasicNameValuePair(strIDs, arrOrders.get(i).ProductID));
                    nameValuePairs.add(new BasicNameValuePair(strQtys, arrOrders.get(i).Qty));
                }

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //execute http post
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseBody = EntityUtils.toString(entity);
                    Log.e("Response Make Order", responseBody);
                    return responseBody;
                }
            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MyApplication.dismissProgressDialog();

            try {
                JSONObject loginObj = new JSONObject(s);

                if (loginObj.getInt("status") == 1) {

                    ((HomeActivity) getActivity()).ShowAlert("Order Placed Successfully");
                    arrOrders.clear();
                    adapter.notifyDataSetChanged();
                    btnSubmit.setVisibility(View.GONE);
                    lnMainlayout.setVisibility(View.GONE);
                } else {

                    ((HomeActivity) getActivity()).ShowAlert("Something went wrong, Plz try again later.");
                }

            } catch (Exception e) {

            }

        }
    }

}
