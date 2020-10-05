package com.example.foodtruck.Models;

import android.widget.ImageView;

import com.example.foodtruck.R;

public class Card {
    private int imageDrawable;
    private String mOption;

    public Card(String option) {
        imageDrawable = R.drawable.ic_arrow;
        mOption = option;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public String getOption() {
        return mOption;
    }

    public void setOption(String option) {
        this.mOption = option;
    }
}
