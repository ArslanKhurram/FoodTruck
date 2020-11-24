package com.example.foodtruck.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.foodtruck.Models.Option;
import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.Models.OrderedItemOptions;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class OptionsAdapter extends ArrayAdapter<OrderedItemOptions> {


    ArrayList<OrderedItemOptions> options;

    public OptionsAdapter(Context context, ArrayList<OrderedItemOptions> mOptions)
    {
        super(context, 0);
        options = mOptions;
    }

    static class LayoutHandler{
        TextView option;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public OrderedItemOptions getItem(int position) {
        return options.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mview = convertView;
        OptionsAdapter.LayoutHandler layoutHandler;

        if(mview==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mview = layoutInflater.inflate(R.layout.optionslist_layout, parent, false);
            layoutHandler = new OptionsAdapter.LayoutHandler();
            layoutHandler.option = mview.findViewById(R.id.tvoption);
            mview.setTag(layoutHandler);
        }
        else
        {
            layoutHandler = (OptionsAdapter.LayoutHandler) mview.getTag();
        }

        OrderedItemOptions option = (OrderedItemOptions) this.getItem(position);
        layoutHandler.option.setText(option.getM_Option().getM_Option());
        return mview;
    }
}
