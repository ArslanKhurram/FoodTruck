package com.example.foodtruck.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class ItemsOrderedAdapter extends ArrayAdapter<OrderedItem>{

    ArrayList<OrderedItem> orderedItems;

    public ItemsOrderedAdapter(Context context, ArrayList<OrderedItem> mOrderedItems)
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
            layoutHandler.items = mview.findViewById(R.id.tvoption);
            layoutHandler.quantity = mview.findViewById((R.id.tvChecked));
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
