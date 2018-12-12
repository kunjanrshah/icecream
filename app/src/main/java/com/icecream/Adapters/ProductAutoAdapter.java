package com.icecream.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.icecream.Activities.HomeActivity;
import com.icecream.Models.Products.Msg;
import com.icecream.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAutoAdapter extends ArrayAdapter<Msg> {

    final String TAG = "AutocompleteCustomArrayAdapter.java";

    Context mContext;
    int layoutResourceId;
    List<Msg> items, tempItems, suggestions;

    public ProductAutoAdapter(Context mContext, int layoutResourceId, List<Msg>  data) {
        super(mContext,layoutResourceId,data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.items = data;
        tempItems = new ArrayList<Msg>(items); // this makes the difference.
        suggestions = new ArrayList<Msg>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         
        try{

            if(convertView==null){
                // inflate the layout
                LayoutInflater inflater = ((HomeActivity) mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }
             
            // object item based on the position
            Msg data=items.get(position);
 
            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
            TextView textViewID = (TextView) convertView.findViewById(R.id.textViewID);
            TextView textPackingType= (TextView) convertView.findViewById(R.id.textPackingType);
            TextView textPricePerKG= (TextView) convertView.findViewById(R.id.textPricePerKG);
            textViewItem.setText(data.getProductName());
            textViewID.setText(data.getProductId());
            textPackingType.setText(data.getPackingType());
            textPricePerKG.setText(data.getPricePerKG());

             
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        return convertView;
         
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Msg) resultValue).getProductName().trim();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Msg model : tempItems) {
                    if (model.getProductName().toLowerCase().startsWith(constraint.toString().trim().toLowerCase())) {
                        suggestions.add(model);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Msg> filterList = (ArrayList<Msg>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Msg model : filterList) {
                    add(model);
                    notifyDataSetChanged();
                }
            }
        }
    };

    @Nullable
    @Override
    public Msg getItem(int position) {
        return super.getItem(position);
    }
}