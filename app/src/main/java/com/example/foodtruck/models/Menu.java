package com.example.foodtruck.models;

public class Menu {

    private long m_Id;
    private Vendor m_Vendor;

    public Menu() {}

    public long getM_Id() {
        return m_Id;
    }

    public void setM_Id(long m_Id) {
        this.m_Id = m_Id;
    }

    public Vendor getM_Vendor() {
        return m_Vendor;
    }

    public void setM_Vendor(Vendor m_Vendor) {
        this.m_Vendor = m_Vendor;
    }
}
