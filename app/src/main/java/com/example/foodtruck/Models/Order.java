package com.example.foodtruck.Models;

public class Order {
    private long m_Id;
    private String m_OrderNumber;
    private String m_DateAdded;
    private String m_Status;
    private Customer m_Customer;
    private Vendor m_Vendor;

    //default constructor
    public Order() {
    }

    public long getM_Id() {
        return m_Id;
    }

    public void setM_Id(long m_Id) {
        this.m_Id = m_Id;
    }

    public String getM_OrderNumber() {
        return m_OrderNumber;
    }

    public void setM_OrderNumber(String m_OrderNumber) {
        this.m_OrderNumber = m_OrderNumber;
    }

    public String getM_DateAdded() {
        return m_DateAdded;
    }

    public void setM_DateAdded(String m_DateAdded) {
        this.m_DateAdded = m_DateAdded;
    }

    public String getM_Status() {
        return m_Status;
    }

    public void setM_Status(String m_Status) {
        this.m_Status = m_Status;
    }

    public Customer getM_Customer() {
        return m_Customer;
    }

    public void setM_Customer(Customer m_Customer) {
        this.m_Customer = m_Customer;
    }

    public Vendor getM_Vendor() {
        return m_Vendor;
    }

    public void setM_Vendor(Vendor m_Vendor) {
        this.m_Vendor = m_Vendor;
    }
}
