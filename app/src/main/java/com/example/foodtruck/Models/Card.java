package com.example.foodtruck.Models;

import com.example.foodtruck.R;

public class Card {
    private int imageDrawable;
    private String mOption;
    private String mInfo;

    public Card(String option, String info) {
        imageDrawable = R.drawable.ic_arrow;
        mOption = option;
        mInfo = info;
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

    public String getInfo() {
        return mInfo;
    }

    public void setInfo(String mInfo) {
        this.mInfo = mInfo;
    }
}
