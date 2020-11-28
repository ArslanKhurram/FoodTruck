package com.example.foodtruck.Models;

public class Cart {
    private long m_Id;
    private Customer m_Customer;
    private String m_Item;
    private String m_Price;
    private String m_Quantity;
    private String m_Selected_Options;



    public Cart(){}

    public long getM_ID() {return m_Id;}
    public void setM_ID(long m_Id){this.m_Id = m_Id; }

    public String getM_Item() {return m_Item;}
    public void setM_Item(String m_Item) {this.m_Item = m_Item; }

    public String getM_Price() {
        return m_Price;
    }

    public void setM_Price(String m_Price) {
        this.m_Price = m_Price;
    }

    public String getM_Quantity() {
        return m_Quantity;
    }

    public void setM_Quantity(String m_Quantity) {
        this.m_Quantity = m_Quantity;
    }

    public Customer getCustId() {
        return m_Customer;
    }

    public void setCustId(Customer cust_Id) {
        this.m_Customer = cust_Id;
    }

    public String getM_Selected_Options() {
        return m_Selected_Options;
    }

    public void setM_Selected_Options(String m_Options) {
        this.m_Selected_Options = m_Options;
    }

}

