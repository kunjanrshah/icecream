package com.icecream.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icecream.Fragments.FragmentCreateOrderListing;
import com.icecream.Models.CreateOrder.OrderDetails;
import com.icecream.R;
import com.icecream.Utils.SharepreferenceUtils;

import java.util.List;

import static com.icecream.Fragments.FragmentCreateOrderListing.btnSubmit;

public class CreatedOrdersAdapter extends RecyclerView.Adapter<CreatedOrdersAdapter.MyViewHolder> {

    SharepreferenceUtils sharepreferenceUtils;
    private List<OrderDetails> arrOrders;
    private Activity activityContext;

    public CreatedOrdersAdapter(Activity activity, List<OrderDetails> arrResouceslist) {
        this.arrOrders = arrResouceslist;
        this.activityContext = activity;
        sharepreferenceUtils = new SharepreferenceUtils(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_createorderdetail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.txtName.setText(arrOrders.get(position).ProductName);
        holder.txtcategory.setText(arrOrders.get(position).CategoryName);

        Double totalPrice = 0.0d;
        if (arrOrders.get(position).CartonAvailability.equals("1")) {
            totalPrice = Double.parseDouble(arrOrders.get(position).PricePerKG) * (Integer.parseInt(arrOrders.get(position).Qty) * (Integer.parseInt(arrOrders.get(position).TotalCarton)));
        } else {
            totalPrice = Double.parseDouble(arrOrders.get(position).PricePerKG) * (Integer.parseInt(arrOrders.get(position).Qty));
        }

        holder.txtPackingType.setText(arrOrders.get(position).PackingType);
        holder.txtPrice.setText("Rs. " + totalPrice);
        holder.txtQty.setText(arrOrders.get(position).Qty);

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activityContext, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(activityContext.getResources().getString(R.string.app_name));
                builder.setMessage("Are you sure want to Delete Order?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        arrOrders.remove(position);
                        Fragment oldFragment = activityContext.getFragmentManager().findFragmentById(R.id.fragmentmain);
                        if (oldFragment instanceof FragmentCreateOrderListing) {
                            ((FragmentCreateOrderListing) oldFragment).getTotalPrice();
                            // ((FragmentCreateOrderListing)oldFragment).getTotalQuantity();
                        }
                        if (arrOrders.size() != 0) {
                            btnSubmit.setVisibility(View.VISIBLE);
                            FragmentCreateOrderListing.lnMainlayout.setVisibility(View.VISIBLE);
                        } else {
                            btnSubmit.setVisibility(View.GONE);
                            FragmentCreateOrderListing.lnMainlayout.setVisibility(View.GONE);
                        }


                        notifyDataSetChanged();
                        dialog.dismiss();
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
        });

    }

    @Override
    public int getItemCount() {
        return arrOrders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtcategory, txtQty, txtPrice, txtPackingType;

        public ImageView imgDelete;
        public LinearLayout lnMainlayout;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtcategory = (TextView) view.findViewById(R.id.txtcategory);
            txtQty = (TextView) view.findViewById(R.id.txtQty);
            txtPackingType = (TextView) view.findViewById(R.id.txtPackingType);
            txtPrice = (TextView) view.findViewById(R.id.txtPrice);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            lnMainlayout = (LinearLayout) view.findViewById(R.id.lnMainlayout);
        }
    }


}