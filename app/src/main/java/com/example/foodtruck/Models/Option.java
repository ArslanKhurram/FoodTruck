package com.example.foodtruck.Models;

public class Option {

    private long m_Id;
    private String m_Option;
    private Item m_Item;

    //default constructor
    public Option() {
    }

    public long getM_Id() {
        return m_Id;
    }

    public void setM_Id(long m_Id) {
        this.m_Id = m_Id;
    }

    public String getM_Option() {
        return m_Option;
    }

    public void setM_Option(String m_Option) {
        this.m_Option = m_Option;
    }

    public Item getM_Item() {
        return m_Item;
    }

    public void setM_Item(Item m_Item) {
        this.m_Item = m_Item;
    }
}
