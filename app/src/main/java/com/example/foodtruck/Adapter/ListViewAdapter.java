package com.example.foodtruck.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.foodtruck.DataBase.OrderedItemsContract;
import com.example.foodtruck.Fragments.Vendor.OrderFragment;
import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<OrderedItem>{

    ArrayList<OrderedItem> orderedItems;

    public ListViewAdapter(Context context, ArrayList<OrderedItem> mOrderedItems)
    {
        super(context, 0);
        orderedItems = mOrderedItems;
    }

    static class LayoutHandler{
        TextView items, quantity;
    }

    @Override
    public int getCount() {
        return orderedItems.size();
    }

    @Override
    public OrderedItem getItem(int position) {
        return orderedItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mview = convertView;
        LayoutHandler layoutHandler;

        if(mview==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mview = layoutInflater.inflate(R.layout.listview_layout, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.items = mview.findViewById(R.id.tvItem);
            layoutHandler.quantity = mview.findViewById((R.id.tvQuantity));
            mview.setTag(layoutHandler);
        }
        else
        {
         layoutHandler = (LayoutHandler) mview.getTag();
        }

        OrderedItem orderedItem = (OrderedItem) this.getItem(position);
        layoutHandler.items.setText(orderedItem.getM_Item().getM_Name());
        layoutHandler.quantity.setText(orderedItem.getM_Quantity());
        return mview;
    }
}
